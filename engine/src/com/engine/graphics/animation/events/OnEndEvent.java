package com.engine.graphics.animation.events;

import com.engine.events.Event;
import com.engine.graphics.animation.Animation;

public class OnEndEvent extends Event<IAnimationHandler>
{
    public void invoke(Animation animation)
    {
        if (handlers != null)
        {
            for (IAnimationHandler handler : handlers)
            {
                handler.onEnd(animation);
            }
        }
    }
}
