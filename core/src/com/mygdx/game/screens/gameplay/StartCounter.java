package com.mygdx.game.screens.gameplay;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.engine.GameTime;
import com.engine.actors.ActorOrigin;
import com.engine.actors.DistanceFieldFontActor;
import com.engine.events.Event;
import com.engine.events.EventsArgs;
import com.engine.graphics.GraphicsSettings;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;

public class StartCounter
{
    private String[] sequences =
    {
            "3",
            "2",
            "1",
            "SMASH!"
    };

    private int currentSequence;
    private boolean isActive;

    private float drawX;
    private float drawY;

    private DistanceFieldFontActor textActor;
    private Runnable resetTextRunnable;
    private Runnable resetSmashTextRunnable;
    private Runnable stopRunnable;

    public Event<EventsArgs> onEnd;

    public StartCounter(GraphicsSettings graphicsSettings)
    {
        currentSequence = -1;
        isActive =  false;

        drawX = graphicsSettings.virtualWidth / 2f;
        drawY = graphicsSettings.virtualHeight / 2f - 10f;

        textActor = new DistanceFieldFontActor();
        textActor.setFontBaseScale(1.75f);
        textActor.setActorOrigin(ActorOrigin.Center);
        textActor.setPosition(drawX, drawY);

        resetTextRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                textActor.setPosition(drawX, drawY - 10f);
                textActor.setScale(1f);
                textActor.getColor().a = 0f;
                textActor.setText(sequences[currentSequence]);
                currentSequence++;
            }
        };

        resetSmashTextRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                textActor.setPosition(drawX, drawY);
                textActor.setScale(0.5f);
                textActor.getColor().a = 0f;
                textActor.setText(sequences[currentSequence]);
                currentSequence++;
            }
        };

        stopRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                isActive = false;
                onEnd.invoke(null);
            }
        };

        onEnd = new Event<EventsArgs>();
    }

    public void start()
    {
        if (!isActive)
        {
            currentSequence = 0;
            isActive = true;

            textActor.addAction(
                    Actions.sequence(
                            Actions.sequence(
                                    Actions.run(resetTextRunnable),
                                    Actions.delay(0.05f),
                                    Actions.parallel(
                                            Actions.alpha(1f, 0.15f, Interpolation.linear),
                                            Actions.moveTo(drawX, drawY, 0.15f, Interpolation.linear)),
                                    Actions.delay(0.2f),
                                    Actions.alpha(0f, 0.2f)),
                            Actions.sequence(
                                    Actions.run(resetTextRunnable),
                                    Actions.parallel(
                                            Actions.alpha(1f, 0.15f, Interpolation.linear),
                                            Actions.moveTo(drawX, drawY, 0.15f, Interpolation.linear)),
                                    Actions.delay(0.2f),
                                    Actions.alpha(0f, 0.2f)),
                            Actions.sequence(
                                    Actions.run(resetTextRunnable),
                                    Actions.scaleTo(1f, 1f),
                                    Actions.parallel(
                                            Actions.alpha(1f, 0.15f, Interpolation.linear),
                                            Actions.moveTo(drawX, drawY, 0.15f, Interpolation.linear)),
                                    Actions.delay(0.2f),
                                    Actions.alpha(0f, 0.2f)),
                            Actions.sequence(
                                    Actions.run(resetSmashTextRunnable),
                                    Actions.sequence(
                                            Actions.parallel(
                                                    Actions.alpha(1f, 0.4f, Interpolation.pow4Out),
                                                    Actions.scaleTo(1f, 1f, 0.4f, Interpolation.bounceOut)),
                                            Actions.delay(0.3f),
                                            Actions.alpha(0f, 0.3f)),
                                    Actions.run(stopRunnable))));
        }
    }

    public void update(GameTime gameTime)
    {
        textActor.act(gameTime.delta);
    }

    public void drawText(DistanceFieldRenderer renderer, DistanceFieldFont font)
    {
        textActor.draw(renderer, font);
    }
}
