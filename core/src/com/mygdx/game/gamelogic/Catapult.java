package com.mygdx.game.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.engine.GameTime;
import com.engine.Interpolation.Interpolators;
import com.engine.events.EventsArgs;
import com.engine.events.IEventHandler;
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

    // Variables para lanzamiento de bola
    private float pullAngle;
    private static final float MIN_PULL_ANGLE = 137f;
    private static final float MAX_PULL_ANGLE = 179f;
    private static final float MIN_LAUNCH_POWER = 150f;
    private static final float MAX_LAUNCH_POWER = 1500f;
    private static final float LAUNCH_ANGLE = 45f;

    private boolean holding = false;

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
        player.position.x = 135f;
        player.position.y = 397f;
        player.rotation = 2f;
        player.play("default");
//        player.speed = 0.25f;

        Gdx.input.setInputProcessor(new InputAdapter()
        {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button)
            {
                player.play("pull");
                holding = true;

                return super.touchDown(screenX, screenY, pointer, button);
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button)
            {
                pullAngle = spoon.getFinalRotation();
                player.play("launch");
                holding = false;

                return super.touchUp(screenX, screenY, pointer, button);
            }
        });

        player.getAnimation("launch").onAnimationEnd.subscribe(new IEventHandler<EventsArgs>()
        {
            @Override
            public void onAction(EventsArgs args)
            {
                launch();
                player.play("recover");
            }
        });
    }

    private void launch()
    {
        float diff = MAX_PULL_ANGLE - MIN_PULL_ANGLE;
        float factor = (pullAngle - MIN_PULL_ANGLE) / diff;
        float power = Interpolators.LinearInterpolator.interpolate(MIN_LAUNCH_POWER,
                MAX_LAUNCH_POWER, factor);

        ball.launch(power, LAUNCH_ANGLE);
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
//        if (!ball.isFlying())
        if (holding)
        {
            ball.setPosition(spoon.getTransformedPosition(98f, 25f));
        }
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
