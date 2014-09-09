package com.engine.assets;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;

public class Asset<T>
{
    public final String path;
    public final AssetLoaderParameters parameters;
    public final Class<T> instanceClass;

    public T instance;

    public Asset(String path, Class<T> instanceClass)
    {
        this(path, instanceClass, null);
    }

    public Asset(String path, Class<T> instanceClass, AssetLoaderParameters parameters)
    {
        this.path = path;
        this.instanceClass = instanceClass;
        this.parameters = parameters;
    }

    public void getInstanceFromManager(AssetManager manager)
    {
        instance = manager.get(path);
    }
}
