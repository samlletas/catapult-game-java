package com.engine;

import com.badlogic.gdx.Gdx;

public class GameTime
{
    private float delta;
    private float elapsed;

    public double getDelta()
    {
        return delta;
    }

    public double getElapsed()
    {
        return elapsed;
    }

    public GameTime()
    {
        this.elapsed = 0f;
        this.delta = 0f;
    }

    public void update()
    {
        delta = Gdx.graphics.getDeltaTime();
        elapsed += delta;
    }
}
