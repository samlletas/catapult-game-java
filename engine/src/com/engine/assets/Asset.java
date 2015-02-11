package com.engine.assets;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;

public class Asset<T> implements IAsset<T>
{
    private String fileName;
    private AssetLoaderParameters<T> parameters;
    private Class<T> type;
    private T instance;

    public Asset(String fileName, Class<T> instanceClass)
    {
        this(fileName, instanceClass, null);
    }

    public Asset(String fileName, Class<T> type, AssetLoaderParameters<T> parameters)
    {
        this.fileName = fileName;
        this.type = type;
        this.parameters = parameters;
    }

    @Override
    public T getInstance()
    {
        return instance;
    }

    @Override
    public void load(AssetManager manager)
    {
        if (parameters == null)
        {
            manager.load(fileName, type);
        }
        else
        {
            manager.load(fileName, type, parameters);
        }
    }

    @Override
    public void retrieveInstance(AssetManager manager)
    {
        instance = manager.get(fileName);
    }
}
