package com.engine.utilities;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;

public class ActorUtilities
{
    private static Action action = new MoveByAction();

    public static void growActionsArray(Actor actor, int n)
    {
        for (int i = 0; i < n; i++)
        {
            actor.addAction(action);
        }

        actor.clearActions();
    }
}
