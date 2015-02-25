package com.mygdx.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.graphics.GraphicsSettings;
import com.engine.screens.Overlay;
import com.engine.screens.Screen;
import com.engine.screens.transitions.AlphaTransition;
import com.engine.utilities.ColorUtilities;
import com.mygdx.game.Global;

public class OverlayedScreen extends Screen
{
    private Color tempColor;

    public OverlayedScreen(String name, GraphicsSettings graphicsSettings,
                           Viewport viewport2D, Viewport viewport3D,
                           Batch batch)
    {
        super(name, graphicsSettings, viewport2D, viewport3D, batch);

        tempColor = ColorUtilities.createColor(0, 0, 0, 0);
    }

    protected Color getBackgroundForeColor()
    {
        return getBackgroundForeColor(Global.Colors.MAIN_BACKGROUND);
    }

    protected Color getBackgroundForeColor(Color backgroundColor)
    {
        tempColor.set(getTransitionForeColor());
        return ColorUtilities.alphaBlend(backgroundColor, tempColor, false);
    }

    protected Color getBackgroundForeColor(Overlay overlay)
    {
        return getBackgroundForeColor(Global.Colors.MAIN_BACKGROUND, overlay);
    }

    protected Color getBackgroundForeColor(Color backgroundColor, Overlay overlay)
    {
        tempColor.set(getTransitionForeColor(overlay));
        return ColorUtilities.alphaBlend(backgroundColor, tempColor, false);
    }

    protected Color getTransitionForeColor(Overlay overlay)
    {
        return ColorUtilities.alphaBlend(overlay.getColor(), getTransitionForeColor(), true);
    }

    protected Color getTransitionForeColor()
    {
        if (!screenManager.isOnTransition())
        {
            return Global.Colors.NO_OVERLAY;
        }
        else
        {
            AlphaTransition transition = (AlphaTransition)screenManager.getCurrentTransition();
            return transition.getOverlay().getColor();
        }
    }
}
