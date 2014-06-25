package com.engine.graphics.animation;

public class FrameData
{
    public int boneID;
    public float rotation;
    public float offsetDirection;
    public float offsetDistance;
    public float scaleX;
    public float scaleY;

    public FrameData(int boneID,
                     float rotation,
                     float offsetDirection, float offsetDistance,
                     float scaleX, float scaleY)
    {
        this.boneID = boneID;
        this.rotation = rotation;
        this.offsetDirection = offsetDirection;
        this.offsetDistance = offsetDistance;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }
}
