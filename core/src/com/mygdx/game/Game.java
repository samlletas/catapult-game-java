package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
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

        player = new AnimationPlayer();
        initializeAnimation();

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

    void initializeAnimation()
    {
        // Bones
        Bone b_body = new Bone(Assets.TextureAtlases.pack.value, "body", 0, 102f, 97f, 0f, 0f);
        Bone b_spoon = new Bone(Assets.TextureAtlases.pack.value, "spoon", 1, 21f, 28f, 126f, 25f);
        Bone b_spoon2 = new Bone(Assets.TextureAtlases.pack.value, "spoon", 2, 21f, 28f, 110f, 26f);

        b_body.addChild(b_spoon);
        b_spoon.addChild(b_spoon2);

        player.addBone(b_body);
        player.addBone(b_spoon);
        player.addBone(b_spoon2);

        // Frames
        Frame frame1 = new Frame(2000, FrameInterpolations.Sine);
        frame1.addFrameData(new FrameData(0, 0f, 0f, 0f, 1f, 1f));
        frame1.addFrameData(new FrameData(1, 0f, -90f, 0f, 1f, 1f));
        frame1.addFrameData(new FrameData(2, 0f, 0f, 0f, 1f, 1f));

        Frame frame2 = new Frame(2000, FrameInterpolations.Sine);
        frame2.addFrameData(new FrameData(0, 0f, 0f, 300f, 1f, 1f));
        frame2.addFrameData(new FrameData(1, -180f, -90f, 50f, 1f, 1f));
        frame2.addFrameData(new FrameData(2, -90f, 0f, 50f, 1f, 1f));

        // Animations
        Animation animation = new Animation("default", 1.0f, true);
        animation.addFrame(frame1);
        animation.addFrame(frame2);

        player.addAnimation(animation);
        player.rotation = 90f;
        player.scale = 0.25f;
        player.position.x = 500f;
        player.position.y = 240f;
    }
}
