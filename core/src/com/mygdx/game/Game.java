package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.engine.GameAdapter;
import com.engine.GameTime;
import com.engine.graphics.animation.*;
import com.engine.graphics.animation.events.IAnimationHandler;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.assets.GameAssetMaster;

public final class Game extends GameAdapter
{
    private GameAssetMaster assetMaster;
    private FPSLogger fpsLogger;
    private AnimationPlayer player;
    private int updates = 3000;
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

        player = Assets.Animations.catapult.value;

//        player.rotation = 270f;
//        player.scale = 0.5f;
        player.position.x = 500f;
        player.position.y = 240f;

        player.getAnimation("default").onEnd.subscribe(new IAnimationHandler()
        {
            @Override
            public void onEnd(Animation animation)
            {
                player.play("default");
            }
        });

        player.play("default");
    }

    @Override
    protected void update(GameTime gameTime)
    {
        fpsLogger.log();

        for(int i = 0; i < updates; i++)
        {
            player.update(gameTime);
        }
    }

    @Override
    protected void draw(GameTime gameTime)
    {
        spriteBatch.begin();

        for(int i = 0; i < draws; i++)
        {
            player.draw(spriteBatch);
        }

        spriteBatch.end();
    }
}
