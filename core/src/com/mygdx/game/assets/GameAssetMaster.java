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

    /*************************************************************************
     * Carga Síncrona
     *************************************************************************/

    @Override
    protected void addToSyncQueue(Array<Asset> queue)
    {
        queue.add(GameAssets.Animations.catapult);
        queue.add(GameAssets.Animations.tulipan);
        queue.add(GameAssets.Animations.grassFlower);
        queue.add(GameAssets.Animations.flower);
    }

    @Override
    protected void onSyncLoadCompleted(AssetManager manager)
    {
        TextureAtlas atlas = manager.get("textures/textures.atlas");

        loadGroundRegions(atlas);
        loadGrassRegions(atlas);

        GameAssets.AtlasRegions.star.instance = atlas.findRegion("star");
        GameAssets.AtlasRegions.ball.instance = atlas.findRegion("ball");
    }

    private void loadGroundRegions(TextureAtlas atlas)
    {
        Array<Asset<TextureAtlas.AtlasRegion>> localAtlasRegions =
                GameAssets.AtlasRegions.groundRegions;

        for (int i = 0; i < 16; i++)
        {
            Asset<TextureAtlas.AtlasRegion> newRegion =
                    new Asset<TextureAtlas.AtlasRegion>(null, TextureAtlas.AtlasRegion.class);

            newRegion.instance = atlas.findRegion("ground" + String.valueOf(i));
            localAtlasRegions.add(newRegion);
        }
    }

    private void loadGrassRegions(TextureAtlas atlas)
    {
        GameAssets.AtlasRegions.grass1.instance = atlas.findRegion("grass1");
        GameAssets.AtlasRegions.grass2.instance = atlas.findRegion("grass2");
        GameAssets.AtlasRegions.grass3.instance = atlas.findRegion("grass3");
    }

    /*************************************************************************
     * Carga Asíncrona
     *************************************************************************/

    @Override
    protected void addToASyncQueue(Array<Asset> queue)
    {

    }

    @Override
    protected void onAsyncLoadCompleted(AssetManager manager)
    {

    }
}
