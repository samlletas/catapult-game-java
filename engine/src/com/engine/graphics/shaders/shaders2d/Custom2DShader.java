package com.engine.graphics.shaders.shaders2d;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.engine.graphics.shaders.UniformCollection;

public abstract class Custom2DShader extends ShaderProgram
{
    private boolean hasCustomUniforms;
    private UniformCollection customUniforms;

    protected Custom2DShader(String vertexShader, String fragmentShader,
                             boolean hasCustomUniforms)
    {
        super(vertexShader, fragmentShader);

        this.hasCustomUniforms = hasCustomUniforms;
        handleCompilation();
    }

    private void handleCompilation()
    {
        if (isCompiled())
        {
            if (hasCustomUniforms)
            {
                customUniforms = new UniformCollection();
                addCustomUniforms(customUniforms);
                customUniforms.initialize(this);
            }
        }
        else
        {
            throw new GdxRuntimeException(getLog());
        }
    }

    @Override
    public final void begin()
    {
        beforeBegin();
        super.begin();
        afterBegin();
    }

    public final void sendCustomUniforms()
    {
        if (hasCustomUniforms)
        {
            customUniforms.setUniforms(this, null);
        }
    }

    @Override
    public final void end()
    {
        beforeEnd();
        super.end();
        afterEnd();
    }

    protected void addCustomUniforms(UniformCollection uniforms)
    {

    }

    protected void beforeBegin()
    {

    }

    protected void afterBegin()
    {

    }

    protected void beforeEnd()
    {

    }

    protected void afterEnd()
    {

    }

    @Override
    public void dispose()
    {
        super.dispose();

        if (hasCustomUniforms)
        {
            customUniforms.dispose();
        }
    }
}
