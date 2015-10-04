package com.sammacedo.smashingcrystals.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.GameTime;
import com.engine.actors.Actions;
import com.engine.graphics.GraphicsSettings;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.input.BackInputProcessor;
import com.engine.screens.Overlay;
import com.engine.screens.ScreenManager;
import com.engine.utilities.ActorUtilities;
import com.engine.utilities.StageUtilities;
import com.sammacedo.smashingcrystals.Common;
import com.sammacedo.smashingcrystals.Global;
import com.sammacedo.smashingcrystals.screens.menus.SettingsScreen;
import com.sammacedo.smashingcrystals.screens.ui.GameButton;
import com.sammacedo.smashingcrystals.screens.ui.Header;

public class PauseOverlay
{
    private static final String HEADER_TEXT = "PAUSED";
    private static final float SHOW_HIDE_DURATION = 0.500f;

    private BaseGameplayScreen gameplayScreen;
    private Overlay overlay;
    private Header header;

    private GameButton homeButton;
    private GameButton replayButton;
    private GameButton continueButton;
    private GameButton settingsButton;
    private GameButton howToPlayButton;

    private Stage stage;

    private InputMultiplexer inputMultiplexer;
    private Runnable enableInputRunnable;

    private SequenceAction sequence;
    private AlphaAction alpha;
    private DelayAction delay;
    private RunnableAction runnableAction;
    private Runnable unpausedRunnable;

    public PauseOverlay(Common common, GraphicsSettings graphicsSettings,
                        Viewport viewport2D, Batch batch)
    {
        overlay = new Overlay(graphicsSettings, Global.Colors.OVERLAY, 0f);
        header = new Header(common, graphicsSettings, HEADER_TEXT);
        homeButton = new GameButton(common, Global.ButtonStyles.HOME_SMALL);
        replayButton = new GameButton(common, Global.ButtonStyles.REPLAY);
        continueButton = new GameButton(common, Global.ButtonStyles.PLAY);
        settingsButton = new GameButton(common, Global.ButtonStyles.SETTINGS);
        howToPlayButton = new GameButton(common, Global.ButtonStyles.QUESTION);

        stage = new Stage(viewport2D, batch);
        ActorUtilities.growActionsArray(overlay, 2);

        homeButton.setOriginalPosition((graphicsSettings.virtualWidth / 2f) - 300f, 200f);
        replayButton.setOriginalPosition((graphicsSettings.virtualWidth / 2f) - 170f, 260f);
        continueButton.setOriginalPosition(graphicsSettings.virtualWidth / 2f, 300f);
        settingsButton.setOriginalPosition((graphicsSettings.virtualWidth / 2f) + 170f, 260f);
        howToPlayButton.setOriginalPosition((graphicsSettings.virtualWidth / 2f) + 300f, 200f);

        header.setShowDelay(0f);

        homeButton.setShowDelay(0.150f);
        replayButton.setShowDelay(0.075f);
        continueButton.setShowDelay(0f);
        settingsButton.setShowDelay(0.075f);
        howToPlayButton.setShowDelay(0.150f);

        header.setHideDelay(0f);
        homeButton.setHideDelay(0.150f);
        replayButton.setHideDelay(0.075f);
        continueButton.setHideDelay(0f);
        settingsButton.setHideDelay(0.075f);
        howToPlayButton.setHideDelay(0.150f);

        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                disableInput();
                hideUiActors();

                gameplayScreen.getScreenManager().transitionTo(Global.ScreenNames.MAIN_MENU);
            }
        });

        replayButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                disableInput();
                hideUiActors();

                gameplayScreen.getScreenManager().transitionTo(gameplayScreen);
            }
        });

        continueButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                hide();
            }
        });

        settingsButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                disableInput();
                hideUiActors();

                ScreenManager screenManager = gameplayScreen.getScreenManager();
                SettingsScreen screen = (SettingsScreen)screenManager.getScreen(Global.ScreenNames.SETTINGS);
                screen.setBackScreen(gameplayScreen);
                screenManager.transitionTo(screen);
            }
        });

        howToPlayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);
            }
        });

        unpausedRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                gameplayScreen.resumeGame();
            }
        };

        stage.addActor(homeButton);
        stage.addActor(replayButton);
        stage.addActor(continueButton);
        stage.addActor(settingsButton);
        stage.addActor(howToPlayButton);

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

        sequence = new SequenceAction();
        alpha = new AlphaAction();
        delay = new DelayAction();
        runnableAction = new RunnableAction();

        enableInputRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                enableInput();
            }
        };
    }

    public void setGameplayScreen(BaseGameplayScreen gameplayScreen)
    {
        this.gameplayScreen = gameplayScreen;
    }

    public void reset()
    {
        overlay.clearActions();
        overlay.getColor().a = 0f;
    }

    public void enableInput()
    {
        Gdx.input.setInputProcessor(PauseOverlay.this.inputMultiplexer);
        StageUtilities.enableTouch(PauseOverlay.this.stage);
    }

    public void disableInput()
    {
        Gdx.input.setInputProcessor(null);
    }

    public void show()
    {
        overlay.getColor().a = 0f;
        overlay.clearActions();
        overlay.addAction(
                Actions.sequence(sequence,
                        Actions.alpha(alpha, Global.OVERLAY_PAUSE_ALPHA, SHOW_HIDE_DURATION),
                        Actions.run(runnableAction, enableInputRunnable)));

        showUiActors();
    }

    private void showUiActors()
    {
        header.show();
        homeButton.show();
        replayButton.show();
        continueButton.show();
        settingsButton.show();
        howToPlayButton.show();

        stage.unfocusAll();
    }

    public void resumePause()
    {
        overlay.clearActions();
        overlay.addAction(Actions.delay(delay, SHOW_HIDE_DURATION, Actions.run(runnableAction, enableInputRunnable)));
        
        showUiActors();
    }

    public void hide()
    {
        disableInput();

        overlay.clearActions();
        overlay.addAction(
                Actions.sequence(sequence,
                        Actions.fadeOut(alpha, SHOW_HIDE_DURATION),
                        Actions.run(runnableAction, unpausedRunnable)));

        hideUiActors();
    }

    private void hideUiActors()
    {
        header.hide();
        homeButton.hide();
        replayButton.hide();
        continueButton.hide();
        settingsButton.hide();
        howToPlayButton.hide();
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

    public void dispose()
    {
        stage.dispose();
    }
}
