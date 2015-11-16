package com.engine.graphics.graphics2D.text;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

public class DistanceFieldRenderer implements Disposable
{
    private Batch batch;
    private DistanceFieldShader shader;

    private DistanceFieldFont currentFont;
    private float drawScale;

    private boolean drawing;
    private boolean flushed;

    public DistanceFieldRenderer(Batch batch)
    {
        this(batch, new DistanceFieldShader());
    }

    public DistanceFieldRenderer(Batch batch, DistanceFieldShader shader)
    {
        this.batch = batch;
        this.shader = shader;
        this.drawing = false;
    }

    public void begin()
    {
        begin(1f);
    }

    public void begin(float scale)
    {
        if (!drawing)
        {
            batch.setShader(shader);
            batch.begin();

            currentFont = null;
            drawScale = scale;

            drawing = true;
            flushed = false;
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
            if (!flushed)
            {
                flush();
            }

            batch.end();
            batch.setShader(null);

            currentFont = null;
            drawing = false;
        }
        else
        {
            throw new IllegalStateException("DistanceFieldRenderer.begin must be called before end.");
        }
    }

    private void flush()
    {
        shader.thickness = currentFont.thickness;
        shader.smoothing = currentFont.sharpness / (currentFont.spread * drawScale);
        shader.sendCustomUniforms();
        batch.flush();

        flushed = true;
    }

    public GlyphLayout draw(DistanceFieldFont font, CharSequence str, float x, float y)
    {
        return draw(font, str, x, y, 0, Align.left, false);
    }

    public GlyphLayout draw(DistanceFieldFont font, CharSequence str, float x, float y, float targetWidth, int halign,
                            boolean wrap)
    {
        return draw(font, str, x, y, 0, str.length(), targetWidth, halign, wrap);
    }

    public GlyphLayout draw(DistanceFieldFont font, CharSequence str, float x, float y, int start, int end,
                            float targetWidth, int halign, boolean wrap)
    {
        if (currentFont != null && currentFont.getFont() != font.getFont())
        {
            flush();
        }

        GlyphLayout layout = font.getFont().draw(batch, str, x, y, start, end, targetWidth, halign, wrap);

        currentFont = font;
        flushed = false;

        return layout;
    }
    public void draw(DistanceFieldFont font, GlyphLayout layout, float x, float y)
    {
        if (currentFont != null && currentFont.getFont() != font.getFont())
        {
            flush();
        }

        font.getFont().draw(batch, layout, x, y);

        currentFont = font;
        flushed = false;
    }

    @Override
    public void dispose()
    {
        shader.dispose();
    }
}
