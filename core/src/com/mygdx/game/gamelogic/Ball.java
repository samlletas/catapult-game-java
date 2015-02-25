package com.mygdx.game.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.engine.collision2d.IPhysicsObject;
import com.engine.graphics.GraphicsSettings;
import com.engine.GameTime;
import com.engine.camera.CameraShaker2D;
import com.engine.collision2d.GamePolygon;
import com.engine.graphics.graphics2D.effects.TrailEffect;
import com.engine.utilities.ParticleUtilities;
import com.mygdx.game.Common;
import com.mygdx.game.gamelogic.scene.Grass;
import com.mygdx.game.shaders.TrailShader;

public final class Ball implements IPhysicsObject
{
    public static final float GRAVITY = 2500f;
    public static final float LAUNCH_X = 113f;
    public static final float LAUNCH_Y = 272f;

    private Common common;
    private GraphicsSettings graphicsSettings;

    public GamePolygon polygon;
    private Vector2 speed;
    private boolean flying;

    private TrailShader trailShader;
    private TrailEffect ballTrail;

    // Assets
    private TextureAtlas.AtlasRegion region;
    private ParticleEffect ballExplosion;

    public Ball(Common common, GraphicsSettings graphicsSettings)
    {
        this.common = common;
        this.graphicsSettings = graphicsSettings;

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
        polygon.setPosition(LAUNCH_X, LAUNCH_Y);
        ballTrail.reset(polygon.getX(), polygon.getY());

        speed.x = power * MathUtils.cosDeg(angle);
        speed.y = -(power * MathUtils.sinDeg(angle));

        flying = true;
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
        ballExplosion.update(gameTime.delta);
        ballTrail.update(gameTime);
    }

    @Override
    public void step(float elapsed, float delta)
    {
        if (flying)
        {
            speed.y += GRAVITY * delta;

            polygon.speed.x = speed.x * delta;
            polygon.speed.y = speed.y * delta;
        }
    }

    public void checkCollisions(Grass grass)
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
                logMaxY();
                explode();
            }
        }
    }

    private boolean isOutsideBounds()
    {
        return (polygon.getX() < 0) ||  (polygon.getY() < 0) ||
                (polygon.getX() + (region.getRegionWidth() / 2f) > graphicsSettings.virtualWidth) ||
                (polygon.getY() + (region.getRegionHeight() / 2f) > graphicsSettings.virtualHeight);
    }

    private void logMaxY()
    {
//        Gdx.app.log("CollisionY", "CollisionY: " + polygon.getY());
    }

    public void setTrailForeColor(Color color)
    {
        trailShader.setForegroundColor(color);
    }

    public void drawTrail(OrthographicCamera orthographicCamera)
    {
        ballTrail.draw(orthographicCamera, false);
    }

    public void drawTextures(Batch batch)
    {
        batch.draw(region, polygon.getX() - region.getRegionWidth() / 2,
                polygon.getY() - region.getRegionHeight() / 2);

        ballExplosion.draw(batch);
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