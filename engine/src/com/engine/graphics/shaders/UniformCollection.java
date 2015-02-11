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
        Array<Uniform> localUniforms = this.uniforms;
        Uniform uniform;

        for (int i = 0, n = localUniforms.size; i < n; i++)
        {
            uniform = localUniforms.get(i);

            if (uniform.name.equals(name))
            {
                return uniform;
            }
        }

        return  null;
    }

    public Uniform get(int id)
    {
        Array<Uniform> localUniforms = this.uniforms;
        Uniform uniform;

        for (int i = 0, n = localUniforms.size; i < n; i++)
        {
            uniform = localUniforms.get(i);

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
            Array<Uniform> localUniforms = this.uniforms;

            for (int i = 0, n = localUniforms.size; i < n; i++)
            {
                localUniforms.get(i).initialize(program);
            }

            initialized = true;
        }
    }

    public void setUniforms(ShaderProgram program, Renderable renderable)
    {
        Array<Uniform> localUniforms = this.uniforms;

        for (int i = 0, n = localUniforms.size; i < n; i++)
        {
            localUniforms.get(i).setValue(program, renderable);
        }
    }

    @Override
    public void dispose()
    {
        uniforms.clear();
        uniforms = null;
    }
}
