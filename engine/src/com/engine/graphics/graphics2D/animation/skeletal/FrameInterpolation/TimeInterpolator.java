package com.engine.graphics.graphics2D.animation.skeletal.FrameInterpolation;

import com.engine.GameTime;
import com.engine.graphics.graphics2D.animation.skeletal.Frame;

public class TimeInterpolator implements IFrameInterpolator
{
    private float totalFrameTime;
    private float currentFrameTime;

    public TimeInterpolator()
    {
        totalFrameTime = 0f;
        currentFrameTime = 0f;
    }

    @Override
    public void start(Frame current, Frame next, float timeOffset)
    {
        totalFrameTime = current.duration;
        currentFrameTime = Math.max(0f, totalFrameTime - timeOffset);
    }

    @Override
    public float getFactor()
    {
        return 1f - (currentFrameTime / totalFrameTime);
    }

    @Override
    public boolean autoAdvanceFrame()
    {
        return true;
    }

    @Override
    public void update(GameTime gameTime, float speed)
    {
        currentFrameTime -= (gameTime.delta * 1000f) * speed;
    }
}
