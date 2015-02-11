package com.engine.utilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;

public final class ColorUtilities
{
    private static Color cachedColor = createColor(0, 0, 0, 0);

    /**
     * Crea una nueva instancia Color a partir de una ya existente
     * @param color color
     * @return Nueva instancia de Color
     */
    public static Color createColor(Color color)
    {
        return new Color(color.r, color.g, color.b, color.a);
    }

    /**
     * Crea una nueva instancia Color con los valores RGBA especificados
     * en int dentro del rango (0 a 255)
     *
     * @param r red
     * @param g green
     * @param b blue
     * @param a alpha
     * @return Nueva instancia de Color
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
     * @param color valor del color dentro del rango de (0 a 255)
     * @return valor del color en el rango (0.0 a 1.0)
     */
    public static float intToFloat(int color)
    {
        return (float)color / 255f;
    }

    /**
     * Convierte un valor RGBA en float a su valor respectivo en int
     *
     * @param color valor del color dentro del rango de (0.0 a 1.0)
     * @return valor del color en el rango (0 a 255)
     */
    public static int floatToInt(float color)
    {
        return (int)(color * 255f);
    }

    /**
     * Asigna el valor del canal alpha de un batch
     *
     * @param batch batch
     * @param alpha valor del canal alpha en el rango (0 a 255)
     */
    public static void setAlpha(Batch batch, int alpha)
    {
        Color color = batch.getColor();
        batch.setColor(color.r, color.g, color.b, intToFloat(alpha));
    }

    /**
     * Asigna el valor del canal alpha de un batch
     *
     * @param batch batch
     * @param alpha valor del canal alpha en el rango (0.0 a 1.0)
     */
    public static void setAlpha(Batch batch, float alpha)
    {
        Color color = batch.getColor();
        batch.setColor(color.r, color.g, color.b, alpha);
    }

    /**
     * Asigna un color a un batch con el color RGBA especificado
     *
     * @param batch batch
     * @param r red
     * @param g green
     * @param b blue
     * @param a alpha
     */
    public static void setColor(Batch batch, int r, int g, int b,
                                int a)
    {
        batch.setColor(
                intToFloat(r),
                intToFloat(g),
                intToFloat(b),
                intToFloat(a));
    }

    /**
     * Asigna un color a un batch con el color RGBA especificado
     *
     * @param batch batch
     * @param r red
     * @param g green
     * @param b blue
     * @param a alpha
     */
    public static void setColor(Batch batch, float r, float g,
                                float b, float a)
    {
        batch.setColor(r, g, b, a);
    }

    /**
     * Asigna un color a un batch con el color RGBA especificado
     *
     * @param batch batch
     * @param color color
     */
    public static void setColor(Batch batch, Color color)
    {
        batch.setColor(color);
    }

    /**
     * Reinicia el color de un batch
     *
     * @param batch batch
     */
    public static void resetColor(Batch batch)
    {
        batch.setColor(1f, 1f, 1f, 1f);
    }

    /**
     * Configura un batch para dibujar on additive blending
     *
     * @param batch batch
     */
    public static void setAdditiveBlending(Batch batch)
    {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
    }

    /**
     * Configura un batch para dibujar con alpha blending
     *
     * @param batch batch
     */
    public static void setAlphaBlending(Batch batch)
    {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * Aplica alpha blending de un color sobre otro
     *
     * @param background color de fondo
     * @param foreground color de encima
     * @param blendAlphaChannel si el background no es opaco y se desea hacer
     *                          blend de los canales alpha
     * @return color resultante de aplicar alpha blending (regresa una instancia
     *               cacheada)
     */
    public static Color alphaBlend(final Color background, final Color foreground,
                                   boolean blendAlphaChannel)
    {
        float alpha = foreground.a;

        cachedColor.r = (background.r * (1f - alpha)) + (foreground.r * alpha);
        cachedColor.g = (background.g * (1f - alpha)) + (foreground.g * alpha);
        cachedColor.b = (background.b * (1f - alpha)) + (foreground.b * alpha);

        if (blendAlphaChannel)
        {
            cachedColor.a = foreground.a + background.a * (1f - alpha);
        }

        return cachedColor;
    }
}
