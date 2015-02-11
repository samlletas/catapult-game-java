package com.engine.screens.transitions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.engine.GameTime;
import com.engine.graphics.GraphicsSettings;

public class OptimizedFadeInTransition extends FadeInTransition
{
    public OptimizedFadeInTransition(GraphicsSettings settings, float duration,
                                     Color color)
    {
        super(settings, null, null, duration, color);
    }

    @Override
    protected void onDraw(GameTime gameTime)
    {

    }
}
