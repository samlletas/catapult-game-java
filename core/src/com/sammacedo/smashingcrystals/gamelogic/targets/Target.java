package com.sammacedo.smashingcrystals.gamelogic.targets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.engine.GameTime;
import com.engine.collision2d.GamePolygon;
import com.engine.collision2d.IPhysicsObject;
import com.engine.graphics.graphics2D.animation.basic.BasicAnimation;
import com.engine.graphics.graphics2D.animation.basic.BasicFrame;
import com.engine.utilities.ColorUtilities;
import com.engine.utilities.FastArray;
import com.engine.utilities.ParticleUtilities;

public class Target implements IPhysicsObject
{
    private static final float FLOATING_SPEED = 3.5f;
    private static final float FLOATING_DIFFERENCE = 7.5f;
    private static final float POSITION_Z = 100f;
    private static final float GLOW_SCALE = 2f;

    protected final ModelInstance modelInstance;
    protected final GamePolygon polygon;
    protected final TextureAtlas.AtlasRegion glowRegion;
    protected final float initialModelScale;
    private FastArray<ParticleEffect> particleEffects;

    private float scale;
    protected float rotationX;
    protected float rotationY;

    public boolean floating;
    private boolean isActive;

    protected Color baseColor;
    protected Color glowColor;

    private BasicAnimation appearAnimation;
    private BasicAnimation disappearAnimation;
    private BasicAnimation currentAnimation;

    public Target(Model model, GamePolygon polygon,
                  TextureAtlas.AtlasRegion glowRegion,
                  float initialModelScale)
    {
        this.modelInstance = new ModelInstance(model);
        this.polygon = polygon;
        this.glowRegion = glowRegion;
        this.initialModelScale = initialModelScale;
        this.particleEffects = new FastArray<ParticleEffect>(2);

        setPosition(0f, 0f);
        setScale(1f);

        this.rotationX = 0f;
        this.rotationY = 0f;

        this.floating = false;
        this.isActive = false;

        initializeAnimations();
    }

    private void initializeAnimations()
    {
        appearAnimation = new BasicAnimation(false,
                new BasicFrame(200f, 0f, 0.6f, 0f, 0f, 0f, 0f, Interpolation.sine),
                new BasicFrame(200f, 0f, 1.15f, 0f, 0f, 0f, 0f, Interpolation.sine),
                new BasicFrame(0f, 0f, 1.0f, 0f, 0f, 0f, 0f, Interpolation.sine));

        disappearAnimation = new BasicAnimation(false,
                new BasicFrame(200f, 0f, 1.0f, 0f, 0f, 0f, 0f, Interpolation.sine),
                new BasicFrame(200f, 0f, 1.2f, 0f, 0f, 0f, 0f, Interpolation.sine),
                new BasicFrame(0f, 0f, 0.6f, 0f, 0f, 0f, 0f, Interpolation.sine));
    }

    public void reset()
    {
        for (ParticleEffect effect : particleEffects)
        {
            ParticleUtilities.initialize(effect);
        }
    }

    public void start()
    {
        isActive = true;

        currentAnimation = appearAnimation;
        currentAnimation.start();
    }

    public void disappear()
    {
        currentAnimation = disappearAnimation;
        currentAnimation.start(500f);
    }

    public void destroy()
    {
        isActive = false;

        polygon.speed.x = 0f;
        polygon.speed.y = 0f;

        rotationX = 0f;
        rotationY = 0f;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public float getX()
    {
        return polygon.getX();
    }

    public float getY()
    {
        return polygon.getY();
    }

    public ModelInstance getModelInstance()
    {
        return modelInstance;
    }

    public void setPosition(float x, float y)
    {
        polygon.setPosition(x, y);
    }

    public void setPosition(Vector2 position)
    {
        polygon.setPosition(position.x, position.y);
    }

    public void setSpeed(float x, float y)
    {
        polygon.speed.x = x;
        polygon.speed.y = y;
    }

    public void setScale(float scale)
    {
        this.scale = scale;
        polygon.setScale(scale, scale);
    }

    protected void addParticleEffect(ParticleEffect effect)
    {
        particleEffects.add(effect);
    }

    public boolean onCollision(GamePolygon polygon)
    {
        return isActive && polygon.onCollision(this.polygon);
    }

    public void update(GameTime gameTime)
    {
        updateParticles(gameTime);

//        currentAnimation.update(gameTime.delta);
//        setScale(currentAnimation.getScaleX());
    }

    @Override
    public void step(float elapsed, float delta)
    {
        if (isActive)
        {
            if (floating)
            {
                polygon.speed.y = (FLOATING_DIFFERENCE * delta) *
                        MathUtils.sin(elapsed * FLOATING_SPEED);
            }

            currentAnimation.update(delta);
            setScale(currentAnimation.getScaleX());
        }
    }

    private void updateParticles(GameTime gameTime)
    {
        for (ParticleEffect effect : particleEffects)
        {
            effect.update(gameTime.delta);
        }
    }

    public void move()
    {
        polygon.move();
    }

    public void drawGlow(Batch batch)
    {
        if (isActive)
        {
            float width = glowRegion.getRegionWidth() * GLOW_SCALE * scale;
            float height = glowRegion.getRegionHeight() * GLOW_SCALE * scale;

            float x = polygon.getX() - (width / 2f);
            float y = polygon.getY() - (height / 2f);

            ColorUtilities.setColor(batch, glowColor);
            batch.draw(glowRegion, x, y, width, height);
            ColorUtilities.resetColor(batch);
        }
    }

    public void drawModel(ModelBatch modelBatch)
    {
        if (isActive)
        {
            modelInstance.userData = baseColor;
            modelInstance.transform.idt();
            modelInstance.transform.translate(polygon.getX(), polygon.getY(), POSITION_Z);

            float modelScale = initialModelScale * scale;

            modelInstance.transform.scl(modelScale);
            modelInstance.transform.rotate(1f, 0f, 0f, rotationX);
            modelInstance.transform.rotate(0f, 1f, 0f, rotationY);

            modelBatch.render(modelInstance);
        }
    }

    public void drawEffects(Batch batch)
    {
        for (ParticleEffect effect : particleEffects)
        {
            if (!effect.isComplete())
            {
                effect.draw(batch);
            }
        }
    }

    public void drawPolygon(ShapeRenderer shapeRenderer)
    {
        if (isActive)
        {
            polygon.draw(shapeRenderer, Color.RED);
        }
    }
}
