package com.engine.graphics.graphics3D;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.RenderableSorter;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;

public class FastModelBatch extends ModelBatch
{
    public FastModelBatch()
    {
        super(new FastRenderableSorter());
    }

    public FastModelBatch(RenderableSorter sorter)
    {
        super(sorter);
    }

    public FastModelBatch(ShaderProvider shaderProvider)
    {
        super(shaderProvider, new FastRenderableSorter());
    }

    public FastModelBatch(ShaderProvider shaderProvider, RenderableSorter sorter)
    {
        super(shaderProvider, sorter);
    }

    public void initializeRenderable(ModelInstance instance)
    {
        instance.getRenderables(renderables, renderablesPool);
    }

    public void resetPool()
    {
        renderablesPool.flush();
        renderables.clear();
    }
}
