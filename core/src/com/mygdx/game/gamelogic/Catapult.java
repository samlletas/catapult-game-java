package com.mygdx.game.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
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

    private Bone spoon;
    private Bone handleGear;
    private Bone ropeHolder;

    // Variables para el dibujado de la cuerda
    private float ropeX;
    private float ropeY;
    private float ropeRotation;
    private float ropeLength;

    // Variables para lanzamiento de bola
    private boolean pulling = false;
    private float pullAngle = 0f;
    private FixedFrameInterpolator pullAnimationInterpolator;

    // Assets
    private AnimationPlayer player;
    private TextureAtlas.AtlasRegion ropeRegion;

    // Input
    private boolean inputEnabled = false;
    private InputProcessor inputProcessor;

    public Catapult(Common common, Ball ball, BallPath ballPath, OrthographicCamera camera)
    {
        this.common = common;
        this.ball = ball;
        this.ballPath = ballPath;

        this.ropeRegion = this.common.assets.atlasRegions.rope.getInstance();
        this.pullAnimationInterpolator = new FixedFrameInterpolator();
        this.inputProcessor = new GestureDetector(new CatapultGestureListener(camera));

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

    public InputProcessor getInputProcessor()
    {
        return inputProcessor;
    }

    public void reset()
    {
        player.play(ANIMATION_DEFAULT);
        pullAnimationInterpolator.factor = 0f;
    }

    private void BeginLaunch()
    {
        if (inputEnabled && pulling)
        {
            pullAngle = spoon.getFinalRotation();
            player.play(Catapult.ANIMATION_LAUNCH);
            pullAnimationInterpolator.factor = 0f;
            ballPath.hide();

            pulling = false;
        }
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
        player.update(gameTime);

        setBallPosition();
        updateBallPath();
        calculateRopePosition();
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

    /***************************************************************************
     *                                 Input
     * ************************************************************************/
    public class CatapultGestureListener extends GestureDetector.GestureAdapter
    {
        private static final float MIN_ADD_FORCE_ANGLE = 135f;
        private static final float MAX_ADD_FORCE_ANGLE = 315f;
        private static final float FACTOR_INCREMENT = 0.0085f;

        private OrthographicCamera camera;
        private Vector3 previousTouchPosition;
        private Vector3 currentTouchPosition;

        CatapultGestureListener(OrthographicCamera camera)
        {
            this.camera = camera;
            this.previousTouchPosition = new Vector3();
            this.currentTouchPosition = new Vector3();
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY)
        {
            if (Catapult.this.inputEnabled && !Catapult.this.ball.isFlying())
            {
                previousTouchPosition.set(x - deltaX, y - deltaY, 0f);
                currentTouchPosition.set(x, y, 0f);

                camera.unproject(previousTouchPosition);
                camera.unproject(currentTouchPosition);

                deltaX = currentTouchPosition.x - previousTouchPosition.x;
                deltaY = currentTouchPosition.y - previousTouchPosition.y;

                float length = Vector2.len(deltaX, deltaY);
                float angle = normalizeAngle(MathUtils.atan2(-deltaY, deltaX));
                float factor = 0f;

                if (onAddForceAngle(angle))
                {
                    factor += length * FACTOR_INCREMENT;
                    Gdx.app.log("Pan", "length: " + length);
                }
                else
                {
                    factor -= length * FACTOR_INCREMENT;
                    length = -length;
                    Gdx.app.log("Pan", "length: " + -length);
                }

                float duration = getCurrentTime();
                acummmulatedDistance += length;

                if (Math.abs(acummmulatedDistance) < POSSIBLE_SHOT_MAX_DISTANCE)
                {
                    if (onPossibleShot)
                    {
                        acummulatedTime += duration;
                    }

                    onPossibleShot = true;
                }
                else
                {
                    onPossibleShot = false;
                    acummulatedFactor = 0f;
                    acummulatedTime = 0f;
                    acummmulatedDistance = 0f;
                }

                if (onPossibleShot)
                {
                    acummulatedFactor += factor;

                    if (acummulatedTime > POSSIBLE_SHOT_WAIT_TIME)
                    {
                        factor = acummulatedFactor;
                        Catapult.this.pullAnimationInterpolator.factor = MathUtils.clamp(Catapult.this.pullAnimationInterpolator.factor + factor, 0f, 1f);

                        onPossibleShot = false;
                        acummulatedFactor = 0f;
                        acummulatedTime = 0f;
                        acummmulatedDistance = 0f;
                    }
                }
                else
                {
                    Catapult.this.player.play(Catapult.ANIMATION_PULL);
                    Catapult.this.pullAnimationInterpolator.factor = MathUtils.clamp(Catapult.this.pullAnimationInterpolator.factor + factor, 0f, 1f);
                    Catapult.this.ballPath.show();
                    Catapult.this.pulling = true;
                }

                Gdx.app.log("Time", "Time: " + duration);

                onPan = true;
            }

            return true;
        }

//        final static long POSSIBLE_SHOT_WAIT_TIME = 100l;
//        final static float POSSIBLE_SHOT_MAX_DISTANCE = 2f;
        final static long POSSIBLE_SHOT_WAIT_TIME = 1000l;
        final static float POSSIBLE_SHOT_MAX_DISTANCE = 3f;

        boolean onPossibleShot = false;
        float acummulatedFactor = 0f;
        float acummulatedTime = 0f;
        float acummmulatedDistance = 0f;

        long prevTime = 0l;
        boolean onPan = false;

        long getCurrentTime()
        {
            long result = 0l;
            long current = TimeUtils.millis();

            if (onPan)
            {
                result = current - prevTime;
            }

            prevTime = current;

            return result;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button)
        {
            if (Catapult.this.pulling)
            {
                BeginLaunch();

                onPan = false;
                onPossibleShot = false;
                acummulatedFactor = 0f;
                acummulatedTime = 0f;
                acummmulatedDistance = 0f;
            }

            return true;
        }

        /**
         * Convierte un ángulo en radianes a grados y lo normaliza para que
         * se encuentre dentro del rango (0, 360)
         * @param angle ángulo en radianes
         * @return Angulo en grados
         */
        private float normalizeAngle(float angle)
        {
            angle *= MathUtils.radiansToDegrees;

            if (angle < 0f)
            {
                angle = 360f + angle;
            }

            return angle;
        }

        private boolean onAddForceAngle(float angle)
        {
            return angle >= MIN_ADD_FORCE_ANGLE && angle <= MAX_ADD_FORCE_ANGLE;
        }
    }
}
