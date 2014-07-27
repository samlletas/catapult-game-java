package com.mygdx.game;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.engine.GameAdapter;
import com.engine.utilities.ColorUtilities;
import com.engine.utilities.GameProfiler;
import com.engine.GameSettings;
import com.engine.GameTime;
import com.mygdx.game.assets.GameAssetMaster;
import com.mygdx.game.screens.gameplay.BaseGameScreen;

public final class Game extends GameAdapter
{
    private GameAssetMaster assetMaster;
    private GameProfiler profiler;
    private BaseGameScreen gameScreen;

    public Game()
    {
        super(new GameSettings(854, 480,
                ColorUtilities.createColor(36, 82, 130, 255)));
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
        profiler = new GameProfiler(true, false);
        gameScreen = new BaseGameScreen(settings, orthographicCamera,
                perspectiveCamera);

        assetMaster.loadSync();
        gameScreen.initialize();
    }

    @Override
    protected void update(GameTime gameTime)
    {
        gameScreen.update(gameTime);
    }

    @Override
    protected void draw(GameTime gameTime)
    {
        gameScreen.draw(gameTime, spriteBatch);
        profiler.profile(spriteBatch);
    }
}