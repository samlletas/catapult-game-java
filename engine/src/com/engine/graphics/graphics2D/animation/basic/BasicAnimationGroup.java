package com.engine.graphics.graphics2D.animation.basic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.engine.GameTime;
import com.engine.events.Event;
import com.engine.events.EventsArgs;

/**
 * Representa un conjunto de animacines básicas, las clases derivadas se deben
 * de encargar de iniciar y dibujar las animaciones
 */
public abstract class BasicAnimationGroup
{
    private Array<BasicAnimation> animations;
    private boolean isActive;
    private boolean isPaused;

    // Eventos
    public Event<EventsArgs> onFinish;
    private EventsArgs onFinishArguments;

    public BasicAnimationGroup()
    {
        animations = new Array<BasicAnimation>();
        isActive = false;
        isPaused = false;

        onFinish = new Event<EventsArgs>();
        onFinishArguments = new EventsArgs();

        initialize();
        addAnimations(animations);
    }

    public final void start()
    {
        isActive = true;
        isPaused = false;

        onStart();
    }

    public final void pause()
    {
        isPaused = true;
    }

    public final void stop()
    {
        isActive = false;
        isPaused = false;
    }

    public final void update(GameTime gameTime)
    {
        if (isActive && !isPaused)
        {
            int finishedCount = 0;

            for (BasicAnimation animation : animations)
            {
                animation.update(gameTime);

                if (!animation.isPlaying())
                {
                    finishedCount++;
                }
            }

            if (finishedCount == animations.size)
            {
                stop();

                onFinishArguments.sender = this;
                onFinish.invoke(onFinishArguments);
            }
        }
    }

    public final void draw(SpriteBatch spriteBatch, float x, float y)
    {
        onDraw(spriteBatch, x, y);
    }

    /**
     * Aquí las clases derivadas realizan su propia lógica de inicialización
     */
    protected abstract void initialize();

    /**
     * Aquí las clases derivadas agregan las animaciones que pertenecerán al grupo
     *
     * @param animations array en donde se deberán insertar las animaciones
     */
    protected abstract void addAnimations(Array<BasicAnimation> animations);

    /**
     * Aquí las clases derivadas deben iniciar la reproducción de las animaciones
     */
    protected abstract void onStart();

    /**
     * Aquí las clases derivadas deben de dibujar cada una de las animaciones
     *
     * @param spriteBatch spriteBatch
     * @param x posición de dibujado en x
     * @param y posición de dibujado en y
     */
    protected abstract void onDraw(SpriteBatch spriteBatch, float x, float y);
}
