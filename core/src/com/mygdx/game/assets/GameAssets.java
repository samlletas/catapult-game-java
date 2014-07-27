package com.mygdx.game.assets;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.engine.assets.Asset;
import com.engine.graphics.animation.AnimationLoader;
import com.engine.graphics.animation.AnimationPlayer;

public final class GameAssets
{
    public final static class Animations
    {
        private static AnimationLoader.AnimationLoaderParameter parameters =
                new AnimationLoader.AnimationLoaderParameter("textures");

        public static Asset<AnimationPlayer> catapult =
                new Asset<AnimationPlayer>("animations/catapult.xml", AnimationPlayer.class, parameters);

        public static Asset<AnimationPlayer> tulipan =
                new Asset<AnimationPlayer>("animations/tulipan.xml", AnimationPlayer.class, parameters);

        public static Asset<AnimationPlayer> grassFlower =
                new Asset<AnimationPlayer>("animations/grassflower.xml", AnimationPlayer.class, parameters);

        public static Asset<AnimationPlayer> flower =
                new Asset<AnimationPlayer>("animations/flower.xml", AnimationPlayer.class, parameters);
    }

    public static final class AtlasRegions
    {
        public static Array<Asset<TextureAtlas.AtlasRegion>> groundRegions =
                new Array<Asset<TextureAtlas.AtlasRegion>>();

        public static Asset<TextureAtlas.AtlasRegion> grass1 =
                new Asset<TextureAtlas.AtlasRegion>(null, TextureAtlas.AtlasRegion.class);

        public static Asset<TextureAtlas.AtlasRegion> grass2 =
                new Asset<TextureAtlas.AtlasRegion>(null, TextureAtlas.AtlasRegion.class);

        public static Asset<TextureAtlas.AtlasRegion> grass3 =
                new Asset<TextureAtlas.AtlasRegion>(null, TextureAtlas.AtlasRegion.class);

        public static Asset<TextureAtlas.AtlasRegion> star =
                new Asset<TextureAtlas.AtlasRegion>(null, TextureAtlas.AtlasRegion.class);

        public static Asset<TextureAtlas.AtlasRegion> ball =
                new Asset<TextureAtlas.AtlasRegion>(null, TextureAtlas.AtlasRegion.class);

        public static Asset<TextureAtlas.AtlasRegion> rope =
                new Asset<TextureAtlas.AtlasRegion>(null, TextureAtlas.AtlasRegion.class);
    }

    public static final class Particles
    {
        public static Asset<ParticleEffect> ballTrace =
                new Asset<ParticleEffect>("particles/ball.p", ParticleEffect.class);

        public static Asset<ParticleEffect> ballExplosion =
                new Asset<ParticleEffect>("particles/ballexplosion.p", ParticleEffect.class);
    }
}
