package com.mygdx.game.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.engine.assets.Asset;
import com.engine.graphics.animation.AnimationPlayer;

public final class GameAssets
{
    public final static class Animations
    {
        public static Asset<AnimationPlayer> catapult =
                new Asset<AnimationPlayer>("animations/catapult.anim", AnimationPlayer.class);
    }

    public static final class AtlasRegions
    {
        public static Array<Asset<TextureAtlas.AtlasRegion>> groundRegions =
                new Array<Asset<TextureAtlas.AtlasRegion>>(16);
    }
}
