package com.engine.graphics.graphics2D.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.engine.GameTime;

public class TrailEffect
{
    private Color color;
    private int size;
    private float maxLength;
    private float minAmplitude;
    private float maxAmplitude;
    private float minOpacity;
    private float maxOpacity;
    private float travelSpeed;

    private boolean updatedPosition;
    private float lenght;
    private float distanceTraveled;

    private Vector2[] positions;
    private Vector2 headPosition;
    private Vector2 previousHeadPosition;

    private Mesh mesh;
    private float[] vertices;
    private short[] indices;
    private int idx = 0;

    private ShaderProgram shader;
    private int u_projTransIndex;
    private int u_color;

    private final static int VERTICES_PER_POINT = 2;
    private final static int VERTICES_PER_TRIANGLE = 3;
    private final static int TRIANGLES_PER_RECTANGLE = 2;
    private final static int POSITION_COMPONENTS = 2;
    private final static int OPACITY_COMPONENTS = 1;

    public TrailEffect()
    {
        this(Color.WHITE, 5, 200f, 0f, 25f, 0f, 0.5f, 800f);
    }

    public TrailEffect(Color color, int size, float maxLength,
                       float minAmplitude, float maxAmplitude,
                       float minOpacity, float maxOpacity,
                       float travelSpeed)
    {
        this(null, color, size, maxLength, minAmplitude, maxAmplitude,
                minOpacity, maxOpacity, travelSpeed);
    }

    public TrailEffect(ShaderProgram shader, Color color, int size, float maxLength,
                       float minAmplitude, float maxAmplitude,
                       float minOpacity, float maxOpacity,
                       float travelSpeed)
    {
        if (size < 2)
        {
            throw new GdxRuntimeException("A trail effect must contain at least 2 points");
        }

        this.color = color;
        this.size = size;
        this.maxLength = maxLength;
        this.minAmplitude = minAmplitude;
        this.maxAmplitude = maxAmplitude;
        this.minOpacity = minOpacity;
        this.maxOpacity = maxOpacity;
        this.travelSpeed = travelSpeed;

        updatedPosition = false;
        lenght = 0f;
        distanceTraveled = 0f;

        initPositions();
        initMesh();

        if (shader == null)
        {
            this.shader = getDefaultShader();
        }
        else
        {
            this.shader = shader;
        }

        getUniforms();
    }

    private void initPositions()
    {
        positions = new Vector2[size];

        for (int i = 0; i < size; i++)
        {
            positions[i] = new Vector2();
        }

        headPosition = positions[0];
        previousHeadPosition = new Vector2();
    }

    private void initMesh()
    {
        int rectangles = size - 1;
        int triangles = rectangles * 2;

        int totalVertices = size * VERTICES_PER_POINT;
        int totalIndices = VERTICES_PER_TRIANGLE * TRIANGLES_PER_RECTANGLE * rectangles;

        vertices = new float[totalVertices * (POSITION_COMPONENTS + OPACITY_COMPONENTS)];
        indices = new short[totalIndices];

        int index = 0;

        for (short i = 0; i < triangles; i+=2)
        {
            // Triángulo superior
            indices[index++] = i;
            indices[index++] = (short)(i + 2);
            indices[index++] = (short)(i + 1);

            // Triángulo inferior
            indices[index++] = (short)(i + 1);
            indices[index++] = (short)(i + 3);
            indices[index++] = (short)(i + 2);
        }

        float div = (totalVertices / 2f) - 1f;

        for (int i = 2; i < vertices.length; i+=3)
        {
            vertices[i] = Interpolation.linear.apply(maxOpacity, minOpacity,
                    ((float)(i / 6)) / div);
        }

        mesh = new Mesh(false, true, totalVertices, totalIndices,
                new VertexAttributes(
                        new VertexAttribute(VertexAttributes.Usage.Position,
                                POSITION_COMPONENTS, "a_position"),
                        new VertexAttribute(VertexAttributes.Usage.Generic,
                                OPACITY_COMPONENTS, "a_opacity")));

        mesh.setVertices(vertices);
        mesh.setIndices(indices);
    }

