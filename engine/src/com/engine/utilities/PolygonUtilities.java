package com.engine.utilities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.engine.GameAdapter;

public class PolygonUtilities
{
    public static Polygon createRectangle(int width, int height)
    {
        int halfWidth = width / 2;
        int halfHeight = height / 2;

        float[] vertices =
        {
                -halfWidth, -halfHeight, // Esquina superior izquierda
                 halfWidth, -halfHeight, // Esquina superior derecha
                 halfWidth,  halfHeight, // Esquina inferior derecha
                -halfWidth,  halfHeight  // Esquina inferior izquierda
        };

        return new Polygon(vertices);
    }

    public static Polygon createTriangle(int base, int height)
    {
        int halfBase = base / 2;
        int halfHeight = height / 2;

        float[] vertices =
                {
                        -halfBase, halfHeight,  // Esquina izquierda de base
                                0, -halfHeight, // Punta
                         halfBase, halfHeight   // Esquina derecha de base
                };

        return new Polygon(vertices);
    }

    public static Polygon createRhombus(int length, int height)
    {
        int halfLength = length / 2;
        int halfHeight = height / 2;

        float[] vertices =
                {
                        -halfLength, 0,           // Esquina horizontal izquierda
                                  0, -halfHeight, // Esquina vertical derecha
                         halfLength, 0,           // Esquina horizontal derecha
                                  0,  halfHeight  // Esquina vertical izquierda
                };

        return new Polygon(vertices);
    }

    public static Polygon createConvex(int points, int radius)
    {
        if (points < 2)
        {
            throw new GdxRuntimeException("polygons must contain at least 3 points.");
        }

        float[] vertices = new float[points * 2];
        float angleDiff = 360f / points;
        float angle;

        for (int i = 0; i < vertices.length; i+=2)
        {
            angle = 90f + angleDiff * (i / 2);

            vertices[i] = radius * MathUtils.cosDeg(angle);
            vertices[i + 1] = radius * MathUtils.sinDeg(-angle);
        }

        return new Polygon(vertices);
    }

    private static Vector2 axis = new Vector2();
    private static Vector2 point1 = new Vector2();
    private static Vector2 point2 = new Vector2();
    private static Vector2 projectionA = new Vector2();
    private static Vector2 projectionB = new Vector2();

    public static boolean onCollision(Polygon a, Polygon b)
    {
        float[] verticesA = a.getTransformedVertices();
        float[] verticesB = b.getTransformedVertices();

        // Polígono a
        for (int i = 0; i < verticesA.length; i+=2)
        {
            point1.set(verticesA[i], verticesA[i + 1]);

            // Ultimo vertice
            if (i == verticesA.length - 2)
            {
                point2.set(verticesA[0], verticesA[1]);
            }
            else
            {
                point2.set(verticesA[i + 2], verticesA[i + 3]);
            }

            point2.sub(point1);
            axis.set(-point2.y, point2.x);

            projectionA = getProjection(projectionA, a, axis);
            projectionB = getProjection(projectionB, b, axis);

            if (!overlaps(projectionA, projectionB))
            {
                return  false;
            }
        }

        // Polígono b
        for (int i = 0; i < verticesB.length; i+=2)
        {
            point1.set(verticesB[i], verticesB[i + 1]);

            // Ultimo vertice
            if (i == verticesB.length - 2)
            {
                point2.set(verticesB[0], verticesB[1]);
            }
            else
            {
                point2.set(verticesB[i + 2], verticesB[i + 3]);
            }

            point2.sub(point1);
            axis.set(-point2.y, point2.x);

            projectionA = getProjection(projectionA, a, axis);
            projectionB = getProjection(projectionB, b, axis);

            if (!overlaps(projectionA, projectionB))
            {
                return  false;
            }
        }

        return true;
    }

    private static Vector2 getProjection(Vector2 projection, Polygon polygon, Vector2 axis)
    {
        float[] vertices = polygon.getTransformedVertices();
        float min = axis.dot(vertices[0], vertices[1]);
        float max = min;
        float dot;

        for (int i = 2; i < vertices.length; i+=2)
        {
            dot = axis.dot(vertices[i], vertices[i + 1]);

            if (dot < min)
            {
                min = dot;
            }
            else if (dot > max)
            {
                max = dot;
            }
        }

        projection.set(min, max);
        return projection;
    }

    private static boolean overlaps(Vector2 projectionA, Vector2 projectionB)
    {
        return !(projectionB.y < projectionA.x || projectionB.x > projectionA.y);
    }

    public static void draw(SpriteBatch spriteBatch, Polygon polygon, Color color)
    {
        float[] vertices = polygon.getTransformedVertices();
        int size = vertices.length;

        float x1;
        float y1;

        float x2;
        float y2;

        float dx;
        float dy;
        float angle;
        float distance;

        for (int i = 0; i < size; i+=2)
        {
            // Ultimo vértice
            if (i == size - 2)
            {
                x1 = vertices[i];
                x2 = vertices[0];

                y1 = vertices[i + 1];
                y2 = vertices[1];
            }
            else
            {
                x1 = vertices[i];
                x2 = vertices[i + 2];

                y1 = vertices[i + 1];
                y2 = vertices[i + 3];
            }

            dx = x2 - x1;
            dy = y2 - y1;

            angle = (float)Math.toDegrees(MathUtils.atan2(dy, dx));
            distance = (float)Math.sqrt((dx * dx) + (dy * dy));

            ColorUtilities.setTint(spriteBatch, color);

            spriteBatch.draw(GameAdapter.dotTexture, x1, y1, 0f, 0f, 1f, 1f,
                    distance, 1f, angle, 0, 0, 1, 1, false, false);

            ColorUtilities.resetTint(spriteBatch);
        }
    }
}
