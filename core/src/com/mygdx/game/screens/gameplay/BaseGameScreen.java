package com.mygdx.game.screens.gameplay;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.engine.GameSettings;
import com.engine.GameTime;
import com.engine.camera.CameraShaker2D;
import com.engine.screens.GameScreen;
import com.mygdx.game.gamelogic.*;

public class BaseGameScreen extends GameScreen
{
    protected CameraShaker2D cameraShaker;
    protected Background background;
    protected Grass grass;
    protected Ball ball;
    protected Catapult catapult;

    protected CollisionTester collisionTester;

    // Para 60 FPS casi constantes(Android-Sam): 30 máx
    protected int updates = 1;

    // Para 60 FPS casi constantes(Android-Sam): 7 máx
    protected int draws = 1;

    public BaseGameScreen(GameSettings settings,
                          OrthographicCamera orthographicCamera,
                          PerspectiveCamera perspectiveCamera)
    {
        super(settings, orthographicCamera, perspectiveCamera);
    }

    @Override
    public void initialize()
    {
        cameraShaker = new CameraShaker2D(orthographicCamera, 80, 0, 0, 0.75f, 0.99f);
        background = new Background();
        grass = new Grass(settings);
        ball = new Ball(settings, cameraShaker, grass);
        catapult = new Catapult(ball);

        collisionTester = new CollisionTester(grass);
    }

    @Override
    public void update(GameTime gameTime)
    {
        for (int i = 0; i < updates; i++)
        {
            cameraShaker.update(gameTime);
            background.update(gameTime);
            ball.update(gameTime);
            catapult.update(gameTime);
            grass.update(gameTime);
            collisionTester.update(gameTime);
        }
    }

    @Override
    public void draw(GameTime gameTime, SpriteBatch spriteBatch)
    {
        spriteBatch.begin();

        for (int i = 0; i < draws; i++)
        {
            cameraShaker.beginDraw(spriteBatch);

            background.draw(spriteBatch);
            ball.draw(spriteBatch);
            catapult.draw(spriteBatch);
            grass.draw(spriteBatch);
            collisionTester.draw(spriteBatch);

            cameraShaker.endDraw(spriteBatch);
        }

        spriteBatch.end();
    }
}
