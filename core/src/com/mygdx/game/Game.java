package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.engine.GameAdapter;
import com.engine.GameTime;
import com.engine.graphics.animation.*;
import com.engine.graphics.animation.events.IAnimationHandler;
import com.mygdx.game.assets.GameAssets;
import com.mygdx.game.assets.GameAssetMaster;

public final class Game extends GameAdapter
{
    private GameAssetMaster assetMaster;
    private FPSLogger fpsLogger;
    private AnimationPlayer player;
    private int updates = 1;
    private int draws = 1;

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
        assetMaster = new GameAssetMaster();
        fpsLogger = new FPSLogger();

        assetMaster.loadSync();

        player = GameAssets.Animations.catapult.instance;
        player.position.x = 160f;
        player.position.y = 394f;
        player.play("default");

        Gdx.input.setInputProcessor(new InputAdapter()
        {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button)
            {
                player.play("pull");
                return super.touchDown(screenX, screenY, pointer, button);
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button)
            {
                player.play("launch");
                return super.touchUp(screenX, screenY, pointer, button);
            }
        });
    }

    @Override
    protected void update(GameTime gameTime)
    {
        fpsLogger.log();

        for (int i = 0; i < updates; i++)
        {
            player.update(gameTime);
        }
    }

    @Override
    protected void draw(GameTime gameTime)
    {
        spriteBatch.begin();

        for (int i = 0; i < draws; i++)
        {
            player.draw(spriteBatch);
        }

        spriteBatch.end();
    }
}
