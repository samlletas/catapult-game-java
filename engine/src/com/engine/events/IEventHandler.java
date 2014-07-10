package com.engine.events;

public interface IEventHandler<T extends EventsArgs>
{
    void onAction(T args);
}
