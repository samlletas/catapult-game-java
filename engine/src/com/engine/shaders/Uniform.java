package com.engine.shaders;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Uniform
{
    public int id;
    public String name;
    public boolean isGlobal;
    private IUniformSetter setter;

    public Uniform(String name, IUniformSetter setter)
    {
        this(name, false, setter);
    }

    public Uniform(String name, boolean isGlobal, IUniformSetter setter)
    {
        this.id = -1;
        this.name = name;
        this.isGlobal = isGlobal;
        this.setter = setter;
    }

    public void initialize(ShaderProgram program)
    {
        id = program.getUniformLocation(name);

        if (id == -1)
        {
            throw new GdxRuntimeException("Uniform: " + name + " does not exist");
        }
    }

    public void setValue(ShaderProgram program, Renderable renderable)
    {
        setter.set(this, program, renderable);
    }
}
