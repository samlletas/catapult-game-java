package com.engine.graphics.graphics2D.animation.skeletal;

import com.badlogic.gdx.utils.Array;
import com.engine.GameTime;
import com.engine.events.Event;
import com.engine.events.EventsArgs;

public class Animation
{
    public final String name;
    public final boolean loop;

    private Array<Frame> frames;
    private int currentFramePosition;
    private int nextFramePosition;
    private float totalFrameTime;
    private float currentFrameTime;
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
        this.totalFrameTime = 0.0f;
        this.currentFrameTime = 0.0f;

        this.isPlaying = false;
        this.isPaused = false;
    }

    public void start(float timeOffset)
    {
        currentFramePosition = 0;
        nextFramePosition = advanceFramePosition(currentFramePosition);

        totalFrameTime = frames.get(currentFramePosition).duration;
        currentFrameTime = Math.max(0f, totalFrameTime - timeOffset);

        isPlaying = true;
        isPaused = false;
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
        Frame currentFrame = frames.get(currentFramePosition);
        Frame nextFrame = frames.get(nextFramePosition);
        float factor = 1f - (currentFrameTime / totalFrameTime);

        root.update(currentFrame, nextFrame, factor, rotation, scale);

        if (isPlaying)
        {
            if (!isPaused)
            {
                currentFrameTime -= (gameTime.delta * 1000f) * speed;

                if (currentFrameTime <= 0.0f)
                {
                    currentFramePosition = advanceFramePosition(currentFramePosition);
                    nextFramePosition = advanceFramePosition(nextFramePosition);

                    if (!loop && currentFramePosition == frames.size - 1)
                    {
                        isPlaying = false;
                        isPaused = false;

                        currentFrameTime = 0.0f;

                        onAnimationEndArgs.sender = this;
                        onAnimationEnd.invoke(onAnimationEndArgs);
                    }
                    else
                    {
                        totalFrameTime = frames.get(currentFramePosition).duration;
                        currentFrameTime = totalFrameTime;
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
