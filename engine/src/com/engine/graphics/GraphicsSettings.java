package com.engine.graphics;

import com.badlogic.gdx.graphics.Color;
import com.engine.utilities.ColorUtilities;

public class GraphicsSettings
{
    public final int virtualWidth;
    public final int virtualHeight;
    public Color clearColor;

    public GraphicsSettings(int virtualWidth, int virtualHeight)
    {
        this(virtualWidth, virtualHeight,
                ColorUtilities.createColor(100, 149, 237, 255)); // Color CornFlowerBlue
    }

    public GraphicsSettings(int virtualWidth, int virtualHeight, Color clearColor)
    {
        this.virtualWidth = virtualWidth;
        this.virtualHeight = virtualHeight;
        this.clearColor = clearColor;
    }
}
