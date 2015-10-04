package com.sammacedo.smashingcrystals.screens.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.GameTime;
import com.engine.graphics.GraphicsSettings;
import com.engine.input.BackInputProcessor;
import com.engine.screens.Screen;
import com.engine.utilities.ColorUtilities;
import com.engine.utilities.StageUtilities;
import com.sammacedo.smashingcrystals.Common;
import com.sammacedo.smashingcrystals.Global;
import com.sammacedo.smashingcrystals.gamelogic.scene.Background;
import com.sammacedo.smashingcrystals.screens.OverlayedScreen;
import com.sammacedo.smashingcrystals.screens.ui.GameButton;
import com.sammacedo.smashingcrystals.screens.ui.GameTitle;

public final class MainMenuScreen extends OverlayedScreen
{
    private final Common common;

    private Stage stage;
    private GameTitle gameTitle;
    private Array<GameButton> buttons;

    private InputMultiplexer inputMultiplexer;

    public MainMenuScreen(GraphicsSettings graphicsSettings, Viewport viewport2D,
                          Viewport viewport3D, Batch batch, Common common)
    {
        super(Global.ScreenNames.MAIN_MENU, graphicsSettings, viewport2D,
                viewport3D, batch);

        this.common = common;
    }

    @Override
    public void initialize()
    {
        stage = new Stage(viewport2D, batch);
        gameTitle = new GameTitle(common, graphicsSettings);

        GameButton playButton = new GameButton(common, Global.ButtonStyles.PLAY);
        GameButton settingsButton = new GameButton(common, Global.ButtonStyles.SETTINGS);
        GameButton scoresButton = new GameButton(common, Global.ButtonStyles.SCORES_MEDIUM);
        GameButton powerButton = new GameButton(common, Global.ButtonStyles.POWER);
        GameButton infoButton = new GameButton(common, Global.ButtonStyles.INFO);

        playButton.setOriginalPosition(graphicsSettings.virtualWidth / 2f, 295f);
        settingsButton.setOriginalPosition((graphicsSettings.virtualWidth / 2f) + 200f, 275f);
        scoresButton.setOriginalPosition((graphicsSettings.virtualWidth / 2f) - 200f, 275f);
        powerButton.setOriginalPosition(787f, 217f);
        infoButton.setOriginalPosition(67f, 217f);

        gameTitle.setShowDelay(0.200f);
        playButton.setShowDelay(0f);
        settingsButton.setShowDelay(0.100f);
        scoresButton.setShowDelay(0.100f);
        powerButton.setShowDelay(0.200f);
        infoButton.setShowDelay(0.200f);

        gameTitle.setHideDelay(0f);
        playButton.setHideDelay(0f);
        settingsButton.setHideDelay(0.100f);
        scoresButton.setHideDelay(0.100f);
        powerButton.setHideDelay(0.200f);
        infoButton.setHideDelay(0.200f);

        infoButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                screenManager.transitionTo(Global.ScreenNames.INFO);
            }
        });

        scoresButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                screenManager.transitionTo(Global.ScreenNames.GAME_OVER);
            }
        });

        playButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                screenManager.transitionTo(Global.ScreenNames.MODE_SELECT);
            }
        });

        settingsButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                SettingsScreen screen = (SettingsScreen)screenManager.getScreen(Global.ScreenNames.SETTINGS);
                screen.setBackScreen(MainMenuScreen.this);
                screenManager.transitionTo(screen);
            }
        });

        powerButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                exitGame();
            }
        });

        buttons = new Array<GameButton>(5);
        buttons.add(playButton);
        buttons.add(settingsButton);
        buttons.add(scoresButton);
        buttons.add(powerButton);
        buttons.add(infoButton);

        Array<GameButton> localButtons = buttons;

        for (int i = 0, n = localButtons.size; i < n; i++)
        {
            stage.addActor(localButtons.get(i));
        }

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new BackInputProcessor()
        {
            @Override
            protected void onBackDown()
            {
                exitGame();
            }
        });
        inputMultiplexer.addProcessor(stage);
    }

    private void exitGame()
    {
        StageUtilities.disableTouch(stage);
        Gdx.app.exit();
    }

    @Override
    public void onEnter(Screen from)
    {
        gameTitle.show();

        Array<GameButton> localButtons = buttons;

        for (int i = 0, n = localButtons.size; i < n; i++)
        {
            localButtons.get(i).show();
        }

        stage.unfocusAll();

        common.assets.distanceFieldFonts.furore.getInstance().setColor(Global.Colors.DEFAULT_TEXT);
        common.background.setMode(Background.Mode.Main);

        common.assets.musics.menu.getInstance().setLooping(true);
        common.assets.musics.menu.getInstance().play();
    }

    @Override
    public void onTransitionFromFinish(Screen from)
    {
        // Habilitación de input al terminar la transición
        Gdx.input.setInputProcessor(inputMultiplexer);
        StageUtilities.enableTouch(stage);
    }

    @Override
    public void onTransitionToStart(Screen to)
    {
        // Deshabilitación del input
        Gdx.input.setInputProcessor(null);

        gameTitle.hide();

        Array<GameButton> localButtons = buttons;

        for (int i = 0, n = localButtons.size; i < n; i++)
        {
            localButtons.get(i).hide();
        }
    }

    @Override
    public void onLeave(Screen to)
    {
        Array<GameButton> localButtons = buttons;

        for (int i = 0, n = localButtons.size; i < n; i++)
        {
            localButtons.get(i).clearActions();
        }

        common.assets.musics.menu.getInstance().stop();
    }

    @Override
    public void update(GameTime gameTime)
    {
        common.background.update(gameTime);
        common.grass.update(gameTime);
        stage.act(gameTime.delta);
        gameTitle.update(gameTime);

        clearColor.set(getBackgroundForeColor());
    }

    @Override
    public void draw(GameTime gameTime)
    {
        // Reinicio del color ya que al parecer hay un bug en libgdx en el
        // que no se recupera el alpha original durante la ejecución de un
        // AlphaAction en un Actor
        ColorUtilities.resetColor(batch);

        common.shaders.defaultShader.setForegroundColor(getTransitionForeColor());
        batch.begin();
        common.background.draw(batch);
        common.grass.draw(batch);
        gameTitle.drawTextures(batch);
        batch.end();

        stage.draw();
    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }
}