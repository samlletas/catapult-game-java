package com.engine.graphics.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.engine.GameTime;
import com.engine.Interpolation.IInterpolator;

public class Bone
{
    private TextureAtlas.AtlasRegion region;

    public final int id;

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

    public boolean isRoot()
    {
        return parent == null;
    }

    public Bone(TextureAtlas.AtlasRegion region, int id,
                float pivotX, float pivotY, float offsetX, float offsetY)
    {
        this.region = region;

        this.id = id;
        this.pivotX = pivotX;
        this.pivotY = pivotY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
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

    // Variables auxiliares para la actualización de los bones
    private static float factor;
    private static float offsetDistance;
    private static float offsetDirection;
    private static float frameOffsetX;
    private static float frameOffsetY;
    private static float diffX;
    private static float diffY;
    private static float distance;
    private static float angle;

    public void update(GameTime gameTime,
                       final Frame currentFrame, final Frame nextFrame,
                       final float totalFrameTime, final float currentFrameTime,
                       float globalRotation, float globalScale)
    {
        FrameData current = currentFrame.getFrameData(id);
        FrameData next = nextFrame.getFrameData(id);
        IInterpolator interpolator = current.interpolator;

        factor = 1f - (currentFrameTime / totalFrameTime);

        finalRotation = getRotationInterpolation(current.rotation, next.rotation, interpolator, factor);
        finalScaleX = interpolator.interpolate(current.scaleX, next.scaleX, factor) * globalScale;
        finalScaleY = interpolator.interpolate(current.scaleY, next.scaleY, factor) * globalScale;
        offsetDistance = interpolator.interpolate(current.offsetDistance, next.offsetDistance, factor);
        offsetDirection = getRotationInterpolation(current.offsetDirection, next.offsetDirection, interpolator, factor);

        if (isRoot())
        {
            frameOffsetX = offsetDistance * MathUtils.cosDeg(offsetDirection - globalRotation);
            frameOffsetY = offsetDistance * MathUtils.sinDeg(offsetDirection - globalRotation);

            finalX = offsetX + (frameOffsetX * globalScale) - pivotX;
            finalY = offsetY + (frameOffsetY * globalScale) - pivotY;

            finalRotation -= globalRotation;
        }
        else
        {
            frameOffsetX = offsetDistance * MathUtils.cosDeg(offsetDirection);
            frameOffsetY = offsetDistance * MathUtils.sinDeg(offsetDirection);

            diffX = ((offsetX + frameOffsetX) - parent.pivotX) * parent.finalScaleX;
            diffY = ((offsetY + frameOffsetY) - parent.pivotY) * parent.finalScaleY;

            distance = (float)Math.sqrt((diffX * diffX) + (diffY * diffY));
            angle = MathUtils.atan2(diffY, diffX) + (float)Math.toRadians(parent.finalRotation);

            finalX = ((parent.finalX + parent.pivotX) - pivotX) + (distance * MathUtils.cos(angle));
            finalY = ((parent.finalY + parent.pivotY) - pivotY) + (distance * MathUtils.sin(angle));

            finalRotation += parent.finalRotation;
        }

        if (childs != null)
        {
            for (Bone child : childs)
            {
                child.update(gameTime, currentFrame, nextFrame, totalFrameTime,
                        currentFrameTime, globalRotation, globalScale);
            }
        }
    }

    private float getRotationInterpolation(float currentRotation,
                                           float nextRotation,
                                           IInterpolator interpolator,
                                           float factor)
    {
        // Interpolación de rotación directa
        if (Math.abs(nextRotation - currentRotation) <= 180f)
        {
            return interpolator.interpolate(-currentRotation, -nextRotation, factor);
        }
        // Interpolación de rotación en la dirección que menos diferencia tiene
        // respecto al siguiente frame
        else
        {
            if (currentRotation > nextRotation)
            {
                return interpolator.interpolate(360f - currentRotation, -nextRotation, factor);
            }
            else
            {
                return interpolator.interpolate(-currentRotation, 360f - nextRotation, factor);
            }
        }
    }

    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(region, finalX, finalY, pivotX, pivotY,
                region.getRegionWidth(), region.getRegionHeight(),
                finalScaleX, finalScaleY, finalRotation);
    }
}
