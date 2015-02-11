package com.engine.graphics.graphics2D.text;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class DistanceFieldRenderer implements Disposable
{
    private SpriteBatch spriteBatch;
    private DistanceFieldShader shader;
    private DistanceFieldFont distanceFieldFont; // Font usada para dibujar
    private float drawScale;
    private boolean drawing;

    public DistanceFieldRenderer(SpriteBatch spriteBatch)
    {
        this(spriteBatch, new DistanceFieldShader());
    }

    public DistanceFieldRenderer(SpriteBatch spriteBatch, DistanceFieldShader shader)
    {
        this.spriteBatch = spriteBatch;
        this.shader = shader;
        this.drawing = false;
    }

    public void begin(DistanceFieldFont font)
    {
        begin(font, 1f);
    }

    public void begin(DistanceFieldFont font, float scale)
    {
        if (!drawing)
        {
            spriteBatch.setShader(shader);
            spriteBatch.begin();

            distanceFieldFont = font;
            drawScale = scale;
            drawing = true;
        }
        else
        {
            throw new IllegalStateException("DistanceFieldRenderer.end must be called before begin.");
        }
    }

    public void end()
    {
        if (drawing)
        {
            shader.thickness = distanceFieldFont.thickness;
            shader.smoothing = distanceFieldFont.sharpness / (distanceFieldFont.spread * drawScale);
            shader.sendCustomUniforms();

            spriteBatch.end();
            spriteBatch.setShader(null);

            distanceFieldFont = null;
            drawing = false;
        }
        else
        {
            throw new IllegalStateException("DistanceFieldRenderer.begin must be called before end.");
        }
    }

    public BitmapFont.TextBounds draw(CharSequence str, float x, float y)
    {
        return distanceFieldFont.font.draw(spriteBatch, str, x, y);
    }

    public BitmapFont.TextBounds draw(CharSequence str, float x, float y,
                                      int start, int end)
    {
        return distanceFieldFont.font.draw(spriteBatch, str, x, y, start, end);
    }

    public BitmapFont.TextBounds drawMultiLine(CharSequence str, float x, float y)
    {
        return distanceFieldFont.font.drawMultiLine(spriteBatch, str, x, y);
    }

    public BitmapFont.TextBounds drawMultiLine(CharSequence str, float x, float y,
                                               float alignmentWidth,
                                               BitmapFont.HAlignment alignment)
    {
        return distanceFieldFont.font.drawMultiLine(spriteBatch, str, x, y,
                alignmentWidth, alignment);
    }

    public BitmapFont.TextBounds drawWrapped(CharSequence str, float x, float y,
                                             float wrapWidth)
    {
        return distanceFieldFont.font.drawWrapped(spriteBatch, str, x, y, wrapWidth);
    }

    public BitmapFont.TextBounds drawWrapped(CharSequence str,float x, float y,
                                             float wrapWidth,
                                             BitmapFont.HAlignment alignment)
    {
        return distanceFieldFont.font.drawWrapped(spriteBatch, str, x, y,
                wrapWidth, alignment);
    }

    @Override
    public void dispose()
    {
        shader.dispose();
    }
}
