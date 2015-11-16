package com.engine.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;

public class DistanceFieldFontActor extends CustomActor
{
    private DistanceFieldFont font;
    private CharSequence text;
    private GlyphLayout glyphLayout;

    private float fontBaseScaleX;
    private float fontBaseScaleY;
    private boolean isVisible = true;

    public DistanceFieldFontActor(DistanceFieldFont font)
    {
        this(font, "");
    }

    public DistanceFieldFontActor(DistanceFieldFont font, CharSequence text)
    {
        this.font = font;
        this.text = text;
        this.glyphLayout = new GlyphLayout(font.getFont(), text);

        this.fontBaseScaleX = 1f;
        this.fontBaseScaleY = 1f;
    }

    public CharSequence getText()
    {
        return text;
    }

    public void setText(CharSequence text)
    {
        setText(text, 0, text.length(), 0, Align.left, false, null);
    }

    public void setText(CharSequence text, int start, int end, float targetWidth, int halign, boolean wrap,
                        String truncate)
    {
        this.text = text;
        this.glyphLayout.setText(font.getFont(), text, start, end, getColor(), targetWidth, halign, wrap, truncate);
    }

    public DistanceFieldFont getFont()
    {
        return font;
    }

    public void setFont(DistanceFieldFont font)
    {
        this.font = font;
        this.glyphLayout.setText(font.getFont(), text);
    }

    public void setFontBaseScale(float scaleX, float scaleY)
    {
        fontBaseScaleX = scaleX;
        fontBaseScaleY = scaleY;
    }

    public void setFontBaseScale(float scale)
    {
        fontBaseScaleX = scale;
        fontBaseScaleY = scale;
    }

    public void setFontBaseScaleX(float fontBaseScaleX)
    {
        this.fontBaseScaleX = fontBaseScaleX;
    }

    public void setFontBaseScaleY(float fontBaseScaleY)
    {
        this.fontBaseScaleY = fontBaseScaleY;
    }

    public float getFontBaseScaleX()
    {
        return fontBaseScaleX;
    }

    public float getFontBaseScaleY()
    {
        return fontBaseScaleY;
    }

    @Override
    public void setVisible(boolean visible)
    {
        this.isVisible = visible;
    }

    public boolean isVisible()
    {
        return isVisible;
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        throw new GdxRuntimeException("Please use the draw(DistanceFieldRenderer renderer, DistanceFieldFont font) function.");
    }

    /**
     * Dibuja una fuente DistanceField con los valores del frame actual
     * Nota: La rotación no se toma en cuenta (para evitar un drawCall extra)
     *
     * @param renderer renderer
     */
    public void draw(DistanceFieldRenderer renderer)
    {
        if (isVisible && text != null)
        {
            Color originalFontColor = font.getColor();
            float originalScaleX = font.getScaleX();
            float originalScaleY = font.getScaleY();

            float scaleX = fontBaseScaleX * getScaleX();
            float scaleY = fontBaseScaleY * getScaleY();

            font.setColor(getColor());
            font.setScale(scaleX, scaleY);

            float drawX = getX();
            float drawY = getY();

            ActorOrigin actorOrigin = getActorOrigin();

            switch (actorOrigin)
            {
                case Custom:
                    drawX -= getOriginX() * scaleX;
                    drawY -= getOriginY() * scaleY;
                    break;
                case TopCenter:
                    drawX -= glyphLayout.width / 2f;
                    break;
                case TopRight:
                    drawX -= glyphLayout.width;
                    break;
                case CenterLeft:
                    drawY -= glyphLayout.height / 2f;
                    break;
                case Center:
                    drawX -= glyphLayout.width / 2f;
                    drawY -= glyphLayout.height / 2f;
                    break;
                case CenterRight:
                    drawX -= glyphLayout.width;
                    drawY -= glyphLayout.height / 2f;
                    break;
                case BottomLeft:
                    drawY -= glyphLayout.height;
                    break;
                case BottomCenter:
                    drawX -= glyphLayout.width / 2f;
                    drawY -= glyphLayout.height;
                    break;
                case BottomRight:
                    drawX -= glyphLayout.width;
                    drawY -= glyphLayout.height;
                    break;
            }

            renderer.draw(font, glyphLayout, drawX, drawY);

            font.setColor(originalFontColor);
            font.setScale(originalScaleX, originalScaleY);
        }
    }
}
