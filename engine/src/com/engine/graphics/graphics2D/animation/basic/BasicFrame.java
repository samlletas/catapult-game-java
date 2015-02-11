package com.engine.graphics.graphics2D.animation.basic;

import com.badlogic.gdx.math.Interpolation;

public class BasicFrame
{
    public float duration;
    public float rotation;
    public float scaleX;
    public float scaleY;
    public float offsetX;
    public float offsetY;
    public float alpha;
    public Interpolation interpolation;

    public BasicFrame(float duration, float rotation, float scaleX, float scaleY,
                      float offsetX, float offsetY, float alpha,
                      Interpolation interpolation)
    {
        this.duration = duration;
        this.rotation = rotation;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.alpha = alpha;
        this.interpolation = interpolation;
    }
}