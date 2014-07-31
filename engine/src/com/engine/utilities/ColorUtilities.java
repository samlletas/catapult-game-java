package com.engine.utilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public final class ColorUtilities
{
    /**
     * Crea un color con los valores RGBA especificados en int dentro del
     * rango (0 a 255)
     *
     * @param r
     * @param g
     * @param b
     * @param a
     * @return
     */
    public static Color createColor(int r, int g, int b, int a)
    {
        return new Color(
                intToFloat(r),
                intToFloat(g),
                intToFloat(b),
                intToFloat(a));
    }

    /**
     * Convierte un valor RGBA en int a su valor respectivo en float
     *
     * @param color Valor del color dentro del rango de (0 a 255)
     * @return Valor del color en el rango (0.0 a 1.0)
     */
    public static float intToFloat(int color)
    {
        return (float)color / 255f;
    }

    /**
     * Convierte un valor RGBA en float a su valor respectivo en int
     *
     * @param color Valor del color dentro del rango de (0.0 a 1.0)
     * @return Valor del color en el rango (0 a 255)
     */
    public static int floatToInt(float color)
    {
        return (int)(color * 255f);
    }

    /**
     * Asigna un tinte a un spritebatch con el color RGBA especificado
     *
     * @param spriteBatch
     * @param r Red
     * @param g Green
     * @param b Blue
     * @param a Alpha
     */
    public static void setTint(SpriteBatch spriteBatch, int r, int g, int b,
                               int a)
    {
        spriteBatch.setColor(
                intToFloat(r),
                intToFloat(g),
                intToFloat(b),
                intToFloat(a));
    }

    /**
     * Asigna un tinte a un spritebatch con el color RGBA especificado
     *
     * @param spriteBatch
     * @param tint Tinte
     */
    public static void setTint(SpriteBatch spriteBatch, Color tint)
    {
        spriteBatch.setColor(tint);
    }

    /**
     * Reinicia el tinte a un spritebatch
     *
     * @param spriteBatch
     */
    public static void resetTint(SpriteBatch spriteBatch)
    {
        spriteBatch.setColor(1f, 1f, 1f, 1f);
    }
}
