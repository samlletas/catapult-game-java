package com.mygdx.game.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.engine.assets.Asset;
import com.engine.assets.AssetMaster;

public class GameAssetMaster extends AssetMaster
{
    public GameAssetMaster()
    {

    }

    @Override
    protected void setCustomLoaders(AssetManager manager)
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

    @Override
    protected void onSyncLoadCompleted(AssetManager manager)
    {
        loadGroundRegions(manager);
    }

    private void loadGroundRegions(AssetManager manager)
    {
        Array<Asset<TextureAtlas.AtlasRegion>> localAtlasRegions =
                GameAssets.AtlasRegions.groundRegions;
        int size = localAtlasRegions.size;

        TextureAtlas atlas = manager.get("animations/textures.atlas");

        for(int i = 0; i < size; i++)
        {
            Asset<TextureAtlas.AtlasRegion> newRegion =
                    new Asset<TextureAtlas.AtlasRegion>(null, TextureAtlas.AtlasRegion.class);

            newRegion.instance = atlas.findRegion("ground" + String.valueOf(i));
            localAtlasRegions.add(newRegion);
        }
    }

    @Override
    protected void onAsyncLoadCompleted(AssetManager manager)
    {

    }
}
