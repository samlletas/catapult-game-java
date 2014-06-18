package com.engine.graphics.shaders;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class UniformCollection implements Disposable
{
    private Array<Uniform> uniforms;
    private boolean initialized;

    public UniformCollection()
    {
        uniforms = new Array<Uniform>();
        initialized = false;
    }

    public void add(Uniform uniform)
    {
        uniforms.add(uniform);
    }

    public Uniform get(String name)
    {
        for (Uniform uniform : uniforms)
        {
            if (uniform.name.equals(name))
            {
                return uniform;
            }
        }

        return  null;
    }

    public Uniform get(int id)
    {
        for (Uniform uniform : uniforms)
        {
            if (uniform.id == id)
            {
                return uniform;
            }
        }

        return  null;
    }

    public void initialize(ShaderProgram program)
    {
        if (!initialized)
        {
            for (Uniform uniform : uniforms)
            {
                uniform.initialize(program);
            }

            initialized = true;
        }
    }

    public void setUniforms(ShaderProgram program, Renderable renderable)
    {
        for (Uniform uniform : uniforms)
        {
            uniform.setValue(program, renderable);
        }
    }

    @Override
    public void dispose()
    {
        uniforms.clear();
        uniforms = null;
    }
}
