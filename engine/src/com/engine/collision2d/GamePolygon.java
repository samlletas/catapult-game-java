package com.engine.collision2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.engine.GameAdapter;
import com.engine.utilities.ColorUtilities;

/**
 * Representa un polígono en 2D que permite revisión de colisiones
 */
public class GamePolygon extends Polygon
{
    private Projection cachedProjection = new Projection();
    private Vector2 cachedVector = new Vector2();

    private Array<Vector2> normals;

    public boolean isSolid;
    public boolean checkCollisions;

    public Vector2 speed = new Vector2();

    private float maxTEnter = -Float.MAX_VALUE;
    private float minTLeave = Float.MAX_VALUE;

    /**
     * Construye un polígono sin vértices
     */
    public GamePolygon()
    {
        this(new float[0]);
    }

    /**
     * Construye un polígono con los vértices especificados
     * @param vertices Arreglo en donde en cada par de elementos el primero
     *                 representa el componente en X y el segundo en Y
     */
    public GamePolygon(float[] vertices)
    {
        super(vertices);

        this.isSolid = false;
        this.checkCollisions = true;

        calculateNormals(vertices);
    }

    /**
     * Calcula las normales para cada uno de los lados del polígono (ignorando
     * las normales paralelas para optimizar la revisión de colisiones)
     * @param vertices
     */
    private void calculateNormals(float[] vertices)
    {
        normals = new Array<Vector2>(vertices.length / 2);

        float x1;
        float y1;

        float x2;
        float y2;

        boolean foundParallel;
        int size = vertices.length;

        for (int i = 0; i < size; i+= 2)
        {
            x1 = vertices[i];
            y1 = vertices[i + 1];

            // Ultimo vértice
            if (i == size - 2)
            {
                x2 = vertices[0];
                y2 = vertices[1];
            }
            else
            {
                x2 = vertices[i + 2];
                y2 = vertices[i + 3];
            }

            // Coordenadas del vector edge (Lado o línea conformada por el
            // vértice actual y el siguiente)
            x2 -= x1;
            y2 -= y1;

            if (normals.size == 0)
            {
                normals.add(new Vector2(-y2, x2).nor());
            }
            else
            {
                foundParallel = false;

                // Búsqueda de normales paralelas
                for (Vector2 normal : normals)
                {
                    cachedVector.set(-y2, x2).nor();

                    if (cachedVector.isOnLine(normal))
                    {
                        foundParallel = true;
                        break;
                    }
                }

                // Solamente agregar si no hay normales paralelas
                if (!foundParallel)
                {
                    normals.add(new Vector2(-y2, x2).nor());
                }
            }
        }

        Gdx.app.log("Axes: ", String.valueOf(normals.size));
    }

    @Override
    public void setVertices(float[] vertices)
    {
        throw new GdxRuntimeException("setVertices is not supported on GamePolygon");
    }

    @Override
    public void setRotation(float degrees)
    {
        super.setRotation(degrees);
        Array<Vector2> localNormals = normals;

        // Rotación de las normales
        for (Vector2 normal : localNormals)
        {
            normal.setAngle(degrees);
        }
    }

    @Override
    public void rotate(float degrees)
    {
        super.rotate(degrees);
        Array<Vector2> localNormals = normals;

        // Rotación de las normales
        for (Vector2 normal : localNormals)
        {
            normal.rotate(degrees);
        }
    }

    /**
     * Revisa si el polígono se encuentra en intersección con otro polígono
     * convexo utilizando
     * <a href="www.codezealot.org/archives/55">SAT (Separation Axis Theorem)</a>
     * @param other Polígono con el cual se revisará si hay interseción
     * @return true en caso de haber intersección, false en caso contrario
     */
    public boolean onCollision(GamePolygon other)
    {
        cachedVector.setZero();
        maxTEnter = 0;
        minTLeave = Float.MAX_VALUE;

        Gdx.app.log("", "--BEGIN--");

        if (checkCollisions && checkProjections(this, other) &&
                checkProjections(other, this))
        {
            speed.x *= maxTEnter;
            speed.y *= maxTEnter;

            Gdx.app.log("", "MAX TIME ENTER:" + maxTEnter);

            return true;
        }

        Gdx.app.log("", "MAX TIME ENTER:" + maxTEnter);

        return false;
    }

