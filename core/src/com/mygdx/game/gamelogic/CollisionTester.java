package com.mygdx.game.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.engine.GameTime;
import com.engine.collision2d.GamePolygon;

public class CollisionTester
{
    final static float ROTATION_SPEED = 50f;

    final static float SPEED_X = 200f;
    final static float SPEED_Y = 200f;

    private GamePolygon a;
    private GamePolygon b;
    private Grass grass;

    private Color drawColor;

    public CollisionTester(Grass grass)
    {
//        a = GamePolygon.createConvex(5, 15);
//        a = GamePolygon.createRectangle(50, 50);
        a = GamePolygon.createRhombus(32, 40);
        a.setPosition(400, 100);
//        a.isSolid = true;

        b = GamePolygon.createRhombus(32, 40);
//        b = GamePolygon.createConvex(6, 14);

//        b = GamePolygon.createRectangle(40, 40);
        b.setPosition(330, 100);
//        b.setPosition(a.getX() - 25 - 20, 100);

        this.grass = grass;
    }

    public void update(GameTime gameTime)
    {
        updateA(gameTime);
        updateB(gameTime);

//        a.rotate(ROTATION_SPEED * gameTime.delta);
//        b.rotate(ROTATION_SPEED * gameTime.delta);

        if (b.onCollision(a))
        {
            drawColor = Color.RED;
        }
        else
        {
            drawColor = Color.WHITE;
        }

        a.move();
        b.move();
    }

    private void updateA(GameTime gameTime)
    {
        a.speed.setZero();

        float sx = SPEED_X * gameTime.delta;
        float sy = SPEED_Y * gameTime.delta;

        // Translación
        if (Gdx.input.isKeyPressed(Input.Keys.A))
        {
            a.speed.x = -sx;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D))
        {
            a.speed.x = sx;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W))
        {
            a.speed.y = -sy;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S))
        {
            a.speed.y = sy;
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
        b.speed.setZero();

        float sx = SPEED_X * gameTime.delta;
        float sy = SPEED_Y * gameTime.delta;

        // Translación
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            b.speed.x = -sx;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            b.speed.x = sx;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            b.speed.y = -sy;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            b.speed.y = sy;
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
