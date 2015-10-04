package com.sammacedo.smashingcrystals.screens.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.engine.utilities.ActorUtilities;
import com.sammacedo.smashingcrystals.Common;
import com.sammacedo.smashingcrystals.helpers.ConfiguredAction;

public final class GameButton extends ImageButton implements ICustomWidget
{
    private static final float SHOW_HIDE_DURATION = 0.500f;
    private static final float HIDDEN_OFFSET_X = 0f;
    private static final float HIDDEN_OFFSET_Y = 400f;

    private float originalX;
    private float originalY;
    private float hiddenX;
    private float hiddenY;
    private float showDelay;
    private float hideDelay;

    private ConfiguredAction showHideAction;

    public GameButton(Common common, String style)
    {
        super(common.assets.skins.ui.getInstance(), style);
        this.showHideAction = new ConfiguredAction();

        setOriginalPosition(0f, 0f);
        setShowDelay(0f);
        setHideDelay(0f);

        ActorUtilities.growActionsArray(this, 1);
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
        hideImmediately();
        clearActions();
        addAction(showHideAction.showMovement(showDelay, SHOW_HIDE_DURATION, originalX, originalY));
    }

    public void hideImmediately()
    {
        setPosition(hiddenX, hiddenY);
        getColor().a = 0f;
    }

    @Override
    public void hide()
    {
        clearActions();
        addAction(showHideAction.hideMovement(hideDelay, SHOW_HIDE_DURATION, hiddenX, hiddenY));
    }
}
