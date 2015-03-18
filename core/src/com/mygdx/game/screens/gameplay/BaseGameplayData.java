package com.mygdx.game.screens.gameplay;

import com.engine.GameTime;

public abstract class BaseGameplayData
{
    protected int score;

    public BaseGameplayData()
    {

    }

    public final void reset()
    {
        score = 0;
        onReset();
    }

    public final void increaseScore(int points)
    {
        score += points;
    }

    public final int getScore()
    {
        return score;
    }

    public final void update(GameTime gameTime)
    {
        onOpdate(gameTime);
    }

    protected abstract void onReset();
    protected abstract void onOpdate(GameTime gameTime);
}
