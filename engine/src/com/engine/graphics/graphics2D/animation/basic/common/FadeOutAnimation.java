package com.engine.graphics.graphics2D.animation.basic.common;

import com.badlogic.gdx.math.Interpolation;
import com.engine.graphics.graphics2D.animation.basic.BasicAnimation;
import com.engine.graphics.graphics2D.animation.basic.BasicFrame;

public class FadeOutAnimation extends BasicAnimation
{
    public FadeOutAnimation(float duration, Interpolation interpolation)
    {
        super(false,
                new BasicFrame(duration, 0f, 0f, 0f, 0f, 0f, 1f, interpolation),
                new BasicFrame(0f, 0f, 0f, 0f, 0f, 0f, 0f, interpolation));
    }
}
