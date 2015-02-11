package com.engine.assets.custom;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.engine.assets.IAsset;

public class AtlasRegionAsset implements IAsset<TextureAtlas.AtlasRegion>
{
    private final String regionName;
    private final String textureAtlasName;

    private TextureAtlas.AtlasRegion region;

    public AtlasRegionAsset(String regionName, String textureAtlasName)
    {
        this.regionName = regionName;
        this.textureAtlasName = textureAtlasName;
    }

    @Override
    public TextureAtlas.AtlasRegion getInstance()
    {
        return region;
    }

    @Override
    public void load(AssetManager manager)
    {
        // No se realiza carga debido a que la región depende de un TextureAtlas
    }

    @Override
    public void retrieveInstance(AssetManager manager)
    {
        TextureAtlas atlas = manager.get(textureAtlasName, TextureAtlas.class);
        region = atlas.findRegion(regionName);
    }
}
