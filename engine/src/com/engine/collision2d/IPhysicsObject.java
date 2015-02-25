package com.engine.collision2d;

public interface IPhysicsObject
{
    /**
     * @param elapsed Tiempo transcurrido en total
     * @param delta Delta utilizado para realizar el step
     */
    public void step(float elapsed, float delta);
}
