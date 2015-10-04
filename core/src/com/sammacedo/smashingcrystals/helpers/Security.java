package com.sammacedo.smashingcrystals.helpers;

public final class Security
{
    public static int protect(int value)
    {
        return value - 587493;
    }

    public static int recover(int value)
    {
        return value + 587493;
    }
}
