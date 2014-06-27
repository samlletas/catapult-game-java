package com.engine.utilities;

import com.badlogic.gdx.math.MathUtils;

public class Interpolation
{
    static public final float PI_DIV_BY_2 = MathUtils.PI / 2f;

    public static float linear(float x1, float x2, float factor)
    {
        factor = MathUtils.clamp(factor, 0f, 1f);

        return x1 + ((x2 - x1) * factor);
    }

    public static float sine(float x1, float x2, float factor)
    {
        factor = MathUtils.clamp(factor, 0f, 1f);
        factor = MathUtils.sin(PI_DIV_BY_2 * factor);

        return x1 + ((x2 - x1) * factor);
    }
}
