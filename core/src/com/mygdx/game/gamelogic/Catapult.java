package com.mygdx.game.gamelogic;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.engine.GameTime;
import com.engine.events.EventsArgs;
import com.engine.events.IEventHandler;
import com.engine.graphics.graphics2D.animation.skeletal.AnimationPlayer;
import com.engine.graphics.graphics2D.animation.skeletal.Bone;
import com.mygdx.game.Common;

public final class Catapult
{
    // Constantes para lanzamiento de bola
    private static final float MIN_PULL_ANGLE = 137f;
    private static final float MAX_PULL_ANGLE = 179f;
    private static final float MIN_LAUNCH_POWER = 150f;
    private static final float MAX_LAUNCH_POWER = 1600f;
    private static final float LAUNCH_ANGLE = 45f;

    // Constantes para el dibujado de la cuerda
    private static final float ROPE_PIVOT_X = 1f;
    private static final float ROPE_PIVOT_Y = 6f;

    private static final String ANIMATION_DEFAULT = "default";
    private static final String ANIMATION_PULL    = "pull";
    private static final String ANIMATION_LAUNCH  = "launch";
    private static final String ANIMATION_RECOVER = "recover";

    private Common common;
    private Ball ball;
    private Bone spoon;
    private Bone handleGear;
    private Bone ropeHolder;

    // Variables para el dibujado de la cuerda
    private float ropeX;
    private float ropeY;
    private float ropeRotation;
    private float ropeLength;

    // Variables para lanzamiento de bola
    private boolean inputEnabled = false;
    private boolean pulling = false;
    private float pullAngle;

    // Assets
    private AnimationPlayer player;
    private TextureAtlas.AtlasRegion ropeRegion;

    // Input
    private CatapultInputProcessor inputProcessor;

    public Catapult(Common common, Ball ball)
    {
        this.common = common;
        this.ball = ball;
        this.ropeRegion = this.common.assets.atlasRegions.rope.getInstance();
        this.inputProcessor = new CatapultInputProcessor();

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

    public CatapultInputProcessor getInputProcessor()
    {
        return inputProcessor;
    }

    public void reset()
    {
        player.play(ANIMATION_DEFAULT);
    }

    private void launch()
    {
        float diff = MAX_PULL_ANGLE - MIN_PULL_ANGLE;
        float factor = (pullAngle - MIN_PULL_ANGLE) / diff;
        float power = Interpolation.linear.apply(MIN_LAUNCH_POWER,
                MAX_LAUNCH_POWER, factor);

        ball.launch(power, LAUNCH_ANGLE);
        common.soundPlayer.playShoot();
    }

    public void update(GameTime gameTime)
    {
        player.update(gameTime);

        setBallPosition();
        calculateRopePosition();
    }

    private void setBallPosition()
    {
        if (!ball.isFlying())
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

    public class CatapultInputProcessor extends InputAdapter
    {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button)
        {
            if (Catapult.this.inputEnabled && !Catapult.this.ball.isFlying())
            {
                Catapult.this.player.play(Catapult.ANIMATION_PULL);
                Catapult.this.pulling = true;
            }

            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button)
        {
            if (Catapult.this.inputEnabled && Catapult.this.pulling)
            {
                Catapult.this.pullAngle = Catapult.this.spoon.getFinalRotation();
                Catapult.this.player.play(Catapult.ANIMATION_LAUNCH);
            }

            pulling = false;

            return false;
        }
    }
}
