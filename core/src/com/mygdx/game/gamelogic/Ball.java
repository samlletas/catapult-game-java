package com.mygdx.game.gamelogic;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.engine.GameTime;
import com.engine.camera.CameraShaker2D;
import com.mygdx.game.assets.GameAssets;

public final class Ball
{
    public float x;
    public float y;

    private final TextureAtlas.AtlasRegion region;
    private boolean flying;
    private float speedX;
    private float speedY;

    private CameraShaker2D cameraShaker;
    private ParticleEffect ballTrace;
    private ParticleEffect ballExplosion;

    private static float GRAVITY = 2500f;

    public boolean isFlying()
    {
        return flying;
    }

    public Ball(CameraShaker2D cameraShaker)
    {
        this.x = 0f;
        this.y = 0f;

        this.region = GameAssets.AtlasRegions.ball.instance;
        this.flying = false;

        this.cameraShaker = cameraShaker;
        this.ballTrace = GameAssets.Particles.ballTrace.instance;
        this.ballExplosion = GameAssets.Particles.ballExplosion.instance;
    }

    public void launch(float power, float angle)
    {
        speedX = power * MathUtils.cosDeg(angle);
        speedY = -(power * MathUtils.sinDeg(angle));

        flying = true;

        ballTrace.reset();
    }

    private void cancelFlight()
    {
        flying = false;

        speedX = 0f;
        speedY = 0f;
    }

    private void explode()
    {
        ballExplosion.setPosition(x - 34, y);
        ballExplosion.reset();
    }

    public void update(GameTime gameTime)
    {
        if (flying)
        {
            x += speedX * gameTime.delta;
            y += speedY * gameTime.delta;

            speedY += GRAVITY * gameTime.delta;

            ballTrace.setPosition(x + 10, y + 14);

            if (x >= 854 || y >= 480)
            {
                explode();
                cancelFlight();

                cameraShaker.shake(2);
            }
        }

        ballTrace.update(gameTime.delta);
        ballExplosion.update(gameTime.delta);
    }

    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(region, x, y);
        ballTrace.draw(spriteBatch);
        ballExplosion.draw(spriteBatch);
    }
}
