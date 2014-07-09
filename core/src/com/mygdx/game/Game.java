package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.engine.GameAdapter;
import com.engine.GameSettings;
import com.engine.GameTime;
import com.engine.graphics.animation.*;
import com.engine.graphics.animation.events.IAnimationHandler;
import com.mygdx.game.assets.GameAssets;
import com.mygdx.game.assets.GameAssetMaster;
import com.mygdx.game.screens.gameplay.BaseGameScreen;
import javafx.scene.Scene;

public final class Game extends GameAdapter
{
    private GameAssetMaster assetMaster;
    private FPSLogger fpsLogger;
    private BaseGameScreen gameScreen;

    public Game()
    {
        super(new GameSettings(854, 480,
                new Color(36f / 255f, 82f / 255f, 130f / 255f, 1f)));
    }

    @Override
    protected void setup2DViewport()
    {
        viewport2D = new FitViewport(settings.virtualWidth,
                settings.virtualHeight, orthographicCamera);
    }

    @Override
    protected void setup3DViewport()
    {
        viewport3D = new FitViewport(settings.virtualWidth,
                settings.virtualHeight, perspectiveCamera);
    }

    @Override
    public void initialize()
    {
        assetMaster = new GameAssetMaster();
        fpsLogger = new FPSLogger();
        gameScreen = new BaseGameScreen(settings, orthographicCamera,
                perspectiveCamera);

        assetMaster.loadSync();
        gameScreen.initialize();
    }

    @Override
    protected void update(GameTime gameTime)
    {
//        fpsLogger.log();

        gameScreen.update(gameTime);
    }

    @Override
    protected void draw(GameTime gameTime)
    {
        gameScreen.draw(gameTime, spriteBatch);
    }
}
