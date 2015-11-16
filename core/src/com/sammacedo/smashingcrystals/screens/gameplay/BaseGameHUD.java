package com.sammacedo.smashingcrystals.screens.gameplay;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.engine.GameTime;
import com.engine.actors.Actions;
import com.engine.actors.ActorOrigin;
import com.engine.actors.DistanceFieldFontActor;
import com.engine.actors.TextureRegionActor;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.text.IntegerSequence;
import com.engine.utilities.ActorUtilities;
import com.sammacedo.smashingcrystals.Common;

public abstract class BaseGameHUD
{
    public final static float SCORE_TEXT_X = 74f;
    public final static float SCORE_TEXT_Y = 35f;
    private final static float SCORE_TEXT_SCALE = 0.9f;

    private final static float SCORE_CRYSTAL_X = 50f;
    private final static float SCORE_CRYSTAL_Y = 38f;

    private Common common;
    private BaseGameplayData gameplayData;

    private TextureRegionActor crystalActor;
    private IntegerSequence scoreSequence;
    private DistanceFieldFontActor scoreActor;

    private SequenceAction sequence;
    private ScaleToAction scaleToGrow;
    private ScaleToAction scaleToShrink;

    public BaseGameHUD(Common common, BaseGameplayData gameplayData)
    {
        this.common = common;
        this.gameplayData = gameplayData;

        this.sequence = new SequenceAction();
        this.scaleToGrow = new ScaleToAction();
        this.scaleToShrink = new ScaleToAction();

        initializeScoreActors();
    }

    private void initializeScoreActors()
    {
        crystalActor = new TextureRegionActor(common.assets.atlasRegions.hudCrystal.getInstance());
        crystalActor.setActorOrigin(ActorOrigin.Center);
        crystalActor.setPosition(SCORE_CRYSTAL_X, SCORE_CRYSTAL_Y);

        scoreSequence = new IntegerSequence();
        scoreSequence.set(0);

        scoreActor = new DistanceFieldFontActor(common.assets.distanceFieldFonts.furore.getInstance(), scoreSequence);
        scoreActor.setFontBaseScale(SCORE_TEXT_SCALE);
        scoreActor.setActorOrigin(ActorOrigin.CenterLeft);
        scoreActor.setPosition(SCORE_TEXT_X, SCORE_TEXT_Y);

        ActorUtilities.growActionsArray(scoreActor, 1);
    }

    public final void reset()
    {
        scoreSequence.set(0);
        scoreActor.clearActions();
        onReset();
    }

    public final void bloomScore()
    {
        if (scoreActor.getActions().size == 0)
        {
            scoreActor.clearActions();
            scoreActor.addAction(Actions.sequence(sequence,
                    Actions.scaleTo(scaleToGrow, 1.4f, 1.4f, 0.060f, Interpolation.sine),
                    Actions.scaleTo(scaleToShrink, 1.0f, 1.0f, 0.060f, Interpolation.sine)));
        }
    }

    public final void update(GameTime gameTime)
    {
        crystalActor.act(gameTime.delta);
        scoreActor.act(gameTime.delta);
        onUpdate(gameTime);
    }

    public final void drawTextures(Batch batch)
    {
        crystalActor.draw(batch, 1f);
        onDrawTextures(batch);
    }

    public final void drawText(DistanceFieldRenderer renderer)
    {
        scoreSequence.set(gameplayData.getScore());
        scoreActor.draw(renderer);
        onDrawText(renderer);
    }

    protected abstract void onReset();
    protected abstract void onUpdate(GameTime gameTime);
    protected abstract void onDrawTextures(Batch batch);
    protected abstract void onDrawText(DistanceFieldRenderer renderer);
}
