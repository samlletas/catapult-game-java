package com.mygdx.game.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.engine.GameTime;
import com.engine.graphics.animation.AnimationPlayer;
import com.engine.graphics.animation.Bone;
import com.mygdx.game.assets.GameAssets;

public final class Catapult
{
    private Ball ball;
    private AnimationPlayer player;
    private Bone spoon;

    public Catapult(Ball ball)
    {
        this.ball = ball;

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

        spoon = player.getBone(1);
    }

    public void update(GameTime gameTime)
    {
        player.update(gameTime);
        setBallPosition();
    }

    private void setBallPosition()
    {
        Vector2 position = spoon.getTransformedPosition(98f, 25f);

        ball.x = position.x - 17;
        ball.y = position.y - 17;
    }

    public void draw(SpriteBatch spriteBatch)
    {
        player.draw(spriteBatch);
    }
}
