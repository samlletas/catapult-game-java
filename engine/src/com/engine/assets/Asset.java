package com.engine.assets;

import com.badlogic.gdx.assets.AssetManager;

public final class Asset<T>
{
    public String path;
    public Class<T> valueClass;
    public T value;

    public Asset(String path, Class<T> valueClass)
    {
        this.path = path;
        this.valueClass = valueClass;
    }

    public void setValue(AssetManager manager)
    {
        value = manager.get(path);
    }
}
