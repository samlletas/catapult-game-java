package com.mygdx.game.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.engine.graphics.shaders.IUniformSetter;
import com.engine.graphics.shaders.Uniform;
import com.engine.graphics.shaders.UniformCollection;
import com.engine.graphics.shaders.shaders3d.Custom3DShader;
import com.engine.utilities.ColorUtilities;

public class CrystalShader extends Custom3DShader
{
    private Color foregroundColor;

    public CrystalShader(Renderable renderable)
    {
        super(renderable);

        this.foregroundColor = ColorUtilities.createColor(0, 0, 0, 0);
    }

    @Override
    protected String getCustomVertexShader()
    {
        return Gdx.files.internal("shaders/crystal.vert.glsl").readString();
    }

    @Override
    protected String getCustomFragmentShader()
    {
        return Gdx.files.internal("shaders/crystal.frag.glsl").readString();
    }

    public void setForegroundColor(Color color)
    {
        foregroundColor.set(color);
    }

    @Override
    protected void addCustomGlobalUniforms(UniformCollection uniforms)
    {
        uniforms.add(new Uniform("u_foregroundColor", new IUniformSetter()
        {
            @Override
            public void set(Uniform uniform, ShaderProgram program, Renderable renderable)
            {
                program.setUniformf(uniform.id,
                        foregroundColor.r,
                        foregroundColor.g,
                        foregroundColor.b,
                        foregroundColor.a);
            }
        }));
    }

    @Override
    protected void addCustomLocalUniforms(UniformCollection uniforms)
    {
        uniforms.add(new Uniform("u_baseColor", new IUniformSetter()
        {
            @Override
            public void set(Uniform uniform, ShaderProgram program, Renderable renderable)
            {
                Color color = (Color)renderable.userData;
                program.setUniformf(uniform.id, color.r, color.g, color.b);
            }
        }));
    }
}
