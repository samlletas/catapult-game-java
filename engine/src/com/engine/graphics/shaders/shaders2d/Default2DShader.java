package com.engine.graphics.shaders.shaders2d;

import com.badlogic.gdx.Gdx;

public final class Default2DShader extends Custom2DShader
{
    public Default2DShader()
    {
        super(Gdx.files.classpath("com/engine/graphics/shaders/shaders2d/default2d.vert.glsl").readString(),
                Gdx.files.classpath("com/engine/graphics/shaders/shaders2d/default2d.frag.glsl").readString(),
                false);
    }
}
