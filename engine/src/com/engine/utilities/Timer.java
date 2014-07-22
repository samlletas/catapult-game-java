package com.engine.utilities;

import com.badlogic.gdx.Gdx;
import com.engine.GameTime;
import com.engine.events.Event;
import com.engine.events.EventsArgs;

public final class Timer
{
    public boolean enabled;

    private float duration;
    private float counter;
    private boolean isActive;

    public Event<EventsArgs> timerStopped = new Event<EventsArgs>();
    public Event<EventsArgs> timerReachedZero = new Event<EventsArgs>();
    private EventsArgs timerStoppedArgs = new EventsArgs();
    private EventsArgs timerReachedZeroArgs = new EventsArgs();

    /**
     *
     * @return El porcentaje de tiempo transcurrido del contador, entre más
     * cercano a 1 indica que ha transcurrudo más tiempo y que está a punto de
     * terminar el conteo
     */
    public float elapsedTimePercentage()
    {
        if (!isActive)
        {
            return 0f;
        }
        else
        {
            return counter / duration;
        }
    }

    /**
     *
     * @param duration Duración en milisegundos
     */
    public Timer(float duration)
    {
        this.enabled = true;

        this.duration = duration;
        this.counter = 0f;
        this.isActive = false;
    }

    /**
     * Inicia el contador sólo si el contador ya ha terminado
     */
    public void start()
    {
        start(duration);
    }

    /**
     * Inicia el contador sólo si el contador ya ha terminado
     * @param duration Duración en milisegundos
     */
    public void start(float duration)
    {
        if (!isActive && enabled)
        {
            this.duration = duration;

            counter = 0f;
            isActive = true;
        }
    }

    /**
     * Reinicia el contador sin importar si ha terminado o no
     */
    public void restart()
    {
        restart(duration);
    }

    /**
     * Reinicia el contador sin importar si ha terminado o no
     * @param duration Duración en milisegundos
     */
    public void restart(float duration)
    {
        if (enabled)
        {
            counter = 0f;
            isActive = true;
        }
    }

    /**
     * Detiene el contador
     */
    public void stop()
    {
        if (isActive && enabled)
        {
            counter = 0f;
            isActive = false;

            timerStoppedArgs.sender = this;
            timerStopped.invoke(timerStoppedArgs);
        }
    }

    public void update(GameTime gameTime)
    {
        if (isActive && enabled)
        {
            counter += 1000f * gameTime.delta;

            if (counter >= duration)
            {
                stop();

                timerReachedZeroArgs.sender = this;
                timerReachedZero.invoke(timerReachedZeroArgs);
            }
        }
    }
}
