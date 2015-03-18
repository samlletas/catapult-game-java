package com.mygdx.game.screens.gameplay;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.engine.GameTime;
import com.engine.actors.ActorOrigin;
import com.engine.actors.DistanceFieldFontActor;
import com.engine.actors.TextureRegionActor;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.text.IntegerSequence;
import com.engine.utilities.ActorUtilities;
import com.mygdx.game.Common;

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

    public BaseGameHUD(Common common, BaseGameplayData gameplayData)
    {
        this.common = common;
        this.gameplayData = gameplayData;

        initializeScoreActors();
    }

    private void initializeScoreActors()
    {
        crystalActor = new TextureRegionActor(common.assets.atlasRegions.hudCrystal.getInstance());
        crystalActor.setActorOrigin(ActorOrigin.Center);
        crystalActor.setPosition(SCORE_CRYSTAL_X, SCORE_CRYSTAL_Y);

        scoreSequence = new IntegerSequence();
        scoreSequence.set(0);

        scoreActor = new DistanceFieldFontActor(scoreSequence);
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
            scoreActor.addAction(Actions.sequence(
                    Actions.scaleTo(1.4f, 1.4f, 0.045f, Interpolation.sine),
                    Actions.scaleTo(1.0f, 1.0f, 0.055f, Interpolation.sine)));
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

    public final void drawText(DistanceFieldRenderer renderer, DistanceFieldFont font)
    {
        scoreSequence.set(gameplayData.getScore());
        scoreActor.draw(renderer, font);
        onDrawText(renderer, font);
    }

    protected abstract void onReset();
    protected abstract void onUpdate(GameTime gameTime);
    protected abstract void onDrawTextures(Batch batch);
    protected abstract void onDrawText(DistanceFieldRenderer renderer, DistanceFieldFont font);
}
