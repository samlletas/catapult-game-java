package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.engine.GameAdapter;
import com.engine.GameTime;

public final class Game extends GameAdapter
{
    private AssetManager assetManager;
    private float x = 0f;
    FPSLogger fpsLogger;
    TextureAtlas atlas;
    TextureAtlas.AtlasRegion region1;
    TextureAtlas.AtlasRegion region2;
    TextureAtlas.AtlasRegion region3;

    public Game()
    {
        super(854, 480, new Color(44f / 255f, 90f / 255f, 160f / 255f, 1f));
    }

    @Override
    protected void setup2DViewport()
    {
        viewport2D = new FitViewport(virtualWidth, virtualHeight,
                orthographicCamera);
    }

    @Override
    protected void setup3DViewport()
    {
        viewport3D = new FitViewport(virtualWidth, virtualHeight,
                perspectiveCamera);
    }

    @Override
    public void initialize()
    {
        TextureAtlasLoader.TextureAtlasParameter parameter =
                new TextureAtlasLoader.TextureAtlasParameter();
        parameter.flip = true;

        assetManager = new AssetManager();
        assetManager.load("textures/pack.atlas", TextureAtlas.class, parameter);
        assetManager.finishLoading();

        atlas = assetManager.get("textures/pack.atlas");
        region1 = atlas.findRegion("small");
        region2 = atlas.findRegion("pow2");
        region3 = atlas.findRegion("face");

        fpsLogger = new FPSLogger();
    }

    @Override
    protected void update(GameTime gameTime)
    {
        x += gameTime.delta * 10f;
        fpsLogger.log();
    }

    @Override
    protected void draw(GameTime gameTime)
    {
        spriteBatch.begin();
        spriteBatch.draw(region1, 0, 0);
        spriteBatch.end();
    }
}
