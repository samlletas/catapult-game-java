package com.engine.graphics.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.engine.GameTime;
import com.engine.utilities.Interpolation;

public class Bone
{
    private Bone parent;
    private int id;

    private float pivotX;
    private float pivotY;

    public float offsetX;
    public float offsetY;

    private TextureAtlas.AtlasRegion region;
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

    public Bone(TextureAtlas atlas, String regionName, int id,
                float pivotX, float pivotY, float offsetX, float offsetY)
    {
        this.region = atlas.findRegion(regionName);
        this.childs = new Array<Bone>();

        this.id = id;
        this.pivotX = pivotX;
        this.pivotY = pivotY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void addChild(Bone child)
    {
        child.parent = this;
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

        factor = 1f - (currentFrameTime / totalFrameTime);

        switch (currentFrame.interpolation)
        {
            case Linear:
                finalRotation = Interpolation.linear(current.rotation, next.rotation, factor) + globalRotation;
                finalScaleX = Interpolation.linear(current.scaleX, next.scaleX, factor) * globalScale;
                finalScaleY = Interpolation.linear(current.scaleY, next.scaleY, factor) * globalScale;

                offsetDistance = Interpolation.linear(current.offsetDistance, next.offsetDistance, factor);
                offsetDirection = Interpolation.linear(current.offsetDirection, next.offsetDirection, factor);
                break;

            case Sine:
                finalRotation = Interpolation.sine(current.rotation, next.rotation, factor) + globalRotation;
                finalScaleX = Interpolation.sine(current.scaleX, next.scaleX, factor) * globalScale;
                finalScaleY = Interpolation.sine(current.scaleY, next.scaleY, factor) * globalScale;

                offsetDistance = Interpolation.sine(current.offsetDistance, next.offsetDistance, factor);
                offsetDirection = Interpolation.sine(current.offsetDirection, next.offsetDirection, factor);
                break;
        }

        if (isRoot())
        {
            frameOffsetX = offsetDistance * MathUtils.cosDeg(offsetDirection + globalRotation);
            frameOffsetY = offsetDistance * MathUtils.sinDeg(offsetDirection + globalRotation);

            finalX = offsetX + (frameOffsetX * globalScale) - pivotX;
            finalY = offsetY + (frameOffsetY * globalScale) - pivotY;
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

            // Restamos globalRotation ya que finalRotation ya la incluye
            finalRotation += parent.finalRotation - globalRotation;
        }

        for (Bone child : childs)
        {
            child.update(gameTime, currentFrame, nextFrame, totalFrameTime,
                    currentFrameTime, globalRotation, globalScale);
        }
    }

    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(region, finalX, finalY, pivotX, pivotY,
                region.getRegionWidth(), region.getRegionHeight(),
                finalScaleX, finalScaleY, finalRotation);
    }
}
