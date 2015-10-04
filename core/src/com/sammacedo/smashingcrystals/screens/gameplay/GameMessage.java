package com.sammacedo.smashingcrystals.screens.gameplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.engine.GameTime;
import com.engine.actors.*;
import com.engine.actors.Actions;
import com.engine.graphics.GraphicsSettings;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.utilities.ActorUtilities;
import com.sammacedo.smashingcrystals.Common;

public class GameMessage
{
    public static final float FONT_SCALE = 1f;

    private static final float BAR_HEIGHT = 55f;
    private static final float BAR_ALPHA = 0.55f;

    private static final float LINE_OFFSET_X = 80f;
    private static final float LINE_OFFSET_Y = 28f;
    private static final float LINE_HIDDEN_OFFSET_X = 0f;
    private static final float LINE_HIDDEN_OFFSET_Y = 27.5f;

    private static final float TEXT_OFFSET_X = 0f;
    private static final float TEXT_OFFSET_Y = -4f;

    private float x;
    private float y;

    private TextureRegionActor topLineActor;
    private TextureRegionActor bottomLineActor;
    private TextureRegionActor barActor;
    private DistanceFieldFontActor textActor;

    private LineConfiguredAction topLineAction;
    private LineConfiguredAction bottomLineAction;
    private BarConfiguredAction gradientAction;
    private TextConfiguredAction textAction;

    public GameMessage(Common common, GraphicsSettings graphicsSettings)
    {
        this.x = graphicsSettings.virtualWidth / 2f;
        this.y = 225f;

        TextureRegion pixel = common.assets.atlasRegions.pixel.getInstance();
        TextureRegion line = common.assets.atlasRegions.uiMessageLine.getInstance();

        this.topLineActor = new TextureRegionActor(line);
        this.bottomLineActor = new TextureRegionActor(line);
        this.barActor = new TextureRegionActor(pixel);
        this.textActor = new DistanceFieldFontActor();

        this.topLineActor.setActorOrigin(ActorOrigin.Center);
        this.bottomLineActor.setActorOrigin(ActorOrigin.Center);
        this.barActor.setActorOrigin(ActorOrigin.Center);
        this.textActor.setActorOrigin(ActorOrigin.Center);

        this.barActor.setScaleX(graphicsSettings.virtualWidth);
        this.barActor.setColor(Color.BLACK);
        this.barActor.getColor().a = BAR_ALPHA;
        this.textActor.setFontBaseScale(FONT_SCALE);

        this.topLineAction = new LineConfiguredAction();
        this.bottomLineAction = new LineConfiguredAction();
        this.gradientAction = new BarConfiguredAction();
        this.textAction = new TextConfiguredAction();

        ActorUtilities.growActionsArray(topLineActor, 1);
        ActorUtilities.growActionsArray(bottomLineActor, 1);
        ActorUtilities.growActionsArray(barActor, 1);
        ActorUtilities.growActionsArray(textActor, 1);
    }

    public void setText(CharSequence sequence)
    {
        textActor.setText(sequence);
    }

    public CharSequence getText()
    {
        return textActor.getText();
    }

    public void show(Runnable onFinishRunnable)
    {
        // Animación de la línea de arriba
        topLineActor.getColor().a = 0f;
        topLineActor.setPosition(x + LINE_OFFSET_X + LINE_HIDDEN_OFFSET_X,
                y - LINE_OFFSET_Y + LINE_HIDDEN_OFFSET_Y);

        topLineActor.clearActions();
        topLineActor.addAction(topLineAction.show(-LINE_HIDDEN_OFFSET_X, -LINE_HIDDEN_OFFSET_Y));

        // Animación de la línea de abajo
        bottomLineActor.getColor().a = 0f;
        bottomLineActor.setPosition(x - LINE_OFFSET_X - LINE_HIDDEN_OFFSET_X,
                y + LINE_OFFSET_Y - LINE_HIDDEN_OFFSET_Y);

        bottomLineActor.clearActions();
        bottomLineActor.addAction(bottomLineAction.show(LINE_HIDDEN_OFFSET_X, LINE_HIDDEN_OFFSET_Y));

        // Animación del fondo
        barActor.getColor().a = 0f;
        barActor.setPosition(x, y);
        barActor.setScaleY(0f);

        barActor.clearActions();
        barActor.addAction(gradientAction.show());

        // Animación del Texto
        textActor.getColor().a = 0f;
        textActor.setPosition(x + TEXT_OFFSET_X - 200, y + TEXT_OFFSET_Y);

        textActor.clearActions();
        textActor.addAction(textAction.show(onFinishRunnable));
    }

