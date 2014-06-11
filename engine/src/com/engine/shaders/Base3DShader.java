package com.engine.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;

public class Base3DShader implements Shader
{
    @Override
    public void init()
    {
        String vertex = Gdx.files.classpath("com/engine/shaders/base3d.vert.glsl").readString();
    }

    @Override
    public int compareTo(Shader other)
    {
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance)
    {
        return false;
    }

    @Override
    public void begin(Camera camera, RenderContext context)
    {

    }

    @Override
    public void render(Renderable renderable)
    {

    }

    @Override
    public void end()
    {

    }

    @Override
    public void dispose()
    {

    }
}
