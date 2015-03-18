package com.mygdx.game.screens.gameplay;

import com.engine.GameTime;
import com.engine.events.Event;
import com.engine.events.IEventHandler;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.utilities.FastArray;
import com.mygdx.game.gamelogic.CrystalManager;
import com.mygdx.game.gamelogic.targets.patterns.TargetCollisionArgs;

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

    public void show(String text, float x, float y, float destX, float destY,
                     Runnable onFinish)
    {
        show(text, x, y, destX, destY, 0.3f, onFinish);
    }

    public void show(String text, float x, float y, float destX, float destY,
                     float duration, Runnable onFinish)
    {
        GameLabel label = getFreeLabel();

        if (label != null)
        {
            label.show(text, x, y, destX, destY, duration, onFinish);
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
