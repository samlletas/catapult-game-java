package com.engine.Interpolation;

import com.badlogic.gdx.math.MathUtils;

public class Interpolators
{
    public static IInterpolator LinearInterpolator = new IInterpolator()
    {
        @Override
        public float interpolate(float x1, float x2, float factor)
        {
            factor = MathUtils.clamp(factor, 0f, 1f);

            return x1 + ((x2 - x1) * factor);
        }
    };

    public static IInterpolator CosineInterpolator = new IInterpolator()
    {
        @Override
        public float interpolate(float x1, float x2, float factor)
        {
            factor = MathUtils.clamp(factor, 0f, 1f);

            float half = (x2 - x1) / 2f;

            return x1 + half + (half  * MathUtils.cos(MathUtils.PI * factor));
        }
    };
}
