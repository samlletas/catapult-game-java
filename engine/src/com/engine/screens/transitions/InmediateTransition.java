package com.engine.screens.transitions;

import com.engine.GameTime;

public class InmediateTransition extends Transition
{
    public InmediateTransition()
    {
        super(null);
    }

    @Override
    protected void onStart()
    {

    }

    @Override
    protected void onFinish()
    {

    }

    @Override
    public boolean canRenderNextScreen()
    {
        return true;
    }

    @Override
    protected void onUpdate(GameTime gameTime)
    {
        finish();
    }

    @Override
    protected void onDraw(GameTime gameTime)
    {

    }

    @Override
    protected void onDispose()
    {

    }
}
