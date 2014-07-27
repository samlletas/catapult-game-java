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
        // Color de fondo por default (CornFlower Blue)
        this(virtualWidth, virtualHeight,
                ColorUtilities.createColor(100, 149, 237, 255));
    }

    public GameSettings(int virtualWidth, int virtualHeight, Color clearColor)
    {
        this.virtualWidth = virtualWidth;
        this.virtualHeight = virtualHeight;
        this.clearColor = clearColor;
    }
}
