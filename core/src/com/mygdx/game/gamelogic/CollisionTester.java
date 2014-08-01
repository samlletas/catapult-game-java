package com.mygdx.game.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.engine.GameTime;
import com.engine.utilities.PolygonUtilities;

public class CollisionTester
{
    final static float ROTATION_SPEED = 50f;
    final static float TRANSLATION_SPEED = 100f;

    private Polygon a;
    private Polygon b;

    private Color drawColor;

    public CollisionTester()
    {
        a = PolygonUtilities.createConvex(4, 64);
        a.setPosition(400, 100);

        b = PolygonUtilities.createRhombus(64, 128);
        b.setPosition(600, 200);
    }

    public void update(GameTime gameTime)
    {
        updateA(gameTime);
        updateB(gameTime);

        if (PolygonUtilities.onCollision(a, b))
        {
            drawColor = Color.RED;
        }
        else
        {
            drawColor = Color.WHITE;
        }
    }

    private void updateA(GameTime gameTime)
    {
        // Translación
        if (Gdx.input.isKeyPressed(Input.Keys.A))
        {
            a.translate(-TRANSLATION_SPEED * gameTime.delta, 0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D))
        {
            a.translate(TRANSLATION_SPEED * gameTime.delta, 0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W))
        {
            a.translate(0f, -TRANSLATION_SPEED * gameTime.delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S))
        {
            a.translate(0f, TRANSLATION_SPEED * gameTime.delta);
        }

        // Rotación
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1))
        {
            a.rotate(-ROTATION_SPEED * gameTime.delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2))
        {
            a.rotate(ROTATION_SPEED * gameTime.delta);
        }
    }

    private void updateB(GameTime gameTime)
    {
        // Translación
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            b.translate(-TRANSLATION_SPEED * gameTime.delta, 0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            b.translate(TRANSLATION_SPEED * gameTime.delta, 0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            b.translate(0f, -TRANSLATION_SPEED * gameTime.delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            b.translate(0f, TRANSLATION_SPEED * gameTime.delta);
        }

        // Rotación
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_9))
        {
            b.rotate(-ROTATION_SPEED * gameTime.delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_0))
        {
            b.rotate(ROTATION_SPEED * gameTime.delta);
        }
    }

    public void draw(SpriteBatch spriteBatch)
    {
        PolygonUtilities.draw(spriteBatch, a, drawColor);
        PolygonUtilities.draw(spriteBatch, b, drawColor);
    }
}
