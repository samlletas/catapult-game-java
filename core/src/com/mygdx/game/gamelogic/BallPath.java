package com.mygdx.game.gamelogic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.engine.GameTime;
import com.engine.actors.ActorOrigin;
import com.engine.actors.TextureRegionActor;
import com.engine.utilities.FastArray;
import com.mygdx.game.Common;

public class BallPath
{
    private static final int MAX_DOTS = 10;

    private FastArray<TextureRegionActor> redDots;
    private boolean isActive;

    private float angle = Catapult.LAUNCH_ANGLE;
    private float gravity = Ball.GRAVITY;
    private float power;

    public BallPath(Common common)
    {
        redDots = new FastArray<TextureRegionActor>(MAX_DOTS);

        for (int i = 0; i < MAX_DOTS; i++)
        {
            TextureRegionActor actor = new TextureRegionActor(common.assets.atlasRegions.redDot.getInstance());
            actor.setActorOrigin(ActorOrigin.Center);
            redDots.add(actor);
        }
    }

    public void show()
    {
        isActive = true;
    }

    public void hide()
    {
//        isActive = false;
    }

    public void setPower(float power)
    {
        this.power = power;
    }

    public void update(GameTime gameTime)
    {
        if (isActive)
        {
            float x = 113;
            float y = 272;

            float vX = power * MathUtils.cosDeg(angle);
            float vY = -(power * MathUtils.sinDeg(angle));

            int i = 0;
            float time;
            float frameDelta = 1f / 40f;
            float frameDiff = 5f;

            for (TextureRegionActor dot : redDots)
            {
                time = (i * frameDiff) * frameDelta;
                dot.setX(x + vX * time);
                dot.setY(y + (vY * time) + (gravity * 0.5f * (time * time)));
                dot.act(gameTime.delta);
                i++;
            }
        }
    }

    public void draw(SpriteBatch spriteBatch)
    {
        if (isActive)
        {
            for (TextureRegionActor dot : redDots)
            {
                dot.draw(spriteBatch, 1f);
            }
        }
    }
}