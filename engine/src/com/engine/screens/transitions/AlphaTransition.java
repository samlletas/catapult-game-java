package com.engine.screens.transitions;

import com.engine.graphics.GraphicsSettings;
import com.engine.screens.Overlay;

public abstract class AlphaTransition extends Transition
{
    protected Overlay overlay;

    public AlphaTransition(GraphicsSettings graphicsSettings, Overlay overlay)
    {
        super(graphicsSettings);

        this.overlay = overlay;
    }

    public Overlay getOverlay()
    {
        return overlay;
    }

    public void setOverlay(Overlay overlay)
    {
        this.overlay = overlay;
    }
}
