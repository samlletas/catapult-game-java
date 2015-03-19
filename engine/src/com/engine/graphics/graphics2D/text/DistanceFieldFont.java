package com.engine.graphics.graphics2D.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public final class DistanceFieldFont
{
    final BitmapFont font;

    final float spread;
    final float thickness;
    final float sharpness;

    public DistanceFieldFont(BitmapFont font, float spread)
    {
        this(font, spread, 0.5f);
    }

    public DistanceFieldFont(BitmapFont font, float spread, float thickness)
    {
        this(font, spread, thickness, 0.25f);
    }

    public DistanceFieldFont(BitmapFont font, float spread, float thickness, float sharpness)
    {
        this.font = font;
        this.spread = spread;
        this.thickness = thickness;
        this.sharpness = sharpness;
    }

    public BitmapFont getFont()
    {
        return font;
    }

    public float getSpread()
    {
        return spread;
    }

    public void setScale(float scaleXY)
    {
        font.setScale(scaleXY);
    }

    public void setScale(float scaleX, float scaleY)
    {
        font.setScale(scaleX, scaleY);
    }

    public float getScaleX()
    {
        return font.getScaleX();
    }

    public float getScaleY()
    {
        return font.getScaleY();
    }

    public void setColor(Color color)
    {
        font.setColor(color);
    }

    public void setColor(float r, float g, float b, float a)
    {
        font.setColor(r, g, b, a);
    }

    public Color getColor()
    {
        return font.getColor();
    }

    public void setAlpha(float a)
    {
        Color originalColor = font.getColor();
        font.setColor(originalColor.r, originalColor.g, originalColor.b, a);
    }

    public float getAlpha()
    {
        return font.getColor().a;
    }

    public BitmapFont.TextBounds getBounds(CharSequence str)
    {
        return font.getBounds(str);
    }

    public void setUseIntegerPositions(boolean integer)
    {
        font.setUseIntegerPositions(integer);
    }

    public void setFixedWidthGlyphs(CharSequence glyphs)
    {
        font.setFixedWidthGlyphs(glyphs);
    }
}
