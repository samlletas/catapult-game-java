package com.mygdx.game.gamelogic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.engine.GameTime;
import com.mygdx.game.assets.GameAssets;

public final class Ball
{
    public float x;
    public float y;

    private final TextureAtlas.AtlasRegion region;
    private boolean flying;
    private float speedX;
    private float speedY;

    private static float GRAVITY = 35f;
    private static float MAX_SPEED_Y = 2000f;

    public boolean isFlying()
    {
        return flying;
    }

    public Ball()
    {
        this.x = 0f;
        this.y = 0f;

        this.region = GameAssets.AtlasRegions.ball.instance;
        this.flying = false;
    }

    public void launch(float power, float angle)
    {
        speedX = power * MathUtils.cosDeg(angle);
        speedY = -(power * MathUtils.sinDeg(angle));

        flying = true;
    }

    private void cancelFlight()
    {
        flying = false;

        speedX = 0f;
        speedY = 0f;
    }

    public void update(GameTime gameTime)
    {
        if (flying)
        {
            x += speedX * gameTime.delta;
            y += speedY * gameTime.delta;

            speedY = MathUtils.clamp(speedY + GRAVITY, -MAX_SPEED_Y, MAX_SPEED_Y);

            if (x >= 854 || y >= 480)
            {
                cancelFlight();
            }
        }
    }

    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(region, x, y);
    }
}
