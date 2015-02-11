package com.engine.screens.transitions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.engine.actors.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.engine.graphics.GraphicsSettings;
import com.engine.GameTime;
import com.engine.screens.Overlay;

public class FadeTransition extends AlphaTransition
{
    private SpriteBatch spriteBatch;
    private TextureRegion pixelRegion;

    private float duration;
    private boolean canRenderNextScreen;

    private SequenceAction sequence;
    private AlphaAction fadeIn;
    private RunnableAction renderNextAction;
    private AlphaAction fadeOut;
    private RunnableAction finishAction;

    private RenderNextRunnable renderNextRunnable;
    private FinishRunnable finishRunnable;

    public FadeTransition(GraphicsSettings settings, SpriteBatch spriteBatch,
                          TextureRegion pixelRegion, float duration, Color color)
    {
        super(settings, new Overlay(settings, color, 0f));

        this.spriteBatch = spriteBatch;
        this.pixelRegion = pixelRegion;

        this.duration = duration;
        this.canRenderNextScreen = false;

        this.sequence = new SequenceAction();
        this.fadeIn = new AlphaAction();
        this.renderNextAction = new RunnableAction();
        this.fadeOut = new AlphaAction();
        this.finishAction = new RunnableAction();

        this.renderNextRunnable = new RenderNextRunnable();
        this.finishRunnable = new FinishRunnable();
    }

    @Override
    protected void onStart()
    {
        overlay.clearActions();
        overlay.addAction(
                Actions.sequence(sequence,
                        Actions.fadeIn(fadeIn, duration / 2f),
                        Actions.run(renderNextAction, renderNextRunnable),
                        Actions.fadeOut(fadeOut, duration / 2f),
                        Actions.run(finishAction, finishRunnable)));

        canRenderNextScreen = false;
    }

    @Override
    protected void onFinish()
    {

    }

    @Override
    public boolean canRenderNextScreen()
    {
        return canRenderNextScreen;
    }

    @Override
    protected void onUpdate(GameTime gameTime)
    {
        overlay.act(gameTime.delta);
    }

    @Override
    protected void onDraw(GameTime gameTime)
    {
        spriteBatch.begin();
        overlay.draw(spriteBatch, pixelRegion);
        spriteBatch.end();
    }

    @Override
    protected void onDispose()
    {

    }

    class RenderNextRunnable implements Runnable
    {
        @Override
        public void run()
        {
            FadeTransition.this.canRenderNextScreen = true;
        }
    }

    class FinishRunnable implements Runnable
    {
        @Override
        public void run()
        {
            FadeTransition.this.finish();
        }
    }
}
