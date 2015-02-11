package com.mygdx.game.gamelogic;

import com.engine.GameTime;
import com.engine.events.Event;
import com.engine.events.EventsArgs;
import com.engine.events.IEventHandler;
import com.engine.utilities.Timer;

public class GameplayData
{
    public final static int MAX_LIVES = 3;
    public final static float SPECIAL_MAX_VALUE = 100f;
    private final static float SPECIAL_DECREASE_SPEED = 3.5f;
    private final static float SPECIAL_ACTIVATED_DURATION = 10000f;

    private int score;
    private int lives;
    private float special;

    private Timer specialActivatedTimer;

    // Eventos
    public Event<EventsArgs> onSpecialActivated;
    public Event<EventsArgs> onSpecialFinalized;
    public Event<EventsArgs> onOutOfLives;
    private EventsArgs gameplayDataEventArgs;

    public GameplayData()
    {
        this.specialActivatedTimer = new Timer(SPECIAL_ACTIVATED_DURATION);
        this.specialActivatedTimer.timerReachedZero.subscribe(
                new SpecialActivatedTimerReachedZeroHandler());

        this.onSpecialActivated = new Event<EventsArgs>();
        this.onSpecialFinalized = new Event<EventsArgs>();
        this.onOutOfLives = new Event<EventsArgs>();

        this.gameplayDataEventArgs = new EventsArgs();
        this.gameplayDataEventArgs.sender = this;
    }

    public void reset()
    {
        score = 0;
        lives = MAX_LIVES;
        special = 0f;
        specialActivatedTimer.stop();
    }

    public void increaseScore(int points)
    {
        score += points;
    }

    public int getScore()
    {
        return score;
    }

    public void decreaseLive()
    {
        lives = Math.max(0, lives - 1);
    }

    public int getLives()
    {
        return lives;
    }

    public void increaseSpecial(float points)
    {
        if (!isSpecialActivated())
        {
            special = Math.min(SPECIAL_MAX_VALUE, special + points);

            if (special == SPECIAL_MAX_VALUE)
            {
                specialActivatedTimer.start();
                onSpecialActivated.invoke(gameplayDataEventArgs);
            }
        }
    }

    public float getSpecial()
    {
        return special;
    }

    public boolean isSpecialActivated()
    {
        return specialActivatedTimer.isRunning();
    }

    public void updateSpecial(GameTime gameTime)
    {
        specialActivatedTimer.update(gameTime);

        if (specialActivatedTimer.isRunning())
        {
            special = SPECIAL_MAX_VALUE * (1f - specialActivatedTimer.elapsedTimePercentage());
        }
        else
        {
            special = Math.max(0f, special - (gameTime.delta * SPECIAL_DECREASE_SPEED));
        }
    }

    class SpecialActivatedTimerReachedZeroHandler implements IEventHandler<EventsArgs>
    {
        @Override
        public void onAction(EventsArgs args)
        {
            onSpecialFinalized.invoke(gameplayDataEventArgs);
        }
    }
}
