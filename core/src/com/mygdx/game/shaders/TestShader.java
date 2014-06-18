package com.mygdx.game.shaders;

import com.badlogic.gdx.Gdx;
import com.engine.graphics.shaders.shaders2d.Custom2DShader;

public class TestShader extends Custom2DShader
{
    public TestShader()
    {
        super(false);
    }

    @Override
    protected String getCustomVertexShader()
    {
        return Gdx.files.internal("shaders/test.vert.glsl").readString();
    }

    @Override
    protected String getCustomFragmentShader()
    {
        return Gdx.files.internal("shaders/test.frag.glsl").readString();
    }

    @Override
    protected void addCustomUniforms()
    {

    }
}
