package com.engine.events;

import com.badlogic.gdx.utils.Array;

public class Event<T extends EventsArgs>
{
    protected Array<IEventHandler<T>> handlers;

    public final void invoke(T args)
    {
        Array<IEventHandler<T>> localHandlers = handlers;

        if (localHandlers != null)
        {
            int size = localHandlers.size;

            for (int i = 0; i < size; i++)
            {
                IEventHandler handler = localHandlers.get(i);

                handler.onAction(args);
            }
        }
    }

    public final void subscribe(IEventHandler<T> handler)
    {
        if (handlers == null)
        {
            handlers = new Array<IEventHandler<T>>(8);
        }

        handlers.add(handler);
    }
}
