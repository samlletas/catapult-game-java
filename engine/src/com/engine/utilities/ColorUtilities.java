package com.engine.utilities;

public final class ColorUtilities
{
    public static float ByteToFloat(int color)
    {
        return (float)color / 255f;
    }

    public static int FloatToByte(float color)
    {
        return (int)(color * 255f);
    }
}
