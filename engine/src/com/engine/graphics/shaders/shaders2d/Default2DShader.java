package com.engine.graphics.shaders.shaders2d;

import com.badlogic.gdx.Gdx;

public class Default2DShader extends Custom2DShader
{
    public Default2DShader()
    {
        super(false);
    }

    @Override
    protected String getCustomVertexShader()
    {
        return Gdx.files.classpath(
                "com/engine/graphics/shaders/shaders2d/default2d.vert.glsl").readString();
    }

    @Override
    protected String getCustomFragmentShader()
    {
        return Gdx.files.classpath(
                "com/engine/graphics/shaders/shaders2d/default2d.frag.glsl").readString();
    }

    @Override
    protected void addCustomUniforms()
    {

    }
}
