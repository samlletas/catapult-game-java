package com.engine.utilities;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;

public class StageUtilities
{
    public static void enableTouch(Stage stage)
    {
        setTouchable(stage, Touchable.enabled);
    }

    public static void disableTouch(Stage stage)
    {
        setTouchable(stage, Touchable.disabled);
    }

    public static void setTouchable(Stage stage, Touchable touchable)
    {
        Array<Actor> actors = stage.getActors();

        for(int i = 0, n = actors.size; i < n; i++)
        {
            actors.get(i).setTouchable(touchable);
        }
    }
}
