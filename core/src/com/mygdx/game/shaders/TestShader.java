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
    private Color color = new Color(1f, 1f, 1f, 1f);

    public TestShader(long attributesMask)
    {
        super(attributesMask);
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
        uniforms.add(new Uniform("u_color", new IUniformSetter()
        {
            @Override
            public void set(Uniform uniform, ShaderProgram program,
                            Renderable renderable)
            {
                program.setUniformf(uniform.id, color);
            }
        }));
    }
}
