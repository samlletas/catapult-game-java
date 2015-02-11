package com.engine.graphics.graphics2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GradientSprite extends Sprite
{
    public GradientSprite(TextureRegion white)
    {
        setRegion(white);
    }

    public void setGradientColor(Color a, Color b, boolean horizontal)
    {
        float[] vertices = getVertices();
        float ca = a.toFloatBits();
        float cb = b.toFloatBits();

        vertices[SpriteBatch.C1] = horizontal ? ca : cb; //bottom left
        vertices[SpriteBatch.C2] = ca; //top left
        vertices[SpriteBatch.C3] = horizontal ? cb : ca; //top right
        vertices[SpriteBatch.C4] = cb; //bottom right
    }
}
