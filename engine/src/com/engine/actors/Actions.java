package com.engine.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.*;

public class Actions
{
    static public MoveToAction moveTo (MoveToAction action, float x, float y)
    {
        return moveTo(action, x, y, 0, null);
    }

    static public MoveToAction moveTo (MoveToAction action, float x, float y,
                                       float duration)
    {
        return moveTo(action, x, y, duration, null);
    }

    static public MoveToAction moveTo (MoveToAction action, float x, float y,
                                       float duration, Interpolation interpolation)
    {
        action.reset();
        action.setPosition(x, y);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    static public MoveByAction moveBy (MoveByAction action, float amountX, float amountY)
    {
        return moveBy(action, amountX, amountY, 0, null);
    }

    static public MoveByAction moveBy (MoveByAction action, float amountX, float amountY,
                                       float duration)
    {
        return moveBy(action, amountX, amountY, duration, null);
    }

    static public MoveByAction moveBy (MoveByAction action, float amountX, float amountY,
                                       float duration, Interpolation interpolation)
    {
        action.reset();
        action.setAmount(amountX, amountY);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    static public SizeToAction sizeTo (SizeToAction action, float x, float y)
    {
        return sizeTo(action, x, y, 0, null);
    }

    static public SizeToAction sizeTo (SizeToAction action, float x, float y,
                                       float duration)
    {
        return sizeTo(action, x, y, duration, null);
    }

    static public SizeToAction sizeTo (SizeToAction action, float x, float y,
                                       float duration, Interpolation interpolation)
    {
        action.reset();
        action.setSize(x, y);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    static public SizeByAction sizeBy (SizeByAction action, float amountX, float amountY)
    {
        return sizeBy(action, amountX, amountY, 0, null);
    }

    static public SizeByAction sizeBy (SizeByAction action, float amountX, float amountY, float duration)
    {
        return sizeBy(action, amountX, amountY, duration, null);
    }

    static public SizeByAction sizeBy (SizeByAction action, float amountX, float amountY,
                                       float duration, Interpolation interpolation)
    {
        action.reset();
        action.setAmount(amountX, amountY);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    static public ScaleToAction scaleTo (ScaleToAction action, float x, float y)
    {
        return scaleTo(action, x, y, 0, null);
    }

    static public ScaleToAction scaleTo (ScaleToAction action, float x, float y,
                                         float duration)
    {
        return scaleTo(action, x, y, duration, null);
    }

    static public ScaleToAction scaleTo (ScaleToAction action, float x, float y,
                                         float duration, Interpolation interpolation)
    {
        action.reset();
        action.setScale(x, y);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    static public ScaleByAction scaleBy (ScaleByAction action, float amountX, float amountY)
    {
        return scaleBy(action, amountX, amountY, 0, null);
    }

    static public ScaleByAction scaleBy (ScaleByAction action, float amountX, float amountY,
                                         float duration)
    {
        return scaleBy(action, amountX, amountY, duration, null);
    }

    static public ScaleByAction scaleBy (ScaleByAction action, float amountX, float amountY,
                                         float duration, Interpolation interpolation)
    {
        action.reset();
        action.setAmount(amountX, amountY);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    static public RotateToAction rotateTo (RotateToAction action, float rotation)
    {
        return rotateTo(action, rotation, 0, null);
    }

    static public RotateToAction rotateTo (RotateToAction action, float rotation,
                                           float duration) {
        return rotateTo(action, rotation, duration, null);
    }

    static public RotateToAction rotateTo (RotateToAction action, float rotation,
                                           float duration, Interpolation interpolation)
    {
        action.reset();
        action.setRotation(rotation);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    static public RotateByAction rotateBy (RotateByAction action, float rotationAmount)
    {
        return rotateBy(action, rotationAmount, 0, null);
    }

    static public RotateByAction rotateBy (RotateByAction action, float rotationAmount,
                                           float duration)
    {
        return rotateBy(action, rotationAmount, duration, null);
    }

    static public RotateByAction rotateBy (RotateByAction action, float rotationAmount,
                                           float duration, Interpolation interpolation)
    {
        action.reset();
        action.setAmount(rotationAmount);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    static public ColorAction color (ColorAction action, Color color)
    {
        return color(action, color, 0, null);
    }

    static public ColorAction color (ColorAction action, Color color, float duration)
    {
        return color(action, color, duration, null);
    }

    static public ColorAction color (ColorAction action, Color color, float duration,
                                     Interpolation interpolation)
    {
        action.reset();
        action.setEndColor(color);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    static public AlphaAction alpha (AlphaAction action, float a)
    {
        return alpha(action, a, 0, null);
    }

    static public AlphaAction alpha (AlphaAction action, float a, float duration)
    {
        return alpha(action, a, duration, null);
    }

    static public AlphaAction alpha (AlphaAction action, float a, float duration,
                                     Interpolation interpolation)
    {
        action.reset();
        action.setAlpha(a);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    static public AlphaAction fadeOut (AlphaAction action, float duration)
    {
        return alpha(action, 0, duration, null);
    }

    static public AlphaAction fadeOut (AlphaAction action, float duration,
                                       Interpolation interpolation) {
        action.reset();
        action.setAlpha(0);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    static public AlphaAction fadeIn (AlphaAction action, float duration)
    {
        return alpha(action, 1, duration, null);
    }

    static public AlphaAction fadeIn (AlphaAction action, float duration,
                                      Interpolation interpolation)
    {
        action.reset();
        action.setAlpha(1);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    static public VisibleAction show (VisibleAction action)
    {
        return visible(action, true);
    }

    static public VisibleAction hide (VisibleAction action)
    {
        return visible(action, false);
    }

    static public VisibleAction visible (VisibleAction action, boolean visible)
    {
        action.reset();
        action.setVisible(visible);
        return action;
    }

    static public TouchableAction touchable (TouchableAction action, Touchable touchable)
    {
        action.reset();
        action.setTouchable(touchable);
        return action;
    }

    static public DelayAction delay (DelayAction action, float duration)
    {
        action.reset();
        action.setDuration(duration);
        return action;
    }

    static public DelayAction delay (DelayAction action, float duration, Action delayedAction)
    {
        action.reset();
        action.setDuration(duration);
        action.setAction(delayedAction);
        return action;
    }

    static public TimeScaleAction timeScale (TimeScaleAction action, float scale,
                                             Action scaledAction)
    {
        action.reset();
        action.setScale(scale);
        action.setAction(scaledAction);
        return action;
    }

    static public SequenceAction sequence (SequenceAction sequence, Action action1) 
    {
        sequence.reset();
        sequence.addAction(action1);
        return sequence;
    }

    static public SequenceAction sequence (SequenceAction sequence, 
                                           Action action1, Action action2) 
    {
        sequence.reset();
        sequence.addAction(action1);
        sequence.addAction(action2);
        return sequence;
    }

    static public SequenceAction sequence (SequenceAction sequence, 
                                           Action action1, Action action2, 
                                           Action action3) 
    {
        sequence.reset();
        sequence.addAction(action1);
        sequence.addAction(action2);
        sequence.addAction(action3);
        return sequence;
    }

    static public SequenceAction sequence (SequenceAction sequence, 
                                           Action action1, Action action2, 
                                           Action action3, Action action4) 
    {
        sequence.reset();
        sequence.addAction(action1);
        sequence.addAction(action2);
        sequence.addAction(action3);
        sequence.addAction(action4);
        return sequence;
    }

    static public SequenceAction sequence (SequenceAction sequence, 
                                           Action action1, Action action2, 
                                           Action action3, Action action4, 
                                           Action action5) 
    {
        sequence.reset();
        sequence.addAction(action1);
        sequence.addAction(action2);
        sequence.addAction(action3);
        sequence.addAction(action4);
        sequence.addAction(action5);
        return sequence;
    }

    static public SequenceAction sequence (SequenceAction sequence, Action... actions) 
    {
        sequence.reset();

        for (int i = 0, n = actions.length; i < n; i++)
        {
            sequence.addAction(actions[i]);
        }

        return sequence;
    }

    static public ParallelAction parallel (ParallelAction parallel,
                                           Action action1)
    {
        parallel.reset();
        parallel.addAction(action1);
        return parallel;
    }

    static public ParallelAction parallel (ParallelAction parallel,
                                           Action action1, Action action2)
    {
        parallel.reset();
        parallel.addAction(action1);
        parallel.addAction(action2);
        return parallel;
    }

    static public ParallelAction parallel (ParallelAction parallel,
                                           Action action1, Action action2,
                                           Action action3)
    {
        parallel.reset();
        parallel.addAction(action1);
        parallel.addAction(action2);
        parallel.addAction(action3);
        return parallel;
    }

    static public ParallelAction parallel (ParallelAction parallel,
                                           Action action1, Action action2,
                                           Action action3, Action action4)
    {
        parallel.reset();
        parallel.addAction(action1);
        parallel.addAction(action2);
        parallel.addAction(action3);
        parallel.addAction(action4);
        return parallel;
    }

    static public ParallelAction parallel (ParallelAction parallel,
                                           Action action1, Action action2,
                                           Action action3, Action action4,
                                           Action action5)
    {
        parallel.reset();
        parallel.addAction(action1);
        parallel.addAction(action2);
        parallel.addAction(action3);
        parallel.addAction(action4);
        parallel.addAction(action5);
        return parallel;
    }

    static public ParallelAction parallel (ParallelAction parallel, Action... actions) 
    {
        parallel.reset();

        for (int i = 0, n = actions.length; i < n; i++)
        {
            parallel.addAction(actions[i]);
        }

        return parallel;
    }

    static public RepeatAction repeat (RepeatAction action, int count,
                                       Action repeatedAction)
    {
        action.reset();
        action.setCount(count);
        action.setAction(repeatedAction);
        return action;
    }

    static public RepeatAction forever (RepeatAction action, Action repeatedAction)
    {
        action.reset();
        action.setCount(RepeatAction.FOREVER);
        action.setAction(repeatedAction);
        return action;
    }

    static public RunnableAction run (RunnableAction action, Runnable runnable)
    {
        action.reset();
        action.setRunnable(runnable);
        return action;
    }

    static public AfterAction after (AfterAction action, Action afterAction)
    {
        action.reset();
        action.setAction(afterAction);
        return action;
    }
}