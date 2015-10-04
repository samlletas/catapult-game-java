package com.engine.utilities;

import com.engine.GameTime;
import com.engine.events.Event;
import com.engine.events.EventsArgs;

public final class Timer
{
    public boolean enabled;

    private float duration;
    private float counter;
    private boolean isRunning;
    private boolean isPaused;

    public Event<EventsArgs> timerStopped = new Event<EventsArgs>();
    public Event<EventsArgs> timerReachedZero = new Event<EventsArgs>();
    private EventsArgs timerStoppedArgs = new EventsArgs();
    private EventsArgs timerReachedZeroArgs = new EventsArgs();

    /**
     *
     * @param duration Duración en milisegundos
     */
    public Timer(float duration)
    {
        this.enabled = true;

        this.duration = duration;
        this.counter = 0f;
        this.isRunning = false;
        this.isPaused = false;
    }

    /**
     *
     * @return Valor booleano que indica si se encuentra activo el contador
     */
    public boolean isRunning()
    {
        return isRunning;
    }

    /**
     *
     * @return Valor booleano que indica si se encuentra en pausa el contador
     */
    public boolean isPaused()
    {
        return isPaused;
    }

    /**
     *
     * @return El tiempo transcurrido en milisegundos
     */
    public float elapsedTime()
    {
        if (!isRunning)
        {
            return 0f;
        }
        else
        {
            return duration - counter;
        }
    }

    /**
     *
     * @return El porcentaje de tiempo transcurrido del contador, entre más
     * cercano a 1 indica que ha transcurrudo más tiempo y que está a punto de
     * terminar el conteo
     */
    public float elapsedTimePercentage()
    {
        if (!isRunning)
        {
            return 0f;
        }
        else
        {
            return (duration - counter) / duration;
        }
    }

    /**
     *
     * @return El tiempo restante en milisegundos
     */
    public float remainingTime()
    {
        if (!isRunning())
        {
            return 0f;
        }
        else
        {
            return counter;
        }
    }

    /**
     *
     * @return El tiempo restante en segundos
     */
    public int remainingSeconds()
    {
        return (int)(remainingTime() / 1000f);
    }

    /**
     *
     * @return El tiempo restante en centisegundos
     */
    public int remainingCentiseconds()
    {
        return (int)(remainingTime() / 10f);
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
        if (!isRunning)
        {
            restart(duration);
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
            this.duration = duration;

            counter = duration;
            isRunning = true;
            isPaused = false;
        }
    }

    /**
     * Detiene el contador
     */
    public void stop()
    {
        if (isRunning && enabled)
        {
            counter = 0f;
            isRunning = false;
            isPaused = false;

            timerStoppedArgs.sender = this;
            timerStopped.invoke(timerStoppedArgs);
        }
    }

    public void pause()
    {
        if (isRunning && enabled)
        {
            isPaused = true;
        }
    }

    public void resume()
    {
        if (isRunning && isPaused && enabled)
        {
            isPaused = false;
        }
    }

    public void add(float time)
    {
        if (isRunning && enabled)
        {
            counter += time;

            if (counter > duration)
            {
                duration = counter;
            }
        }
    }

    public void substract(float time)
    {
        if (isRunning && enabled)
        {
            counter = Math.max(0, counter - time);
        }
    }

    public void update(GameTime gameTime)
    {
        if (isRunning && !isPaused && enabled)
        {
            counter -= 1000f * gameTime.delta;

            if (counter <= 0f)
            {
                stop();

                timerReachedZeroArgs.sender = this;
                timerReachedZero.invoke(timerReachedZeroArgs);
            }
        }
    }
}
