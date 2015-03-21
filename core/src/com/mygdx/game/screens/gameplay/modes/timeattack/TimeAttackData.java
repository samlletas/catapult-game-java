package com.mygdx.game.screens.gameplay.modes.timeattack;

import com.engine.GameTime;
import com.engine.events.Event;
import com.engine.events.EventsArgs;
import com.engine.events.IEventHandler;
import com.engine.utilities.Timer;
import com.mygdx.game.screens.gameplay.BaseGameplayData;

public class TimeAttackData extends BaseGameplayData
{
    public final static float START_TIME_SECONDS = 14.99f;

    public Timer timer;

    public TimeAttackData()
    {
        timer = new Timer(START_TIME_SECONDS * 1000f);
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
