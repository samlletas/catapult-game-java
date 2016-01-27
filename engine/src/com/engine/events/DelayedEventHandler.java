package com.engine.events;

public abstract class DelayedEventHandler<T extends EventsArgs> implements IEventHandler<T>
{
    private T arguments;
    private boolean hasCallToResolve;

    public DelayedEventHandler()
    {
        this.hasCallToResolve = false;
    }

    @Override
    public final void onAction(T args)
    {
        if (!hasCallToResolve)
        {
            arguments = args;
            hasCallToResolve = true;
        }
    }

    public final void resolve()
    {
        if (hasCallToResolve)
        {
            onResolve(arguments);
            hasCallToResolve = false;
        }
    }

    public abstract void onResolve(T args);
}
