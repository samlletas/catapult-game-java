package com.mygdx.game.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.engine.shaders.CustomShader;
import com.engine.shaders.IUniformSetter;
import com.engine.shaders.Uniform;

public class TestShader extends CustomShader
{
    private float glowFactor = 1.0f;

    public TestShader(Renderable renderable)
    {
        super(renderable);
    }

    @Override
    protected String getCustomVertexShader()
    {
        return Gdx.files.internal("shaders/color.vert.glsl").readString();
    }

    @Override
    protected String getCustomFragmentShader()
    {
        return Gdx.files.internal("shaders/color.frag.glsl").readString();
    }

    @Override
    protected void addCustomUniforms()
    {
        uniforms.add(new Uniform("u_glowFactor", new IUniformSetter()
        {
            @Override
            public void set(Uniform uniform, ShaderProgram program,
                            Renderable renderable)
            {
                program.setUniformf(uniform.id, glowFactor);
            }
        }));
    }
}
