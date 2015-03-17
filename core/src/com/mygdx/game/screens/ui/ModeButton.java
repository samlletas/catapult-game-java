package com.mygdx.game.screens.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.engine.actors.ActorOrigin;
import com.engine.actors.DistanceFieldFontActor;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.utilities.ActorUtilities;
import com.mygdx.game.Common;
import com.mygdx.game.Global;
import com.mygdx.game.helpers.ConfiguredAction;

public final class ModeButton extends Button implements ICustomWidget
{
    public static final float TITLE_FONT_SCALE = 0.70f;
    public static final float TEXT_FONT_SCALE  = 0.65f;

    private static final float SHOW_HIDE_DURATION = 0.500f;
    private static final float HIDDEN_OFFSET_X = 500f;
    private static final float HIDDEN_OFFSET_Y = 500f;

    private static final float TITLE_OFFSET_X = 185f;
    private static final float TITLE_OFFSET_Y = 35f;
    private static final float TEXT_OFFSET_X = 185f;
    private static final float TEXT_OFFSET_Y = 70f;

    private float originalX;
    private float originalY;
    private float hiddenX;
    private float hiddenY;
    private float showDelay;
    private float hideDelay;
    private boolean fromRight;

    private DistanceFieldFontActor titleActor;
    private DistanceFieldFontActor textActor;

    private ConfiguredAction buttonShowHideAction;
    private ConfiguredAction titleShowHideAction;
    private ConfiguredAction textShowHideAction;

    public ModeButton(Common common, String title, String text)
    {
        this(common, title, text, false);
    }

    public ModeButton(Common common, String title, String text, boolean fromRight)
    {
        super(common.assets.skins.ui.getInstance(), Global.ButtonStyles.MODE);

        this.titleActor = new DistanceFieldFontActor(title);
        this.textActor = new DistanceFieldFontActor(text);

        this.buttonShowHideAction = new ConfiguredAction();
        this.titleShowHideAction = new ConfiguredAction();
        this.textShowHideAction = new ConfiguredAction();

        this.titleActor.setFontBaseScale(TITLE_FONT_SCALE);
        this.textActor.setFontBaseScale(TEXT_FONT_SCALE);

        this.titleActor.setActorOrigin(ActorOrigin.Center);
        this.textActor.setActorOrigin(ActorOrigin.TopCenter);

        this.fromRight = fromRight;

        setOriginalPosition(0f, 0f);
        setShowDelay(0f);
        setHideDelay(0f);

        ActorUtilities.growActionsArray(this, 1);
        ActorUtilities.growActionsArray(titleActor, 1);
        ActorUtilities.growActionsArray(textActor, 1);
    }

    @Override
    public void setOriginalPosition(float x, float y)
    {
        float halfWidth = getWidth() / 2f;
        float halfHeight = getHeight() / 2f;

        originalX = x - halfWidth;
        originalY = y - halfHeight;

        if (fromRight)
        {
            hiddenX = x - halfWidth + HIDDEN_OFFSET_X;
            hiddenY = y - halfHeight + HIDDEN_OFFSET_Y;
        }
        else
        {
            hiddenX = x - halfWidth - HIDDEN_OFFSET_X;
            hiddenY = y - halfHeight + HIDDEN_OFFSET_Y;
        }

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
        addAction(buttonShowHideAction.showMovement(showDelay, SHOW_HIDE_DURATION, originalX, originalY));

        titleActor.setPosition(hiddenX + TITLE_OFFSET_X, hiddenY + TITLE_OFFSET_Y);
        titleActor.getColor().a = 0f;
        titleActor.clearActions();
        titleActor.addAction(titleShowHideAction.showMovement(showDelay, SHOW_HIDE_DURATION,
                originalX + TITLE_OFFSET_X, originalY + TITLE_OFFSET_Y));

        textActor.setPosition(hiddenX + TITLE_OFFSET_X, hiddenY + TITLE_OFFSET_Y);
        textActor.getColor().a = 0f;
        textActor.clearActions();
        textActor.addAction(textShowHideAction.showMovement(showDelay, SHOW_HIDE_DURATION,
                originalX + TEXT_OFFSET_X, originalY + TEXT_OFFSET_Y));
    }

    @Override
    public void hide()
    {
        clearActions();
        addAction(buttonShowHideAction.hideMovement(hideDelay, SHOW_HIDE_DURATION, hiddenX, hiddenY));

        titleActor.clearActions();
        titleActor.addAction(titleShowHideAction.hideMovement(hideDelay, SHOW_HIDE_DURATION,
                hiddenX + TITLE_OFFSET_X, hiddenY + TITLE_OFFSET_Y));

        textActor.clearActions();
        textActor.addAction(textShowHideAction.hideMovement(hideDelay, SHOW_HIDE_DURATION,
                hiddenX + TEXT_OFFSET_X, hiddenY + TEXT_OFFSET_Y));
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);

        titleActor.act(delta);
        textActor.act(delta);
    }

    public void drawText(DistanceFieldRenderer renderer, DistanceFieldFont font)
    {
        titleActor.draw(renderer, font);
        textActor.drawMultiline(renderer, font, BitmapFont.HAlignment.CENTER);
    }
}
