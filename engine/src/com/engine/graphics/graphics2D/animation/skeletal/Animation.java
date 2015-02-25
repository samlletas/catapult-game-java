package com.engine.graphics.graphics2D.animation.skeletal;

import com.badlogic.gdx.utils.Array;
import com.engine.GameTime;
import com.engine.events.Event;
import com.engine.events.EventsArgs;
import com.engine.graphics.graphics2D.animation.skeletal.FrameInterpolation.IFrameInterpolator;
import com.engine.graphics.graphics2D.animation.skeletal.FrameInterpolation.TimeInterpolator;

public class Animation
{
    public final String name;
    public final boolean loop;

    private Array<Frame> frames;
    private int currentFramePosition;
    private int nextFramePosition;
    private Frame currentFrame;
    private Frame nextFrame;
    private IFrameInterpolator frameInterpolator;

    private boolean isPlaying;
    private boolean isPaused;

    public Event<EventsArgs> onAnimationEnd = new Event<EventsArgs>();
    private EventsArgs onAnimationEndArgs = new EventsArgs();

    public Animation(String name, boolean loop)
    {
        this.name = name;
        this.loop = loop;

        this.frames = new Array<Frame>();
        this.currentFramePosition = -1;
        this.nextFramePosition = -1;
        this.currentFrame = null;
        this.nextFrame = null;
        this.frameInterpolator = new TimeInterpolator();

        this.isPlaying = false;
        this.isPaused = false;
    }

    public void start(float timeOffset)
    {
        currentFramePosition = 0;
        nextFramePosition = advanceFramePosition(currentFramePosition);
        currentFrame = frames.get(currentFramePosition);
        nextFrame = frames.get(nextFramePosition);

        frameInterpolator.start(currentFrame, nextFrame, timeOffset);

        isPlaying = true;
        isPaused = false;
    }

    public IFrameInterpolator getFrameInterpolator()
    {
        return frameInterpolator;
    }

    public void setFrameInterpolator(IFrameInterpolator frameInterpolator)
    {
        this.frameInterpolator = frameInterpolator;
    }

    public void pause()
    {
        if (isPlaying)
        {
            isPaused = true;
        }
    }

    public void resume()
    {
        if (isPlaying)
        {
            isPaused = false;
        }
    }

    public void update(GameTime gameTime, Bone root, float speed, float rotation, float scale)
    {
        float factor = frameInterpolator.getFactor();
        root.update(currentFrame, nextFrame, factor, rotation, scale);

        if (isPlaying)
        {
            if (!isPaused)
            {
                frameInterpolator.update(gameTime, speed);

                if (factor >= 1f && frameInterpolator.autoAdvanceFrame())
                {
                    currentFramePosition = advanceFramePosition(currentFramePosition);
                    nextFramePosition = advanceFramePosition(nextFramePosition);
                    currentFrame = frames.get(currentFramePosition);
                    nextFrame = frames.get(nextFramePosition);

                    if (!loop && currentFramePosition == frames.size - 1)
                    {
                        isPlaying = false;
                        isPaused = false;

                        onAnimationEndArgs.sender = this;
                        onAnimationEnd.invoke(onAnimationEndArgs);
                    }
                    else
                    {
                        frameInterpolator.start(currentFrame, nextFrame, 0f);
                    }
                }
            }
        }
    }

    private int advanceFramePosition(int position)
    {
        position++;

        if (position >= frames.size)
        {
            if (loop)
            {
                position = 0;
            }
            else
            {
                position = frames.size - 1;
            }
        }

        return position;
    }

    public void addFrame(Frame frame)
    {
        frames.add(frame);
    }

    public Animation copy()
    {
        Animation copy = new Animation(name, loop);
        Array<Frame> localFrames = frames;

        for (int i = 0, n = localFrames.size; i < n; i++)
        {
            copy.addFrame(localFrames.get(i).copy());
        }

        return copy;
    }
}
