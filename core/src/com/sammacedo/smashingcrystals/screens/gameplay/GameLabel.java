package com.sammacedo.smashingcrystals.screens.gameplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.engine.actors.Actions;
import com.engine.actors.ActorOrigin;
import com.engine.actors.DistanceFieldFontActor;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.utilities.ActorUtilities;

public class GameLabel extends DistanceFieldFontActor
{
    private boolean isActive;
    private ActionContainer actionContainer;
    private Runnable deactivateRunnable;

    public GameLabel(DistanceFieldFont font)
    {
        super(font);

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

    private void prepareForShow(String text, float x, float y)
    {
        setText(text);
        setPosition(x, y);
        setColor(Color.WHITE);
        getColor().a = 0f;
        setScale(0.1f, 0.1f);
        isActive = true;
    }

    public void showNormal(String text, float x, float y, float destX, float destY,
                           float duration, Runnable onFinish)
    {
        prepareForShow(text, x, y);

        clearActions();
        addAction(actionContainer.getNormalAction(destX, destY, duration, onFinish));
    }

    public void showFlashing(String text, float x, float y, float destX, float destY,
                             float duration, Runnable onFinish, Color color)
    {
        prepareForShow(text, x, y);

        clearActions();
        addAction(actionContainer.getFlashingAction(destX, destY, duration, onFinish, color));
    }

    @Override
    public void act(float delta)
    {
        if (isActive)
        {
            super.act(delta);
        }
    }

    public void drawText(DistanceFieldRenderer renderer)
    {
        if (isActive)
        {
            draw(renderer);
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

        ParallelAction flashParrallel = new ParallelAction();
        RepeatAction flashRepeat = new RepeatAction();
        SequenceAction flashSequence = new SequenceAction();
        ColorAction flashColor = new ColorAction();
        ColorAction flashWhite = new ColorAction();

        Action getNormalAction(float destX, float destY, float duration, Runnable onFinish)
        {
            return Actions.sequence(sequence,
                    Actions.parallel(showParallel,
                            Actions.moveBy(moveBy, 0f, -10f, 0.6f, Interpolation.sine),
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

        Action getFlashingAction(float destX, float destY, float duration, Runnable onFinish, Color color)
        {
            return Actions.parallel(flashParrallel,
                    getNormalAction(destX, destY, duration, onFinish),
                    Actions.repeat(flashRepeat, RepeatAction.FOREVER,
                        Actions.sequence(flashSequence,
                                Actions.color(flashColor, Color.WHITE, 0.07f, Interpolation.sine),
                                Actions.color(flashWhite, color, 0.07f, Interpolation.sine))));
        }
    }
}