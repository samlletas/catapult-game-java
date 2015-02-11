package com.mygdx.game.gamelogic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.engine.graphics.GraphicsSettings;
import com.engine.GameTime;
import com.engine.camera.CameraShaker2D;
import com.engine.collision2d.GamePolygon;
import com.engine.graphics.graphics2D.effects.TrailEffect;
import com.engine.utilities.ParticleUtilities;
import com.mygdx.game.Common;
import com.mygdx.game.gamelogic.scene.Grass;
import com.mygdx.game.shaders.TrailShader;

public final class Ball
{
    private static final float GRAVITY = 2500f;

    private Common common;
    private GraphicsSettings graphicsSettings;
    private CameraShaker2D cameraShaker;

    public GamePolygon polygon;
    private Vector2 speed;
    private boolean flying;

    private TrailShader trailShader;
    private TrailEffect ballTrail;

    // Assets
    private TextureAtlas.AtlasRegion region;
    private ParticleEffect ballExplosion;

    public Ball(Common common, GraphicsSettings graphicsSettings,
                CameraShaker2D cameraShaker)
    {
        this.common = common;
        this.graphicsSettings = graphicsSettings;
        this.cameraShaker = cameraShaker;

        this.polygon = GamePolygon.createConvex(6, 14);
        this.polygon.setPosition(0f, 0f);
        this.speed = new Vector2();
        this.flying = false;

        this.trailShader = new TrailShader();
        this.ballTrail = new TrailEffect(trailShader, Color.CYAN, 5, 140f, 0f, 25f, 0f, 0.3f, 800f);

        this.region = common.assets.atlasRegions.ball.getInstance();
        this.ballExplosion = common.assets.particles.ballExplosion.getInstance();
        ParticleUtilities.initialize(ballExplosion);
    }

    public boolean isFlying()
    {
        return flying;
    }

    public void reset()
    {
        stopMovement();
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

    public void explode()
    {
        stopMovement();

        ballExplosion.setPosition(polygon.getX(), polygon.getY());
        ballExplosion.reset();
    }

    private void stopMovement()
    {
        flying = false;

        speed.x = 0f;
        speed.y = 0f;

        polygon.speed.x = 0f;
        polygon.speed.y = 0f;
    }

    public void update(GameTime gameTime)
    {
        if (flying)
        {
            speed.y += GRAVITY * gameTime.delta;

            polygon.speed.x = speed.x * gameTime.delta;
            polygon.speed.y = speed.y * gameTime.delta;
        }
    }

    public void checkCollisions(GameTime gameTime, Grass grass)
    {
        if (flying)
        {
            boolean collided = false;

            if (grass.onCollision(polygon))
            {
                collided = true;
                polygon.move();
            }
            else
            {
                polygon.move();

                if (isOutsideBounds())
                {
                    collided = true;
                    float onBoundsX = MathUtils.clamp(polygon.getX(), 0f, graphicsSettings.virtualWidth - (region.getRegionWidth() / 2f));
                    float onBoundsY = MathUtils.clamp(polygon.getY(), 0f, graphicsSettings.virtualHeight - (region.getRegionHeight() / 2f));

                    polygon.setPosition(onBoundsX, onBoundsY);
                }

            }

            ballTrail.setPosition(polygon.getX(), polygon.getY());

            if (collided)
            {
//                logMaxY();
                explode();
                cameraShaker.shake(2);
            }
        }

        ballTrail.update(gameTime);
        ballExplosion.update(gameTime.delta);
    }

    private boolean isOutsideBounds()
    {
        return (polygon.getX() < 0) ||  (polygon.getY() < 0) ||
                (polygon.getX() + (region.getRegionWidth() / 2f) > graphicsSettings.virtualWidth) ||
                (polygon.getY() + (region.getRegionHeight() / 2f) > graphicsSettings.virtualHeight);
    }

//    private void logMaxY()
//    {
//        Gdx.app.log("", "CollisionY: " + polygon.getY());
//    }

    public void setTrailForeColor(Color color)
    {
        trailShader.setForegroundColor(color);
    }

    public void drawTrail(OrthographicCamera orthographicCamera)
    {
        ballTrail.draw(orthographicCamera, false);
    }

    public void drawTextures(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(region, polygon.getX() - region.getRegionWidth() / 2,
                polygon.getY() - region.getRegionHeight() / 2);

        ballExplosion.draw(spriteBatch);
    }

    public void drawPolygon(ShapeRenderer shapeRenderer)
    {
        polygon.draw(shapeRenderer, Color.CYAN);
    }

    public void dispose()
    {
        ballTrail.dispose();
    }
}