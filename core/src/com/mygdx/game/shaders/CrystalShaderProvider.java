package com.mygdx.game.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.BaseShaderProvider;
import com.mygdx.game.gamelogic.targets.Crystal;

public class CrystalShaderProvider extends BaseShaderProvider
{
    public void setForegroundColor(Color color)
    {
        for (int i = 0, n = shaders.size; i < n; i++)
        {
            ((CrystalShader)shaders.get(i)).setForegroundColor(color);
        }
    }

    @Override
    public Shader getShader(Renderable renderable)
    {
//        Gdx.app.log("SHADERS", String.valueOf(shaders.size));
        return super.getShader(renderable);
    }

    @Override
    protected Shader createShader(Renderable renderable)
    {
        return new CrystalShader(renderable);
    }
}