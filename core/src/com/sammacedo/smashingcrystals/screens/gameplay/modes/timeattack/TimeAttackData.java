package com.sammacedo.smashingcrystals.screens.gameplay.modes.timeattack;

import com.engine.GameTime;
import com.engine.utilities.Timer;
import com.sammacedo.smashingcrystals.Settings;
import com.sammacedo.smashingcrystals.screens.gameplay.BaseGameplayData;

public class TimeAttackData extends BaseGameplayData
{
    public final static float START_TIME_SECONDS = 59.99f;

    private Settings settings;
    public Timer timer;

    public TimeAttackData(Settings settings)
    {
        this.settings = settings;
        this.timer = new Timer(START_TIME_SECONDS * 1000f);
    }

    @Override
    public int getBest()
    {
        return settings.getLocalTimeAttackScore();
    }

    @Override
    public void saveBest(int score)
    {
        settings.setLocalTimeAttackScore(score);
    }

    @Override
    protected void onReset()
    {
        timer.restart();
        timer.pause();
    }

    @Override
    protected void onOpdate(GameTime gameTime)
    {
        timer.update(gameTime);
    }
}
