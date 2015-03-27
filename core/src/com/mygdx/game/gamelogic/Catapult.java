package com.mygdx.game.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.*;
import com.engine.GameTime;
import com.engine.events.EventsArgs;
import com.engine.events.IEventHandler;
import com.engine.graphics.graphics2D.animation.skeletal.AnimationPlayer;
import com.engine.graphics.graphics2D.animation.skeletal.Bone;
import com.engine.graphics.graphics2D.animation.skeletal.FrameInterpolation.FixedFrameInterpolator;
import com.mygdx.game.Common;

public final class Catapult
{
    // Constantes para lanzamiento de bola
    public static final float LAUNCH_ANGLE = 45f;
    private static final float MIN_PULL_ANGLE = 137f;
    private static final float MAX_PULL_ANGLE = 179f;
    private static final float MIN_LAUNCH_POWER = 450f;
    private static final float MAX_LAUNCH_POWER = 1600f;
    private static final float MAX_PULL_DISTANCE = 200f;

    // Constantes para el dibujado de la cuerda
    private static final float ROPE_PIVOT_X = 1f;
    private static final float ROPE_PIVOT_Y = 6f;

    private static final String ANIMATION_DEFAULT = "default";
    private static final String ANIMATION_PULL    = "pull";
    private static final String ANIMATION_LAUNCH  = "launch";
    private static final String ANIMATION_RECOVER = "recover";

    private Common common;
    private Ball ball;
    private BallPath ballPath;
    private OrthographicCamera camera;

    private Bone spoon;
    private Bone handleGear;
    private Bone ropeHolder;

    // Variables para el dibujado de la cuerda
    private float ropeX;
    private float ropeY;
    private float ropeRotation;
    private float ropeLength;

    // Variables para lanzamiento de bola
    private float pullAngle = 0f;
    private FixedFrameInterpolator pullAnimationInterpolator;

    // Assets
    private AnimationPlayer player;
    private TextureAtlas.AtlasRegion ropeRegion;

    // Input
    private boolean inputEnabled = false;
    private boolean pulling = false;
    private boolean onRelease = false;
    private Vector3 originTouchPosition = new Vector3();
    private Vector3 currentTouchPosition = new Vector3();
    private Rectangle forbidenBounds = new Rectangle(754f, 0f, 100f, 100f);

    public Catapult(Common common, Ball ball, BallPath ballPath, OrthographicCamera camera)
    {
        this.common = common;
        this.ball = ball;
        this.ballPath = ballPath;
        this.camera = camera;

        this.ropeRegion = this.common.assets.atlasRegions.rope.getInstance();
        this.pullAnimationInterpolator = new FixedFrameInterpolator();

        initializeAnimation();
        getBones();
    }

    private void initializeAnimation()
    {
        player = common.assets.animations.catapult.getInstance();
        player.position.x = 135f;
        player.position.y = 397f;
        player.rotation = 2f;
        player.play(ANIMATION_DEFAULT);

        player.getAnimation(ANIMATION_PULL).setFrameInterpolator(pullAnimationInterpolator);

        player.getAnimation(ANIMATION_LAUNCH).onAnimationEnd.subscribe(new IEventHandler<EventsArgs>()
        {
            @Override
            public void onAction(EventsArgs args)
            {
                Catapult.this.launch();
                Catapult.this.player.play(Catapult.ANIMATION_RECOVER);
            }
        });
    }

    private void getBones()
    {
        spoon = player.getBone(1);
        handleGear = player.getBone(3);
        ropeHolder = player.getBone(0);
    }

    public void enableInput()
    {
        inputEnabled = true;
    }

    public void disableInput()
    {
        inputEnabled = false;
    }

    public boolean isPulling()
    {
        return pulling;
    }

    public void reset()
    {
        player.play(ANIMATION_DEFAULT);
        pullAnimationInterpolator.factor = 0f;
        pulling = false;
        onRelease = false;
    }

    private void launch()
    {
        float power = getCurrentPower(pullAngle);

        ball.launch(power, LAUNCH_ANGLE);
        common.soundPlayer.playShoot();
    }

    private float getCurrentPower(float angle)
    {
        float diff = MAX_PULL_ANGLE - MIN_PULL_ANGLE;
        float factor = (angle - MIN_PULL_ANGLE) / diff;
        return Interpolation.linear.apply(MIN_LAUNCH_POWER,
                MAX_LAUNCH_POWER, factor);
    }

    public void update(GameTime gameTime)
    {
        handleInput();
        player.update(gameTime);

        setBallPosition();
        updateBallPath();
        calculateRopePosition();
    }

    private void handleInput()
    {
        if (inputEnabled)
        {
            if (ball.isFlying())
            {
                onRelease = false;
            }
            else
            {
                if (Gdx.input.isTouched(0))
                {
                    if (!onRelease)
                    {
                        if (!pulling)
                        {
                            originTouchPosition.set(Gdx.input.getX(0), Gdx.input.getY(0), 0f);
                            camera.unproject(originTouchPosition);

                            if (!forbidenBounds.contains(originTouchPosition.x, originTouchPosition.y))
                            {
                                pulling = true;
                            }
                        }
                        else
                        {
                            float x = Gdx.input.getX(0);
                            float y = Gdx.input.getY(0);

                            currentTouchPosition.set(x, y, 0f);
                            camera.unproject(currentTouchPosition);

                            float deltaX = currentTouchPosition.x - originTouchPosition.x;
                            float deltaY = currentTouchPosition.y - originTouchPosition.y;
                            float length = Vector2.len(deltaX, deltaY);

                            player.play(Catapult.ANIMATION_PULL);
                            pullAnimationInterpolator.factor = MathUtils.clamp(length / MAX_PULL_DISTANCE, 0f, 1f);
                            ballPath.show();
                        }
                    }
                }
                else
                {
                    if (pulling)
                    {
                        pullAngle = spoon.getFinalRotation();
                        player.play(Catapult.ANIMATION_LAUNCH);
                        pullAnimationInterpolator.factor = 0f;
                        ballPath.hide();

                        pulling = false;
                        onRelease = true;
                    }
                }
            }
        }
    }

    private void setBallPosition()
    {
        if (!ball.isFlying())
        {
            ball.setPosition(spoon.getTransformedPosition(104f, 30f));
        }
    }

    private void updateBallPath()
    {
        if (pulling)
        {
            ballPath.setPower(getCurrentPower(spoon.getFinalRotation()));
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

    public void draw(Batch batch)
    {
        drawRope(batch);
        player.draw(batch);
    }

    private void drawRope(Batch batch)
    {
        batch.draw(ropeRegion, ropeX, ropeY, ROPE_PIVOT_X, ROPE_PIVOT_Y,
                ropeRegion.getRegionWidth(), ropeRegion.getRegionHeight(),
                ropeLength, 1f, ropeRotation);
    }
}
