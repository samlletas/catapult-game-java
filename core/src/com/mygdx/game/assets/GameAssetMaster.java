package com.mygdx.game.assets;

import com.badlogic.gdx.utils.Array;
import com.engine.assets.Asset;
import com.engine.assets.AssetMaster;

public class GameAssetMaster extends AssetMaster
{
    public GameAssetMaster()
    {

    }

    @Override
    protected void setCustomLoaders()
    {

    }

    @Override
    protected void addToSyncQueue(Array<Asset> queue)
    {
        queue.add(GameAssets.Animations.catapult);
    }

    @Override
    protected void addToASyncQueue(Array<Asset> queue)
    {

    }
}
