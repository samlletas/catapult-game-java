package com.engine.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.engine.utilities.ColorUtilities;

public class TextureRegionActor extends CustomActor
{
    private TextureRegion region;

    private float customWidth;
    private float customHeight;
    private boolean hasCustomWidth;
    private boolean hasCustomHeight;
    private boolean isVisible = true;

    public TextureRegionActor(TextureRegion region)
    {
        this.region = region;
    }

    public TextureRegion getRegion()
    {
        return region;
    }

    public void setRegion(TextureRegion region)
    {
        this.region = region;
    }

    @Override
    public float getWidth()
    {
        if (hasCustomWidth)
        {
            return customWidth;
        }
        else
        {
            return region.getRegionWidth();
        }
    }

    @Override
    public float getHeight()
    {
        if (hasCustomHeight)
        {
            return customHeight;
        }
        else
        {
            return region.getRegionHeight();
        }
    }

    @Override
    public void setWidth(float width)
    {
        customWidth = width;
        hasCustomWidth = true;
    }

    @Override
    public void setHeight(float height)
    {
        customHeight = height;
        hasCustomHeight = true;
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
        if (isVisible)
        {
            Color originalColor = batch.getColor();
            Color actorColor = getColor();

            float alpha = parentAlpha * actorColor.a;
            float drawX = getX();
            float drawY = getY();
            float width = getWidth();
            float height = getHeight();
            float originX = 0f;
            float originY = 0f;

            ActorOrigin actorOrigin = getActorOrigin();

            switch (actorOrigin)
            {
                case Custom:
                    originX = getOriginX();
                    originY = getOriginY();
                    break;
                case TopCenter:
                    originX = width / 2f;
                    break;
                case TopRight:
                    originX = width;
                    break;
                case CenterLeft:
                    originY = height / 2f;
                    break;
                case Center:
                    originX = width / 2f;
                    originY = height / 2f;
                    break;
                case CenterRight:
                    originX = width;
                    originY = height / 2f;
                    break;
                case BottomLeft:
                    originY = height;
                    break;
                case BottomCenter:
                    originX = width / 2f;
                    originY = height;
                    break;
                case BottomRight:
                    originX = width;
                    originY = height;
                    break;
            }

            drawX -= originX;
            drawY -= originY;

            ColorUtilities.setColor(batch, actorColor.r, actorColor.g, actorColor.b, alpha);
            batch.draw(region, drawX, drawY, originX, originY, width, height,
                    getScaleX(), getScaleY(), getRotation());
            ColorUtilities.setColor(batch, originalColor);
        }
    }
}
