package com.mygdx.game.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
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
    private RunnableAction runnableAction;
    private Runnable unpausedRunnable;

    public PauseOverlay(GameplayScreen gameplayScreen, Common common,
                        GraphicsSettings graphicsSettings, Viewport viewport2D,
                        Batch batch)
    {
        this.gameplayScreen = gameplayScreen;

        overlay = new Overlay(graphicsSettings, Global.Colors.OVERLAY, 0f);
        header = new Header(common, graphicsSettings, HEADER_TEXT);
        homeButton = new GameButton(common, Global.ButtonStyles.HOME_SMALL);
        replayButton = new GameButton(common, Global.ButtonStyles.REPLAY);
        continueButton = new GameButton(common, Global.ButtonStyles.PLAY);
        settingsButton = new GameButton(common, Global.ButtonStyles.SETTINGS);
        howToPlayButton = new GameButton(common, Global.ButtonStyles.QUESTION);

        stage = new Stage(viewport2D, batch);

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
                PauseOverlay.this.gameplayScreen.transitionTo(Global.ScreenNames.MAIN_MENU_SCREEN);
            }
        });

        replayButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);
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
                super.clicked(event, x, y);
            }
        });

        howToPlayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);
            }
        });

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
        runnableAction = new RunnableAction();
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
                Actions.sequence(sequence,
                        Actions.alpha(alpha, Global.OVERLAY_PAUSE_ALPHA, 0.500f),
                        Actions.run(runnableAction, enableInputRunnable)));

        header.show();
        homeButton.show();
        replayButton.show();
        continueButton.show();
        settingsButton.show();
        howToPlayButton.show();

        stage.unfocusAll();
    }

    public void hide()
    {
        // Deshabilitación del input
        Gdx.input.setInputProcessor(null);

        overlay.clearActions();
        overlay.addAction(
                Actions.sequence(sequence,
                        Actions.fadeOut(alpha, 0.500f),
                        Actions.run(runnableAction, unpausedRunnable)));

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
