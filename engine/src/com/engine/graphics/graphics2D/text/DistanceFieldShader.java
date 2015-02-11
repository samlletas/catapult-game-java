package com.engine.graphics.graphics2D.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.engine.graphics.shaders.IUniformSetter;
import com.engine.graphics.shaders.Uniform;
import com.engine.graphics.shaders.UniformCollection;
import com.engine.graphics.shaders.shaders2d.Custom2DShader;

public class DistanceFieldShader extends Custom2DShader
{
    public float thickness;
    public float smoothing;

    public DistanceFieldShader()
    {
        this(Gdx.files.classpath("com/engine/graphics/graphics2D/text/distanceField.vert.glsl").readString(),
                Gdx.files.classpath("com/engine/graphics/graphics2D/text/distanceField.frag.glsl").readString());
    }

    protected DistanceFieldShader(String vertexShader, String fragmentShader)
    {
        super(vertexShader, fragmentShader, true);

        thickness = 0f;
        smoothing = 0f;
    }

    @Override
    protected void addCustomUniforms(UniformCollection uniforms)
    {
        uniforms.add(new Uniform("u_thickness", new IUniformSetter()
        {
            @Override
            public void set(Uniform uniform, ShaderProgram program, Renderable renderable)
            {
                program.setUniformf(uniform.id, thickness);
            }
        }));

        uniforms.add(new Uniform("u_smoothing", new IUniformSetter()
        {
            @Override
            public void set(Uniform uniform, ShaderProgram program, Renderable renderable)
            {
                program.setUniformf(uniform.id, smoothing);
            }
        }));
    }
}