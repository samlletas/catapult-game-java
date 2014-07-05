package com.mygdx.game.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.engine.GameSettings;
import com.engine.GameTime;
import com.engine.graphics.animation.AnimationPlayer;
import com.engine.screens.GameScreen;
import com.mygdx.game.assets.GameAssets;
import com.mygdx.game.gamelogic.Grass;

public class BaseGameScreen extends GameScreen
{
    protected AnimationPlayer player;
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
        player = GameAssets.Animations.catapult.instance;
        player.position.x = 160f;
        player.position.y = 395f;
        player.play("default");

        Gdx.input.setInputProcessor(new InputAdapter()
        {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button)
            {
                player.play("pull");
                return super.touchDown(screenX, screenY, pointer, button);
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button)
            {
                player.play("launch");
                return super.touchUp(screenX, screenY, pointer, button);
            }
        });

        grass = new Grass(settings);
    }

    @Override
    public void update(GameTime gameTime)
    {
        for (int i = 0; i < updates; i++)
        {
            player.update(gameTime);
            grass.update(gameTime);
        }
    }

    @Override
    public void draw(GameTime gameTime, SpriteBatch spriteBatch)
    {
        spriteBatch.begin();

        for (int i = 0; i < draws; i++)
        {
            player.draw(spriteBatch);
            grass.draw(spriteBatch);
        }

        spriteBatch.end();
    }
}
