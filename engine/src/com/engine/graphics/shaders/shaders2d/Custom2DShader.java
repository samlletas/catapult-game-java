package com.engine.graphics.shaders.shaders2d;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.engine.graphics.shaders.UniformCollection;

public abstract class Custom2DShader implements Disposable
{
    public ShaderProgram program;
    protected UniformCollection customUniforms;
    private boolean hasCustomUniforms;

    protected Custom2DShader(boolean hasCustomUniforms)
    {
        this.hasCustomUniforms = hasCustomUniforms;

        if (hasCustomUniforms)
        {
            customUniforms = new UniformCollection();
        }
    }

    public void init()
    {
        program = new ShaderProgram(getCustomVertexShader(), getCustomFragmentShader());

        if (program.isCompiled())
        {
            if (hasCustomUniforms)
            {
                addCustomUniforms();
            }
        }
        else
        {
            throw new GdxRuntimeException(program.getLog());
        }
    }

    public final void setCustomUniforms()
    {
        if (hasCustomUniforms)
        {
            customUniforms.setUniforms(program, null);
        }
    }

    @Override
    public void dispose()
    {
        program.dispose();

        if (hasCustomUniforms)
        {
            customUniforms.dispose();
        }
    }

    protected abstract String getCustomVertexShader();
    protected abstract String getCustomFragmentShader();
    protected abstract void addCustomUniforms();
}
