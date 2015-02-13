package com.mygdx.game.screens.gameplay;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.engine.actors.ActorOrigin;
import com.engine.actors.DistanceFieldFontActor;
import com.engine.events.Event;
import com.engine.events.EventsArgs;
import com.engine.events.IEventHandler;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.mygdx.game.gamelogic.targets.Crystal;
import com.mygdx.game.gamelogic.targets.CrystalTypes;
import com.mygdx.game.gamelogic.targets.Spike;
import com.mygdx.game.gamelogic.targets.Target;
import com.mygdx.game.gamelogic.targets.patterns.TargetCollisionArgs;

public class ScoreLabel extends DistanceFieldFontActor
{
    private static final String TEXT_CRYSTAL_NORMAL = "+1";
    private static final String TEXT_CRYSTAL_SPECIAL = "+3";
    private static final String TEXT_SPIKE = "BAD";

    private boolean isActive;
    public Event<TargetCollisionArgs> onFinish;
    private TargetCollisionArgs targetCollisionArgs;
    private Runnable onFinishRunnable;

    public ScoreLabel()
    {
        isActive = false;
        onFinish = new Event<TargetCollisionArgs>();
        targetCollisionArgs = new TargetCollisionArgs();

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

        addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveTo(args.target.getX(), args.target.getY() - 10f, 0.5f, Interpolation.sine),
                        Actions.sequence(
                                Actions.parallel(
                                        Actions.alpha(1f, 0.15f, Interpolation.linear),
                                        Actions.scaleTo(1.3f, 1.3f, 0.15f, Interpolation.sine)),
                                Actions.scaleTo(1f, 1f, 0.15f, Interpolation.sine))),
                Actions.parallel(
                        Actions.moveTo(destX, destY, 0.4f, Interpolation.sine),
                        Actions.scaleTo(0.7f, 0.7f, 0.4f, Interpolation.exp10)),
                Actions.run(onFinishRunnable)));
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
}