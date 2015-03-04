package com.mygdx.game.screens.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.engine.GameTime;
import com.engine.actors.Actions;
import com.engine.actors.DistanceFieldFontActor;
import com.engine.actors.TextureRegionActor;
import com.engine.actors.ActorOrigin;
import com.engine.graphics.GraphicsSettings;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.utilities.ActorUtilities;
import com.mygdx.game.Common;

public final class Header implements ICustomWidget
{
    public static final float FONT_SCALE = 1f;
    private static final float SHOW_HIDE_DURATION = 0.500f;
    private static final float GRADIENT_SCALE = 2f;

    private static final float LINE_OFFSET_X = 80f;
    private static final float LINE_OFFSET_Y = 23f;
    private static final float LINE_HIDDEN_OFFSET_X = 1000f;
    private static final float LINE_HIDDEN_OFFSET_Y = 0f;

    private static final float TEXT_OFFSET_X = 0f;
    private static final float TEXT_OFFSET_Y = -4f;

    private final String text;
    private float x;
    private float y;

    private TextureRegionActor topLineActor;
    private TextureRegionActor bottomLineActor;
    private TextureRegionActor gradientActor;
    private DistanceFieldFontActor textActor;

    private LineConfiguredAction topLineAction;
    private LineConfiguredAction bottomLineAction;
    private GradientConfiguredAction gradientAction;
    private TextConfiguredAction textAction;

    private float showDelay;
    private float hideDelay;

    public Header(Common common, GraphicsSettings graphicsSettings, String text)
    {
        this.text = text;
        this.x = graphicsSettings.virtualWidth / 2f;
        this.y = 75f;

        TextureRegion gradient = common.assets.atlasRegions.uiHeaderBackground.getInstance();
        TextureRegion line = common.assets.atlasRegions.uiHeaderLine.getInstance();

        this.topLineActor = new TextureRegionActor(line);
        this.bottomLineActor = new TextureRegionActor(line);
        this.gradientActor = new TextureRegionActor(gradient);
        this.textActor = new DistanceFieldFontActor(text);

        this.topLineActor.setActorOrigin(ActorOrigin.Center);
        this.bottomLineActor.setActorOrigin(ActorOrigin.Center);
        this.gradientActor.setActorOrigin(ActorOrigin.Center);
        this.textActor.setActorOrigin(ActorOrigin.Center);

        this.gradientActor.setScale(GRADIENT_SCALE);
        this.textActor.setFontBaseScale(FONT_SCALE);

        this.topLineAction = new LineConfiguredAction();
        this.bottomLineAction = new LineConfiguredAction();
        this.gradientAction = new GradientConfiguredAction();
        this.textAction = new TextConfiguredAction();

        ActorUtilities.growActionsArray(topLineActor, 1);
        ActorUtilities.growActionsArray(bottomLineActor, 1);
        ActorUtilities.growActionsArray(gradientActor, 1);
        ActorUtilities.growActionsArray(textActor, 1);
    }

    @Override
    public void setOriginalPosition(float x, float y)
    {
        this.x = x;
        this.y = y;
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
        // Animación de la línea de arriba
        topLineActor.getColor().a = 0f;
        topLineActor.setPosition(x + LINE_OFFSET_X + LINE_HIDDEN_OFFSET_X,
                y - LINE_OFFSET_Y - LINE_HIDDEN_OFFSET_Y);

        topLineActor.clearActions();
        topLineActor.addAction(topLineAction.show(showDelay, SHOW_HIDE_DURATION,
                -LINE_HIDDEN_OFFSET_X, LINE_HIDDEN_OFFSET_Y));

        // Animación de la línea de abajo
        bottomLineActor.getColor().a = 0f;
        bottomLineActor.setPosition(x - LINE_OFFSET_X - LINE_HIDDEN_OFFSET_X,
                y + LINE_OFFSET_Y + LINE_HIDDEN_OFFSET_Y);

        bottomLineActor.clearActions();
        bottomLineActor.addAction(bottomLineAction.show(showDelay, SHOW_HIDE_DURATION,
                LINE_HIDDEN_OFFSET_X, -LINE_HIDDEN_OFFSET_Y));

        // Animación del fondo de gradiente
        gradientActor.getColor().a = 0f;
        gradientActor.setPosition(x, y);

        gradientActor.clearActions();
        gradientActor.addAction(gradientAction.show(showDelay, SHOW_HIDE_DURATION));

        // Animación del Texto
        textActor.getColor().a = 0f;
        textActor.setPosition(x + TEXT_OFFSET_X, y + TEXT_OFFSET_Y);
        textActor.setScale(0.1f);

        textActor.clearActions();
        textActor.addAction(textAction.show(showDelay, SHOW_HIDE_DURATION));
    }

