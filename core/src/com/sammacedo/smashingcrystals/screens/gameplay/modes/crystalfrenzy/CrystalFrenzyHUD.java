package com.sammacedo.smashingcrystals.screens.gameplay.modes.crystalfrenzy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.engine.GameTime;
import com.engine.actors.ActorOrigin;
import com.engine.actors.TextureRegionActor;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.utilities.ActorUtilities;
import com.engine.utilities.ColorUtilities;
import com.engine.utilities.FastArray;
import com.sammacedo.smashingcrystals.Common;
import com.sammacedo.smashingcrystals.screens.gameplay.BaseGameHUD;

public class CrystalFrenzyHUD extends BaseGameHUD
{
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

    private final static Color SPECIAL_BAR_BACK_COLOR = ColorUtilities.createColor(0, 0, 0, 76);
    private final static Color SPECIAL_BAR_NORMAL_COLOR = ColorUtilities.createColor(0, 204, 255, 255);
    private final static Color SPECIAL_BAR_ACTIVE_COLOR = ColorUtilities.createColor(12, 231, 0, 255);

    private CrystalFrenzyData crystalFrenzyData;
    private Color specialBarColor;
    private int activeChances;

    private TextureAtlas.AtlasRegion pixelRegion;
    private TextureAtlas.AtlasRegion specialBarRegion;
    private TextureAtlas.AtlasRegion specialBarCornerRegion;
    private TextureRegion chanceInactiveRegion;
    private FastArray<TextureRegionActor> chanceActors;

    public CrystalFrenzyHUD(Common common, CrystalFrenzyData crystalFrenzyData)
    {
        super(common, crystalFrenzyData);
        this.crystalFrenzyData = crystalFrenzyData;

        initializeSpecialBar(common);
        initializeChances(common);
    }

    private void initializeSpecialBar(Common common)
    {
        pixelRegion = common.assets.atlasRegions.pixel.getInstance();
        specialBarRegion = common.assets.atlasRegions.hudBar.getInstance();
        specialBarCornerRegion = common.assets.atlasRegions.hudBarCorner.getInstance();
    }

    private void initializeChances(Common common)
    {
        chanceActors = new FastArray<TextureRegionActor>(CrystalFrenzyData.MAX_LIVES);
        TextureRegionActor actor;

        for (int i = 0; i < CrystalFrenzyData.MAX_LIVES; i++)
        {
            actor = new TextureRegionActor(common.assets.atlasRegions.hudChanceActive.getInstance());
            actor.setActorOrigin(ActorOrigin.Center);
            chanceActors.add(actor);
            ActorUtilities.growActionsArray(actor, 1);
        }

        chanceInactiveRegion = common.assets.atlasRegions.hudChanceInactive.getInstance();
    }

    @Override
    protected void onReset()
    {
        specialBarColor = SPECIAL_BAR_NORMAL_COLOR;
        resetChances();
    }

    private void resetChances()
    {
        TextureRegionActor actor;

        for (int i = 0; i < CrystalFrenzyData.MAX_LIVES; i++)
        {
            actor = chanceActors.get(i);

            actor.getColor().a = 1f;
            actor.setRotation(0f);
            actor.setScale(1f);
            actor.setPosition(CHANCES_ACTIVE_START_X + CHANCES_ACTIVE_OFFSET_X * i,
                    CHANCES_ACTIVE_START_Y);
            actor.clearActions();
        }

        activeChances = CrystalFrenzyData.MAX_LIVES;
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

    @Override
    protected void onUpdate(GameTime gameTime)
    {
        for (TextureRegionActor chance : chanceActors)
        {
            chance.act(gameTime.delta);
        }
    }

    @Override
    protected void onDrawTextures(Batch batch)
    {
        drawChances(batch);
        drawSpecialBar(batch);
    }

    private void drawChances(Batch batch)
    {
        // Chances inactivas
        for (int i = 0; i < CrystalFrenzyData.MAX_LIVES; i++)
        {
            batch.draw(chanceInactiveRegion,
                    CHANCES_INACTIVE_START_X + CHANCES_INACTIVE_OFFSET_X * i,
                    CHANCES_INACTIVE_START_Y);
        }

        // Chances activas
        for (TextureRegionActor chance : chanceActors)
        {
            chance.draw(batch, 1f);
        }
    }

    private void drawSpecialBar(Batch batch)
    {
        ColorUtilities.setColor(batch, SPECIAL_BAR_BACK_COLOR);
        batch.draw(pixelRegion, SPECIAL_BAR_X, SPECIAL_BAR_Y, SPECIAL_BAR_WIDTH, SPECIAL_BAR_HEIGHT);

        ColorUtilities.setColor(batch, specialBarColor);
        float special = crystalFrenzyData.getSpecial();
        float barWidth = special * SPECIAL_BAR_WIDTH / 100f;

        batch.draw(specialBarRegion, SPECIAL_BAR_X, SPECIAL_BAR_Y - 9f,
                barWidth, specialBarRegion.getRegionHeight());

        if (special> 0f)
        {
            if (specialBarCornerRegion.isFlipX()) specialBarCornerRegion.flip(true, false);
            batch.draw(specialBarCornerRegion, SPECIAL_BAR_X - (float) specialBarCornerRegion.getRegionWidth(),
                    SPECIAL_BAR_Y - 9f);
            specialBarCornerRegion.flip(true, false);
            batch.draw(specialBarCornerRegion, SPECIAL_BAR_X + barWidth, SPECIAL_BAR_Y - 9f);
        }

        ColorUtilities.resetColor(batch);
    }

    @Override
    protected void onDrawText(DistanceFieldRenderer renderer)
    {

    }
}
