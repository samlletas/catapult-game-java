package com.mygdx.game.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.engine.GameTime;
import com.engine.collision2d.GamePolygon;

public class CollisionTester
{
    final static float ROTATION_SPEED = 50f;
    final static float TRANSLATION_SPEED = 2500f;

    private GamePolygon a;
    private GamePolygon b;

    private Color drawColor;

    public CollisionTester()
    {
        a = GamePolygon.createConvex(5, 15);
        a.setPosition(400, 100);
        a.isSolid = true;

        b = GamePolygon.createRhombus(32, 40);
        b.setPosition(400, 70);
    }

    public void update(GameTime gameTime)
    {
        updateA(gameTime);
        updateB(gameTime);

        a.rotate(ROTATION_SPEED * gameTime.delta);
//        b.rotate(ROTATION_SPEED * gameTime.delta);

        if (a.onCollision(b))
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
        a.draw(spriteBatch, drawColor);
        b.draw(spriteBatch, drawColor);
    }
}
