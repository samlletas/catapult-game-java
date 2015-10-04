package com.engine.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;

public class DistanceFieldFontActor extends CustomActor
{
    private CharSequence text;
    private float fontBaseScaleX;
    private float fontBaseScaleY;
    private boolean isVisible = true;

    public DistanceFieldFontActor()
    {
        this(null);
    }

    public DistanceFieldFontActor(CharSequence text)
    {
        this.text = text;
        this.fontBaseScaleX = 1f;
        this.fontBaseScaleY = 1f;
    }

    public CharSequence getText()
    {
        return text;
    }

    public void setText(CharSequence text)
    {
        this.text = text;
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
     * @param font font
     * @return los bounds de la fuente al ser dibujada
     */
    public BitmapFont.TextBounds draw(DistanceFieldRenderer renderer,
                                      DistanceFieldFont font)
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

            BitmapFont.TextBounds bounds = font.getFont().getBounds(text);
            ActorOrigin actorOrigin = getActorOrigin();

            switch (actorOrigin)
            {
                case Custom:
                    drawX -= getOriginX() * scaleX;
                    drawY -= getOriginY() * scaleY;
                    break;
                case TopCenter:
                    drawX -= bounds.width / 2f;
                    break;
                case TopRight:
                    drawX -= bounds.width;
                    break;
                case CenterLeft:
                    drawY -= bounds.height / 2f;
                    break;
                case Center:
                    drawX -= bounds.width / 2f;
                    drawY -= bounds.height / 2f;
                    break;
                case CenterRight:
                    drawX -= bounds.width;
                    drawY -= bounds.height / 2f;
                    break;
                case BottomLeft:
                    drawY -= bounds.height;
                    break;
                case BottomCenter:
                    drawX -= bounds.width / 2f;
                    drawY -= bounds.height;
                    break;
                case BottomRight:
                    drawX -= bounds.width;
                    drawY -= bounds.height;
                    break;
            }

            bounds = renderer.draw(text, drawX, drawY);

            font.setColor(originalFontColor);
            font.setScale(originalScaleX, originalScaleY);

            return bounds;
        }

        return null;
    }

    /**
     * Dibuja una fuente DistanceField con los valores del frame actual
     * Nota: La rotación no se toma en cuenta (para evitar un drawCall extra)
     *
     * @param renderer renderer
     * @param font font
     * @return los bounds de la fuente al ser dibujada
     */
    public BitmapFont.TextBounds drawMultiline(DistanceFieldRenderer renderer,
                                      DistanceFieldFont font)
    {
        return drawMultiline(renderer, font, BitmapFont.HAlignment.LEFT);
    }

    /**
     * Dibuja una fuente DistanceField con los valores del frame actual
     * Nota: La rotación no se toma en cuenta (para evitar un drawCall extra)
     *
     * @param renderer renderer
     * @param font font
     * @param alignment alignment
     * @return los bounds de la fuente al ser dibujada
     */
    public BitmapFont.TextBounds drawMultiline(DistanceFieldRenderer renderer,
                                               DistanceFieldFont font,
                                               BitmapFont.HAlignment alignment)
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

            BitmapFont.TextBounds bounds = font.getFont().getMultiLineBounds(text);
            ActorOrigin actorOrigin = getActorOrigin();

            switch (actorOrigin)
            {
                case Custom:
                    drawX -= getOriginX() * scaleX;
                    drawY -= getOriginY() * scaleY;
                    break;
                case TopCenter:
                    drawX -= bounds.width / 2f;
                    break;
                case TopRight:
                    drawX -= bounds.width;
                    break;
                case CenterLeft:
                    drawY -= bounds.height / 2f;
                    break;
                case Center:
                    drawX -= bounds.width / 2f;
                    drawY -= bounds.height / 2f;
                    break;
                case CenterRight:
                    drawX -= bounds.width;
                    drawY -= bounds.height / 2f;
                    break;
                case BottomLeft:
                    drawY -= bounds.height;
                    break;
                case BottomCenter:
                    drawX -= bounds.width / 2f;
                    drawY -= bounds.height;
                    break;
                case BottomRight:
                    drawX -= bounds.width;
                    drawY -= bounds.height;
                    break;
            }

            bounds = renderer.drawMultiLine(text, drawX, drawY, bounds.width, alignment);

            font.setColor(originalFontColor);
            font.setScale(originalScaleX, originalScaleY);

            return bounds;
        }

        return null;
    }
}
