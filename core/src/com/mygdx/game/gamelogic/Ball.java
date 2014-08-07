package com.mygdx.game.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.engine.GameSettings;
import com.engine.GameTime;
import com.engine.camera.CameraShaker2D;
import com.engine.collision2d.GamePolygon;
import com.mygdx.game.assets.GameAssets;

public final class Ball
{
    private final TextureAtlas.AtlasRegion region;
    private boolean flying;

    private float speedX;
    private float speedY;

    private GameSettings gameSettings;
    private CameraShaker2D cameraShaker;
    private Grass grass;

    private GamePolygon polygon;

    private ParticleEffect ballTrace;
    private ParticleEffect ballExplosion;

    private static float GRAVITY = 2500f;

    public boolean isFlying()
    {
        return flying;
    }

    public Ball(GameSettings gameSettings, CameraShaker2D cameraShaker, Grass grass)
    {
        this.region = GameAssets.AtlasRegions.ball.instance;
        this.flying = false;

        this.gameSettings = gameSettings;
        this.cameraShaker = cameraShaker;
        this.grass = grass;

        this.polygon = GamePolygon.createConvex(6, 14);
        this.polygon.setPosition(0f, 0f);

        this.ballTrace = GameAssets.Particles.ballTrace.instance;
        this.ballExplosion = GameAssets.Particles.ballExplosion.instance;
    }

    public void setPosition(float x, float y)
    {
        polygon.setPosition(x, y);
    }

    public void setPosition(Vector2 position)
    {
        polygon.setPosition(position.x, position.y);
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
        ballExplosion.setPosition(polygon.getX() - region.getRegionWidth() / 2,
                polygon.getY() - region.getRegionHeight() / 2);
        ballExplosion.reset();
    }

    public void update(GameTime gameTime)
    {
        if (flying)
        {
            polygon.translate(speedX * gameTime.delta, speedY * gameTime.delta);

            speedY += GRAVITY * gameTime.delta;

//            launchTime = TimeUtils.timeSinceMillis(startTime) / 1000f;
//
//            x = x0 + (speedX * launchTime);
//            y = y0 + (speedY * launchTime) + (0.5f * GRAVITY * launchTime * launchTime);

            ballTrace.setPosition(polygon.getX(), polygon.getY());

            if (isOutsideBounds() || onCollisionWithGrass())
            {
                logMaxY();

                explode();
                cancelFlight();

                cameraShaker.shake(2);
            }
        }

        ballTrace.update(gameTime.delta);
        ballExplosion.update(gameTime.delta);
    }

    private boolean isOutsideBounds()
    {
        return (polygon.getX() < 0) ||  (polygon.getY() < 0) ||
                (polygon.getX() + region.getRegionWidth() > gameSettings.virtualWidth) ||
                (polygon.getY() + region.getRegionHeight() > gameSettings.virtualHeight);
    }

    private boolean onCollisionWithGrass()
    {
        return grass.onCollision(polygon);
    }

    private void logMaxY()
    {
        Gdx.app.log("", "CollisionY: " + polygon.getY());
    }

    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(region, polygon.getX() - region.getRegionWidth() / 2,
                polygon.getY() - region.getRegionHeight() / 2);
        ballTrace.draw(spriteBatch);
        ballExplosion.draw(spriteBatch);
        polygon.draw(spriteBatch, Color.CYAN);
    }
}
