package com.engine.graphics.animation;

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

        for (FrameData fd : framesData)
        {
            copy.addFrameData(fd.copy());
        }

        return copy;
    }
}
