package com.engine.graphics.animation;

import com.badlogic.gdx.utils.Array;

public class Frame
{
    public float duration;
    public FrameInterpolations interpolation;
    private Array<FrameData> framesData;

    public Frame(float duration, FrameInterpolations interpolation)
    {
        this.duration = duration;
        this.interpolation = interpolation;

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
