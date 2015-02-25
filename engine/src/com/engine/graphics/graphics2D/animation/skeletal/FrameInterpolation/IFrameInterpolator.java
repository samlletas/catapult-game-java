package com.engine.graphics.graphics2D.animation.skeletal.FrameInterpolation;

import com.engine.GameTime;
import com.engine.graphics.graphics2D.animation.skeletal.Frame;

public interface IFrameInterpolator
{
    public void start(Frame current, Frame next, float timeOffset);

    /**
     * Factor de interpolación (entre 0.0 y 1.0) entre el frame actual y
     * el siguiente
     * @return
     */
    public float getFactor();

    /**
     * Si se desea que se avance de manera automática al siguiente frame al
     * terminar la interpolación (Cuando sea 1.0)
     * @return
     */
    public boolean autoAdvanceFrame();

    public void update(GameTime gameTime, float speed);
}
