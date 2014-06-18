package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.engine.GameAdapter;
import com.engine.GameTime;
import com.mygdx.game.shaders.TestShader;

public final class Game extends GameAdapter
{
    private AssetManager assetManager;
    private TestShader shader;
    private float x = 0f;
    FPSLogger fpsLogger;
    TextureAtlas atlas;
    TextureAtlas.AtlasRegion region1;
    TextureAtlas.AtlasRegion region2;
    TextureAtlas.AtlasRegion region3;

    public Game()
    {
        super(854, 480);
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

        shader = new TestShader();
        shader.init();

        viewport2D = new FitViewport(virtualWidth, virtualHeight, orthographicCamera);
        viewport3D = new FitViewport(virtualWidth, virtualHeight, perspectiveCamera);

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
//        spriteBatch.setShader(shader.program);
        spriteBatch.begin();

        for(int i = 0; i < 700; i++)
        {
            spriteBatch.draw(region3, x, i);
        }

        spriteBatch.end();
    }
}
