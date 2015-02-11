package com.engine.graphics.shaders.shaders2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.engine.graphics.shaders.IUniformSetter;
import com.engine.graphics.shaders.Uniform;
import com.engine.graphics.shaders.UniformCollection;
import com.engine.utilities.ColorUtilities;

public class AlphaBlended2DShader extends Custom2DShader
{
    private Color foregroundColor;

    public AlphaBlended2DShader()
    {
        this(Gdx.files.classpath("com/engine/graphics/shaders/shaders2d/alphaBlended2d.vert.glsl").readString(),
                Gdx.files.classpath("com/engine/graphics/shaders/shaders2d/alphaBlended2d.frag.glsl").readString());
    }

    protected AlphaBlended2DShader(String vertexShader, String fragmentShader)
    {
        super(vertexShader, fragmentShader, true);

        this.foregroundColor = ColorUtilities.createColor(0, 0, 0, 0);
    }

    public void setForegroundColor(Color color)
    {
        foregroundColor.set(color);
    }

    @Override
    protected void addCustomUniforms(UniformCollection uniforms)
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
    protected void afterBegin()
    {
        sendCustomUniforms();
    }
}
