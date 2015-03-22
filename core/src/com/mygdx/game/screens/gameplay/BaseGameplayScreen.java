package com.mygdx.game.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.graphics.GraphicsSettings;
import com.engine.GameTime;
import com.engine.events.EventsArgs;
import com.engine.events.IEventHandler;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.input.BackInputProcessor;
import com.engine.screens.Screen;
import com.engine.utilities.ColorUtilities;
import com.engine.utilities.StageUtilities;
import com.mygdx.game.Common;
import com.mygdx.game.Global;
import com.mygdx.game.gamelogic.scene.Background;
import com.mygdx.game.gamelogic.targets.patterns.TargetCollisionArgs;
import com.mygdx.game.screens.OverlayedScreen;
import com.mygdx.game.screens.ui.Header;

public abstract class BaseGameplayScreen extends OverlayedScreen
{
    //region Campos

    // Para 60 FPS casi constantes(Android-Sam): 30 máx
    private int updates = 1;

    // Para 60 FPS casi constantes(Android-Sam): 3 máx
    private int draws = 1;

    /**
     * Acumulador para permitir un fixed timestep
     *
     * <a href="http://gafferongames.com/game-physics/fix-your-timestep/">Link</a>
     */
    private float accumulator;

    protected final Common common;
    protected final GameInstances gameInstances;

    private final BaseGameplayData gameplayData;
    private final BaseGameHUD gameHUD;

    protected final DistanceFieldRenderer distanceFieldRenderer;
    protected final DistanceFieldFont distanceFieldFont;

    // Para procesar el input de la catapulta y el botón de pausa
    private InputMultiplexer inputMultiplexer;

    // Estado de la pantalla
    protected GameStates gameState;
    private boolean gameOverFlag;

    protected Runnable gotoScoreScreenRunnable;

    //endregion

    //region Constructor e Inicialización

    public BaseGameplayScreen(String name, GraphicsSettings settings, Viewport viewport2D,
                              Viewport viewport3D, Batch batch, Common common, GameInstances gameInstances)
    {
        super(name, settings, viewport2D, viewport3D, batch);

        this.common = common;
        this.gameInstances = gameInstances;

        this.gameplayData = createGameplayData();
        this.gameHUD = createHUD();

        this.distanceFieldRenderer = common.distanceFieldRenderer;
        this.distanceFieldFont = common.assets.distanceFieldFonts.furore.getInstance();

        this.gameState = GameStates.None;
    }

    @Override
    public void initialize()
    {
        initializeInput();
        initializeEvents();
        onInitialize();
    }

    //endregion

    //region Eventos de Screen

    @Override
    public void onEnter(Screen from)
    {
        if (isSubmenu(from))
        {
            gameInstances.pauseOverlay.resumePause();
        }
        else
        {
            reset();

            common.background.setMode(Background.Mode.Gameplay);
            gameInstances.pauseOverlay.setGameplayScreen(this);
        }
    }

    @Override
    public void onTransitionFromFinish(Screen from)
    {
        if (!isSubmenu(from))
        {
            gameInstances.startCounter.start();
        }
    }

    @Override
    public void onLeave(Screen to)
    {
        if (!isSubmenu(to))
        {
            gameState = GameStates.None;
        }
    }

    private boolean isSubmenu(Screen screen)
    {
        return screen.getName().equals(Global.ScreenNames.SETTINGS_SCREEN) ||
                screen.getName().equals(Global.ScreenNames.HOW_TO_TIME_ATTACK) ||
                screen.getName().equals(Global.ScreenNames.HOW_TO_CYRSTAL_FRENZY);
    }

    //endregion

    //region Eventos de Gameplay

