package com.engine.collision2d;

/**
 * Representa la proyección de un polígono sobre un eje
 */
public class Projection
{
    float min;
    float max;

    public Projection()
    {
        min = 0f;
        max = 0f;
    }

    /**
     * Revisa si la proyección tiene un overlap con otra proyección
     * @param other
     * @return true en caso de existir overlap, false en caso contrario
     */
    public boolean overlaps(Projection other)
    {
        return !(other.max < min || other.min > max);
    }
}
