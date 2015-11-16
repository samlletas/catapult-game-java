package com.sammacedo.smashingcrystals.screens.menus;

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
import com.sammacedo.smashingcrystals.Common;
import com.sammacedo.smashingcrystals.Global;
import com.sammacedo.smashingcrystals.gamelogic.scene.Background;
import com.sammacedo.smashingcrystals.screens.OverlayedScreen;
import com.sammacedo.smashingcrystals.screens.ui.GameButton;
import com.sammacedo.smashingcrystals.screens.ui.Header;
import com.sammacedo.smashingcrystals.screens.ui.ModeButton;
import com.sammacedo.smashingcrystals.screens.ui.TextBlock;

public final class ModeSelectScreen extends OverlayedScreen
{
    private final Common common;
    private final DistanceFieldRenderer distanceFieldRenderer;
    private final DistanceFieldFont distanceFieldFont;

    private Stage stage;
    private Overlay overlay;
    private Header header;
    private ModeButton timeAttackButton;
    private ModeButton crystalFrenzyButton;
    private GameButton backButton;

    private InputMultiplexer inputMultiplexer;

    public ModeSelectScreen(GraphicsSettings graphicsSettings, Viewport viewport2D,
                            Viewport viewport3D, Batch batch, Common common)
    {
        super(Global.ScreenNames.MODE_SELECT, graphicsSettings, viewport2D,
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
        header = new Header(common, graphicsSettings, "SELECT MODE");

        timeAttackButton = new ModeButton(common, "TIME ATTACK", "DESTROY AS MANY\nCRYSTALS AS YOU\nCAN BEFORE TIME\nRUNS OUT!");
        crystalFrenzyButton = new ModeButton(common, "CRYSTAL FRENZY", "LAST AS LONG AS\nPOSSIBLE AND ACHIEVE\nTHE HIGHEST SCORE!", true);
        backButton = new GameButton(common, Global.ButtonStyles.BACK);

        timeAttackButton.setOriginalPosition(215f, 235f);
        crystalFrenzyButton.setOriginalPosition(630f, 235f);
        backButton.setOriginalPosition(770f, 400f);

        header.setShowDelay(0.200f);
        timeAttackButton.setShowDelay(0.200f);
        crystalFrenzyButton.setShowDelay(0.275f);
        backButton.setShowDelay(0.300f);

        header.setHideDelay(0f);
        timeAttackButton.setHideDelay(0.050f);
        crystalFrenzyButton.setHideDelay(0.100f);
        backButton.setHideDelay(0.100f);

        timeAttackButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                screenManager.transitionTo(Global.ScreenNames.TIME_ATTACK);
            }
        });

        crystalFrenzyButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                screenManager.transitionTo(Global.ScreenNames.CRYSTAL_FRENZY);
            }
        });

        backButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                screenManager.transitionTo(Global.ScreenNames.MAIN_MENU);
            }
        });

        stage.addActor(timeAttackButton);
        stage.addActor(crystalFrenzyButton);
        stage.addActor(backButton);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new BackInputProcessor()
        {
            @Override
            protected void onBackDown()
            {
                StageUtilities.disableTouch(stage);
                screenManager.transitionTo(Global.ScreenNames.MAIN_MENU);
            }
        });
        inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void onEnter(Screen from)
    {
        header.show();
        timeAttackButton.show();
        crystalFrenzyButton.show();
        backButton.show();

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
        timeAttackButton.hide();
        crystalFrenzyButton.hide();
        backButton.hide();
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
        distanceFieldRenderer.begin(TextBlock.TEXT_FONT_SCALE);
        header.drawText(distanceFieldRenderer);
        timeAttackButton.drawText(distanceFieldRenderer);
        crystalFrenzyButton.drawText(distanceFieldRenderer);
        distanceFieldRenderer.end();
    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }
}
