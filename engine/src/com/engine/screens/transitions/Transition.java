package com.engine.screens.transitions;

import com.badlogic.gdx.utils.Disposable;
import com.engine.graphics.GraphicsSettings;
import com.engine.GameTime;

public abstract class Transition implements Disposable
{
    protected final GraphicsSettings graphicsSettings;
    private boolean isActive;

    public Transition(GraphicsSettings graphicsSettings)
    {
        this.graphicsSettings = graphicsSettings;
        this.isActive = false;
    }

    public final boolean isActive()
    {
        return isActive;
    }

    public final void start()
    {
        isActive = true;
        onStart();
    }

    protected final void finish()
    {
        isActive = false;
        onFinish();
    }

    public final void update(GameTime gameTime)
    {
        if (isActive)
        {
            onUpdate(gameTime);
        }
    }

    public final void draw(GameTime gameTime)
    {
        if (isActive)
        {
            onDraw(gameTime);
        }
    }

    @Override
    public final void dispose()
    {
        onDispose();
    }

    protected abstract void onStart();
    protected abstract void onFinish();
    public abstract boolean canRenderNextScreen();
    protected abstract void onUpdate(GameTime gameTime);
    protected abstract void onDraw(GameTime gameTime);
    protected abstract void onDispose();
}
