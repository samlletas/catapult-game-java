package com.engine.graphics.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.engine.GameTime;

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

    public void update(GameTime gameTime,
                       final Frame currentFrame, final Frame nextFrame,
                       final float totalFrameTime, final float currentFrameTime)
    {
        FrameData current = currentFrame.getFrameData(id);
        FrameData next = nextFrame.getFrameData(id);

        finalRotation = current.rotation + (((next.rotation - current.rotation) / totalFrameTime) * (totalFrameTime - currentFrameTime));
        finalScaleX = current.scaleX + (((next.scaleX - current.scaleX) / totalFrameTime) * (totalFrameTime - currentFrameTime));
        finalScaleY = current.scaleY + (((next.scaleY - current.scaleY) / totalFrameTime) * (totalFrameTime - currentFrameTime));

        float offsetDirection = current.offsetDirection + (((next.offsetDirection - current.offsetDirection) / totalFrameTime) * (totalFrameTime - currentFrameTime));
        float offsetDistance = current.offsetDistance + (((next.offsetDistance - current.offsetDistance) / totalFrameTime) * (totalFrameTime - currentFrameTime));
        float frameOffsetX = offsetDistance * MathUtils.cosDeg(offsetDirection + finalRotation);
        float frameOffsetY = offsetDistance * MathUtils.sinDeg(offsetDirection + finalRotation);

        if (isRoot())
        {
            finalX = offsetX + frameOffsetX - pivotX;
            finalY = offsetY + frameOffsetY - pivotY;
        }
        else
        {
            float diffX = offsetX + frameOffsetX - parent.pivotX;
            float diffY = offsetY + frameOffsetY - parent.pivotY;
            float distance = (float)Math.sqrt((diffX * diffX) + (diffY * diffY));
            float angle = MathUtils.atan2(diffY, diffX) + (float)Math.toRadians(parent.finalRotation);

            finalX = (parent.finalX + parent.pivotX - pivotX) + (parent.finalScaleX * distance * MathUtils.cos(angle));
            finalY = (parent.finalY + parent.pivotY - pivotY) + (parent.finalScaleY * distance * MathUtils.sin(angle));

            finalRotation += parent.finalRotation;
        }

        for (Bone child : childs)
        {
            child.update(gameTime, currentFrame, nextFrame, totalFrameTime, currentFrameTime);
        }
    }

    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(region, finalX, finalY, pivotX, pivotY,
                region.getRegionWidth(), region.getRegionHeight(),
                finalScaleX, finalScaleY, finalRotation);
    }
}
