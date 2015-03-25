package com.mygdx.game.screens.menus;

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
import com.mygdx.game.screens.ui.Header;
import com.mygdx.game.screens.ui.TextBlock;

public class InfoScreen extends OverlayedScreen
{
    private final Common common;
    private final Array<TextBlock> textBlocks;

    private final DistanceFieldRenderer distanceFieldRenderer;
    private final DistanceFieldFont distanceFieldFont;

    private Stage stage;
    private Overlay overlay;
    private Header header;
    private GameButton backButton;

    private InputMultiplexer inputMultiplexer;

    public InfoScreen(GraphicsSettings graphicsSettings, Viewport viewport2D,
                          Viewport viewport3D, Batch batch, Common common)
    {
        super(Global.ScreenNames.INFO, graphicsSettings, viewport2D,
                viewport3D, batch);

        this.common = common;
        this.textBlocks = new Array<TextBlock>(3);

        this.distanceFieldRenderer = common.distanceFieldRenderer;
        this.distanceFieldFont = common.assets.distanceFieldFonts.furore.getInstance();
    }

    @Override
    public void initialize()
    {
        stage = new Stage(viewport2D, batch);
        overlay = new Overlay(graphicsSettings, Global.Colors.OVERLAY, Global.OVERLAY_ALPHA);
        header = new Header(common, graphicsSettings, "ABOUT");

        TextBlock gameByTextBlock = new TextBlock(common, "A GAME BY:", "SAMUEL MACEDO");
        TextBlock musicByTextBlock = new TextBlock(common, "MUSIC BY:", "PLACE HOLDER");

        String specialThanksText = "ALFREDO ALVAREZ\nCARLOS MELO\nERICK AGUAYO\nEVELYN MACEDO\nMARIA ESQUIVEL\nMARYSOL MACEDO\nMIGUEL MELO\nPEDRO SAAVEDRA";
        TextBlock specialThanksTextBlock = new TextBlock(common, "SPECIAL THANKS TO:", specialThanksText, 340f, true);
        backButton = new GameButton(common, Global.ButtonStyles.BACK);

        gameByTextBlock.setOriginalPosition(120f, 165f);
        musicByTextBlock.setOriginalPosition(120f, 290f);
        specialThanksTextBlock.setOriginalPosition(450f, 165f);
        backButton.setOriginalPosition(770f, 400f);

        header.setShowDelay(0.200f);
        gameByTextBlock.setShowDelay(0.100f);
        musicByTextBlock.setShowDelay(0.200f);
        specialThanksTextBlock.setShowDelay(0.150f);
        backButton.setShowDelay(0.300f);

        header.setHideDelay(0f);
        gameByTextBlock.setHideDelay(0f);
        musicByTextBlock.setHideDelay(0.100f);
        specialThanksTextBlock.setHideDelay(0.050f);
        backButton.setHideDelay(0.300f);

        backButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                screenManager.transitionTo(Global.ScreenNames.MAIN_MENU);
            }
        });

        textBlocks.add(gameByTextBlock);
        textBlocks.add(musicByTextBlock);
        textBlocks.add(specialThanksTextBlock);

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

        Array<TextBlock> localTextBlocks = textBlocks;

        for (int i = 0, n = localTextBlocks.size; i < n; i++)
        {
            localTextBlocks.get(i).show();
        }

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

        Array<TextBlock> localTextBlocks = textBlocks;

        for (int i = 0, n = localTextBlocks.size; i < n; i++)
        {
            localTextBlocks.get(i).hide();
        }

        backButton.hide();
    }

    @Override
    public void update(GameTime gameTime)
    {
        common.background.update(gameTime);
        common.grass.update(gameTime);
        stage.act(gameTime.delta);
        header.update(gameTime);

        Array<TextBlock> localTextBlocks = textBlocks;

        for (int i = 0, n = localTextBlocks.size; i < n; i++)
        {
            localTextBlocks.get(i).update(gameTime);
        }

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
        Array<TextBlock> localTextBlocks = textBlocks;
        for (int i = 0, n = localTextBlocks.size; i < n; i++)
        {
           localTextBlocks.get(i).drawTextures(batch);
        }
        batch.end();

        common.shaders.textShader.setForegroundColor(getTransitionForeColor());
        distanceFieldRenderer.begin(distanceFieldFont, TextBlock.TEXT_FONT_SCALE);
        header.drawText(distanceFieldRenderer, distanceFieldFont);
        for (int i = 0, n = localTextBlocks.size; i < n; i++)
        {
            localTextBlocks.get(i).drawText(distanceFieldRenderer, distanceFieldFont);
        }
        distanceFieldRenderer.end();
    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }
}