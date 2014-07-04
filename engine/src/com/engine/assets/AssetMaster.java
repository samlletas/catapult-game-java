package com.engine.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.utils.Array;
import com.engine.graphics.animation.AnimationLoader;
import com.engine.graphics.animation.AnimationPlayer;

public abstract class AssetMaster
{
    private AssetManager manager;

    private Array<Asset> syncQueue;
    private Array<Asset> asyncQueue;

    private boolean addedToAsyncQueue;
    private boolean initializedAsyncInstances;

    public AssetMaster()
    {
        manager = new AssetManager();
        syncQueue = new Array<Asset>();
        asyncQueue = new Array<Asset>();

        addedToAsyncQueue = false;
        initializedAsyncInstances = false;

        // Loader para animaciones
        manager.setLoader(AnimationPlayer.class, new AnimationLoader(new InternalFileHandleResolver()));

        setCustomLoaders(manager);
    }

    private void loadQueue(Array<Asset> queue)
    {
        for (Asset asset : queue)
        {
            if (asset.parameters == null)
            {
                manager.load(asset.path, asset.instanceClass);
            }
            else
            {
                manager.load(asset.path, asset.instanceClass, asset.parameters);
            }
        }
    }

    private void setQueueInstances(Array<Asset> queue)
    {
        for (Asset asset : queue)
        {
            asset.getInstanceFromManager(manager);
        }
    }

    public final void loadSync()
    {
        addToSyncQueue(syncQueue);
        loadQueue(syncQueue);

        manager.finishLoading();
        setQueueInstances(syncQueue);
        onSyncLoadCompleted(manager);
    }

    /**
     *
     * @return True cuando se ha terminado de cargar
     */
    public final boolean loadAsync()
    {
        if (!addedToAsyncQueue)
        {
            addToASyncQueue(asyncQueue);
            loadQueue(asyncQueue);

            addedToAsyncQueue = true;
        }

        boolean finished = manager.update();

        if (finished && !initializedAsyncInstances)
        {
            setQueueInstances(asyncQueue);
            onAsyncLoadCompleted(manager);
            initializedAsyncInstances = true;
        }

        return finished;
    }

    public float getAsyncProgress()
    {
        return manager.getProgress();
    }

    public void Dispose()
    {
        manager.dispose();
    }

    protected abstract void setCustomLoaders(AssetManager manager);
    protected abstract void addToSyncQueue(Array<Asset> queue);
    protected abstract void addToASyncQueue(Array<Asset> queue);
    protected abstract void onSyncLoadCompleted(AssetManager manager);
    protected abstract void onAsyncLoadCompleted(AssetManager manager);
}
