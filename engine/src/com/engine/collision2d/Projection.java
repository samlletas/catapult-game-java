package com.engine.collision2d;

public class Projection
{
    float min;
    float max;

    public Projection()
    {
        min = 0f;
        max = 0f;
    }

    public boolean overlaps(Projection other)
    {
        return !(other.max < min || other.min > max);
    }
}
