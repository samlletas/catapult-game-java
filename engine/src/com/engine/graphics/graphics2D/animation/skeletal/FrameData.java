package com.engine.graphics.graphics2D.animation.skeletal;


import com.badlogic.gdx.math.Interpolation;

public class FrameData
{
    public int boneID;
    public float rotation;
    public float offsetDirection;
    public float offsetDistance;
    public float scaleX;
    public float scaleY;
    public Interpolation interpolation;

    public FrameData(int boneID,
                     float rotation,
                     float scaleX, float scaleY,
                     float offsetDirection, float offsetDistance,
                     Interpolation interpolation)
    {
        this.boneID = boneID;
        this.rotation = rotation;
        this.offsetDirection = offsetDirection;
        this.offsetDistance = offsetDistance;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.interpolation = interpolation;
    }

    public FrameData copy()
    {
        return new FrameData(boneID, rotation, scaleX, scaleY,
                offsetDirection, offsetDistance, interpolation);
    }
}
