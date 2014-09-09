package com.mygdx.game.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.engine.GameSettings;
import com.engine.GameTime;
import com.engine.camera.CameraShaker2D;
import com.engine.collision2d.GamePolygon;
import com.engine.graphics.effects.TrailEffect;
import com.mygdx.game.assets.GameAssets;

public final class Ball
{
    private final TextureAtlas.AtlasRegion region;
    private boolean flying;

    private GameSettings gameSettings;
    private CameraShaker2D cameraShaker;
    private Grass grass;

    private GamePolygon polygon;
    private Vector2 speed;

    private TrailEffect ballTrail;
    private ParticleEffect ballExplosion;

    private OrthographicCamera camera;

    private static float GRAVITY = 2500f;

    public boolean isFlying()
    {
        return flying;
    }

    public Ball(GameSettings gameSettings, CameraShaker2D cameraShaker,
                Grass grass, OrthographicCamera camera)
    {
        this.region = GameAssets.AtlasRegions.ball.instance;
        this.flying = false;

        this.gameSettings = gameSettings;
        this.cameraShaker = cameraShaker;
        this.grass = grass;

        this.polygon = GamePolygon.createConvex(6, 14);
        this.polygon.setPosition(0f, 0f);
        this.speed = new Vector2();

        this.ballTrail = new TrailEffect(Color.CYAN, 5, 200f, 0f, 25f, 0f, 0.5f, 800f);
        this.ballExplosion = GameAssets.Particles.ballExplosion.instance;

        this.camera = camera;
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
        speed.x = power * MathUtils.cosDeg(angle);
        speed.y = -(power * MathUtils.sinDeg(angle));

        flying = true;

        ballTrail.reset(polygon.getX(), polygon.getY());
    }

    private void cancelFlight()
    {
        flying = false;

        speed.x = 0f;
        speed.y = 0f;

        polygon.speed.x = 0f;
        polygon.speed.y = 0f;
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
            speed.y += GRAVITY * gameTime.delta;

            polygon.speed.x = speed.x * gameTime.delta;
            polygon.speed.y = speed.y * gameTime.delta;

            boolean collided = onCollisionWithGrass() || isOutsideBounds();

            polygon.move();
            ballTrail.setPosition(polygon.getX(), polygon.getY());

            if (collided)
            {
                logMaxY();
                explode();
                cancelFlight();
                cameraShaker.shake(2);
            }
        }

        ballTrail.update(gameTime);
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
        ballTrail.draw(camera);

        spriteBatch.begin();
        spriteBatch.draw(region, polygon.getX() - region.getRegionWidth() / 2,
                polygon.getY() - region.getRegionHeight() / 2);
        ballExplosion.draw(spriteBatch);
        polygon.draw(spriteBatch, Color.CYAN);
        spriteBatch.end();
    }
}