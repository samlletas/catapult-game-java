package com.engine.graphics.graphics2D.animation.skeletal;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Bone
{
    private TextureAtlas.AtlasRegion region;

    public final int id;
    public final int parentId;

    public float pivotX;
    public float pivotY;

    public float offsetX;
    public float offsetY;

    private Bone parent;
    private Array<Bone> childs;

    // Valores finales de dibujado
    private float finalX;
    private float finalY;
    private float finalScaleX;
    private float finalScaleY;
    private float finalRotation;

    public Bone(TextureAtlas.AtlasRegion region, int id, int parentId,
                float pivotX, float pivotY, float offsetX, float offsetY)
    {
        this.region = region;
        this.id = id;
        this.parentId = parentId;
        this.pivotX = pivotX;
        this.pivotY = pivotY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public float getFinalX()
    {
        return finalX;
    }

    public float getFinalY()
    {
        return finalY;
    }

    public float getFinalScaleX()
    {
        return finalScaleX;
    }

    public float getFinalScaleY()
    {
        return finalScaleY;
    }

    public float getFinalRotation()
    {
        return -finalRotation;
    }

    public boolean isRoot()
    {
        return parent == null;
    }

    private Vector2 transformed = new Vector2(0f, 0f);

    public Vector2 getTransformedPosition(float x, float y)
    {
        float diffX = (x - pivotX) * finalScaleX;
        float diffY = (y - pivotY) * finalScaleY;
        float distance = (float)Math.sqrt((diffX * diffX) + (diffY * diffY));
        float angle = MathUtils.atan2(diffY, diffX) + (float)Math.toRadians(finalRotation);

        transformed.x = finalX + pivotX + (distance * MathUtils.cos(angle));
        transformed.y = finalY + pivotY + (distance * MathUtils.sin(angle));

        return transformed;
    }

    public void addChild(Bone child)
    {
        child.parent = this;

        if (childs == null)
        {
            childs = new Array<Bone>();
        }

        childs.add(child);
    }

    public void update(Frame currentFrame, Frame nextFrame, float factor,
                       float globalRotation, float globalScale)
    {
        FrameData current = currentFrame.getFrameData(id);
        FrameData next = nextFrame.getFrameData(id);
        Interpolation interpolation = current.interpolation;

        finalRotation = getRotationInterpolation(current.rotation, next.rotation, interpolation, factor);
        finalScaleX = interpolation.apply(current.scaleX, next.scaleX, factor) * globalScale;
        finalScaleY = interpolation.apply(current.scaleY, next.scaleY, factor) * globalScale;

        float offsetDistance = interpolation.apply(current.offsetDistance, next.offsetDistance, factor);
        float offsetDirection = getRotationInterpolation(current.offsetDirection, next.offsetDirection, interpolation, factor);

        if (isRoot())
        {
            float frameOffsetX = offsetDistance * MathUtils.cosDeg(offsetDirection - globalRotation);
            float frameOffsetY = offsetDistance * MathUtils.sinDeg(offsetDirection - globalRotation);

            finalX = offsetX + (frameOffsetX * globalScale) - pivotX;
            finalY = offsetY + (frameOffsetY * globalScale) - pivotY;

            finalRotation -= globalRotation;
        }
        else
        {
            float frameOffsetX = offsetDistance * MathUtils.cosDeg(offsetDirection);
            float frameOffsetY = offsetDistance * MathUtils.sinDeg(offsetDirection);

            float diffX = ((offsetX + frameOffsetX) - parent.pivotX) * parent.finalScaleX;
            float diffY = ((offsetY + frameOffsetY) - parent.pivotY) * parent.finalScaleY;

            float distance = (float)Math.sqrt((diffX * diffX) + (diffY * diffY));
            float angle = MathUtils.atan2(diffY, diffX) + (float)Math.toRadians(parent.finalRotation);

            finalX = ((parent.finalX + parent.pivotX) - pivotX) + (distance * MathUtils.cos(angle));
            finalY = ((parent.finalY + parent.pivotY) - pivotY) + (distance * MathUtils.sin(angle));

            finalRotation += parent.finalRotation;
        }

        if (childs != null)
        {
            Array<Bone> localChilds = childs;

            for (int i = 0, n = localChilds.size; i < n; i++)
            {
                localChilds.get(i).update(currentFrame, nextFrame, factor, globalRotation, globalScale);
            }
        }
    }

    private float getRotationInterpolation(float currentRotation,
                                           float nextRotation,
                                           Interpolation interpolation,
                                           float factor)
    {
        // Interpolación de rotación directa
        if (Math.abs(nextRotation - currentRotation) <= 180f)
        {
            return interpolation.apply(-currentRotation, -nextRotation, factor);
        }
        // Interpolación de rotación en la dirección que menos diferencia tiene
        // respecto al siguiente frame
        else
        {
            if (currentRotation > nextRotation)
            {
                return interpolation.apply(360f - currentRotation, -nextRotation, factor);
            }
            else
            {
                return interpolation.apply(-currentRotation, 360f - nextRotation, factor);
            }
        }
    }

    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(region, finalX, finalY, pivotX, pivotY,
                region.getRegionWidth(), region.getRegionHeight(),
                finalScaleX, finalScaleY, finalRotation);
    }

    public Bone copy()
    {
        return new Bone(region, id, parentId, pivotX, pivotY, offsetX, offsetY);
    }
}
