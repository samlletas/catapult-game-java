package com.mygdx.game.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.graphics.GraphicsSettings;
import com.engine.GameTime;
import com.engine.camera.CameraShaker2D;
import com.engine.events.DelayedEventHandler;
import com.engine.events.EventsArgs;
import com.engine.events.IEventHandler;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.input.BackInputProcessor;
import com.engine.screens.Screen;
import com.engine.utilities.ColorUtilities;
import com.mygdx.game.Common;
import com.mygdx.game.Global;
import com.mygdx.game.gamelogic.*;
import com.mygdx.game.gamelogic.scene.Background;
import com.mygdx.game.gamelogic.scene.Grass;
import com.mygdx.game.gamelogic.targets.patterns.TargetCollisionArgs;
import com.mygdx.game.screens.OverlayedScreen;
import com.mygdx.game.screens.ui.GameButton;
import com.mygdx.game.screens.ui.Header;

public final class GameplayScreen extends OverlayedScreen
{
    //region Campos

    // Para 60 FPS casi constantes(Android-Sam): 30 máx
    private int updates = 1;

    // Para 60 FPS casi constantes(Android-Sam): 3 máx
    private int draws = 1;

    private final Common common;
    private final DistanceFieldRenderer distanceFieldRenderer;
    private final DistanceFieldFont distanceFieldFont;

    private ModelBatch modelBatch;
    private OrthographicCamera orthographicCamera;
    private CameraShaker2D cameraShaker;
    private Background background;
    private Grass grass;
    private Catapult catapult;
    private Ball ball;
    private CrystalManager crystalManager;

    private GameplayData gameplayData;
    private GameHUD gameHUD;
    private StartCounter startCounter;
    private PauseOverlay pauseOverlay;
    private ScoreLabelContainer scoreLabelContainer;

    // Ui para el botón de pausa
    private Stage pauseStage;
    private GameButton pauseButton;

    // Para procesar el input de la catapulta y el botón de pausa
    private InputMultiplexer inputMultiplexer;

    private GameStates gameState;
    private DelayedEventHandler<EventsArgs> outOfLivesDelayedHandler;

    //endregion

    //region Constructor e Inicialización

    public GameplayScreen(GraphicsSettings settings, Viewport viewport2D,
                          Viewport viewport3D, SpriteBatch spriteBatch,
                          Common common)
    {
        super(Global.ScreenNames.GAMEPLAY_SCREEN, settings, viewport2D, viewport3D,
                spriteBatch);

        this.common = common;
        this.distanceFieldRenderer = common.distanceFieldRenderer;
        this.distanceFieldFont = common.assets.distanceFieldFonts.furore.getInstance();
    }

    @Override
    public void initialize()
    {
        modelBatch = common.modelBatch;
        orthographicCamera = (OrthographicCamera)viewport2D.getCamera();
        cameraShaker = new CameraShaker2D(orthographicCamera, 80, 0, 0, 0.75f, 0.99f);
        background = common.background;
        grass = common.grass;
        ball = new Ball(common, graphicsSettings, cameraShaker);
        catapult = new Catapult(common, ball);
        crystalManager = new CrystalManager(common);

        gameplayData = new GameplayData();
        gameHUD = new GameHUD(common, gameplayData);
        startCounter = new StartCounter(graphicsSettings);
        pauseOverlay = new PauseOverlay(this, common, graphicsSettings, viewport2D, spriteBatch);
        scoreLabelContainer = new ScoreLabelContainer();

        pauseButton = new GameButton(common, GameButton.ButtonTypes.TINY, Global.ButtonStyles.PAUSE);
        pauseButton.setPosition(790f, 10f);

        pauseStage = new Stage(viewport2D, spriteBatch);
        pauseStage.addActor(pauseButton);

        initializeInput();
        initializeEvents();
    }

    //endregion

    //region Eventos de Screen

    @Override
    public void onEnter(Screen from)
    {
        reset();
    }

    @Override
    public void onTransitionFromFinish(Screen from)
    {
        startCounter.start();
    }

