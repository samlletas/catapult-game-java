package com.engine.graphics.graphics2D.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.engine.graphics.shaders.IUniformSetter;
import com.engine.graphics.shaders.Uniform;
import com.engine.graphics.shaders.UniformCollection;
import com.engine.utilities.ColorUtilities;

public class AlphaBlendedDistanceFieldShader extends DistanceFieldShader
{
    private Color foregroundColor;
    private Color shadowColor;
    private Vector2 shadowOffset;

    public AlphaBlendedDistanceFieldShader()
    {
        super(Gdx.files.classpath("com/engine/graphics/graphics2D/text/alphaBlendedDistanceField.vert.glsl").readString(),
                Gdx.files.classpath("com/engine/graphics/graphics2D/text/alphaBlendedDistanceField.frag.glsl").readString());

        this.foregroundColor = ColorUtilities.createColor(0, 0, 0, 0);

        this.shadowColor = ColorUtilities.createColor(0, 0, 0, 0);
        this.shadowOffset = new Vector2(0f, 0f);
    }

    public void setForegroundColor(Color color)
    {
        foregroundColor.set(color);
    }

    public void setShadowColor(Color color)
    {
        this.shadowColor.set(color);
    }

    public void setShadowOffset(float x, float y)
    {
        this.shadowOffset.set(x, y);
    }

    @Override
    protected void addCustomUniforms(UniformCollection uniforms)
    {
        super.addCustomUniforms(uniforms);

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

        uniforms.add(new Uniform("u_shadowColor", new IUniformSetter()
        {
            @Override
            public void set(Uniform uniform, ShaderProgram program, Renderable renderable)
            {
                program.setUniformf(uniform.id,
                        shadowColor.r,
                        shadowColor.g,
                        shadowColor.b,
                        shadowColor.a);
            }
        }));

        uniforms.add(new Uniform("u_shadowOffset", new IUniformSetter()
        {
            @Override
            public void set(Uniform uniform, ShaderProgram program, Renderable renderable)
            {
                program.setUniformf(uniform.id, shadowOffset.x, shadowOffset.y);
            }
        }));
    }
}