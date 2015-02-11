package com.engine.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.engine.graphics.GraphicsSettings;
import com.engine.utilities.ColorUtilities;

public final class Overlay extends Actor
{
    private final GraphicsSettings graphicsSettings;

    public Overlay(GraphicsSettings graphicsSettings)
    {
        this(graphicsSettings, Color.BLACK);
    }

    public Overlay(GraphicsSettings graphicsSettings, Color color)
    {
        this(graphicsSettings, color, 0.5f);
    }

    public Overlay(GraphicsSettings graphicsSettings, Color color, float alpha)
    {
        this.graphicsSettings = graphicsSettings;
        setColor(color.r, color.g, color.b, alpha);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        throw new GdxRuntimeException("Please use the draw(SpriteBatch spriteBatch, TextureRegion pixelRegion) function.");
    }

    public void draw(SpriteBatch spriteBatch, TextureRegion pixelRegion)
    {
        Color originalColor = spriteBatch.getColor();

        ColorUtilities.setColor(spriteBatch, getColor());
        spriteBatch.draw(pixelRegion, 0f, 0f, graphicsSettings.virtualWidth,
                graphicsSettings.virtualHeight);
        ColorUtilities.setColor(spriteBatch, originalColor);
    }
}
