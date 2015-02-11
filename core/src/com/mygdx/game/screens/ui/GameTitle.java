package com.mygdx.game.screens.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.engine.GameTime;
import com.engine.actors.ActorOrigin;
import com.engine.actors.DistanceFieldFontActor;
import com.engine.actors.TextureRegionActor;
import com.engine.graphics.GraphicsSettings;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.utilities.ActorUtilities;
import com.mygdx.game.Common;

public class GameTitle implements ICustomWidget
{
    private static final String GAME_TITLE = "CRYSTAL SMASH";
    private static final float SHOW_HIDE_DURATION = 0.500f;

    private static final float GLOW_SCALE_X = 2.3f;
    private static final float GLOW_SCALE_Y = 2.4f;
    public static final float FONT_SCALE = 1.6f;

    private static final float GLOW_OFFSET_X = 5f;
    private static final float GLOW_OFFSET_Y = 5f;

    private float x;
    private float y;

    private TextureRegionActor glowActor;
    private DistanceFieldFontActor textActor;
    private DistanceFieldFontActor textAnimationActor;

    private GlowStartLoopRunnable glowStartLoopRunnable;
    private TextAnimationLoopRunnable textAnimationLoopRunnable;

    private float showDelay;
    private float hideDelay;

    public GameTitle(Common common, GraphicsSettings graphicsSettings)
    {
        this.x = graphicsSettings.virtualWidth / 2f;
        this.y = 105f;

        this.glowActor = new TextureRegionActor(common.assets.atlasRegions.titleGLow.getInstance());
        this.textActor = new DistanceFieldFontActor(GAME_TITLE);
        this.textAnimationActor = new DistanceFieldFontActor(GAME_TITLE);

        this.glowActor.setActorOrigin(ActorOrigin.Center);
        this.textActor.setActorOrigin(ActorOrigin.Center);
        this.textAnimationActor.setActorOrigin(ActorOrigin.Center);

        this.glowActor.setPosition(x + GLOW_OFFSET_X, y + GLOW_OFFSET_Y);
        this.textActor.setPosition(x, y);
        this.textAnimationActor.setPosition(x, y);

        this.glowActor.setScale(GLOW_SCALE_X, GLOW_SCALE_Y);
        this.textActor.setFontBaseScale(FONT_SCALE);
        this.textAnimationActor.setFontBaseScale(FONT_SCALE);

        this.glowStartLoopRunnable = new GlowStartLoopRunnable();
        this.textAnimationLoopRunnable = new TextAnimationLoopRunnable();

        ActorUtilities.growActionsArray(glowActor, 2);
        ActorUtilities.growActionsArray(textActor, 1);
        ActorUtilities.growActionsArray(textAnimationActor, 2);
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
        glowActor.getColor().a = 0f;
        glowActor.setScale(GLOW_SCALE_X * 0.3f, GLOW_SCALE_Y * 0.3f);
        glowActor.addAction(getShowAction(showDelay, SHOW_HIDE_DURATION, GLOW_SCALE_X, GLOW_SCALE_Y));
        glowActor.addAction(Actions.after(Actions.run(glowStartLoopRunnable)));

        textActor.getColor().a = 0f;
        textAnimationActor.setScale(0.3f);
        textActor.addAction(getShowAction(showDelay, SHOW_HIDE_DURATION, 1f, 1f));

        textAnimationActor.getColor().a = 0f;
        textActor.setScale(0.3f);
        textAnimationActor.addAction(getShowAction(showDelay, SHOW_HIDE_DURATION, 1f, 1f));
        textAnimationActor.addAction(Actions.after(Actions.run(textAnimationLoopRunnable)));
    }

    @Override
    public void hide()
    {
        clearActions();

        glowActor.addAction(getHideAction(hideDelay, SHOW_HIDE_DURATION, GLOW_SCALE_X * 0.1f, GLOW_SCALE_Y * 0.1f));
        textActor.addAction(getHideAction(hideDelay, SHOW_HIDE_DURATION, 0.1f, 0.1f));
        textAnimationActor.addAction(getHideAction(hideDelay, SHOW_HIDE_DURATION, 0.1f, 0.1f));
    }

    private Action getShowAction(float delay, float duration, float scaleX, float scaleY)
    {
        return Actions.sequence(
                    Actions.delay(delay),
                    Actions.parallel(
                            Actions.fadeIn(duration, Interpolation.sineOut),
                            Actions.scaleTo(scaleX, scaleY, duration, Interpolation.swingOut)));
    }

    private Action getHideAction(float delay, float duration, float scaleX, float scaleY)
    {
        return Actions.sequence(
                Actions.delay(delay),
                Actions.parallel(
                        Actions.fadeOut(duration, Interpolation.sineIn),
                        Actions.scaleTo(scaleX, scaleY, duration, Interpolation.swingIn)));
    }

    public void update(GameTime gameTime)
    {
        glowActor.act(gameTime.delta);
        textActor.act(gameTime.delta);
        textAnimationActor.act(gameTime.delta);
    }

    public void drawTextures(SpriteBatch spriteBatch)
    {
        glowActor.draw(spriteBatch, 1f);
    }

    public void drawText(DistanceFieldRenderer renderer, DistanceFieldFont font)
    {
        textActor.draw(renderer, font);
        textAnimationActor.draw(renderer, font);
    }

    public void clearActions()
    {
        glowActor.clearActions();
        textActor.clearActions();
        textAnimationActor.clearActions();
    }

    private class GlowStartLoopRunnable implements Runnable
    {
        @Override
        public void run()
        {
            GameTitle.this.glowActor.clearActions();
            GameTitle.this.glowActor.addAction(
                    Actions.repeat(RepeatAction.FOREVER,
                            Actions.sequence(
                                    Actions.scaleTo(GameTitle.GLOW_SCALE_X + 0.05f, GameTitle.GLOW_SCALE_Y + 0.2f, 0.4f, Interpolation.sine),
                                    Actions.scaleTo(GameTitle.GLOW_SCALE_X, GameTitle.GLOW_SCALE_Y, 0.4f, Interpolation.sine))));
        }
    }

    private class TextAnimationLoopRunnable implements Runnable
    {
        @Override
        public void run()
        {
            GameTitle.this.textAnimationActor.clearActions();
            GameTitle.this.textAnimationActor.addAction(
                    Actions.repeat(RepeatAction.FOREVER,
                            Actions.sequence(
                                    Actions.parallel(
                                            Actions.alpha(0.4f, 0f),
                                            Actions.fadeOut(0.5f, Interpolation.linear),
                                            Actions.scaleTo(1f, 1f, 0f),
                                            Actions.scaleTo(1.15f, 1f, 0.5f, Interpolation.sine)),
                                    Actions.delay(1.5f))));
        }
    }
}