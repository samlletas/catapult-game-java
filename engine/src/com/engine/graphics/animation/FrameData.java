package com.engine.graphics.animation;

import com.engine.Interpolation.IInterpolator;

public class FrameData
{
    public int boneID;
    public float rotation;
    public float offsetDirection;
    public float offsetDistance;
    public float scaleX;
    public float scaleY;
    public IInterpolator interpolator;

    public FrameData(int boneID,
                     float rotation,
                     float scaleX, float scaleY,
                     float offsetDirection, float offsetDistance,
                     IInterpolator interpolator)
    {
        this.boneID = boneID;
        this.rotation = rotation;
        this.offsetDirection = offsetDirection;
        this.offsetDistance = offsetDistance;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.interpolator = interpolator;
    }
}
