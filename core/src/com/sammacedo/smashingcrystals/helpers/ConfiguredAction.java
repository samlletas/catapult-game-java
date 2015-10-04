package com.sammacedo.smashingcrystals.helpers;

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
    private ScaleToAction scaleTo;
    private AlphaAction fade;

    public ConfiguredAction()
    {
        sequence = new SequenceAction();
        delay = new DelayAction();
        parallel = new ParallelAction();
        moveTo = new MoveToAction();
        scaleTo = new ScaleToAction();
        fade = new AlphaAction();
    }

    public Action showMovement(float delay, float duration, float x, float y)
    {
        return Actions.sequence(sequence,
                Actions.delay(this.delay, delay),
                Actions.parallel(parallel,
                        Actions.moveTo(moveTo, x, y, duration, Interpolation.exp10Out),
                        Actions.fadeIn(fade, duration, Interpolation.linear)));
    }

    public Action hideMovement(float delay, float duration, float x, float y)
    {
        return Actions.sequence(sequence,
                Actions.delay(this.delay, delay),
                Actions.parallel(parallel,
                        Actions.moveTo(moveTo, x, y, duration, Interpolation.exp5),
                        Actions.fadeOut(fade, duration, Interpolation.linear)));
    }

    public Action showScale(float delay, float duration, float scaleX, float scaleY)
    {
        return Actions.sequence(sequence,
                Actions.delay(this.delay, delay),
                Actions.parallel(parallel,
                        Actions.fadeIn(fade, 0.5f, Interpolation.sineOut),
                        Actions.scaleTo(scaleTo, scaleX, scaleY, duration, Interpolation.swingOut)));
    }

    public Action hideScale(float delay, float duration, float scaleX, float scaleY)
    {
        return Actions.sequence(sequence,
                Actions.delay(this.delay, delay),
                Actions.parallel(parallel,
                        Actions.fadeOut(fade, 0.5f, Interpolation.sineIn),
                        Actions.scaleTo(scaleTo, scaleX, scaleY, duration, Interpolation.swingIn)));
    }
}
