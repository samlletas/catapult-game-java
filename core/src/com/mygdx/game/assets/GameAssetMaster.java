package com.mygdx.game.assets;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.engine.assets.Asset;
import com.engine.assets.AssetMaster;
import com.engine.graphics.animation.AnimationLoader;
import com.engine.graphics.animation.AnimationPlayer;

public class GameAssetMaster extends AssetMaster
{
    TextureAtlasLoader.TextureAtlasParameter textureAtlasParameter =
            new TextureAtlasLoader.TextureAtlasParameter(true);

    @Override
    protected void addToSyncQueue()
    {
//        for (Asset asset : Assets.syncAssets)
//        {
//            if (asset.valueClass.equals(TextureAtlas.class))
//            {
//                manager.load(asset.path, TextureAtlas.class, textureAtlasParameter);
//            }
//            else
//            {
//                manager.load(asset.path, asset.valueClass);
//            }
//        }

        manager.setLoader(AnimationPlayer.class, new AnimationLoader(new InternalFileHandleResolver()));
        manager.load(Assets.Animations.catapult.path, AnimationPlayer.class);
    }

    @Override
    public void setSyncAssetsInstances()
    {
        for (Asset asset : Assets.syncAssets)
        {
            asset.setValue(manager);
        }
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
