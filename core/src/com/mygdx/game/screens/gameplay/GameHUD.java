package com.mygdx.game.screens.gameplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    public final static float SCORE_TEXT_X = 74f;
    public final static float SCORE_TEXT_Y = 35f;
    private final static float SCORE_TEXT_SCALE = 0.9f;

    private final static float SCORE_CRYSTAL_X = 50f;
    private final static float SCORE_CRYSTAL_Y = 38f;

    private final static float SPECIAL_BAR_X = 200f;
    private final static float SPECIAL_BAR_Y = 30f;
    private final static float SPECIAL_BAR_WIDTH = 340f;
    private final static float SPECIAL_BAR_HEIGHT = 13f;

    private final static float CHANCES_ACTIVE_START_X = 620f;
    private final static float CHANCES_ACTIVE_START_Y = 40f;
    private final static float CHANCES_ACTIVE_OFFSET_X = 55f;

    private final static float CHANCES_INACTIVE_START_X = 599f;
    private final static float CHANCES_INACTIVE_START_Y = 22f;
    private final static float CHANCES_INACTIVE_OFFSET_X = 55f;

    private final static int   MAX_LIVES = 3;

    private final static Color SPECIAL_BAR_BACK_COLOR = ColorUtilities.createColor(0, 0, 0, 76);
    private final static Color SPECIAL_BAR_NORMAL_COLOR = ColorUtilities.createColor(0, 204, 255, 255);
    private final static Color SPECIAL_BAR_ACTIVE_COLOR = ColorUtilities.createColor(12, 231, 0, 255);

    private Common common;
    private GameplayData gameplayData;
    private Color specialBarColor;
    private int activeChances;

    private TextureAtlas.AtlasRegion pixelRegion;
    private TextureAtlas.AtlasRegion specialBarRegion;
    private TextureAtlas.AtlasRegion specialBarCornerRegion;
    private TextureRegion chanceInactiveRegion;

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
        initializeChances();
    }

    private void initializeScoreActors()
    {
        crystalActor = new TextureRegionActor(common.assets.atlasRegions.hudCrystal.getInstance());
        crystalActor.setActorOrigin(ActorOrigin.Center);
        crystalActor.setPosition(SCORE_CRYSTAL_X, SCORE_CRYSTAL_Y);

        integerSequence = new IntegerSequence();
        scoreActor = new DistanceFieldFontActor(integerSequence);
        scoreActor.setFontBaseScale(SCORE_TEXT_SCALE);
        scoreActor.setActorOrigin(ActorOrigin.CenterLeft);
        scoreActor.setPosition(SCORE_TEXT_X, SCORE_TEXT_Y);
    }

    private void initializeSpecialBar()
    {
        pixelRegion = common.assets.atlasRegions.pixel.getInstance();
        specialBarRegion = common.assets.atlasRegions.hudBar.getInstance();
        specialBarCornerRegion = common.assets.atlasRegions.hudBarCorner.getInstance();
    }

    private void initializeChances()
    {
        chanceActors = new FastArray<TextureRegionActor>(MAX_LIVES);
        TextureRegionActor actor;

        for (int i = 0; i < MAX_LIVES; i++)
        {
            actor = new TextureRegionActor(common.assets.atlasRegions.hudChanceActive.getInstance());
            actor.setActorOrigin(ActorOrigin.Center);
            chanceActors.add(actor);
        }

        chanceInactiveRegion = common.assets.atlasRegions.hudChanceInactive.getInstance();
    }

    public void reset()
    {
        specialBarColor = SPECIAL_BAR_NORMAL_COLOR;
        resetChances();
    }

    private void resetChances()
    {
        TextureRegionActor actor;

        for (int i = 0; i < MAX_LIVES; i++)
        {
            actor = chanceActors.get(i);

            actor.getColor().a = 1f;
            actor.setRotation(0f);
            actor.setScale(1f);
            actor.setPosition(CHANCES_ACTIVE_START_X + CHANCES_ACTIVE_OFFSET_X * i,
                    CHANCES_ACTIVE_START_Y);
            actor.clearActions();
        }

        activeChances = GameplayData.MAX_LIVES;
        animateCurrentChance();
    }

    public TextureRegionActor getChance(int index)
    {
        if (index >= 0)
        {
            return chanceActors.get(index);
        }

        return null;
    }

    private TextureRegionActor getCurrentChance()
    {
        if (activeChances > 0)
        {
            return chanceActors.get(activeChances - 1);
        }

        return null;
    }

    public void animateCurrentChance()
    {
        if (activeChances > 0)
        {
            getCurrentChance().addAction(Actions.repeat(RepeatAction.FOREVER,
                    Actions.sequence(
                            Actions.scaleTo(1.1f, 1.1f, 0.3f, Interpolation.sine),
                            Actions.scaleTo(1.0f, 1.0f, 0.3f, Interpolation.sine))));
        }
    }

    public void disolveChance()
    {
        TextureRegionActor chance = getCurrentChance();
        activeChances--;

        chance.clearActions();
//        chance.addAction(Actions.sequence(
//                Actions.parallel(
//                        Actions.alpha(0f, 0.5f, Interpolation.sine),
//                        Actions.rotateTo(90f, 0.5f, Interpolation.sine),
//                        Actions.moveBy(20f, 20f, 0.5f, Interpolation.sine))));

        chance.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.alpha(0f, 0.25f, Interpolation.sineOut),
                        Actions.scaleBy(1f, 1f, 0.25f, Interpolation.sineOut))));
    }

    public void activateSpeciarBar()
    {
        specialBarColor = SPECIAL_BAR_ACTIVE_COLOR;
    }

    public void deactivateSpecialBar()
    {
        specialBarColor = SPECIAL_BAR_NORMAL_COLOR;
    }

    public void bloomScore()
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
        // Chances inactivas
        for (int i = 0; i < MAX_LIVES; i++)
        {
            spriteBatch.draw(chanceInactiveRegion,
                    CHANCES_INACTIVE_START_X + CHANCES_INACTIVE_OFFSET_X * i,
                    CHANCES_INACTIVE_START_Y);
        }

        // Chances activas
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
