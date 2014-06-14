package com.engine.shaders;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public interface IUniformSetter
{
    void set(Uniform uniform, ShaderProgram program, Renderable renderable);
}
