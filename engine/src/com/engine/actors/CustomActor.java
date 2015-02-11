package com.engine.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class CustomActor extends Actor
{
    private ActorOrigin actorOrigin = ActorOrigin.Custom;

    public ActorOrigin getActorOrigin()
    {
        return actorOrigin;
    }

    public void setActorOrigin(ActorOrigin actorOrigin)
    {
        this.actorOrigin = actorOrigin;
    }
}