    @Override
    public void hide()
    {
        // Animación de la línea de arriba
        topLineActor.clearActions();
        topLineActor.addAction(topLineAction.hide(hideDelay, SHOW_HIDE_DURATION,
                LINE_HIDDEN_OFFSET_X, -LINE_HIDDEN_OFFSET_Y));

        // Animación de la línea de abajo
        bottomLineActor.clearActions();
        bottomLineActor.addAction(bottomLineAction.hide(hideDelay, SHOW_HIDE_DURATION,
                -LINE_HIDDEN_OFFSET_X, LINE_HIDDEN_OFFSET_Y));

        // Animación del fondo de gradiente
        gradientActor.clearActions();
        gradientActor.addAction(gradientAction.hide(hideDelay, SHOW_HIDE_DURATION));

        // Animación del Texto
        textActor.clearActions();
        textActor.addAction(textAction.hide(hideDelay, SHOW_HIDE_DURATION));
    }

    public void update(GameTime gameTime)
    {
        topLineActor.act(gameTime.delta);
        bottomLineActor.act(gameTime.delta);
        gradientActor.act(gameTime.delta);
        textActor.act(gameTime.delta);
    }

    public void drawTextures(Batch batch)
    {
        topLineActor.draw(batch, 1f);
        bottomLineActor.draw(batch, 1f);
        gradientActor.draw(batch, 1f);
    }

    public void drawText(DistanceFieldRenderer renderer, DistanceFieldFont font)
    {
        textActor.draw(renderer, font);
    }

    //region Auxiliares para Animaciones

    private class LineConfiguredAction
    {
        private SequenceAction sequence;
        private DelayAction delay;
        private ParallelAction parallel;
        private MoveByAction moveBy;
        private AlphaAction fade;

        LineConfiguredAction()
        {
            sequence = new SequenceAction();
            delay = new DelayAction();
            parallel = new ParallelAction();
            moveBy = new MoveByAction();
            fade = new AlphaAction();
        }

        Action show(float delay, float duration, float dx, float dy)
        {
            return Actions.sequence(sequence,
                    Actions.delay(this.delay, delay),
                    Actions.parallel(parallel,
                            Actions.moveBy(moveBy, dx, dy, duration, Interpolation.sineOut),
                            Actions.fadeIn(fade, duration, Interpolation.sineOut)));
        }

        Action hide(float delay, float duration, float dx, float dy)
        {
            return Actions.sequence(sequence,
                    Actions.delay(this.delay, delay),
                    Actions.parallel(parallel,
                            Actions.moveBy(moveBy, dx, dy, duration, Interpolation.sineIn),
                            Actions.fadeOut(fade, duration, Interpolation.sineIn)));
        }
    }

    private class GradientConfiguredAction
    {
        private SequenceAction sequence;
        private DelayAction delay;
        private AlphaAction fade;

        GradientConfiguredAction()
        {
            sequence = new SequenceAction();
            delay = new DelayAction();
            fade = new AlphaAction();
        }

        Action show(float delay, float duration)
        {
            return Actions.sequence(sequence,
                    Actions.delay(this.delay, delay),
                    Actions.fadeIn(fade, duration, Interpolation.sineOut));
        }

        Action hide(float delay, float duration)
        {
            return Actions.sequence(sequence,
                    Actions.delay(this.delay, delay),
                    Actions.fadeOut(fade, duration, Interpolation.sineIn));
        }
    }

    private class TextConfiguredAction
    {
        private SequenceAction sequence;
        private DelayAction delay;
        private ParallelAction parallel;
        private AlphaAction fade;
        private ScaleToAction scaleTo;

        TextConfiguredAction()
        {
            sequence = new SequenceAction();
            delay = new DelayAction();
            parallel = new ParallelAction();
            fade = new AlphaAction();
            scaleTo = new ScaleToAction();
        }

        Action show(float delay, float duration)
        {
            return Actions.sequence(sequence,
                    Actions.delay(this.delay, delay),
                    Actions.parallel(parallel,
                            Actions.fadeIn(fade, duration, Interpolation.sineOut),
                            Actions.scaleTo(scaleTo, 1f, 1f, duration, Interpolation.swingOut)));
        }

        Action hide(float delay, float duration)
        {
            return Actions.sequence(sequence,
                    Actions.delay(this.delay, delay),
                    Actions.parallel(parallel,
                            Actions.fadeOut(fade, duration, Interpolation.sineIn),
                            Actions.scaleTo(scaleTo, 0.1f, 0.1f, duration, Interpolation.swingIn)));
        }
    }

    //endregion
}