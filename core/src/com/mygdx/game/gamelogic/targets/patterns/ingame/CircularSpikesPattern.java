package com.mygdx.game.gamelogic.targets.patterns.ingame;

import com.badlogic.gdx.math.Vector2;
import com.engine.utilities.FastArray;
import com.engine.GameTime;
import com.mygdx.game.Common;
import com.mygdx.game.gamelogic.targets.Crystal;
import com.mygdx.game.gamelogic.targets.Spike;
import com.mygdx.game.gamelogic.targets.patterns.BasePattern;
import com.mygdx.game.gamelogic.targets.patterns.PatternHelper;

public class CircularSpikesPattern extends BasePattern
{
    private final float distance;
    private final float angularSpeed;
    private final boolean clockwise;
    private final float angleDifference;

    private float angle;
    private Crystal crystal;

    public CircularSpikesPattern(Common common, FastArray<Crystal> crystals, FastArray<Spike> spikes,
                                   int spikeCount, float distance,
                                   float angularSpeed, boolean clockwise)
    {
        super(common, crystals, spikes, 1, spikeCount,
                400, 700, 100, 300);

        this.distance = distance;
        this.angularSpeed = angularSpeed;
        this.clockwise = clockwise;
        this.angleDifference = 360f / (float)spikeCount;
    }

    @Override
    protected void initializePositions(int startX, int startY)
    {
        this.angle = 0;

        crystal = crystals.get(0);
        crystal.floating = true;
        crystal.start();
        crystal.setPosition(startX, startY);
        crystal.setSpeed(0f, 0f);

        int i = 0;

        for (Spike spike : spikes)
        {
            spike.floating = false;
            spike.start();
            spike.setPosition(PatternHelper.travelFrom(startX, startY,
                    i * angleDifference, distance));

            i++;
        }
    }

    @Override
    protected void onUpdateStart(GameTime gameTime)
    {
        if (clockwise)
        {
            angle -= angularSpeed * gameTime.delta;
        }
        else
        {
            angle += angularSpeed * gameTime.delta;
        }
    }

    @Override
    protected void updateCrystal(GameTime gameTime, Crystal crystal, int index)
    {

    }

    @Override
    protected void updateSpike(GameTime gameTime, Spike spike, int index)
    {
        Vector2 nextPosition = PatternHelper.travelFrom(this.crystal.getX(), this.crystal.getY(),
                angle + (index * angleDifference), distance);

        float speedX = nextPosition.x - spike.getX();
        float speedY = nextPosition.y - spike.getY();

        spike.setSpeed(speedX, speedY);
    }
}
