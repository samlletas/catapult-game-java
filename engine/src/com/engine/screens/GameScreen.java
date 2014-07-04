package com.engine.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.engine.GameSettings;
import com.engine.GameTime;

public abstract class GameScreen
{
    protected GameSettings settings;
    protected OrthographicCamera orthographicCamera;
    protected PerspectiveCamera perspectiveCamera;

    public GameScreen(GameSettings settings,
                      OrthographicCamera orthographicCamera,
                      PerspectiveCamera perspectiveCamera)
    {
        this.settings = settings;
        this.orthographicCamera = orthographicCamera;
        this.perspectiveCamera = perspectiveCamera;
    }

    public abstract void initialize();
    public abstract void update(GameTime gameTime);
    public abstract void draw(GameTime gameTime, SpriteBatch spriteBatch);
}
