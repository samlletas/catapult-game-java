package com.mygdx.game.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.engine.assets.Asset;

public final class Assets
{
    public static Array<Asset> syncAssets = new Array<Asset>();
    public static Array<Asset> asyncAssets = new Array<Asset>();

    static
    {
        // Assets síncronos
        syncAssets.add(TextureAtlases.pack);

        // Assets asíncronos
    }

    public final static class TextureAtlases
    {
        public static Asset<TextureAtlas> pack =
                new Asset<TextureAtlas>("textures/pack.atlas", TextureAtlas.class);

    }

    public final static class AtlasRegions
    {
        public static Asset<TextureAtlas.AtlasRegion> region =
                new Asset<TextureAtlas.AtlasRegion>(null, TextureAtlas.AtlasRegion.class);
    }
}
