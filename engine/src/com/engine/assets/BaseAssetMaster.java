package com.engine.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.utils.Array;
import com.engine.graphics.graphics2D.animation.skeletal.AnimationLoader;
import com.engine.graphics.graphics2D.animation.skeletal.AnimationPlayer;

public abstract class BaseAssetMaster
{
    private AssetManager manager;

    private Array<IAsset> syncQueue;
    private Array<IAsset> asyncQueue;

    private boolean addedToAsyncQueue;
    private boolean initializedAsyncInstances;

    public BaseAssetMaster()
    {
        manager = new AssetManager();
        syncQueue = new Array<IAsset>();
        asyncQueue = new Array<IAsset>();

        addedToAsyncQueue = false;
        initializedAsyncInstances = false;

        addLoaders();
    }

    private void addLoaders()
    {
        manager.setLoader(AnimationPlayer.class, new AnimationLoader(new InternalFileHandleResolver()));
        addCustomLoaders(manager);
    }

    private void loadQueue(Array<IAsset> queue)
    {
        for (int i = 0, n = queue.size; i < n; i++)
        {
            queue.get(i).load(manager);
        }
    }

    private void retrieveQueueInstances(Array<IAsset> queue)
    {
        for (int i = 0, n = queue.size; i < n; i++)
        {
            queue.get(i).retrieveInstance(manager);
        }
    }

    public final void loadSync()
    {
        addToSyncQueue(syncQueue);
        loadQueue(syncQueue);

        manager.finishLoading();
        retrieveQueueInstances(syncQueue);
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
            retrieveQueueInstances(asyncQueue);
            onAsyncLoadCompleted(manager);
            initializedAsyncInstances = true;
        }

        return finished;
    }

    public float getAsyncProgress()
    {
        return manager.getProgress();
    }

    public void dispose()
    {
        manager.dispose();
    }

    protected abstract void addCustomLoaders(AssetManager manager);
    protected abstract void addToSyncQueue(Array<IAsset> queue);
    protected abstract void addToASyncQueue(Array<IAsset> queue);
    protected abstract void onSyncLoadCompleted(AssetManager manager);
    protected abstract void onAsyncLoadCompleted(AssetManager manager);
}
