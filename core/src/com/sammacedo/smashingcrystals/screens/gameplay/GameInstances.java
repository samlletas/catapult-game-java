package com.sammacedo.smashingcrystals.screens.gameplay;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.camera.CameraShaker2D;
import com.engine.graphics.GraphicsSettings;
import com.sammacedo.smashingcrystals.Common;
import com.sammacedo.smashingcrystals.Global;
import com.sammacedo.smashingcrystals.gamelogic.*;
import com.sammacedo.smashingcrystals.screens.ui.GameButton;

public final class GameInstances
{
    public OrthographicCamera orthographicCamera;

    public CameraShaker2D cameraShaker;
    public Catapult catapult;
    public Ball ball;
    public BallPath ballPath;
    public CrystalManager crystalManager;

    public StartCounter startCounter;
    public GameLabelContainer gameLabelContainer;
    public GameMessage gameOverMessage;
    public PauseOverlay pauseOverlay;

    // Ui para el botón de pausa
    public Stage pauseStage;
    public GameButton pauseButton;

    public GameInstances(Common common, Viewport viewport2D, GraphicsSettings graphicsSettings)
    {
        orthographicCamera = (OrthographicCamera)viewport2D.getCamera();
        cameraShaker = new CameraShaker2D(orthographicCamera, 80, 0, 0, 0.75f, 0.99f);
        ball = new Ball(common, graphicsSettings);
        ballPath = new BallPath(common, graphicsSettings);
        catapult = new Catapult(common, ball, ballPath, orthographicCamera);
        crystalManager = new CrystalManager(common);

        startCounter = new StartCounter(graphicsSettings, common.assets.distanceFieldFonts.furore.getInstance());
        gameLabelContainer = new GameLabelContainer(common, 20);
        gameOverMessage = new GameMessage(common, graphicsSettings);
        pauseOverlay = new PauseOverlay(common, graphicsSettings, viewport2D, common.spriteBatch);

        pauseButton = new GameButton(common, Global.ButtonStyles.PAUSE);
        pauseButton.setPosition(785f, 5f);

        pauseStage = new Stage(viewport2D, common.spriteBatch);
        pauseStage.addActor(pauseButton);
    }

    public void dispose()
    {
        pauseStage.dispose();
        pauseOverlay.dispose();
        ball.dispose();
    }
}
