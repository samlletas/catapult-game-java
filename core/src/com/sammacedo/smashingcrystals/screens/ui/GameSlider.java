package com.sammacedo.smashingcrystals.screens.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.text.IntegerSequence;
import com.engine.utilities.ActorUtilities;
import com.sammacedo.smashingcrystals.Common;
import com.sammacedo.smashingcrystals.helpers.ConfiguredAction;

public final class GameSlider extends Slider implements ICustomWidget
{
    public static final float FONT_SCALE = 0.60f;
    private static final float SHOW_HIDE_DURATION = 0.500f;
    private static final float HIDDEN_OFFSET_X = -400f;
    private static final float HIDDEN_OFFSET_Y = 0f;
    private static final float TEXT_OFFSET_X = 20f;

    private String label;
    private IntegerSequence valueSequence;

    private float originalX;
    private float originalY;
    private float hiddenX;
    private float hiddenY;
    private float showDelay;
    private float hideDelay;

    private ConfiguredAction showHideAction;

    public GameSlider(Common common, float min, float max, float stepSize, String label)
    {
        super(min, max, stepSize, false, common.assets.skins.ui.getInstance(), "default");

        this.label = label;
        this.valueSequence = new IntegerSequence();
        this.showHideAction = new ConfiguredAction();

        TextureRegion sliderBackground = common.assets.textureAtlases.
                gameTextures.getInstance().findRegion("ui-slider-background");
        setWidth(sliderBackground.getRegionWidth());

        setOriginalPosition(0f, 0f);
        setShowDelay(0f);
        setHideDelay(0f);

        ActorUtilities.growActionsArray(this, 1);
    }

    @Override
    public void setOriginalPosition(float x, float y)
    {
        originalX = x;
        originalY = y;

        hiddenX = x + HIDDEN_OFFSET_X;
        hiddenY = y + HIDDEN_OFFSET_Y;

        setPosition(x, y);
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
        setPosition(hiddenX, hiddenY);
        getColor().a = 0f;

        clearActions();
        addAction(showHideAction.showMovement(showDelay, SHOW_HIDE_DURATION, originalX, originalY));
    }

    @Override
    public void hide()
    {
        clearActions();
        addAction(showHideAction.hideMovement(hideDelay, SHOW_HIDE_DURATION, hiddenX, hiddenY));
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        super.draw(batch, parentAlpha);
    }

    public void drawText(DistanceFieldRenderer renderer, DistanceFieldFont font)
    {
        float x;
        float y;

        float originalScaleX = font.getScaleX();
        float originalScaleY = font.getScaleY();
        float originalAlpha = font.getAlpha();

        font.setScale(FONT_SCALE);
        font.setAlpha(getColor().a);

        BitmapFont.TextBounds bounds = font.getBounds(label);
        x = getX() - bounds.width - TEXT_OFFSET_X;
        y = getY()  + (getHeight() / 2f) - (bounds.height / 2f) - 3;
        renderer.draw(label, x, y);

        valueSequence.set((int)getValue());
        x = getX() + getWidth() + TEXT_OFFSET_X;
        renderer.draw(valueSequence, x, y);

        font.setScale(originalScaleX, originalScaleY);
        font.setAlpha(originalAlpha);
    }
}
