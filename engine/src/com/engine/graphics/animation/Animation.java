package com.engine.graphics.animation;

import com.badlogic.gdx.utils.Array;
import com.engine.GameTime;
import com.engine.graphics.animation.events.OnEndEvent;

public class Animation
{
    public String name;
    public float speed;

    private boolean loop;
    private Array<Frame> frames;
    private int currentFramePosition;
    private int nextFramePosition;
    private float totalFrameTime;
    private float currentFrameTime;
    private boolean isPlaying;

    public OnEndEvent onEnd = new OnEndEvent();

    public Animation(String name, float speed, boolean loop)
    {
        this.name = name;
        this.speed = speed;
        this.loop = loop;

        this.frames = new Array<Frame>();

        this.currentFramePosition = -1;
        this.nextFramePosition = -1;
        this.totalFrameTime = 0.0f;
        this.currentFrameTime = 0.0f;
        this.isPlaying = false;
    }

    public void start()
    {
        currentFramePosition = 0;
        nextFramePosition = advanceFramePosition(currentFramePosition);

        totalFrameTime = frames.get(currentFramePosition).duration;
        currentFrameTime = totalFrameTime;
        isPlaying = true;
    }

    public void update(GameTime gameTime, Bone root, float rotation, float scale)
    {
        Frame currentFrame = frames.get(currentFramePosition);
        Frame nextFrame = frames.get(nextFramePosition);

        root.update(gameTime, currentFrame, nextFrame, totalFrameTime, currentFrameTime, rotation, scale);

        if (isPlaying)
        {
            currentFrameTime -= (gameTime.delta * 1000f) * speed;
        }

        if (isPlaying && currentFrameTime <= 0.0f)
        {
            currentFramePosition = advanceFramePosition(currentFramePosition);
            nextFramePosition = advanceFramePosition(nextFramePosition);

            if (!loop && currentFramePosition == frames.size - 1)
            {
                isPlaying = false;
                currentFrameTime = 0.0f;
                onEnd.invoke(this);
            }
            else
            {
                totalFrameTime = frames.get(currentFramePosition).duration;
                currentFrameTime = totalFrameTime;
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
}
