package com.engine.graphics.animation;

import com.badlogic.gdx.utils.Array;
import com.engine.Interpolation.IInterpolator;

public class Frame
{
    public float duration;
    public IInterpolator interpolator;
    private Array<FrameData> framesData;

    public Frame(float duration, IInterpolator interpolator)
    {
        this.duration = duration;
        this.interpolator = interpolator;

        this.framesData = new Array<FrameData>();
    }

    public void addFrameData(FrameData frameData)
    {
        framesData.add(frameData);
    }

    public FrameData getFrameData(int boneID)
    {
        return framesData.get(boneID);
    }
}
