package com.engine.Interpolation;

import com.badlogic.gdx.math.MathUtils;

public class Interpolators
{
    static final float PI_DIV_BY_2 = MathUtils.PI / 2f;

    public static IInterpolator LinearInterpolator = new IInterpolator()
    {
        @Override
        public float interpolate(float x1, float x2, float factor)
        {
            factor = MathUtils.clamp(factor, 0f, 1f);

            return x1 + ((x2 - x1) * factor);
        }
    };

    public static IInterpolator SineInterpolator = new IInterpolator()
    {
        @Override
        public float interpolate(float x1, float x2, float factor)
        {
            factor = MathUtils.clamp(factor, 0f, 1f);
            factor = MathUtils.sin(PI_DIV_BY_2 * factor);

            return x1 + ((x2 - x1) * factor);
        }
    };
}
