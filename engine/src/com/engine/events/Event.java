package com.engine.events;

import com.badlogic.gdx.utils.Array;

public abstract class Event<T>
{
    protected Array<T> handlers;

    public final void subscribe(T handler)
    {
        if (handlers == null)
        {
            handlers = new Array<T>(8);
        }

        handlers.add(handler);
    }
}
