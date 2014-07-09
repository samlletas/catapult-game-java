package com.mygdx.game.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.engine.GameTime;
import com.engine.graphics.animation.AnimationPlayer;
import com.engine.graphics.animation.Bone;
import com.mygdx.game.assets.GameAssets;

public final class Catapult
{
    private Ball ball;
    private AnimationPlayer player;

    private Bone spoon;
    private Bone handleGear;
    private Bone ropeHolder;

    // Variables para el dibujado de la cuerda
    private TextureAtlas.AtlasRegion ropeRegion;
    private float ropeX;
    private float ropeY;
    private float ropeRotation;
    private float ropeLength;
    private static final float ROPE_PIVOT_X = 1f;
    private static final float ROPE_PIVOT_Y = 6f;

    public Catapult(Ball ball)
    {
        this.ball = ball;
        this.ropeRegion = GameAssets.AtlasRegions.rope.instance;

        initializeAnimation();
        getBones();
    }

    private void initializeAnimation()
    {
        player = GameAssets.Animations.catapult.instance;
        player.position.x = 160f;
        player.position.y = 395f;
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

    private void getBones()
    {
        spoon = player.getBone(1);
        handleGear = player.getBone(3);
        ropeHolder = player.getBone(0);
    }

    public void update(GameTime gameTime)
    {
        player.update(gameTime);

        setBallPosition();
        calculateRopePosition();
    }

    private void setBallPosition()
    {
        Vector2 position = spoon.getTransformedPosition(98f, 25f);

        ball.x = position.x - 17;
        ball.y = position.y - 17;
    }

    private void calculateRopePosition()
    {
        Vector2 handleGearPosition = handleGear.getTransformedPosition(20f, 20f);
        Vector2 ropeHolderPosition = ropeHolder.getTransformedPosition(14f, 12f);

        ropeX = handleGearPosition.x;
        ropeY = handleGearPosition.y;

        float dx = -(ropeHolderPosition.x - handleGearPosition.x);
        float dy = -(ropeHolderPosition.y - handleGearPosition.y);

        ropeRotation = (float)Math.toDegrees(MathUtils.atan2(dy, dx));
        ropeLength = (float)Math.sqrt((dx * dx) + (dy * dy));
    }

    public void draw(SpriteBatch spriteBatch)
    {
        drawRope(spriteBatch);
        player.draw(spriteBatch);
    }

    private void drawRope(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(ropeRegion, ropeX, ropeY, ROPE_PIVOT_X, ROPE_PIVOT_Y,
                ropeRegion.getRegionWidth(), ropeRegion.getRegionHeight(),
                ropeLength, 1f, ropeRotation);
    }
}
