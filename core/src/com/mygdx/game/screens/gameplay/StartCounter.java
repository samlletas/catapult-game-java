package com.mygdx.game.screens.gameplay;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.engine.actors.Actions;
import com.engine.GameTime;
import com.engine.actors.ActorOrigin;
import com.engine.actors.DistanceFieldFontActor;
import com.engine.events.Event;
import com.engine.events.EventsArgs;
import com.engine.graphics.GraphicsSettings;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.utilities.ActorUtilities;

public class StartCounter
{
    private String[] sequences =
    {
            "3",
            "2",
            "1",
            "SMASH!"
    };

    private TextActionContainer[] textActions =
    {
            new TextActionContainer(),
            new TextActionContainer(),
            new TextActionContainer()
    };

    SmashActionContainer smashAction = new SmashActionContainer();
    SequenceAction sequence = new SequenceAction();

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
                textActor.setPosition(drawX, drawY - 15f);
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
                textActor.setScale(2.5f);
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

        ActorUtilities.growActionsArray(textActor, 1);
    }

    public void start()
    {
        if (!isActive)
        {
            currentSequence = 0;
            isActive = true;

            textActor.addAction(Actions.sequence(sequence,
                    textActions[0].getAction(),
                    textActions[1].getAction(),
                    textActions[2].getAction(),
                    smashAction.getAction()));
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

    class TextActionContainer
    {
        SequenceAction sequence = new SequenceAction();
        RunnableAction runnable = new RunnableAction();
        DelayAction delay = new DelayAction();
        ParallelAction parallel = new ParallelAction();
        AlphaAction fadeIn = new AlphaAction();
        MoveToAction moveTo = new MoveToAction();
        AlphaAction fadeOut = new AlphaAction();

        Action getAction()
        {
            return Actions.sequence(sequence,
                    Actions.run(runnable, resetTextRunnable),
                    Actions.delay(delay, 0.05f),
                    Actions.parallel(parallel,
                            Actions.alpha(fadeIn, 1f, 0.15f, Interpolation.linear),
                            Actions.moveTo(moveTo, drawX, drawY, 0.15f, Interpolation.sineOut)),
                    Actions.alpha(fadeOut, 0f, 0.4f, Interpolation.pow4In));
        }
    }

    class SmashActionContainer
    {
        SequenceAction sequence = new SequenceAction();
        RunnableAction runnable = new RunnableAction();
        ParallelAction showPararell = new ParallelAction();
        AlphaAction fadeIn = new AlphaAction();
        ScaleToAction scaleFadeIn = new ScaleToAction();
        ParallelAction hidePararell = new ParallelAction();
        AlphaAction fadeOut = new AlphaAction();
        ScaleToAction scaleFadeOut = new ScaleToAction();
        RunnableAction finishRunnable = new RunnableAction();

        Action getAction()
        {
            return Actions.sequence(sequence,
                    Actions.run(runnable, resetSmashTextRunnable),
                    Actions.parallel(showPararell,
                            Actions.alpha(fadeIn, 1f, 0.3f, Interpolation.pow4Out),
                            Actions.scaleTo(scaleFadeIn, 0.9f, 0.9f, 0.3f, Interpolation.pow4Out)),
                    Actions.parallel(hidePararell,
                            Actions.alpha(fadeOut, 0f, 0.8f, Interpolation.pow4Out),
                            Actions.scaleTo(scaleFadeOut, 1.2f, 1.2f, 0.8f, Interpolation.sineIn)),
                    Actions.run(finishRunnable, stopRunnable));
        }
    }
}
