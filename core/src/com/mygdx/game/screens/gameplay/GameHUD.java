package com.mygdx.game.screens.gameplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
    private final static float SPECIAL_BAR_WIDTH = 350f;

    private final static float LIVES_START_X = 620f;
    private final static float LIVES_START_Y = 40f;
    private final static float LIVES_OFFSET_X = 55f;
    private final static int   MAX_LIVES = 3;

    private final static Color SPECIAL_BAR_NORMAL_COLOR = ColorUtilities.createColor(85, 221, 225, 255);
    private final static Color SPECIAL_BAR_ACTIVE_COLOR = ColorUtilities.createColor(85, 212, 0, 255);

    private final static String ANIMATION_DEFAULT = "default";
    private final static String LIVE_ANIMATION_DISOLVE = "disolve";

    private Common common;
    private GameplayData gameplayData;
    private Color specialBarColor;

    private TextureAtlas.AtlasRegion specialBarCornerRegion;
    private TextureAtlas.AtlasRegion specialBarBorderRegion;
    private TextureAtlas.AtlasRegion specialBarRegion;

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
        specialBarCornerRegion = common.assets.atlasRegions.hudBarCorner.getInstance();
        specialBarBorderRegion = common.assets.atlasRegions.hudBarBorder.getInstance();
        specialBarRegion = common.assets.atlasRegions.hudBar.getInstance();
    }

    private void initializeChanceActors()
    {
        chanceActors = new FastArray<TextureRegionActor>(MAX_LIVES);
        TextureRegionActor actor;

        for (int i = 0; i < MAX_LIVES; i++)
        {
            actor = new TextureRegionActor(common.assets.atlasRegions.hudChance.getInstance());
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
        ColorUtilities.setColor(spriteBatch, specialBarColor);

        if (specialBarCornerRegion.isFlipX()) specialBarCornerRegion.flip(true, false);
        spriteBatch.draw(specialBarCornerRegion, SPECIAL_BAR_X, SPECIAL_BAR_Y);

        spriteBatch.draw(specialBarBorderRegion,
                SPECIAL_BAR_X + specialBarCornerRegion.getRegionWidth(),
                SPECIAL_BAR_Y, SPECIAL_BAR_WIDTH, specialBarBorderRegion.getRegionHeight());

        specialBarCornerRegion.flip(true, false);
        spriteBatch.draw(specialBarCornerRegion,
                SPECIAL_BAR_X + specialBarCornerRegion.getRegionWidth() + SPECIAL_BAR_WIDTH,
                SPECIAL_BAR_Y);

        spriteBatch.draw(specialBarRegion, SPECIAL_BAR_X + 3, SPECIAL_BAR_Y + 3,
               gameplayData.getSpecial() * SPECIAL_BAR_WIDTH / 100f, specialBarRegion.getRegionHeight());

        ColorUtilities.resetColor(spriteBatch);
    }
}
