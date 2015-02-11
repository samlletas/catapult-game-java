package com.engine.assets;

import com.badlogic.gdx.assets.AssetManager;

public interface IAsset<T>
{
    public T getInstance();
    public void load(AssetManager manager);
    public void retrieveInstance(AssetManager manager);
}
