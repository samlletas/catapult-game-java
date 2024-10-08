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

    public boolean contains(Projection other)
    {
        return other.min >= min && other.max <= max;
    }

    public static Projection max(Projection a, Projection b)
    {
        if (a.max > b.max)
        {
            return a;
        }
        else
        {
            return b;
        }
    }

    public static Projection min(Projection a, Projection b)
    {
        if (a.max > b.max)
        {
            return b;
        }
        else
        {
            return a;
        }
    }

    public float getOverlap(Projection other)
    {
        // Containment
        if (this.contains(other))
        {
            return (other.max - other.min) + getMinContainmentDiff(other);
        }
        else if (other.contains(this))
        {
            return (max - min) + getMinContainmentDiff(other);
        }
        // Overlap sobre el límite superior
        if (other.min > min)
        {
            return max - other.min;
        }
        // Overlap sobre el límite inferior
        else
        {
            return other.max - min;
        }
    }

    private float getMinContainmentDiff(Projection other)
    {
        float mins = Math.abs(min - other.min);
        float maxs = Math.abs(max - other.max);

        return Math.min(mins, maxs);
    }
}
