package com.mygdx.game.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.GameTime;
import com.engine.graphics.GraphicsSettings;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.input.BackInputProcessor;
import com.engine.screens.Overlay;
import com.engine.utilities.StageUtilities;
import com.mygdx.game.Common;
import com.mygdx.game.Global;
import com.mygdx.game.screens.ui.GameButton;
import com.mygdx.game.screens.ui.Header;

public class PauseOverlay
{
    private static final String HEADER_TEXT = "PAUSED";

    private GameplayScreen gameplayScreen;
    private Overlay overlay;
    private Header header;
    private GameButton howToPlayButton;
    private GameButton continueButton;
    private GameButton homeButton;
    private Stage stage;

    private InputMultiplexer inputMultiplexer;
    private Runnable enableInputRunnable;

    private Runnable unpausedRunnable;

    public PauseOverlay(GameplayScreen gameplayScreen, Common common,
                        GraphicsSettings graphicsSettings, Viewport viewport2D,
                        Batch batch)
    {
        this.gameplayScreen = gameplayScreen;

        overlay = new Overlay(graphicsSettings, Global.Colors.OVERLAY, 0f);
        header = new Header(common, graphicsSettings, HEADER_TEXT);
        howToPlayButton = new GameButton(common, GameButton.ButtonTypes.MEDIUM, Global.ButtonStyles.QUESTION);
        continueButton = new GameButton(common, GameButton.ButtonTypes.BIG, Global.ButtonStyles.PLAY);
        homeButton = new GameButton(common, GameButton.ButtonTypes.MEDIUM, Global.ButtonStyles.HOME);
        stage = new Stage(viewport2D, batch);

        howToPlayButton.setOriginalPosition((graphicsSettings.virtualWidth / 2f) - 200f, 265f);
        continueButton.setOriginalPosition(graphicsSettings.virtualWidth / 2f, 285f);
        homeButton.setOriginalPosition((graphicsSettings.virtualWidth / 2f) + 200f, 265f);

        howToPlayButton.setGlowAngle(90f);
        continueButton.setGlowAngle(0f);
        homeButton.setGlowAngle(225f);

        header.setShowDelay(0f);
        howToPlayButton.setShowDelay(0.100f);
        continueButton.setShowDelay(0f);
        homeButton.setShowDelay(0.100f);

        header.setHideDelay(0f);
        howToPlayButton.setHideDelay(0.100f);
        continueButton.setHideDelay(0f);
        homeButton.setHideDelay(0.100f);

        howToPlayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);
            }
        });

        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                hide();
            }
        });

        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                PauseOverlay.this.gameplayScreen.transitionTo(Global.ScreenNames.MAIN_MENU_SCREEN);
            }
        });

        stage.addActor(howToPlayButton);
        stage.addActor(continueButton);
        stage.addActor(homeButton);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new BackInputProcessor()
        {
            @Override
            protected void onBackDown()
            {
                StageUtilities.disableTouch(stage);
                hide();
            }
        });
        inputMultiplexer.addProcessor(stage);

        enableInputRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                Gdx.input.setInputProcessor(PauseOverlay.this.inputMultiplexer);
                StageUtilities.enableTouch(PauseOverlay.this.stage);
            }
        };
    }

    public void setUnpausedRunnable(Runnable unpausedRunnable)
    {
        this.unpausedRunnable = unpausedRunnable;
    }

    public void reset()
    {
        overlay.clearActions();
        overlay.getColor().a = 0f;
    }

    public void show()
    {
        overlay.getColor().a = 0f;
        overlay.addAction(
                Actions.sequence(
                        Actions.alpha(Global.OVERLAY_PAUSE_ALPHA, 0.500f),
                        Actions.run(enableInputRunnable)));

        header.show();
        howToPlayButton.show();
        continueButton.show();
        homeButton.show();

        stage.unfocusAll();
    }

    public void hide()
    {
        // Deshabilitación del input
        Gdx.input.setInputProcessor(null);

        overlay.clearActions();
        overlay.addAction(
                Actions.sequence(
                        Actions.fadeOut(0.500f),
                        Actions.run(unpausedRunnable)));

        header.hide();
        howToPlayButton.hide();
        continueButton.hide();
        homeButton.hide();
    }

    public Overlay getOverlay()
    {
        return overlay;
    }

    public void update(GameTime gameTime)
    {
        overlay.act(gameTime.delta);
        header.update(gameTime);
        stage.act(gameTime.delta);
    }

    public void drawTextures(Batch batch)
    {
        header.drawTextures(batch);
    }

    public void drawStage()
    {
        stage.draw();
    }

    public void drawText(DistanceFieldRenderer renderer, DistanceFieldFont font)
    {
        header.drawText(renderer, font);
    }
}
