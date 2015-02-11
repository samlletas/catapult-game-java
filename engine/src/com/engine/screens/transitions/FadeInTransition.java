package com.engine.screens.transitions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.engine.graphics.GraphicsSettings;
import com.engine.GameTime;
import com.engine.screens.Overlay;
import com.engine.actors.Actions;

public class FadeInTransition extends AlphaTransition
{
    private SpriteBatch spriteBatch;
    private TextureRegion pixelRegion;
    private float duration;

    private SequenceAction sequence;
    private AlphaAction fadeOut;
    private RunnableAction finishAction;

    private FinishTransitionRunnable finishTransitionRunnable;

    public FadeInTransition(GraphicsSettings settings, SpriteBatch spriteBatch,
                            TextureRegion pixelRegion, float duration, Color color)
    {
        super(settings, new Overlay(settings, color, 0f));

        this.spriteBatch = spriteBatch;
        this.pixelRegion = pixelRegion;
        this.duration = duration;

        this.sequence = new SequenceAction();
        this.fadeOut = new AlphaAction();
        this.finishAction = new RunnableAction();

        this.finishTransitionRunnable = new FinishTransitionRunnable();
    }

    @Override
    protected void onStart()
    {
        overlay.getColor().a = 1f;

        overlay.clearActions();
        overlay.addAction(
                Actions.sequence(sequence,
                        Actions.fadeOut(fadeOut, duration),
                        Actions.run(finishAction, finishTransitionRunnable)));
    }

    @Override
    protected void onFinish()
    {

    }

    @Override
    public boolean canRenderNextScreen()
    {
        return true;
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

    class FinishTransitionRunnable implements Runnable
    {
        @Override
        public void run()
        {
            FadeInTransition.this.finish();
        }
    }
}
