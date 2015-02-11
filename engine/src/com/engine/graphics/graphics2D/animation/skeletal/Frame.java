package com.engine.graphics.graphics2D.animation.skeletal;

import com.badlogic.gdx.utils.Array;

public class Frame
{
    public float duration;
    private Array<FrameData> framesData;

    public Frame(float duration)
    {
        this.duration = duration;
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

    public Frame copy()
    {
        Frame copy = new Frame(duration);
        Array<FrameData> localFramesData = this.framesData;

        for (int i = 0, n = localFramesData.size; i < n; i++)
        {
            copy.addFrameData(localFramesData.get(i).copy());
        }

        return copy;
    }
}
