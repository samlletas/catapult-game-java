package com.sammacedo.smashingcrystals.gamelogic.targets.patterns;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PatternHelper
{
    private static Vector2 cachedVector = new Vector2();

    /**
     *
     * @param startX
     * @param startY
     * @param angle
     * @param distance
     * @return
     */
    public static Vector2 travelFrom(float startX, float startY, float angle,
                                     float distance)
    {
        cachedVector.x = startX + (distance * MathUtils.cosDeg(angle));
        cachedVector.y = startY - (distance * MathUtils.sinDeg(angle));

        return cachedVector;
    }
}
