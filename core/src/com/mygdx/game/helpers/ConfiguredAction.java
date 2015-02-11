package com.mygdx.game.helpers;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.engine.actors.Actions;

public class ConfiguredAction
{
    private SequenceAction sequence;
    private DelayAction delay;
    private ParallelAction parallel;
    private MoveToAction moveTo;
    private AlphaAction fade;

    public ConfiguredAction()
    {
        sequence = new SequenceAction();
        delay = new DelayAction();
        parallel = new ParallelAction();
        moveTo = new MoveToAction();
        fade = new AlphaAction();
    }

    public Action show(float delay, float duration, float x, float y)
    {
        return Actions.sequence(sequence,
                Actions.delay(this.delay, delay),
                Actions.parallel(parallel,
                        Actions.moveTo(moveTo, x, y, duration, Interpolation.exp10Out),
                        Actions.fadeIn(fade, duration, Interpolation.linear)));
    }

    public Action hide(float delay, float duration, float x, float y)
    {
        return Actions.sequence(sequence,
                Actions.delay(this.delay, delay),
                Actions.parallel(parallel,
                        Actions.moveTo(moveTo, x, y, duration, Interpolation.exp5),
                        Actions.fadeOut(fade, duration, Interpolation.linear)));
    }
}