    //endregion

    //region Eventos de Gameplay

    private void initializeEvents()
    {
        pauseOverlay.setUnpausedRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                resumeGame();
            }
        });

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                pauseGame();
            }
        });

        startCounter.onEnd.subscribe(new IEventHandler<EventsArgs>()
        {
            @Override
            public void onAction(EventsArgs args)
            {
                startGame();
            }
        });

        crystalManager.onCrystalCollision.subscribe(new IEventHandler<TargetCollisionArgs>()
        {
            @Override
            public void onAction(TargetCollisionArgs args)
            {
                gameplayData.increaseSpecial(args.special);
                scoreLabelContainer.showScoreLabel(args, 74f, 35f);
            }
        });

        crystalManager.onSpikeCollision.subscribe(new IEventHandler<TargetCollisionArgs>()
        {
            @Override
            public void onAction(TargetCollisionArgs args)
            {
                gameHUD.disolveLive();
                gameplayData.decreaseLive();
            }
        });

        scoreLabelContainer.onScoreLabelFinished.subscribe(new IEventHandler<TargetCollisionArgs>()
        {
            @Override
            public void onAction(TargetCollisionArgs args)
            {
                gameplayData.increaseScore(args.score);
                gameHUD.bloom();
            }
        });

        scoreLabelContainer.onChanceLabelFinished.subscribe(new IEventHandler<TargetCollisionArgs>()
        {
            @Override
            public void onAction(TargetCollisionArgs args)
            {

            }
        });

        gameplayData.onSpecialActivated.subscribe(new IEventHandler<EventsArgs>()
        {
            @Override
            public void onAction(EventsArgs args)
            {
                crystalManager.activateSpecial();
                gameHUD.activateSpeciarBar();
            }
        });

        gameplayData.onSpecialFinalized.subscribe(new IEventHandler<EventsArgs>()
        {
            @Override
            public void onAction(EventsArgs args)
            {
                crystalManager.deactivateSpecial();
                gameHUD.deactivateSpecialBar();
            }
        });

        outOfLivesDelayedHandler = new DelayedEventHandler<EventsArgs>()
        {
            @Override
            public void onResolve(EventsArgs args)
            {
                crystalManager.deactivateSpecial();
                gameHUD.deactivateSpecialBar();
            }
        };

        gameplayData.onOutOfLives.subscribe(outOfLivesDelayedHandler);
    }

    //endregion

    //region Input

    private void initializeInput()
    {
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(pauseStage);
        inputMultiplexer.addProcessor(new BackInputProcessor()
        {
            @Override
            protected void onBackDown()
            {
                disableInput();
                pauseGame();
            }
        });
        inputMultiplexer.addProcessor(catapult.getInputProcessor());
    }

    private void enableInput()
    {
        Gdx.input.setInputProcessor(inputMultiplexer);
        catapult.enableInput();
    }

    private void disableInput()
    {
        Gdx.input.setInputProcessor(null);
        catapult.disableInput();
    }

    //endregion

    //region Estados de Gameplay

    public void reset()
    {
        disableInput();
        gameplayData.reset();
        gameHUD.reset();
        crystalManager.reset();
        catapult.reset();
        ball.reset();
        pauseOverlay.reset();

        gameState = GameStates.Starting;
    }

    private void startGame()
    {
        enableInput();
        crystalManager.start();
    }

    private void pauseGame()
    {
        disableInput();
        gameState = GameStates.Paused;
        pauseOverlay.show();
    }

    private void resumeGame()
    {
        enableInput();
        gameState = GameStates.Playing;
    }

    //endregion

    //region Actualización

    @Override
    public void update(GameTime gameTime)
    {
        for (int i = 0; i < updates; i++)
        {
            outOfLivesDelayedHandler.resolve();

            pauseStage.act(gameTime.delta);

            if (gameState == GameStates.Paused)
            {
                pauseOverlay.update(gameTime);
            }
            else
            {
                if (gameState == GameStates.Starting)
                {
                    startCounter.update(gameTime);
                }

                cameraShaker.update(gameTime);
                background.update(gameTime);
                grass.update(gameTime);
                catapult.update(gameTime);
                ball.update(gameTime);
                gameplayData.updateSpecial(gameTime);
                crystalManager.update(gameTime);

                crystalManager.checkCollisions(ball);
                ball.checkCollisions(gameTime, grass);
                scoreLabelContainer.update(gameTime);
                gameHUD.update(gameTime);

                if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
                {
                    gameplayData.increaseSpecial(100f);
                }
            }
        }

        clearColor.set(getBackgroundForeColor(pauseOverlay.getOverlay()));
    }

    //endregion

    //region Dibujado

    @Override
    public void draw(GameTime gameTime)
    {
        for (int i = 0; i < draws; i++)
        {
            // Reinicio del color ya que al parecer hay un bug en libgdx en el
            // que no se recupera el alpha original durante la ejecución de un
            // AlphaAction en un Actor
            ColorUtilities.resetColor(spriteBatch);

//            cameraShaker.beginDraw(spriteBatch);

            common.shaders.defaultShader.setForegroundColor(getTransitionForeColor(pauseOverlay.getOverlay()));
            spriteBatch.begin();
            background.draw(spriteBatch);
            spriteBatch.end();

            ball.setTrailForeColor(getTransitionForeColor(pauseOverlay.getOverlay()));
            ball.drawTrail(orthographicCamera);

            spriteBatch.begin();
            ball.drawTextures(spriteBatch);
            catapult.draw(spriteBatch);
            crystalManager.drawTextures(spriteBatch);
            grass.draw(spriteBatch);
            gameHUD.drawTextures(spriteBatch);
            pauseButton.draw(spriteBatch, 1f);
            spriteBatch.end();

            common.crystalShaderProvider.setForegroundColor(getTransitionForeColor(pauseOverlay.getOverlay()));
            modelBatch.begin(orthographicCamera);
            crystalManager.drawModels(modelBatch);
            modelBatch.end();

            spriteBatch.begin();
            crystalManager.drawEffects(spriteBatch);
            spriteBatch.end();

            common.shaders.textShader.setForegroundColor(getTransitionForeColor(pauseOverlay.getOverlay()));
            distanceFieldRenderer.begin(distanceFieldFont);
            scoreLabelContainer.drawText(distanceFieldRenderer, distanceFieldFont);
            gameHUD.drawScore(distanceFieldRenderer, distanceFieldFont);
            if (gameState == GameStates.Starting)
            {
                startCounter.drawText(distanceFieldRenderer, distanceFieldFont);
            }
            distanceFieldRenderer.end();

            if (Global.DEBUG_POLYGONS)
            {
                common.shapeRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());
                common.shapeRenderer.setTransformMatrix(spriteBatch.getTransformMatrix());

                common.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                ball.drawPolygon(common.shapeRenderer);
                grass.drawPolygons(common.shapeRenderer);
                crystalManager.drawPolygons(common.shapeRenderer);
                common.shapeRenderer.end();
            }

//            cameraShaker.endDraw(spriteBatch);


            else if (gameState == GameStates.Paused)
            {
                common.shaders.defaultShader.setForegroundColor(getTransitionForeColor());
                spriteBatch.begin();
                pauseOverlay.drawTextures(spriteBatch);
                spriteBatch.end();

                pauseOverlay.drawStage();

                common.shaders.textShader.setForegroundColor(getTransitionForeColor());
                distanceFieldRenderer.begin(distanceFieldFont, Header.FONT_SCALE);
                pauseOverlay.drawText(distanceFieldRenderer, distanceFieldFont);
                distanceFieldRenderer.end();
            }
        }
    }

    //endregion

    //region Helpers

    public void transitionTo(String name)
    {
        screenManager.transitionTo(name);
    }

    @Override
    public void dispose()
    {
        pauseStage.dispose();
        ball.dispose();
    }

    //endregion
}