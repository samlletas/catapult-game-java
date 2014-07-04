package com.engine;

import com.badlogic.gdx.graphics.Color;
import com.engine.utilities.ColorUtilities;

public class GameSettings
{
    public final int virtualWidth;
    public final int virtualHeight;
    public Color clearColor;

    public GameSettings(int virtualWidth, int virtualHeight)
    {
        this(virtualWidth, virtualHeight, new Color(
                ColorUtilities.ByteToFloat(100),
                ColorUtilities.ByteToFloat(149),
                ColorUtilities.ByteToFloat(237),
                1f));
    }

    public GameSettings(int virtualWidth, int virtualHeight, Color clearColor)
    {
        this.virtualWidth = virtualWidth;
        this.virtualHeight = virtualHeight;
        this.clearColor = clearColor;
    }
}
