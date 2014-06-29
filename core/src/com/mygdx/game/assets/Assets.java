package com.mygdx.game.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.engine.assets.Asset;
import com.engine.graphics.animation.Animation;
import com.engine.graphics.animation.AnimationPlayer;

public final class Assets
{
    public static Array<Asset> syncAssets = new Array<Asset>();
    public static Array<Asset> asyncAssets = new Array<Asset>();

    static
    {
        // Assets síncronos
        syncAssets.add(Animations.catapult);

        // Assets asíncronos
    }

    public final static class TextureAtlases
    {
    }

    public final static class AtlasRegions
    {
        public static Asset<TextureAtlas.AtlasRegion> region =
                new Asset<TextureAtlas.AtlasRegion>(null, TextureAtlas.AtlasRegion.class);
    }

    public final static class Animations
    {
        public static Asset<AnimationPlayer> catapult =
                new Asset<AnimationPlayer>("animations/catapult.anim", AnimationPlayer.class);
    }
}
