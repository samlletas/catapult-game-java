package com.mygdx.game.screens.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.GameTime;
import com.engine.graphics.GraphicsSettings;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.input.BackInputProcessor;
import com.engine.screens.Overlay;
import com.engine.screens.Screen;
import com.engine.utilities.ColorUtilities;
import com.engine.utilities.StageUtilities;
import com.mygdx.game.Common;
import com.mygdx.game.Global;
import com.mygdx.game.screens.OverlayedScreen;
import com.mygdx.game.screens.ui.GameButton;
import com.mygdx.game.screens.ui.GameSlider;
import com.mygdx.game.screens.ui.Header;

public class SettingsScreen extends OverlayedScreen
{
    private final Common common;
    private final DistanceFieldRenderer distanceFieldRenderer;
    private final DistanceFieldFont distanceFieldFont;

    private Stage stage;
    private Overlay overlay;
    private Header header;
    private GameSlider musicSlider;
    private GameSlider soundSlider;
    private GameButton backButton;

    private InputMultiplexer inputMultiplexer;

    public SettingsScreen(GraphicsSettings graphicsSettings, Viewport viewport2D,
                          Viewport viewport3D, SpriteBatch spriteBatch, Common common)
    {
        super(Global.ScreenNames.SETTINGS_SCREEN, graphicsSettings, viewport2D,
                viewport3D, spriteBatch);

        this.common = common;
        this.distanceFieldRenderer = common.distanceFieldRenderer;
        this.distanceFieldFont = common.assets.distanceFieldFonts.furore.getInstance();
    }

    @Override
    public void initialize()
    {
        stage = new Stage(viewport2D, spriteBatch);
        overlay = new Overlay(graphicsSettings, Global.Colors.OVERLAY, Global.OVERLAY_ALPHA);
        header = new Header(common, graphicsSettings, "SETTINGS");

        musicSlider = new GameSlider(common, 0f, 100f, 1f, "MUSIC");
        soundSlider = new GameSlider(common, 0f, 100f, 1f, "SOUNDS");
        backButton = new GameButton(common, GameButton.ButtonTypes.SMALL, Global.ButtonStyles.BACK);

        float x = (graphicsSettings.virtualWidth / 2f) - (musicSlider.getWidth() / 2f);

        musicSlider.setOriginalPosition(x, 150f);
        soundSlider.setOriginalPosition(x, 270f);
        backButton.setOriginalPosition(770f, 400f);

        header.setShowDelay(0.100f);
        musicSlider.setShowDelay(0.150f);
        soundSlider.setShowDelay(0.250f);
        backButton.setShowDelay(0.300f);

        header.setHideDelay(0f);
        musicSlider.setHideDelay(0f);
        soundSlider.setHideDelay(0.100f);
        backButton.setHideDelay(0.100f);

        musicSlider.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                common.settings.setMusicVolume(musicSlider.getValue());
            }
        });

        soundSlider.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                common.settings.setSoundsVolume(soundSlider.getValue());
            }
        });

        backButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                screenManager.transitionTo(Global.ScreenNames.MAIN_MENU_SCREEN);
            }
        });

        stage.addActor(musicSlider);
        stage.addActor(soundSlider);
        stage.addActor(backButton);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new BackInputProcessor()
        {
            @Override
            protected void onBackDown()
            {
                StageUtilities.disableTouch(stage);
                screenManager.transitionTo(Global.ScreenNames.MAIN_MENU_SCREEN);
            }
        });
        inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void onEnter(Screen from)
    {
        header.show();
        musicSlider.show();
        soundSlider.show();
        backButton.show();

        stage.unfocusAll();

        common.settings.load();
        musicSlider.setValue(common.settings.getMusicVolume());
        soundSlider.setValue(common.settings.getSoundsVolume());
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

        header.hide();
        musicSlider.hide();
        soundSlider.hide();
        backButton.hide();
    }

    @Override
    public void onLeave(Screen to)
    {
        common.settings.save();
    }

    @Override
    public void update(GameTime gameTime)
    {
        common.background.update(gameTime);
        common.grass.update(gameTime);
        stage.act(gameTime.delta);
        header.update(gameTime);

        clearColor.set(getBackgroundForeColor(overlay));
    }

    @Override
    public void draw(GameTime gameTime)
    {
        // Reinicio del color ya que al parecer hay un bug en libgdx en el
        // que no se recupera el alpha original durante la ejecución de un
        // AlphaAction en un Actor
        ColorUtilities.resetColor(spriteBatch);

        common.shaders.defaultShader.setForegroundColor(getTransitionForeColor(overlay));
        spriteBatch.begin();
        common.background.draw(spriteBatch);
        common.grass.draw(spriteBatch);
        spriteBatch.end();

        common.shaders.defaultShader.setForegroundColor(getTransitionForeColor());
        stage.draw();

        spriteBatch.begin();
        header.drawTextures(spriteBatch);
        spriteBatch.end();

        common.shaders.textShader.setForegroundColor(getTransitionForeColor());
        distanceFieldRenderer.begin(distanceFieldFont, GameSlider.FONT_SCALE);
        header.drawText(distanceFieldRenderer, distanceFieldFont);
        musicSlider.drawText(distanceFieldRenderer, distanceFieldFont);
        soundSlider.drawText(distanceFieldRenderer, distanceFieldFont);
        distanceFieldRenderer.end();
    }
}