    public void hide()
    {
        // Animación de la línea de arriba
        topLineActor.clearActions();
        topLineActor.addAction(topLineAction.hide(LINE_HIDDEN_OFFSET_X, LINE_HIDDEN_OFFSET_Y));

        // Animación de la línea de abajo
        bottomLineActor.clearActions();
        bottomLineActor.addAction(bottomLineAction.hide(-LINE_HIDDEN_OFFSET_X, -LINE_HIDDEN_OFFSET_Y));

        // Animación del fondo
        barActor.clearActions();
        barActor.addAction(gradientAction.hide());

        // Animación del Texto
        textActor.clearActions();
        textActor.addAction(textAction.hide());
    }

    public void update(GameTime gameTime)
    {
        topLineActor.act(gameTime.delta);
        bottomLineActor.act(gameTime.delta);
        barActor.act(gameTime.delta);
        textActor.act(gameTime.delta);
    }

    public void drawTextures(Batch batch)
    {
        barActor.draw(batch, 1f);
        topLineActor.draw(batch, 1f);
        bottomLineActor.draw(batch, 1f);
    }

    public void drawText(DistanceFieldRenderer renderer, DistanceFieldFont font)
    {
        textActor.draw(renderer, font);
    }

    //region Auxiliares para Animaciones

    private class LineConfiguredAction
    {
        private ParallelAction parallel = new ParallelAction();
        private AlphaAction fade = new AlphaAction();
        private MoveByAction moveBy = new MoveByAction();

        Action show(float dx, float dy)
        {
            return Actions.parallel(parallel,
                    Actions.fadeIn(fade, 0.5f, Interpolation.pow4),
                    Actions.moveBy(moveBy, dx, dy, 0.5f, Interpolation.pow4));
        }

        Action hide(float dx, float dy)
        {
            return Actions.parallel(parallel,
                    Actions.fadeOut(fade, 0.5f, Interpolation.pow2Out),
                    Actions.moveBy(moveBy, dx, dy, 0.5f, Interpolation.pow4));
        }
    }

    private class BarConfiguredAction
    {
        private ParallelAction parallel = new ParallelAction();
        private AlphaAction fade = new AlphaAction();
        private ScaleByAction scaleBy = new ScaleByAction();

        Action show()
        {
            return Actions.parallel(parallel,
                    Actions.alpha(fade, BAR_ALPHA, 0.5f, Interpolation.pow4),
                    Actions.scaleBy(scaleBy, 0f, BAR_HEIGHT, 0.5f, Interpolation.pow4));
        }

        Action hide()
        {
            return Actions.parallel(parallel,
                    Actions.fadeOut(fade, 0.5f, Interpolation.pow2Out),
                    Actions.scaleBy(scaleBy, 0f, -BAR_HEIGHT, 0.5f, Interpolation.pow4));
        }
    }

    private class TextConfiguredAction
    {
        private SequenceAction sequence = new SequenceAction();
        private DelayAction delay = new DelayAction();
        private ParallelAction parallel = new ParallelAction();
        private AlphaAction fade = new AlphaAction();
        private MoveByAction moveByShowHide = new MoveByAction();
        private MoveByAction moveByDelay = new MoveByAction();
        private RunnableAction runnable = new RunnableAction();

        Action show(Runnable onFinishRunnable)
        {
            return Actions.sequence(sequence,
                    Actions.delay(delay, 0.1f),
                    Actions.parallel(parallel,
                            Actions.fadeIn(fade, 0.5f, Interpolation.pow2),
                            Actions.moveBy(moveByShowHide, 195f, 0f, 0.5f, Interpolation.pow4Out)),
                    Actions.moveBy(moveByDelay, 10f, 0f, 1.25f, Interpolation.linear),
                    Actions.run(runnable, onFinishRunnable));
        }

        Action hide()
        {
            return Actions.parallel(parallel,
                    Actions.fadeOut(fade, 0.5f, Interpolation.pow2Out),
                    Actions.moveBy(moveByShowHide, 195f, 0f, 0.5f, Interpolation.pow4));
        }
    }

    //endregion
}
