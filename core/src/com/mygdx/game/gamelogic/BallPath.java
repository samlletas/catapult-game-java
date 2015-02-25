package com.mygdx.game.gamelogic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.engine.GameTime;
import com.engine.actors.ActorOrigin;
import com.engine.actors.TextureRegionActor;
import com.engine.collision2d.IPhysicsObject;
import com.engine.graphics.GraphicsSettings;
import com.engine.utilities.ColorUtilities;
import com.engine.utilities.FastArray;
import com.mygdx.game.Common;

public class BallPath implements IPhysicsObject
{
    private static final int MAX_DOTS = 30;
    private static final float MAX_TIME = 0.85f;
    private static final float TIME_OFFSET_SPEED = 0.075f;

    private static final float MAX_Y = 400f;
    private static final float MIN_SCALE = 0.8f;
    private static final float MAX_SCALE = 1.8f;

    private final GraphicsSettings graphicsSettings;
    private final FastArray<TextureRegionActor> redDots;
    private final float dotTimeOffset;

    private final float startX;
    private final float startY;

    private boolean isActive;
    private boolean hiding;
    private float power;
    private float timeOffset;

    private Color fadeOutColor;
    private AlphaAction fadeOutAction;

    public BallPath(Common common, GraphicsSettings graphicsSettings)
    {
        this.graphicsSettings = graphicsSettings;
        this.redDots = new FastArray<TextureRegionActor>(MAX_DOTS);
        this.dotTimeOffset = MAX_TIME / MAX_DOTS;

        this.startX = Ball.LAUNCH_X - 5f;
        this.startY = Ball.LAUNCH_Y + 8f;

        for (int i = 0; i < MAX_DOTS; i++)
        {
            TextureRegionActor actor = new TextureRegionActor(common.assets.atlasRegions.redDot.getInstance());
            actor.setActorOrigin(ActorOrigin.Center);
            redDots.add(actor);
        }

        this.fadeOutColor = ColorUtilities.createColor(255, 255, 255, 255);
        this.fadeOutAction = new AlphaAction();
    }

    public void reset()
    {
        isActive = false;
        hiding = false;
        timeOffset = 0f;

        for (TextureRegionActor dot : redDots)
        {
            dot.clearActions();
            dot.getColor().a = 1f;
            dot.setScale(MAX_SCALE);
            dot.setPosition(-50f, -50f);
        }
    }

    public void show()
    {
        if (!isActive)
        {
            reset();
            isActive = true;
            fadeOutColor.a = 1f;
        }
    }

    public void hide()
    {
        if (isActive)
        {
            hiding = true;

            fadeOutAction.reset();
            fadeOutAction.setColor(fadeOutColor);
            fadeOutAction.setAlpha(0f);
            fadeOutAction.setDuration(0.3f);
            fadeOutAction.setInterpolation(Interpolation.pow4Out);
        }
    }

    public void setPower(float power)
    {
        this.power = power;
    }

    public void update(GameTime gameTime)
    {
        if (isActive)
        {
            if (hiding && fadeOutAction.act(gameTime.delta))
            {
                isActive = false;
                hiding = false;
            }
        }
    }

    @Override
    public void step(float elapsed, float delta)
    {
        if (isActive)
        {
            timeOffset += TIME_OFFSET_SPEED * delta;

            if (timeOffset >= dotTimeOffset)
            {
                timeOffset -= dotTimeOffset;
            }

            float speedX = power * MathUtils.cosDeg(Catapult.LAUNCH_ANGLE);
            float speedY = -(power * MathUtils.sinDeg(Catapult.LAUNCH_ANGLE));

            int i = 0;
            float time;
            float factor;

            for (TextureRegionActor dot : redDots)
            {
                time = i * dotTimeOffset;
                factor = time / MAX_TIME;
                time += timeOffset;

                if (!hiding)
                {
                    dot.setScale(MathUtils.lerp(MAX_SCALE, MIN_SCALE, factor));
                }

                dot.setX(startX + (speedX * time));
                dot.setY(startY + (speedY * time) + (Ball.GRAVITY * 0.5f * (time * time)));
                i++;
            }
        }
    }

    public void draw(Batch batch)
    {
        if (isActive)
        {
            ColorUtilities.setColor(batch, fadeOutColor);

            for (TextureRegionActor dot : redDots)
            {
                if (!outsideBounds(dot))
                {
                    dot.draw(batch, 1f);
                }
                else
                {
                    break;
                }
            }

            ColorUtilities.resetColor(batch);
        }
    }

    private boolean outsideBounds(TextureRegionActor dot)
    {
        float x = dot.getX();
        float y = dot.getY();
        float halfWidth = (dot.getWidth() * dot.getScaleX()) /  2f;
        float halhHeight = (dot.getHeight() * dot.getScaleY()) /  2f;

        float left = x - halfWidth;
        float right = x +halfWidth;
        float top = y - halhHeight;
        float bottom = y + halhHeight;

        return right <= 0f || left >= graphicsSettings.virtualWidth
                || bottom <= 0f || top >= MAX_Y;
    }
}