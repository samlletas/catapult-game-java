package com.mygdx.game.gamelogic.targets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;

public class TargetBatch
{
    private final static int POSITION_COMPONENTS = 3;
    private final static int NORMAL_COMPONENTS = 3;
    private final static int COMPONENTES_PER_VERTEX = POSITION_COMPONENTS + NORMAL_COMPONENTS;
    private final static int VERTICES_PER_TRIANGLE = 3;

    private final float[] singleMeshVertices;
    private final short[] singleMeshIndices;

    private final float[] vertices;
    private final short[] indices;

    private int size;
    private Mesh mesh;
    private int idx = 0;

    public TargetBatch(Mesh targetMesh, int size)
    {
        this.size = size;

        int numVertices = targetMesh.getNumVertices();
        int numIndices = targetMesh.getNumIndices();

        singleMeshVertices = new float[numVertices * COMPONENTES_PER_VERTEX];
        singleMeshIndices = new short[numIndices];

        targetMesh.getVertices(singleMeshVertices);
        targetMesh.getIndices(singleMeshIndices);

        vertices = new float[singleMeshVertices.length * size];
        indices = new short[singleMeshIndices.length * size];

        mesh = new Mesh(false, true, vertices.length, 0,
                new VertexAttributes(
                        new VertexAttribute(VertexAttributes.Usage.Position,
                                POSITION_COMPONENTS, "a_position"),
                        new VertexAttribute(VertexAttributes.Usage.Normal,
                                NORMAL_COMPONENTS, "a_normal")));
    }

    public void begin()
    {

    }

    public void draw()
    {

    }

    public void end()
    {

    }
}
