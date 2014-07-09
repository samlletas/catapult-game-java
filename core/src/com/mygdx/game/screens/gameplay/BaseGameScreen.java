package com.mygdx.game.screens.gameplay;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.engine.GameSettings;
import com.engine.GameTime;
import com.engine.screens.GameScreen;
import com.mygdx.game.gamelogic.Background;
import com.mygdx.game.gamelogic.Ball;
import com.mygdx.game.gamelogic.Catapult;
import com.mygdx.game.gamelogic.Grass;

public class BaseGameScreen extends GameScreen
{
    protected Background background;
    protected Ball ball;
    protected Catapult catapult;
    protected Grass grass;
    protected int updates = 1;
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
        background = new Background();
        ball = new Ball();
        catapult = new Catapult(ball);
        grass = new Grass(settings);
    }

    @Override
    public void update(GameTime gameTime)
    {
        for (int i = 0; i < updates; i++)
        {
            background.update(gameTime);
            ball.update(gameTime);
            catapult.update(gameTime);
            grass.update(gameTime);
        }
    }

    @Override
    public void draw(GameTime gameTime, SpriteBatch spriteBatch)
    {
        spriteBatch.begin();

        for (int i = 0; i < draws; i++)
        {
            background.draw(spriteBatch);
            ball.draw(spriteBatch);
            catapult.draw(spriteBatch);
            grass.draw(spriteBatch);
        }

        spriteBatch.end();
    }
}
