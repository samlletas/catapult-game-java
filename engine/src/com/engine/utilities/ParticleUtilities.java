package com.engine.utilities;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class ParticleUtilities
{
    public static void initialize(ParticleEffect effect)
    {
        effect.start();
        effect.update(1000f);
    }
}