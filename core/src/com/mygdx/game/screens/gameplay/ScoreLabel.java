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

public class ScoreLabel extends DistanceFieldFontActor
{
    private static final String TEXT_CRYSTAL_NORMAL = "+1";
    private static final String TEXT_CRYSTAL_SPECIAL = "+3";
    private static final String TEXT_SPIKE = "MISS!";

    private boolean isActive;
    public Event<TargetCollisionArgs> onFinish;
    private TargetCollisionArgs targetCollisionArgs;
    private ActionContainer actionContainer;
    private Runnable onFinishRunnable;

    public ScoreLabel()
    {
        isActive = false;
        onFinish = new Event<TargetCollisionArgs>();
        targetCollisionArgs = new TargetCollisionArgs();
        actionContainer = new ActionContainer();

        setActorOrigin(ActorOrigin.Center);
        setFontBaseScale(0.8f);

        onFinish.subscribe(new IEventHandler<TargetCollisionArgs>()
        {
            @Override
            public void onAction(TargetCollisionArgs args)
            {
                isActive = false;
            }
        });

        onFinishRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                onFinish.invoke(targetCollisionArgs);
            }
        };

        ActorUtilities.growActionsArray(this, 1);
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void show(TargetCollisionArgs args, float destX, float destY)
    {
        if (args.target instanceof Crystal)
        {
            Crystal crystal = (Crystal)args.target;

            if (crystal.getType() == CrystalTypes.Normal)
            {
                setText(TEXT_CRYSTAL_NORMAL);
            }
            else
            {
                setText(TEXT_CRYSTAL_SPECIAL);
            }
        }
        else if (args.target instanceof Spike)
        {
            setText(TEXT_SPIKE);
        }

        setPosition(args.target.getX(), args.target.getY());
        getColor().a = 0f;
        setScale(0f, 0f);
        isActive = true;
        targetCollisionArgs.target = args.target;
        targetCollisionArgs.score = args.score;
        targetCollisionArgs.special = args.special;

        addAction(actionContainer.getAction(args.target, destX, destY));
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
        MoveToAction showMoveTo = new MoveToAction();
        SequenceAction showSequence = new SequenceAction();
        ParallelAction showBloomParallel = new ParallelAction();
        AlphaAction showBloomFadeIn = new AlphaAction();
        ScaleToAction showBloomScaleTo = new ScaleToAction();
        ScaleToAction showScaleTo = new ScaleToAction();
        ParallelAction travelParallel = new ParallelAction();
        MoveToAction travelMoveTo = new MoveToAction();
        ScaleToAction travelScaleTo = new ScaleToAction();
        RunnableAction runnable = new RunnableAction();

        Action getAction(Target target, float destX, float destY)
        {
            return Actions.sequence(sequence,
                    Actions.parallel(showParallel,
                            Actions.moveTo(showMoveTo, target.getX(), target.getY() - 10f, 0.5f, Interpolation.sine),
                            Actions.sequence(showSequence,
                                    Actions.parallel(showBloomParallel,
                                            Actions.alpha(showBloomFadeIn, 1f, 0.15f, Interpolation.linear),
                                            Actions.scaleTo(showBloomScaleTo, 1.3f, 1.3f, 0.15f, Interpolation.sine)),
                                    Actions.scaleTo(showScaleTo, 1f, 1f, 0.15f, Interpolation.sine))),
                    Actions.parallel(travelParallel,
                            Actions.moveTo(travelMoveTo, destX, destY, 0.3f, Interpolation.sine),
                            Actions.scaleTo(travelScaleTo, 0.7f, 0.7f, 0.3f, Interpolation.exp10)),
                    Actions.run(runnable, onFinishRunnable));
        }
    }
}