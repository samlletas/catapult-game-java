package com.mygdx.game.screens.gameplay;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.engine.actors.Actions;
import com.engine.actors.ActorOrigin;
import com.engine.actors.DistanceFieldFontActor;
import com.engine.events.Event;
import com.engine.events.IEventHandler;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.utilities.ActorUtilities;
import com.mygdx.game.gamelogic.targets.Crystal;
import com.mygdx.game.gamelogic.targets.CrystalTypes;
import com.mygdx.game.gamelogic.targets.Spike;
import com.mygdx.game.gamelogic.targets.Target;
import com.mygdx.game.gamelogic.targets.patterns.TargetCollisionArgs;

public class GameLabel extends DistanceFieldFontActor
{
    private boolean isActive;
    private ActionContainer actionContainer;
    private Runnable deactivateRunnable;

    public GameLabel()
    {
        isActive = false;
        actionContainer = new ActionContainer();

        setActorOrigin(ActorOrigin.Center);
        setFontBaseScale(0.8f);

        deactivateRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                isActive = false;
            }
        };

        ActorUtilities.growActionsArray(this, 1);
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void reset()
    {
        isActive = false;
        clearActions();
    }

    public void show(String text, float x, float y, float destX, float destY,
                     float duration, Runnable onFinish)
    {
        setText(text);
        setPosition(x, y);
        getColor().a = 0f;
        setScale(0f, 0f);
        isActive = true;

        clearActions();
        addAction(actionContainer.getAction(destX, destY, duration, onFinish));
    }

    @Override
    public void act(float delta)
    {
        if (isActive)
        {
            super.act(delta);
        }
    }

    public void drawText(DistanceFieldRenderer renderer, DistanceFieldFont font)
    {
        if (isActive)
        {
            draw(renderer, font);
        }
    }

    class ActionContainer
    {
        SequenceAction sequence = new SequenceAction();
        ParallelAction showParallel = new ParallelAction();
        MoveByAction moveBy = new MoveByAction();
        SequenceAction showSequence = new SequenceAction();
        ParallelAction showBloomParallel = new ParallelAction();
        AlphaAction showBloomFadeIn = new AlphaAction();
        ScaleToAction showBloomScaleTo = new ScaleToAction();
        ScaleToAction showScaleTo = new ScaleToAction();
        ParallelAction travelParallel = new ParallelAction();
        MoveToAction travelMoveTo = new MoveToAction();
        ScaleToAction travelScaleTo = new ScaleToAction();
        RunnableAction deactivateRunnableAction = new RunnableAction();
        RunnableAction finishRunnableAction = new RunnableAction();

        Action getAction(float destX, float destY, float duration,  Runnable onFinish)
        {
            return Actions.sequence(sequence,
                    Actions.parallel(showParallel,
                            Actions.moveBy(moveBy, 0f, -10f, 0.5f, Interpolation.sine),
                            Actions.sequence(showSequence,
                                    Actions.parallel(showBloomParallel,
                                            Actions.alpha(showBloomFadeIn, 1f, 0.15f, Interpolation.linear),
                                            Actions.scaleTo(showBloomScaleTo, 1.3f, 1.3f, 0.15f, Interpolation.sine)),
                                    Actions.scaleTo(showScaleTo, 1f, 1f, 0.15f, Interpolation.sine))),
                    Actions.parallel(travelParallel,
                            Actions.moveTo(travelMoveTo, destX, destY, duration, Interpolation.sine),
                            Actions.scaleTo(travelScaleTo, 0.7f, 0.7f, duration, Interpolation.exp10)),
                    Actions.run(finishRunnableAction, onFinish),
                    Actions.run(deactivateRunnableAction, deactivateRunnable));
        }
    }
}