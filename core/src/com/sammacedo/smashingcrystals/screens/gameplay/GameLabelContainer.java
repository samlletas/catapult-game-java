package com.sammacedo.smashingcrystals.screens.gameplay;

import com.badlogic.gdx.graphics.Color;
import com.engine.GameTime;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.utilities.FastArray;

public class GameLabelContainer
{
    private FastArray<GameLabel> labels;

    public GameLabelContainer(int max)
    {
        labels = new FastArray<GameLabel>(max);

        for (int i = 0; i < max; i++)
        {
            labels.add(new GameLabel());
        }
    }

    public void reset()
    {
        for (GameLabel label : labels)
        {
            label.reset();
        }
    }

    private GameLabel getFreeLabel()
    {
        for (GameLabel label : labels)
        {
            if (!label.isActive())
            {
                return label;
            }
        }

        return null;
    }

    public void showNormal(String text, float duration, float x, float y,
                     float destX, float destY, Runnable onFinish)
    {
        GameLabel label = getFreeLabel();

        if (label != null)
        {
            label.showNormal(text, x, y, destX, destY, duration, onFinish);
        }
    }

    public void showFlashing(String text, float duration, float x, float y,
                     float destX, float destY, Runnable onFinish, Color color)
    {
        GameLabel label = getFreeLabel();

        if (label != null)
        {
            label.showFlashing(text, x, y, destX, destY, duration, onFinish, color);
        }
    }

    public void update(GameTime gameTime)
    {
        for (GameLabel label : labels)
        {
            label.act(gameTime.delta);
        }
    }

    public void drawText(DistanceFieldRenderer renderer, DistanceFieldFont font)
    {
        for (GameLabel label : labels)
        {
            label.drawText(renderer, font);
        }
    }
}
