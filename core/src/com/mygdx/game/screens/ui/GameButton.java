package com.mygdx.game.screens.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.engine.utilities.ActorUtilities;
import com.engine.utilities.ColorUtilities;
import com.mygdx.game.Common;
import com.mygdx.game.helpers.ConfiguredAction;

public final class GameButton extends ImageButton implements ICustomWidget
{
    public static final class ButtonTypes
    {
        public static final ButtonType BIG    = new ButtonType(74f, 80f, 1f);
        public static final ButtonType MEDIUM = new ButtonType(63f, 60f, 0.83f);
        public static final ButtonType SMALL  = new ButtonType(44f, 40f, 0.6f);
        public static final ButtonType TINY   = new ButtonType(23f, 75f, 0.41f);
    }

    private static final float SHOW_HIDE_DURATION = 0.500f;
    private static final float HIDDEN_OFFSET_X = 0f;
    private static final float HIDDEN_OFFSET_Y = 400f;

    private ButtonType buttonType;
    private TextureRegion glowRegion;
    private float glowAngle;
    private float originalX;
    private float originalY;
    private float hiddenX;
    private float hiddenY;
    private float showDelay;
    private float hideDelay;

    private ConfiguredAction showHideAction;

    public GameButton(Common common, ButtonType buttonType, String style)
    {
        super(common.assets.skins.ui.getInstance(), style);

        this.buttonType = buttonType;
        this.glowRegion = common.assets.atlasRegions.buttonGlow.getInstance();
        this.glowAngle = 0f;
        this.showHideAction = new ConfiguredAction();

        setOriginalPosition(0f, 0f);
        setShowDelay(0f);
        setHideDelay(0f);

        ActorUtilities.growActionsArray(this, 1);
    }

    public void setGlowAngle(float glowAngle)
    {
        this.glowAngle = glowAngle;
    }

    @Override
    public void setOriginalPosition(float x, float y)
    {
        float halfWidth = getWidth() / 2f;
        float halfHeight = getHeight() / 2f;

        originalX = x - halfWidth;
        originalY = y - halfHeight;

        hiddenX = x - halfWidth + HIDDEN_OFFSET_X;
        hiddenY = y - halfHeight + HIDDEN_OFFSET_Y;

        setPosition(x, y);
    }

    public float getCenterX()
    {
        return getX() + (getWidth() / 2f);
    }

    public float getCenterY()
    {
        return getY() + (getHeight() / 2f);
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
        addAction(showHideAction.show(showDelay, SHOW_HIDE_DURATION, originalX, originalY));
    }

    @Override
    public void hide()
    {
        clearActions();
        addAction(showHideAction.hide(hideDelay, SHOW_HIDE_DURATION, hiddenX, hiddenY));
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);

        glowAngle += buttonType.glowAngularSpeed * delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        super.draw(batch, parentAlpha);

        drawGlow(batch);
    }

    private void drawGlow(Batch batch)
    {
        float scale = buttonType.glowScale;
        float width = glowRegion.getRegionWidth();
        float height = glowRegion.getRegionHeight();

        float x = getCenterX() - (width / 2f) + (buttonType.distanceToCenter * MathUtils.cosDeg(glowAngle));
        float y = getCenterY() - (height / 2f) + (buttonType.distanceToCenter * MathUtils.sinDeg(glowAngle));

        float originalAlpha = batch.getColor().a;
        float alpha = this.getColor().a;

        ColorUtilities.setAlpha(batch, alpha);
        batch.draw(glowRegion, x, y, width / 2f, height / 2f, width, height, scale, scale, glowAngle);
        ColorUtilities.setAlpha(batch, originalAlpha);
    }
}
