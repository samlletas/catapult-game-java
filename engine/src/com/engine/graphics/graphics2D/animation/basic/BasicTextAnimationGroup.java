package com.engine.graphics.graphics2D.animation.basic;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;

public abstract class BasicTextAnimationGroup extends BasicAnimationGroup
{
    @Override
    protected abstract void initialize();

    @Override
    protected abstract void addAnimations(Array<BasicAnimation> animations);

    @Override
    protected abstract  void onStart();

    @Override
    protected abstract void onDraw(Batch batch, float x, float y);

    public abstract void drawText(DistanceFieldRenderer renderer,
                                     DistanceFieldFont font, float x, float y);
}
