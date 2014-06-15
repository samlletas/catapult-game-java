package com.mygdx.game.shaders;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.BaseShaderProvider;

public class TestShaderProvider extends BaseShaderProvider
{
    @Override
    protected Shader createShader(Renderable renderable)
    {
        return new TestShader(renderable);
    }
}
