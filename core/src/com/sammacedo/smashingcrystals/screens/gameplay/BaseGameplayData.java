package com.sammacedo.smashingcrystals.screens.gameplay;

import com.engine.GameTime;
import com.sammacedo.smashingcrystals.helpers.Security;

public abstract class BaseGameplayData
{
    private int score;

    public BaseGameplayData()
    {

    }

    public final void reset()
    {
        score = Security.protect(0);
        onReset();
    }

    public final void increaseScore(int points)
    {
        score += points;
    }

    public final int getScore()
    {
        return Security.recover(score);
    }

    public final void update(GameTime gameTime)
    {
        onOpdate(gameTime);
    }

    public abstract int getBest();
    public abstract void saveBest(int score);
    protected abstract void onReset();
    protected abstract void onOpdate(GameTime gameTime);
}
