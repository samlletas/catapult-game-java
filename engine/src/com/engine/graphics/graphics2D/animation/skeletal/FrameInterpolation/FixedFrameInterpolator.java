package com.engine.graphics.graphics2D.animation.skeletal.FrameInterpolation;

import com.engine.GameTime;
import com.engine.graphics.graphics2D.animation.skeletal.Frame;

public class FixedFrameInterpolator implements IFrameInterpolator
{
    public float factor = 0f;

    @Override
    public void start(Frame current, Frame next, float timeOffset)
    {
        factor = 0f;
    }

    @Override
    public float getFactor()
    {
        return factor;
    }

    @Override
    public boolean autoAdvanceFrame()
    {
        return false;
    }

    @Override
    public void update(GameTime gameTime, float speed)
    {

    }
}