    private ShaderProgram getDefaultShader()
    {
        String vertex =
                "attribute vec2 a_position;\n" +
                "attribute float a_opacity;" +
                "\n" +
                "uniform mat4 u_projTrans;\n" +
                "uniform vec4 u_color;" +
                "\n" +
                "varying vec4 v_color;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    v_color = u_color;\n" +
                "    v_color.a = a_opacity;\n" +
                "\n" +
                "    gl_Position = u_projTrans * vec4(a_position.xy, 0.0, 1.0);\n" +
                "}";

        String fragment =
                "#ifdef GL_ES\n" +
                "    #define LOWP lowp\n" +
                "    precision mediump float;\n" +
                "#else\n" +
                "    #define LOWP\n" +
                "#endif\n" +
                "\n" +
                "varying LOWP vec4 v_color;\n" +
                "varying vec2 v_texCoords;\n" +
                "\n" +
                "uniform sampler2D u_texture;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    gl_FragColor = v_color;\n" +
                "}\n";

        ShaderProgram program = new ShaderProgram(vertex, fragment);

        if (!program.isCompiled())
        {
            throw new GdxRuntimeException(program.getLog());
        }

        return program;
    }

    private void getUniforms()
    {
        u_projTransIndex = shader.getUniformLocation("u_projTrans");
        u_color = shader.getUniformLocation("u_color");
    }

    public void setPosition(float x, float y)
    {
        previousHeadPosition.set(headPosition);

        headPosition.x = x;
        headPosition.y = y;

        updatedPosition = true;
    }

    public void reset(float x, float y)
    {
        Vector2[] localPositions = positions;

        for (int i = 0, n = localPositions.length; i < n; i++)
        {
            localPositions[i].set(x, y);
        }

        previousHeadPosition.set(headPosition);

        updatedPosition = false;
        lenght = 0f;
        distanceTraveled = 0f;
    }

    public void update(GameTime gameTime)
    {
        Vector2[] localPositions = positions;
        Vector2 current;
        Vector2 previous;

        float dx;
        float dy;
        float maxSeparation;
        float separation;
        float headAngle;
        float angle;
        float distance;
        float speed = travelSpeed * gameTime.delta;

        idx = 0;

        dx = previousHeadPosition.x - headPosition.x;
        dy = previousHeadPosition.y - headPosition.y;

        distance = Vector2.len(dx, dy);
        distanceTraveled += distance;

        if (updatedPosition)
        {
            lenght = Math.min(maxLength, distanceTraveled);
        }
        else
        {
            lenght = Math.max(0f, lenght - speed);
        }

        maxSeparation = lenght / (size - 1);
        headAngle = MathUtils.atan2(dy, dx);

        updatePointVertices(headPosition, headAngle, getAmplitude(0) / 2);

        for (int i = 1; i < size; i++)
        {
            previous = localPositions[i - 1];
            current = localPositions[i];

            dx = current.x - previous.x;
            dy = current.y - previous.y;

            separation = Math.min(maxSeparation, Vector2.len(dx, dy));
            angle = MathUtils.atan2(dy, dx);

            current.x = previous.x + separation * MathUtils.cos(angle);
            current.y = previous.y + separation * MathUtils.sin(angle);

            if (maxSeparation == 0f)
            {
                angle = headAngle;
            }

            updatePointVertices(current, angle, getAmplitude(i) / 2);
        }

        updatedPosition = false;
    }

    private float getAmplitude(int pointIndex)
    {
        return Interpolation.linear.apply(maxAmplitude, minAmplitude,
                (float)pointIndex / (float)(size - 1));
    }

    private void updatePointVertices(Vector2 position, float angle, float distance)
    {
        angle -= MathUtils.PI / 2f;

        vertices[idx++] = position.x + distance * MathUtils.cos(angle);
        vertices[idx++] = position.y + distance * MathUtils.sin(angle);
        idx++;

        angle += MathUtils.PI;

        vertices[idx++] = position.x + distance * MathUtils.cos(angle);
        vertices[idx++] = position.y + distance * MathUtils.sin(angle);
        idx++;
    }

    /**
     * Dibuja el efecto
     *
     * @param camera cámara
     * @param additive Indica si se va a dibujar con additive blending
     */
    public void draw(OrthographicCamera camera, boolean additive)
    {
        if (lenght > 0f)
        {
            mesh.setVertices(vertices);

            Gdx.gl.glDepthMask(false);
            Gdx.gl.glEnable(GL20.GL_BLEND);

            if (additive)
            {
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
            }
            else
            {
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            }

            shader.begin();
            shader.setUniformMatrix(u_projTransIndex, camera.combined);
            shader.setUniformf(u_color, color.r, color.g, color.b, color.a);
            mesh.render(shader, GL20.GL_TRIANGLES);
            shader.end();

            Gdx.gl.glDepthMask(true);
        }
    }

    public void dispose()
    {
        mesh.dispose();
        shader.dispose();
    }
}