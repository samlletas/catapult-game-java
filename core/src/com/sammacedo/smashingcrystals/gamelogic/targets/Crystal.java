package com.sammacedo.smashingcrystals.gamelogic.targets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Array;
import com.engine.GameTime;
import com.engine.collision2d.GamePolygon;
import com.engine.graphics.graphics2D.animation.basic.BasicAnimation;
import com.engine.graphics.graphics2D.animation.basic.BasicAnimationGroup;
import com.engine.graphics.graphics2D.animation.basic.BasicFrame;
import com.engine.utilities.ColorUtilities;
import com.engine.utilities.ParticleUtilities;
import com.sammacedo.smashingcrystals.assets.GameAssets;

public class Crystal extends Target
{
    private static final float ROTATION_SPEED = 60f;
    private static final Color COLOR_NORMAL_BASE = ColorUtilities.createColor(231, 195, 27, 255);
    private static final Color COLOR_NORMAL_GLOW = ColorUtilities.createColor(255, 238, 112, 255);
    private static final Color COLOR_SPECIAL_BASE = ColorUtilities.createColor(76, 209, 0, 255);
    private static final Color COLOR_SPECIAL_GLOW = ColorUtilities.createColor(144, 255, 96, 255);

    private GameAssets assets;
    private CrystalTypes type;
    private ParticleEffect breakNormalEffect;
    private ParticleEffect breakSpecialEffect;
    private WaveAnimation waveAnimation;

    public Crystal(GameAssets assets)
    {
        super(assets.models.crystal.getInstance(), GamePolygon.createRhombus(28.5f, 38.5f),
                assets.atlasRegions.crystalGlow.getInstance(), 13.3f);

        this.assets = assets;
        floating = true;

        setType(CrystalTypes.Normal);

        breakNormalEffect = new ParticleEffect(assets.particles.crystalBreakNormal.getInstance());
        breakSpecialEffect = new ParticleEffect(assets.particles.crystalBreakSpecial.getInstance());
        waveAnimation = new WaveAnimation();

        ParticleUtilities.initialize(breakNormalEffect);
        ParticleUtilities.initialize(breakSpecialEffect);

        addParticleEffect(breakNormalEffect);
        addParticleEffect(breakSpecialEffect);
    }

    public void setType(CrystalTypes type)
    {
        this.type = type;

        if (type == CrystalTypes.Normal)
        {
            baseColor = COLOR_NORMAL_BASE;
            glowColor = COLOR_NORMAL_GLOW;
        }
        else
        {
            baseColor = COLOR_SPECIAL_BASE;
            glowColor = COLOR_SPECIAL_GLOW;
        }
    }

    public CrystalTypes getType()
    {
        return type;
    }

    public void startWaveAnimation()
    {
        waveAnimation.start();
    }

    @Override
    public void start()
    {
        super.start();
        waveAnimation.stop();
    }

    @Override
    public void destroy()
    {
        super.destroy();
        waveAnimation.start();
        waveAnimation.stop();

        if (type == CrystalTypes.Normal)
        {
            breakNormalEffect.setPosition(getX(), getY());
            breakNormalEffect.reset();
        }
        else
        {
            breakSpecialEffect.setPosition(getX(), getY());
            breakSpecialEffect.reset();
        }
    }

    @Override
    public void update(GameTime gameTime)
    {
        super.update(gameTime);

        if (isActive())
        {
            rotationY += ROTATION_SPEED * gameTime.delta;
            rotationX = 15f;

            waveAnimation.update(gameTime.delta);
        }
    }

    @Override
    public void drawEffects(Batch batch)
    {
        super.drawEffects(batch);

        if (isActive())
        {
            waveAnimation.draw(batch, getX(), getY());
        }
    }

    class WaveAnimation extends BasicAnimationGroup
    {
        private BasicAnimation borderBloomAnimation;
        private BasicAnimation fillBloomAnimation;

        private TextureAtlas.AtlasRegion borderRegion;
        private TextureAtlas.AtlasRegion fillRegion;

        @Override
        protected void initialize()
        {
            borderBloomAnimation = new BasicAnimation(false,
                    new BasicFrame(200f, 0f, 1f, 1f, 0f, 0f, 0f,
                            Interpolation.linear),
                    new BasicFrame(300f, 0f, 1.5f, 1.5f, 0f, 0f, 0.75f,
                            Interpolation.linear),
                    new BasicFrame(0f, 0f, 2f, 2f, 0f, 0f, 0f,
                            Interpolation.linear));

            fillBloomAnimation = new BasicAnimation(false,
                    new BasicFrame(200f, 0f, 1f, 1f, 0f, 0f, 0f,
                            Interpolation.linear),
                    new BasicFrame(300f, 0f, 1.5f, 1.5f, 0f, 0f, 0.20f,
                            Interpolation.linear),
                    new BasicFrame(0f, 0f, 2f, 2f, 0f, 0f, 0f,
                            Interpolation.linear));

            borderRegion = assets.atlasRegions.crystalWaveBorder.getInstance();
            fillRegion = assets.atlasRegions.crystalWaveFill.getInstance();
        }

        @Override
        protected void addAnimations(Array<BasicAnimation> animations)
        {
            animations.add(borderBloomAnimation);
            animations.add(fillBloomAnimation);
        }

        @Override
        protected void onStart()
        {
            borderBloomAnimation.start(0f);
            fillBloomAnimation.start(250f);
        }

        @Override
        protected void onDraw(Batch batch, float x, float y)
        {
            if (Crystal.this.type == CrystalTypes.Normal)
            {
                ColorUtilities.setColor(batch, Crystal.COLOR_NORMAL_GLOW);
            }
            else
            {
                ColorUtilities.setColor(batch, Crystal.COLOR_SPECIAL_GLOW);
            }

            borderBloomAnimation.draw(batch, borderRegion, x, y,
                    borderRegion.getRegionWidth() / 2f,
                    borderRegion.getRegionHeight() / 2f);

            fillBloomAnimation.draw(batch, fillRegion, x, y,
                    fillRegion.getRegionWidth() / 2f,
                    fillRegion.getRegionHeight() / 2f);

            ColorUtilities.resetColor(batch);
        }
    }
}