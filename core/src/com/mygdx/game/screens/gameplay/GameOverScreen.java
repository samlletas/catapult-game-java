package com.mygdx.game.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import com.mygdx.game.gamelogic.scene.Background;
import com.mygdx.game.screens.OverlayedScreen;
import com.mygdx.game.screens.ui.GameButton;
import com.mygdx.game.screens.ui.GameSlider;
import com.mygdx.game.screens.ui.Header;

public class GameOverScreen extends OverlayedScreen
{
    private final Common common;
    private final DistanceFieldRenderer distanceFieldRenderer;
    private final DistanceFieldFont distanceFieldFont;

    private Stage stage;
    private Overlay overlay;
    private Header header;
    private GameButton homeButton;

    private InputMultiplexer inputMultiplexer;
    private Screen gameplayScreen;

    public GameOverScreen(GraphicsSettings graphicsSettings, Viewport viewport2D,
                          Viewport viewport3D, Batch batch, Common common)
    {
        super(Global.ScreenNames.GAME_OVER, graphicsSettings, viewport2D,
                viewport3D, batch);

        this.common = common;
        this.distanceFieldRenderer = common.distanceFieldRenderer;
        this.distanceFieldFont = common.assets.distanceFieldFonts.furore.getInstance();
    }

    @Override
    public void initialize()
    {
        stage = new Stage(viewport2D, batch);
        overlay = new Overlay(graphicsSettings, Global.Colors.OVERLAY, Global.OVERLAY_ALPHA);
        header = new Header(common, graphicsSettings, "GAME OVER");

        homeButton = new GameButton(common, Global.ButtonStyles.HOME_SMALL);

        homeButton.setOriginalPosition(300f, 300f);

        header.setShowDelay(0.200f);
        homeButton.setShowDelay(0f);

        header.setHideDelay(0f);
        homeButton.setHideDelay(0f);

        homeButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                StageUtilities.disableTouch(stage);
                screenManager.transitionTo(Global.ScreenNames.MAIN_MENU_SCREEN);
            }
        });

        stage.addActor(homeButton);

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
        homeButton.show();

        stage.unfocusAll();
        common.background.setMode(Background.Mode.Menus);
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
        homeButton.hide();
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
        ColorUtilities.resetColor(batch);

        common.shaders.defaultShader.setForegroundColor(getTransitionForeColor(overlay));
        batch.begin();
        common.background.draw(batch);
        common.grass.draw(batch);
        batch.end();

        common.shaders.defaultShader.setForegroundColor(getTransitionForeColor());
        stage.draw();

        batch.begin();
        header.drawTextures(batch);
        batch.end();

        common.shaders.textShader.setForegroundColor(getTransitionForeColor());
        distanceFieldRenderer.begin(distanceFieldFont, GameSlider.FONT_SCALE);
        header.drawText(distanceFieldRenderer, distanceFieldFont);
        distanceFieldRenderer.end();
    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }
}
