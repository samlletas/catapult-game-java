package com.mygdx.game.assets;

import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.engine.assets.Asset;
import com.engine.assets.AssetMaster;

public class GameAssetMaster extends AssetMaster
{
    TextureAtlasLoader.TextureAtlasParameter textureAtlasParameter =
            new TextureAtlasLoader.TextureAtlasParameter(true);

    @Override
    protected void addToSyncQueue()
    {
        for (Asset asset : Assets.syncAssets)
        {
            if (asset.valueClass.equals(TextureAtlas.class))
            {
                manager.load(asset.path, TextureAtlas.class, textureAtlasParameter);
            }
            else
            {
                manager.load(asset.path, asset.valueClass);
            }
        }
    }

    @Override
    public void setSyncAssetsInstances()
    {
        for (Asset asset : Assets.syncAssets)
        {
            asset.setValue(manager);
        }

//        Assets.AtlasRegions.region.value = Assets.TextureAtlases.pack.value.findRegion("small");
    }

    @Override
    protected void addToASyncQueue()
    {

    }

    @Override
    public void setAsyncAssetsInstances()
    {

    }
}