    private void initializeEvents()
    {
        gameInstances.startCounter.onEnd.subscribe(new IEventHandler<EventsArgs>()
        {
            @Override
            public void onAction(EventsArgs args)
            {
                if (gameState != GameStates.None)
                {
                    startGame();
                }
            }
        });

        gameInstances.pauseButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (gameState != GameStates.None && !gameInstances.catapult.isPulling())
                {
                    pauseGame();
                }
            }
        });

        gameInstances.crystalManager.onTargetCollision.subscribe(new IEventHandler<TargetCollisionArgs>()
        {
            @Override
            public void onAction(TargetCollisionArgs args)
            {
                if (gameState != GameStates.None)
                {
                    onTargetCollision(args);
                }
            }
        });

        gotoScoreScreenRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                gameInstances.gameOverMessage.hide();
                Screen gameOverScreen = screenManager.getScreen(Global.ScreenNames.GAME_OVER);
                screenManager.transitionTo(gameOverScreen);
            }
        };
    }

    //endregion

    //region Input

    private void initializeInput()
    {
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(gameInstances.pauseStage);
        inputMultiplexer.addProcessor(new BackInputProcessor()
        {
            @Override
            protected void onBackDown()
            {
                if (!gameInstances.catapult.isPulling())
                {
                    disableInput();
                    pauseGame();
                }
            }
        });
    }

    private void enableInput()
    {
        Gdx.input.setInputProcessor(inputMultiplexer);
        gameInstances.catapult.enableInput();
    }

    private void disableInput()
    {
        Gdx.input.setInputProcessor(null);
        gameInstances.catapult.disableInput();
    }

    //endregion

    //region Estados de Gameplay

    public void reset()
    {
        disableInput();

        gameHUD.reset();
        gameplayData.reset();
        gameInstances.gameLabelContainer.reset();
        gameInstances.crystalManager.reset();
        gameInstances.catapult.reset();
        gameInstances.ball.reset();
        gameInstances.ballPath.reset();
        gameInstances.pauseOverlay.reset();

        accumulator = 0f;
        gameState = GameStates.Starting;
        gameOverFlag = false;

        onReset();
    }

    private void startGame()
    {
        enableInput();
        gameInstances.crystalManager.start();
        onStart();
    }

    private void pauseGame()
    {
        disableInput();
        gameState = GameStates.Paused;
        gameInstances.pauseOverlay.show();
    }

    public final void resumeGame()
    {
        enableInput();
        gameState = GameStates.Playing;
    }

    protected final void setGameOverFlag()
    {
        gameOverFlag = true;
    }

    protected final void endGame()
    {
        disableInput();
        gameInstances.pauseStage.unfocusAll();
        gameInstances.gameOverMessage.show(gotoScoreScreenRunnable);
        gameState = GameStates.GameOver;
        gameOverFlag = false;
    }

    //endregion

    //region Actualización

    @Override
    public void update(GameTime gameTime)
    {
        for (int i = 0; i < updates; i++)
        {
            if (gameOverFlag)
            {
                endGame();
            }

            gameInstances.pauseStage.act(gameTime.delta);

            if (gameState == GameStates.Paused)
            {
                gameInstances.pauseOverlay.update(gameTime);
            }
            else
            {
                if (gameState == GameStates.Starting)
                {
                    gameInstances.startCounter.update(gameTime);
                }

                gameInstances.cameraShaker.update(gameTime);

                if (gameState != GameStates.GameOver)
                {
                    common.background.update(gameTime);
                    common.grass.update(gameTime);
                    gameInstances.catapult.update(gameTime);
                    gameplayData.update(gameTime);
                    gameInstances.crystalManager.update(gameTime);

                    accumulator += gameTime.delta;

                    while (accumulator >= gameTime.delta)
                    {
                        gameInstances.ball.step(gameTime.elapsed, Global.TIME_STEP);
                        gameInstances.ballPath.step(gameTime.elapsed, Global.TIME_STEP);
                        gameInstances.crystalManager.step(gameTime.elapsed, Global.TIME_STEP);

                        gameInstances.crystalManager.checkCollisions(gameInstances.ball);
                        gameInstances.ball.checkCollisions(common.grass);

                        accumulator -= Global.TIME_STEP;
                    }

                    gameInstances.ball.update(gameTime);
                    gameInstances.ballPath.update(gameTime);

                    if (gameInstances.catapult.isPulling())
                    {
                        StageUtilities.disableTouch(gameInstances.pauseStage);
                    }
                    else
                    {
                        StageUtilities.enableTouch(gameInstances.pauseStage);
                    }
                }
                else
                {
                    gameInstances.gameOverMessage.update(gameTime);
                }
                gameInstances.gameLabelContainer.update(gameTime);
                gameHUD.update(gameTime);
            }

            onUpdate(gameTime);
        }
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
            ColorUtilities.resetColor(common.spriteBatch);

            gameInstances.cameraShaker.beginDraw(common.spriteBatch);
            drawBackground();
            drawBallTrail();
            drawGameTextures();
            drawGameModels();
            drawEffects();
            drawText();
            drawDebug();
            gameInstances.cameraShaker.endDraw(common.spriteBatch);
            drawPause();
        }
    }

    private void drawBackground()
    {
        common.shaders.defaultShader.setForegroundColor(getTransitionForeColor(gameInstances.pauseOverlay.getOverlay()));
        common.spriteBatch.begin();
        common.background.draw(common.spriteBatch);
        common.spriteBatch.end();
    }

    private void drawBallTrail()
    {
        gameInstances.ball.setTrailForeColor(getTransitionForeColor(gameInstances.pauseOverlay.getOverlay()));
        gameInstances.ball.drawTrail(gameInstances.orthographicCamera);
    }

    private void drawGameTextures()
    {
        batch.begin();
        gameInstances.ball.drawTextures(batch);
        gameInstances.catapult.draw(batch);
        gameInstances.crystalManager.drawTextures(batch);
        common.grass.draw(batch);
        gameHUD.drawTextures(batch);
        gameInstances.pauseButton.draw(batch, 1f);
        batch.end();
    }

    private void drawGameModels()
    {
        gameInstances.crystalManager.setForegoundColor(getTransitionForeColor(gameInstances.pauseOverlay.getOverlay()));
        gameInstances.crystalManager.drawModels(gameInstances.orthographicCamera);
    }

    private void drawEffects()
    {
        common.spriteBatch.begin();
        common.spriteBatch.beginParticleDraw();
        gameInstances.crystalManager.drawEffects(common.spriteBatch);
        common.spriteBatch.endParticleDraw();
        gameInstances.ballPath.draw(common.spriteBatch);

        if (gameState == GameStates.GameOver)
        {
            gameInstances.gameOverMessage.drawTextures(common.spriteBatch);
        }

        common.spriteBatch.end();
    }

    private void drawText()
    {
        common.shaders.textShader.setForegroundColor(getTransitionForeColor(gameInstances.pauseOverlay.getOverlay()));
        distanceFieldRenderer.begin(distanceFieldFont);
        gameInstances.gameLabelContainer.drawText(distanceFieldRenderer, distanceFieldFont);
        gameHUD.drawText(distanceFieldRenderer, distanceFieldFont);

        if (gameState == GameStates.Starting)
        {
            gameInstances.startCounter.drawText(distanceFieldRenderer, distanceFieldFont);
        }
        else if (gameState == GameStates.GameOver)
        {
            gameInstances.gameOverMessage.drawText(distanceFieldRenderer, distanceFieldFont);
        }

        distanceFieldRenderer.end();
    }

    private void drawDebug()
    {
        if (Global.DEBUG_POLYGONS)
        {
            common.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            common.shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

            common.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            gameInstances.ball.drawPolygon(common.shapeRenderer);
            common.grass.drawPolygons(common.shapeRenderer);
            gameInstances.crystalManager.drawPolygons(common.shapeRenderer);
            common.shapeRenderer.end();
        }
    }

    private void drawPause()
    {
        if (gameState == GameStates.Paused)
        {
            common.shaders.defaultShader.setForegroundColor(getTransitionForeColor());
            batch.begin();
            gameInstances.pauseOverlay.drawTextures(batch);
            batch.end();

            gameInstances.pauseOverlay.drawStage();

            common.shaders.textShader.setForegroundColor(getTransitionForeColor());
            distanceFieldRenderer.begin(distanceFieldFont, Header.FONT_SCALE);
            gameInstances.pauseOverlay.drawText(distanceFieldRenderer, distanceFieldFont);
            distanceFieldRenderer.end();
        }
    }

    //endregion

    //region Disposing

    @Override
    public void dispose()
    {
        onDispose();
    }

    //endregion

    protected abstract BaseGameplayData createGameplayData();
    protected abstract BaseGameHUD createHUD();
    protected abstract void onInitialize();
    protected abstract void onReset();
    protected abstract void onStart();
    protected abstract void onTargetCollision(TargetCollisionArgs args);
    protected abstract void onUpdate(GameTime gameTime);
    protected abstract void onDispose();
}