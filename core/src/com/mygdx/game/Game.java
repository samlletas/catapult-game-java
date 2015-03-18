package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.GameAdapter;
import com.engine.GameSpriteBatch;
import com.engine.graphics.GraphicsSettings;
import com.engine.graphics.shaders.shaders2d.AlphaBlended2DShader;
import com.engine.graphics.shaders.shaders2d.Custom2DShader;
import com.engine.screens.ScreenManager;
import com.engine.screens.transitions.*;
import com.engine.utilities.ColorUtilities;
import com.engine.utilities.GameProfiler;
import com.engine.GameTime;
import com.mygdx.game.assets.GameAssetMaster;
import com.mygdx.game.assets.GameAssets;
import com.mygdx.game.screens.gameplay.GameInstances;
import com.mygdx.game.screens.gameplay.modes.crystalfrenzy.CrystalFrenzyScreen;
import com.mygdx.game.screens.gameplay.modes.timeattack.TimeAttackScreen;
import com.mygdx.game.screens.menus.InfoScreen;
import com.mygdx.game.screens.menus.MainMenuScreen;
import com.mygdx.game.screens.menus.ModeSelectScreen;
import com.mygdx.game.screens.menus.SettingsScreen;
import com.mygdx.game.screens.splash.PoweredBySplashScreen;

public final class Game extends GameAdapter
{
    private GameAssetMaster assetMaster;
    private Common common;
    private GameInstances gameInstances;
    private GameProfiler profiler;
    private ScreenManager screenManager;

    public Game()
    {
        super(new GraphicsSettings(854, 480, Global.Colors.MAIN_BACKGROUND));
    }

    @Override
    protected Custom2DShader createDefaultShader()
    {
        return new AlphaBlended2DShader();
    }

    @Override
    protected Batch getSpriteBatch()
    {
        return new GameSpriteBatch(1000, defaultShader);
    }

    @Override
    protected ModelBatch getModelBatch()
    {
        return null;
    }

    @Override
    protected Viewport create2DViewport()
    {
        return new FitViewport(graphicsSettings.virtualWidth,
                graphicsSettings.virtualHeight, orthographicCamera);
    }

    @Override
    protected Viewport create3DViewport()
    {
        return new FitViewport(graphicsSettings.virtualWidth,
                graphicsSettings.virtualHeight, perspectiveCamera);
    }

    @Override
    public void initialize()
    {
        GameAssets assets = new GameAssets();
        assetMaster = new GameAssetMaster(assets);
        assetMaster.loadSync();

        common = new Common(assets, graphicsSettings, spriteBatch,
                (AlphaBlended2DShader)defaultShader);
        gameInstances = new GameInstances(common, viewport2D, graphicsSettings);

        if (Global.DEBUG_FPS || Global.DEBUG_OPENGL || Global.DEBUG_MEMORY)
        {
            profiler = new GameProfiler(680f, 10f);
            profiler.profileFPS = Global.DEBUG_FPS;
            profiler.profileOpenGL = Global.DEBUG_OPENGL;
            profiler.profileMemory = Global.DEBUG_MEMORY;
        }

        initializeScreens();

        Gdx.input.setCatchBackKey(true);
    }

    private void initializeScreens()
    {
        screenManager = new ScreenManager(graphicsSettings,
                new OptimizedFadeTransition(graphicsSettings, 1f,
                        ColorUtilities.createColor(Global.Colors.OVERLAY)));

        screenManager.addScreen(new PoweredBySplashScreen(graphicsSettings,
                viewport2D, viewport3D, spriteBatch, common));
        screenManager.addScreen(new MainMenuScreen(graphicsSettings, viewport2D,
                viewport3D, spriteBatch, common));
        screenManager.addScreen(new SettingsScreen(graphicsSettings, viewport2D,
                viewport3D, spriteBatch, common));
        screenManager.addScreen(new InfoScreen(graphicsSettings, viewport2D,
                viewport3D, spriteBatch, common));
        screenManager.addScreen(new ModeSelectScreen(graphicsSettings, viewport2D,
                viewport3D, spriteBatch, common));
        screenManager.addScreen(new TimeAttackScreen(graphicsSettings, viewport2D,
                viewport3D, spriteBatch, common, gameInstances));
        screenManager.addScreen(new CrystalFrenzyScreen(graphicsSettings, viewport2D,
                viewport3D, spriteBatch, common, gameInstances));

        screenManager.initialize();

//        screenManager.transitionTo(Global.ScreenNames.POWERED_BY_SCREEN,
//                new OptimizedFadeInTransition(graphicsSettings, 0.5f,
//                        ColorUtilities.createColor(Global.Colors.OVERLAY)));

        screenManager.transitionTo(Global.ScreenNames.MAIN_MENU_SCREEN,
                new OptimizedFadeInTransition(graphicsSettings, 0.5f,
                        ColorUtilities.createColor(Global.Colors.OVERLAY)));
    }

    @Override
    protected void update(GameTime gameTime)
    {
        screenManager.update(gameTime);
    }

    @Override
    protected void draw(GameTime gameTime)
    {
        screenManager.draw(gameTime);

        if (Global.DEBUG_FPS || Global.DEBUG_OPENGL || Global.DEBUG_MEMORY)
        {
            common.shaders.defaultShader.setForegroundColor(Global.Colors.NO_OVERLAY);
            profiler.profile(spriteBatch);
        }

        common.spriteBatch.clear();
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);

        screenManager.resize(width, height);
    }

    @Override
    public void pause()
    {
        screenManager.pause();
    }

    @Override
    public void resume()
    {
        screenManager.resume();
    }

    @Override
    public void dispose()
    {
        super.dispose();

        screenManager.dispose();
        assetMaster.dispose();
        common.dispose();
        gameInstances.dispose();
    }
}