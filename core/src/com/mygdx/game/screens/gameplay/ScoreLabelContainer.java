package com.mygdx.game.screens.gameplay;

import com.engine.GameTime;
import com.engine.events.Event;
import com.engine.events.IEventHandler;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.utilities.FastArray;
import com.mygdx.game.gamelogic.CrystalManager;
import com.mygdx.game.gamelogic.GameplayData;
import com.mygdx.game.gamelogic.targets.patterns.TargetCollisionArgs;

public class ScoreLabelContainer
{
    private FastArray<ScoreLabel> scoreLabels;
    private FastArray<ScoreLabel> chanceLabels;

    public Event<TargetCollisionArgs> onScoreLabelFinished;
    public Event<TargetCollisionArgs> onChanceLabelFinished;

    IEventHandler<TargetCollisionArgs> scoreHandler;
    IEventHandler<TargetCollisionArgs> chanceHandler;

    public ScoreLabelContainer()
    {
        scoreLabels = new FastArray<ScoreLabel>(CrystalManager.MAX_CRYSTALS);
        chanceLabels = new FastArray<ScoreLabel>(GameplayData.MAX_LIVES);

        for (int i = 0; i < CrystalManager.MAX_CRYSTALS; i++)
        {
            scoreLabels.add(new ScoreLabel());
        }

        for (int i = 0; i < GameplayData.MAX_LIVES; i++)
        {
            chanceLabels.add(new ScoreLabel());
        }

        onScoreLabelFinished = new Event<TargetCollisionArgs>();
        onChanceLabelFinished = new Event<TargetCollisionArgs>();

        scoreHandler = new IEventHandler<TargetCollisionArgs>()
        {
            @Override
            public void onAction(TargetCollisionArgs args)
            {
                onScoreLabelFinished.invoke(args);
            }
        };

        chanceHandler = new IEventHandler<TargetCollisionArgs>()
        {
            @Override
            public void onAction(TargetCollisionArgs args)
            {
                onChanceLabelFinished.invoke(args);
            }
        };

        for (ScoreLabel label : scoreLabels)
        {
            label.onFinish.subscribe(scoreHandler);
        }

        for (ScoreLabel label : chanceLabels)
        {
            label.onFinish.subscribe(chanceHandler);
        }
    }

    private ScoreLabel getFreeLabel(FastArray<ScoreLabel> array)
    {
        for (ScoreLabel label : array)
        {
            if (!label.isActive())
            {
                return label;
            }
        }

        return null;
    }

    public void showScoreLabel(TargetCollisionArgs args, float destX, float destY)
    {
        ScoreLabel label = getFreeLabel(scoreLabels);

        if (label != null)
        {
            label.show(args, destX, destY);
        }
    }

    public void showChanceLabel(TargetCollisionArgs args, float destX, float destY)
    {
        ScoreLabel label = getFreeLabel(chanceLabels);

        if (label != null)
        {
            label.show(args, destX, destY);
        }
    }

    public void update(GameTime gameTime)
    {
        for (ScoreLabel label : scoreLabels)
        {
            label.act(gameTime.delta);
        }

        for (ScoreLabel label : chanceLabels)
        {
            label.act(gameTime.delta);
        }
    }

    public void drawText(DistanceFieldRenderer renderer, DistanceFieldFont font)
    {
        for (ScoreLabel label : scoreLabels)
        {
            label.drawText(renderer, font);
        }

        for (ScoreLabel label : chanceLabels)
        {
            label.drawText(renderer, font);
        }
    }
}
