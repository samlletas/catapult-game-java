package com.engine.assets;

import com.badlogic.gdx.assets.AssetManager;

public abstract class AssetMaster
{
    protected AssetManager manager;
    private boolean addedToAsyncQueue;

    public AssetMaster()
    {
        manager = new AssetManager();
        addedToAsyncQueue = false;
    }

    public final void loadSync()
    {
        addToSyncQueue();
        manager.finishLoading();
        setSyncAssetsInstances();
    }

    public final boolean loadAsync()
    {
        if (!addedToAsyncQueue)
        {
            addToASyncQueue();
            addedToAsyncQueue = true;
        }

        return manager.update();
    }

    public final float getAsyncProgress()
    {
        return manager.getProgress();
    }

    public final void Dispose()
    {
        manager.dispose();
    }

    protected abstract void addToSyncQueue();
    protected abstract void addToASyncQueue();
    protected abstract void setSyncAssetsInstances();
    public abstract void setAsyncAssetsInstances();
}
