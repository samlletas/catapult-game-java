package com.mygdx.game.screens.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.engine.GameTime;
import com.engine.actors.Actions;
import com.engine.actors.ActorOrigin;
import com.engine.actors.TextureRegionActor;
import com.engine.graphics.GraphicsSettings;
import com.engine.utilities.ActorUtilities;
import com.mygdx.game.Common;
import com.mygdx.game.helpers.ConfiguredAction;

public class GameTitle implements ICustomWidget
{
    private static final float SHOW_DURATION = 0.450f;
    private static final float HIDE_DURATION = 0.400f;
    private static final float ACTION_DELAY  = 0.040f;
    private static final float LINE_OFFSET   = 58f;
    private static final float POSITION_Y    = 77f;

    private float x;
    private float y;

    private TextureRegionActor smashingActor;
    private TextureRegionActor crystalsActor;

    private ConfiguredAction smashingActions;
    private ConfiguredAction crystalsActions;

    private TextureRegionActor smashingLoopActor;
    private TextureRegionActor crystalsLoopActor;

    private AnimationLoopAction smashingLoopActions;
    private AnimationLoopAction crystalsLoopActions;

    private float showDelay;
    private float hideDelay;

    public GameTitle(Common common, GraphicsSettings graphicsSettings)
    {
        x = graphicsSettings.virtualWidth / 2f;
        y = POSITION_Y;

        smashingActor = new TextureRegionActor(common.assets.atlasRegions.titleSmashing.getInstance());
        smashingLoopActor = new TextureRegionActor(common.assets.atlasRegions.titleSmashing.getInstance());
        crystalsActor = new TextureRegionActor(common.assets.atlasRegions.titleCrystals.getInstance());
        crystalsLoopActor = new TextureRegionActor(common.assets.atlasRegions.titleCrystals.getInstance());

        smashingActor.setActorOrigin(ActorOrigin.Center);
        smashingLoopActor.setActorOrigin(ActorOrigin.Center);
        crystalsActor.setActorOrigin(ActorOrigin.Center);
        crystalsLoopActor.setActorOrigin(ActorOrigin.Center);

        smashingActor.setPosition(x, y);
        smashingLoopActor.setPosition(x, y);
        crystalsActor.setPosition(x, y + LINE_OFFSET);
        crystalsLoopActor.setPosition(x, y + LINE_OFFSET);

        ActorUtilities.growActionsArray(smashingActor, 2);
        ActorUtilities.growActionsArray(smashingLoopActor, 1);
        ActorUtilities.growActionsArray(crystalsActor, 2);
        ActorUtilities.growActionsArray(crystalsLoopActor, 1);

        smashingActions = new ConfiguredAction();
        crystalsActions = new ConfiguredAction();
        smashingLoopActions = new AnimationLoopAction(smashingLoopActor);
        crystalsLoopActions = new AnimationLoopAction(crystalsLoopActor);
    }

    @Override
    public void setOriginalPosition(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setShowDelay(float delay)
    {
        this.showDelay = delay;
    }

    @Override
    public void setHideDelay(float delay)
    {
        this.hideDelay = delay;
    }

    @Override
    public void show()
    {
        smashingLoopActor.getColor().a = 0f;
        crystalsLoopActor.getColor().a = 0f;

        clearActions();

        smashingActor.getColor().a = 0f;
        smashingActor.setScale(0.0f);
        smashingActor.addAction(smashingActions.showScale(showDelay, SHOW_DURATION, 1f, 1f));
        smashingActor.addAction(smashingLoopActions.getLoopStartAction());

        crystalsActor.getColor().a = 0f;
        crystalsActor.setScale(0.0f);
        crystalsActor.addAction(crystalsActions.showScale(showDelay + ACTION_DELAY, SHOW_DURATION, 1f, 1f));
        crystalsActor.addAction(crystalsLoopActions.getLoopStartAction());
    }

    @Override
    public void hide()
    {
        clearActions();

        smashingActor.addAction(smashingActions.hideScale(hideDelay + ACTION_DELAY, HIDE_DURATION, 0.1f, 0.1f));
        crystalsActor.addAction(crystalsActions.hideScale(hideDelay, HIDE_DURATION, 0.1f, 0.1f));
    }

    public void update(GameTime gameTime)
    {
        smashingActor.act(gameTime.delta);
        smashingLoopActor.act(gameTime.delta);
        crystalsActor.act(gameTime.delta);
        crystalsLoopActor.act(gameTime.delta);
    }

    public void drawTextures(Batch batch)
    {
        smashingActor.draw(batch, 1f);
        smashingLoopActor.draw(batch, 1f);
        crystalsActor.draw(batch, 1f);
        crystalsLoopActor.draw(batch, 1f);

    }

    private void clearActions()
    {
        smashingActor.clearActions();
        smashingLoopActor.clearActions();
        crystalsActor.clearActions();
        crystalsLoopActor.clearActions();
    }

    class AnimationLoopAction
    {
        Actor actor;
        Runnable startLoopRunnable;

        AfterAction after = new AfterAction();
        RunnableAction runnable = new RunnableAction();
        RepeatAction repeat = new RepeatAction();
        SequenceAction sequence = new SequenceAction();
        AlphaAction resetAlpha = new AlphaAction();
        ScaleToAction resetScale = new ScaleToAction();
        ParallelAction parallel = new ParallelAction();
        AlphaAction fade = new AlphaAction();
        ScaleToAction scaleTo = new ScaleToAction();
        DelayAction delay = new DelayAction();

        AnimationLoopAction(Actor actor)
        {
            this.actor = actor;

            startLoopRunnable = new Runnable()
            {
                @Override
                public void run()
                {
                    AnimationLoopAction.this.actor.addAction(Actions.repeat(repeat, RepeatAction.FOREVER,
                                    Actions.sequence(sequence,
                                            Actions.parallel(parallel,
                                                    Actions.alpha(resetAlpha, 0.5f, 0f),
                                                    Actions.fadeOut(fade, 0.5f, Interpolation.linear),
                                                    Actions.scaleTo(resetScale, 1f, 1f, 0f),
                                                    Actions.scaleTo(scaleTo, 1.2f, 1f, 0.5f, Interpolation.sine)),
                                            Actions.delay(delay, 1.5f))));
                }
            };
        }

        Action getLoopStartAction()
        {
            return Actions.after(after, Actions.run(runnable, startLoopRunnable));
        }
    }
}