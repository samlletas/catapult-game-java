package com.sammacedo.smashingcrystals.gamelogic.targets.patterns.ingame;

import com.badlogic.gdx.math.Vector2;
import com.engine.utilities.FastArray;
import com.sammacedo.smashingcrystals.Common;
import com.sammacedo.smashingcrystals.gamelogic.targets.Crystal;
import com.sammacedo.smashingcrystals.gamelogic.targets.Spike;
import com.sammacedo.smashingcrystals.gamelogic.targets.patterns.BasePattern;
import com.sammacedo.smashingcrystals.gamelogic.targets.patterns.PatternHelper;

public class CircularCrystalsPattern extends BasePattern
{
    private final float distance;
    private final float angularSpeed;
    private final boolean clockwise;
    private final float angleDifference;

    private float angle;
    private Spike spike;

    public CircularCrystalsPattern(Common common, FastArray<Crystal> crystals, FastArray<Spike> spikes,
                                   int crystalCount, float distance,
                                   float angularSpeed, boolean clockwise)
    {
        super(common, crystals, spikes, crystalCount, 1,
               400, 700, 100, 300);

        this.distance = distance;
        this.angularSpeed = angularSpeed;
        this.clockwise = clockwise;
        this.angleDifference = 360f / (float)crystalCount;
    }

    @Override
    protected void initializePositions(int startX, int startY)
    {
        this.angle = 0;

        spike = spikes.get(0);
        spike.floating = true;
        spike.start();
        spike.setPosition(startX, startY);
        spike.setSpeed(0f, 0f);

        int i = 0;

        for (Crystal crystal : crystals)
        {
            crystal.floating = false;
            crystal.start();
            crystal.setPosition(PatternHelper.travelFrom(startX, startY,
                    i * angleDifference, distance));

            i++;
        }
    }

    @Override
    protected void onStepStart(float elapsed, float delta)
    {
        if (clockwise)
        {
            angle -= angularSpeed * delta;
        }
        else
        {
            angle += angularSpeed * delta;
        }
    }

    @Override
    protected void stepCrystal(float elapsed, float delta, Crystal crystal, int index)
    {
        Vector2 nextPosition = PatternHelper.travelFrom(spike.getX(), spike.getY(),
                angle + (index * angleDifference), distance);

        float speedX = nextPosition.x - crystal.getX();
        float speedY = nextPosition.y - crystal.getY();

        crystal.setSpeed(speedX, speedY);
    }

    @Override
    protected void stepSpike(float elapsed, float delta, Spike spike, int index)
    {

    }
}
