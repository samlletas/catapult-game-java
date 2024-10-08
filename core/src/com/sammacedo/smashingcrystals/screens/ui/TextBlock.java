package com.sammacedo.smashingcrystals.screens.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.engine.GameTime;
import com.engine.actors.ActorOrigin;
import com.engine.actors.DistanceFieldFontActor;
import com.engine.actors.TextureRegionActor;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.utilities.ActorUtilities;
import com.sammacedo.smashingcrystals.Common;
import com.sammacedo.smashingcrystals.helpers.ConfiguredAction;

public final class TextBlock implements ICustomWidget
{
    public static final float TITLE_FONT_SCALE = 0.70f;
    public static final float TEXT_FONT_SCALE  = 0.60f;
    private static final float DEFAULT_LINE_LENGHT = 270f;

    private static final float SHOW_HIDE_DURATION = 0.500f;
    private static final float HIDDEN_OFFSET_X = 200f;
    private static final float HIDDEN_OFFSET_Y = 0f;

    private static final float LINE_OFFSET_X = -15f;
    private static final float LINE_OFFSET_Y =  17f;
    private static final float LINE_SCALE_Y = 0.5f;

    private static final float TEXT_OFFSET_X = 50f;
    private static final float TEXT_OFFSET_Y = 34f;

    private DistanceFieldFontActor titleActor;
    private TextureRegionActor lineActor;
    private DistanceFieldFontActor textActor;

    private ConfiguredAction titleShowHideAction;
    private ConfiguredAction lineShowHideAction;
    private ConfiguredAction textShowHideAction;

    private float originalX;
    private float originalY;
    private float hiddenX;
    private float hiddenY;
    private float showDelay;
    private float hideDelay;
    private boolean fromRight;

    private float titleOffsetX;
    private float titleOffsetY;

    private float textOffsetX;
    private float textOffsetY;

    public TextBlock(Common common, CharSequence title)
    {
        this(common, title, null);
    }

    public TextBlock(Common common, CharSequence title, CharSequence text)
    {
        this(common, title, text, DEFAULT_LINE_LENGHT, false);
    }

    public TextBlock(Common common, CharSequence title, CharSequence text, float lineLenght,
                     boolean fromRight)
    {
        TextureRegion line = common.assets.atlasRegions.uiHeaderLine.getInstance();

        this.titleActor = new DistanceFieldFontActor(common.assets.distanceFieldFonts.furore.getInstance(), title);
        this.lineActor = new TextureRegionActor(line);
        this.textActor = new DistanceFieldFontActor(common.assets.distanceFieldFonts.furore.getInstance(), text);

        this.titleShowHideAction = new ConfiguredAction();
        this.lineShowHideAction = new ConfiguredAction();
        this.textShowHideAction = new ConfiguredAction();

        this.titleActor.setFontBaseScale(TITLE_FONT_SCALE);
        this.lineActor.setWidth(lineLenght);
        this.lineActor.setScaleY(LINE_SCALE_Y);
        this.textActor.setFontBaseScale(TEXT_FONT_SCALE);

        this.fromRight = fromRight;

        this.titleOffsetX = 0f;
        this.titleOffsetY = 0f;

        this.textOffsetX = TEXT_OFFSET_X;
        this.textOffsetY = TEXT_OFFSET_Y;

        ActorUtilities.growActionsArray(titleActor, 1);
        ActorUtilities.growActionsArray(lineActor, 1);
        ActorUtilities.growActionsArray(textActor, 1);
    }

    public CharSequence getTitle()
    {
        return titleActor.getText();
    }

    public void setTitle(CharSequence title)
    {
        titleActor.setText(title);
    }

    public CharSequence getText()
    {
        return textActor.getText();
    }

    public void setText(CharSequence text)
    {
        textActor.setText(text);
    }

    public boolean isFromRight()
    {
        return fromRight;
    }

    public void setFromRight(boolean fromRight)
    {
        this.fromRight = fromRight;
    }

    public void setTitleOffset(float x, float y)
    {
        titleOffsetX = x;
        titleOffsetY = y;
    }

    public void setTextOffset(float x, float y)
    {
        textOffsetX = x;
        textOffsetY = y;
    }

    public void setTitleOrigin(ActorOrigin origin)
    {
        titleActor.setActorOrigin(origin);
    }

    public void setTextOrigin(ActorOrigin origin)
    {
        textActor.setActorOrigin(origin);
    }

    public void setTitleFontScale(float scale)
    {
        titleActor.setFontBaseScale(scale);
    }

    public void setTextFontScale(float scale)
    {
        textActor.setFontBaseScale(scale);
    }

    @Override
    public void setOriginalPosition(float x, float y)
    {
        originalX = x;
        originalY = y;

        if (fromRight)
        {
            hiddenX = x + HIDDEN_OFFSET_X;
            hiddenY = y + HIDDEN_OFFSET_Y;
        }
        else
        {
            hiddenX = x - HIDDEN_OFFSET_X;
            hiddenY = y + HIDDEN_OFFSET_Y;
        }
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
        // Animación del título
        titleActor.setPosition(hiddenX + titleOffsetX, hiddenY + titleOffsetY);
        titleActor.getColor().a = 0f;

        titleActor.clearActions();
        titleActor.addAction(titleShowHideAction.showMovement(showDelay,
                SHOW_HIDE_DURATION, originalX + titleOffsetX, originalY + titleOffsetY));

        // Animación de la línea
        lineActor.setPosition(hiddenX + LINE_OFFSET_X, hiddenY + LINE_OFFSET_Y);
        lineActor.getColor().a = 0f;

        lineActor.clearActions();
        lineActor.addAction(lineShowHideAction.showMovement(showDelay + 0.05f,
                SHOW_HIDE_DURATION, originalX + LINE_OFFSET_X, originalY + LINE_OFFSET_Y));

        // Animación del texto
        textActor.setPosition(hiddenX + textOffsetX, hiddenY + textOffsetY);
        textActor.getColor().a = 0f;

        textActor.clearActions();
        textActor.addAction(textShowHideAction.showMovement(showDelay + 0.1f,
                SHOW_HIDE_DURATION, originalX + textOffsetX, originalY + textOffsetY));
    }

    @Override
    public void hide()
    {
        // Animación del título
        titleActor.clearActions();
        titleActor.addAction(titleShowHideAction.hideMovement(hideDelay,
                SHOW_HIDE_DURATION, hiddenX, hiddenY));

        // Animación de la línea
        lineActor.clearActions();
        lineActor.addAction(lineShowHideAction.hideMovement(hideDelay + 0.05f,
                SHOW_HIDE_DURATION, hiddenX + LINE_OFFSET_X, hiddenY + LINE_OFFSET_Y));

        // Animación del texto
        textActor.clearActions();
        textActor.addAction(textShowHideAction.hideMovement(hideDelay + 0.05f,
                SHOW_HIDE_DURATION, hiddenX + TEXT_OFFSET_X, hiddenY + TEXT_OFFSET_Y));
    }

    public void update(GameTime gameTime)
    {
        titleActor.act(gameTime.delta);
        lineActor.act(gameTime.delta);
        textActor.act(gameTime.delta);
    }

    public void drawTextures(Batch batch)
    {
        lineActor.draw(batch, 1f);
    }

    public void drawText(DistanceFieldRenderer renderer)
    {
        titleActor.draw(renderer);
        textActor.draw(renderer);
    }
}