    /**
     * Revisa si hay un overlap de las proyecciones de 2 polígonos sobre las
     * normales de cada uno de los lados del polígono
     * @return true en caso de haber un overlap, false en caso contrario
     */
    private boolean checkProjections(GamePolygon a, GamePolygon b)
    {
        Array<Vector2> localNormals = a.normals;

        float vx = a.speed.x - b.speed.x;
        float vy = a.speed.y - b.speed.y;

        float speedProjection;

        float minDistance;
        float maxDistance;

        float tEnter;
        float tLeave;

        // Recorre los vértices del polígono base para obtener las normales
        // de cada uno de los lados y realizar la verificación de overlap
        // de proyecciones
        for (Vector2 normal : localNormals)
        {
//            normal = localNormals.get(0);
//            Gdx.app.log("","X:" + normal.x + ", Y:" + normal.y);

            Projection projectionA = a.getProjection(normal);
            Projection projectionB = b.getProjection(normal);

            speedProjection = normal.dot(vx, vy);

            minDistance = projectionB.min - projectionA.max;
            maxDistance = projectionB.max - projectionA.min;

            if (Math.abs(minDistance) > Math.abs(maxDistance))
            {
                float hold = minDistance;
                minDistance = maxDistance;
                maxDistance = hold;
            }

            tEnter = minDistance / speedProjection;
            tLeave = maxDistance / speedProjection;

            Gdx.app.log("", "Time Enter:" + tEnter);
            Gdx.app.log("", "Time Leave:" + tLeave);

            // Predicción
            if (!projectionA.overlaps(projectionB))
            {
                if (tEnter < 0f || tEnter > 1f)
                {
                    return false;
                }

                if (tEnter > maxTEnter)
                {
                    maxTEnter = tEnter;

                    cachedVector.set(normal);
                }

                minTLeave = Math.min(minTLeave, tLeave);
            }
            else
            {
                // En línea
                if (tEnter == 0f)
                {
                    if (tLeave < 0f)
                    {
                        return false;
                    }

                    if (tEnter > maxTEnter)
                    {
                        maxTEnter = tEnter;

                        cachedVector.set(normal);
                    }

                    minTLeave = Math.min(minTLeave, tLeave);
                }
                // Overlapping
                else
                {
                    if ((tEnter >= 0f && tEnter < 1f))
                    {
                        return false;
                    }
                }
            }
        }

        // Si hay overlap en todas las proyecciones entonces hay intersección
        return true;
    }

    /**
     * Regresa la proyección del polígono sobre un eje dado
     * @param axis Eje en el cual realizar la proyección
     * @return Proyección resultante
     */
    public Projection getProjection(Vector2 axis)
    {
        float[] vertices = getTransformedVertices();
        int size = vertices.length;
        float min = axis.dot(vertices[0], vertices[1]);
        float max = min;
        float dot;

        for (int i = 2; i < size; i+=2)
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

        cachedProjection.min = min;
        cachedProjection.max = max;

        return cachedProjection;
    }

    /**
     * Dibuja el polígono
     * @param spriteBatch
     * @param color Color de dibujado de las líneas que conforman cada uno
     *              de los lados del polígono
     */
    public void draw(SpriteBatch spriteBatch, Color color)
    {
        float[] vertices = getTransformedVertices();
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
            x1 = vertices[i];
            y1 = vertices[i + 1];

            // Ultimo vértice
            if (i == size - 2)
            {
                x2 = vertices[0];
                y2 = vertices[1];
            }
            else
            {
                x2 = vertices[i + 2];
                y2 = vertices[i + 3];
            }

            dx = x2 - x1;
            dy = y2 - y1;

            // Cálculo del ángulo y distancia entre el vértice actual y el
            // siguiente
            angle = (float)Math.toDegrees(MathUtils.atan2(dy, dx));
            distance = (float)Math.sqrt((dx * dx) + (dy * dy));

            ColorUtilities.setTint(spriteBatch, color);

            spriteBatch.draw(GameAdapter.dotTexture, x1, y1, 0f, 0f, 1f, 1f,
                    distance, 1f, angle, 0, 0, 1, 1, false, false);

            ColorUtilities.resetTint(spriteBatch);
        }
    }

    public static GamePolygon createRectangle(int width, int height)
    {
        int halfWidth = width / 2;
        int halfHeight = height / 2;

        float[] vertices =
        {
                -halfWidth, -halfHeight, // Esquina superior izquierda
                halfWidth, -halfHeight,  // Esquina superior derecha
                halfWidth,  halfHeight,  // Esquina inferior derecha
                -halfWidth,  halfHeight  // Esquina inferior izquierda
        };

        return new GamePolygon(vertices);
    }

    public static GamePolygon createTriangle(int base, int height)
    {
        int halfBase = base / 2;
        int halfHeight = height / 2;

        float[] vertices =
        {
                -halfBase, halfHeight, // Esquina izquierda de base
                0, -halfHeight,        // Punta
                halfBase, halfHeight   // Esquina derecha de base
        };

        return new GamePolygon(vertices);
    }

    public static GamePolygon createRhombus(int length, int height)
    {
        int halfLength = length / 2;
        int halfHeight = height / 2;

        float[] vertices =
        {
                -halfLength, 0, // Esquina horizontal izquierda
                0, -halfHeight, // Esquina vertical derecha
                halfLength, 0,  // Esquina horizontal derecha
                0,  halfHeight  // Esquina vertical izquierda
        };

        return new GamePolygon(vertices);
    }

    /**
     * Crea un polígono convexo (3 puntos = Triángulo, 6 = Hexágono, etc)
     * @param points Cantidad de puntos de la figura
     * @param radius Distancia de cada punto al centro de la figura
     * @return Nuevo polígono convexo
     */
    public static GamePolygon createConvex(int points, int radius)
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

        return new GamePolygon(vertices);
    }

    @Override
    public float[] getTransformedVertices()
    {
        dirty();
        return super.getTransformedVertices();
    }
}
