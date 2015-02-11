package com.mygdx.game.gamelogic.targets.patterns;

import com.engine.events.EventsArgs;
import com.mygdx.game.gamelogic.targets.Target;

public class TargetCollisionArgs extends EventsArgs
{
    public Target target;
    public int score;
    public float special;
}
