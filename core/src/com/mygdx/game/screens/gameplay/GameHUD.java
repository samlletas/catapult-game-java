package com.mygdx.game.screens.gameplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.engine.GameTime;
import com.engine.actors.ActorOrigin;
import com.engine.actors.DistanceFieldFontActor;
import com.engine.actors.TextureRegionActor;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.text.IntegerSequence;
import com.engine.utilities.ColorUtilities;
import com.engine.utilities.FastArray;
import com.mygdx.game.Common;
import com.mygdx.game.gamelogic.GameplayData;

public class GameHUD
{
    private final static float SPECIAL_BAR_X = 200f;
    private final static float SPECIAL_BAR_Y = 30f;
    private final static float SPECIAL_BAR_WIDTH = 340f;
    private final static float SPECIAL_BAR_HEIGHT = 13f;

    private final static float LIVES_START_X = 620f;
    private final static float LIVES_START_Y = 40f;
    private final static float LIVES_OFFSET_X = 55f;
    private final static int   MAX_LIVES = 3;

    private final static Color SPECIAL_BAR_BACK_COLOR = ColorUtilities.createColor(0, 0, 0, 76);
    private final static Color SPECIAL_BAR_NORMAL_COLOR = ColorUtilities.createColor(0, 204, 255, 255);
    private final static Color SPECIAL_BAR_ACTIVE_COLOR = ColorUtilities.createColor(12, 231, 0, 255);

    private final static String ANIMATION_DEFAULT = "default";
    private final static String LIVE_ANIMATION_DISOLVE = "disolve";

    private Common common;
    private GameplayData gameplayData;
    private Color specialBarColor;

    private TextureAtlas.AtlasRegion pixelRegion;
    private TextureAtlas.AtlasRegion specialBarRegion;
    private TextureAtlas.AtlasRegion specialBarCornerRegion;

    private TextureRegionActor crystalActor;
    private IntegerSequence integerSequence;
    private DistanceFieldFontActor scoreActor;
    private FastArray<TextureRegionActor> chanceActors;

    public GameHUD(Common common, GameplayData gameplayData)
    {
        this.common = common;
        this.gameplayData = gameplayData;

        initializeScoreActors();
        initializeSpecialBar();
        initializeChanceActors();
    }

    private void initializeScoreActors()
    {
        crystalActor = new TextureRegionActor(common.assets.atlasRegions.hudCrystal.getInstance());
        crystalActor.setActorOrigin(ActorOrigin.Center);
        crystalActor.setPosition(50f, 38f);

        integerSequence = new IntegerSequence();
        scoreActor = new DistanceFieldFontActor(integerSequence);
        scoreActor.setFontBaseScale(0.9f);
        scoreActor.setActorOrigin(ActorOrigin.CenterLeft);
        scoreActor.setPosition(74f, 35f);
    }

    private void initializeSpecialBar()
    {
        pixelRegion = common.assets.atlasRegions.pixel.getInstance();
        specialBarRegion = common.assets.atlasRegions.hudBar.getInstance();
        specialBarCornerRegion = common.assets.atlasRegions.hudBarCorner.getInstance();
    }

    private void initializeChanceActors()
    {
        chanceActors = new FastArray<TextureRegionActor>(MAX_LIVES);
        TextureRegionActor actor;

        for (int i = 0; i < MAX_LIVES; i++)
        {
            actor = new TextureRegionActor(common.assets.atlasRegions.hudChanceActive.getInstance());
            actor.setActorOrigin(ActorOrigin.Center);
            actor.setPosition(LIVES_START_X + LIVES_OFFSET_X * i, LIVES_START_Y);
            chanceActors.add(actor);
        }
    }

    public void reset()
    {
        specialBarColor = SPECIAL_BAR_NORMAL_COLOR;
    }

    public void disolveLive()
    {
        int lives = gameplayData.getLives();

        if (lives > 0)
        {

        }
    }

    public void activateSpeciarBar()
    {
        specialBarColor = SPECIAL_BAR_ACTIVE_COLOR;
    }

    public void deactivateSpecialBar()
    {
        specialBarColor = SPECIAL_BAR_NORMAL_COLOR;
    }

    public void bloom()
    {
        scoreActor.clearActions();
        scoreActor.addAction(Actions.sequence(
                Actions.scaleTo(1.3f, 1.3f, 0.025f, Interpolation.sine),
                Actions.scaleTo(1.0f, 1.0f, 0.025f, Interpolation.sine)));
    }

    public void update(GameTime gameTime)
    {
        crystalActor.act(gameTime.delta);
        scoreActor.act(gameTime.delta);

        for (TextureRegionActor chance : chanceActors)
        {
            chance.act(gameTime.delta);
        }
    }

    public void drawTextures(SpriteBatch spriteBatch)
    {
        crystalActor.draw(spriteBatch, 1f);
        drawChances(spriteBatch);
        drawSpecialBar(spriteBatch);
    }

    public void drawScore(DistanceFieldRenderer renderer, DistanceFieldFont font)
    {
        integerSequence.set(gameplayData.getScore());
        scoreActor.draw(renderer, font);
    }

    private void drawChances(SpriteBatch spriteBatch)
    {
        for (TextureRegionActor chance : chanceActors)
        {
            chance.draw(spriteBatch, 1f);
        }
    }

    private void drawSpecialBar(SpriteBatch spriteBatch)
    {
        ColorUtilities.setColor(spriteBatch, SPECIAL_BAR_BACK_COLOR);
        spriteBatch.draw(pixelRegion, SPECIAL_BAR_X, SPECIAL_BAR_Y, SPECIAL_BAR_WIDTH, SPECIAL_BAR_HEIGHT);

        ColorUtilities.setColor(spriteBatch, specialBarColor);
        float special = gameplayData.getSpecial();
        float barWidth = special * SPECIAL_BAR_WIDTH / 100f;

        spriteBatch.draw(specialBarRegion, SPECIAL_BAR_X, SPECIAL_BAR_Y - 9f,
                barWidth, specialBarRegion.getRegionHeight());

        if (special> 0f)
        {
            if (specialBarCornerRegion.isFlipX()) specialBarCornerRegion.flip(true, false);
            spriteBatch.draw(specialBarCornerRegion, SPECIAL_BAR_X - (float)specialBarCornerRegion.getRegionWidth(),
                    SPECIAL_BAR_Y - 9f);
            specialBarCornerRegion.flip(true, false);
            spriteBatch.draw(specialBarCornerRegion, SPECIAL_BAR_X + barWidth, SPECIAL_BAR_Y - 9f);
        }

        ColorUtilities.resetColor(spriteBatch);
    }
}
