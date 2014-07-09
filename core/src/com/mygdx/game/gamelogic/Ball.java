package com.mygdx.game.gamelogic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.engine.GameTime;
import com.mygdx.game.assets.GameAssets;

public final class Ball
{
    public float x;
    public float y;

    private TextureAtlas.AtlasRegion region;

    public Ball()
    {
        region = GameAssets.AtlasRegions.ball.instance;

        x = 0f;
        y = 0f;
    }

    public void update(GameTime gameTime)
    {
    }

    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(region, x, y);
    }
}
