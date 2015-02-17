package com.engine;

import com.badlogic.gdx.Gdx;

public class GameTime
{
    public float delta;
    public float elapsed;

    public GameTime()
    {
        this.elapsed = 0f;
        this.delta = 0f;
    }

    public void update()
    {
        delta = Gdx.graphics.getDeltaTime();
//        delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f);
        elapsed += delta;
    }
}
