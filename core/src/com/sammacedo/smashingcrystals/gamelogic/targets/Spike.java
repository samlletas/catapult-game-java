package com.sammacedo.smashingcrystals.gamelogic.targets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.engine.GameTime;
import com.engine.collision2d.GamePolygon;
import com.engine.utilities.ColorUtilities;
import com.engine.utilities.ParticleUtilities;
import com.sammacedo.smashingcrystals.assets.GameAssets;

public class Spike extends Target
{
    private static final float ROTATION_SPEED = 80f;
    private static final Color COLOR_BASE = ColorUtilities.createColor(239, 84, 117, 255);
    private static final Color COLOR_GLOW = ColorUtilities.createColor(255, 71, 150, 255);

    private ParticleEffect breakEffect;

    public Spike(GameAssets assets)
    {
        super(assets.models.bomb.getInstance(), GamePolygon.createConvex(6, 14.25f),
                assets.atlasRegions.spikesGlow.getInstance(), 11.4f);

        floating = true;

        baseColor = COLOR_BASE;
        glowColor = COLOR_GLOW;
        polygon.isSolid = true;

        breakEffect = new ParticleEffect(assets.particles.spikeBreak.getInstance());
        ParticleUtilities.initialize(breakEffect);
        addParticleEffect(breakEffect);
    }

    @Override
    public void destroy()
    {
        super.destroy();

        breakEffect.setPosition(getX(), getY());
        breakEffect.reset();
    }

    @Override
    public void update(GameTime gameTime)
    {
        super.update(gameTime);

        if (isActive())
        {
            rotationY += ROTATION_SPEED * gameTime.delta;
            rotationX += ROTATION_SPEED * gameTime.delta;
        }
    }
}
