package com.mygdx.game.assets;

import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.engine.assets.Asset;
import com.engine.assets.custom.AtlasRegionAsset;
import com.engine.assets.custom.DistanceFieldFontAsset;
import com.engine.assets.custom.SkinAsset;
import com.engine.graphics.graphics2D.animation.skeletal.AnimationLoader;
import com.engine.graphics.graphics2D.animation.skeletal.AnimationPlayer;
import com.mygdx.game.Global;

public final class GameAssets
{
    public TextureAtlases textureAtlases;
    public AtlasRegions atlasRegions;
    public Skins skins;
    public Animations animations;
    public Particles particles;
    public Models models;
    public BitmapFonts bitmapFonts;
    public DistanceFieldFonts distanceFieldFonts;
    public Sounds sounds;

    private final String atlas = "textures/textures.atlas";

    public GameAssets()
    {
        textureAtlases = new TextureAtlases();
        atlasRegions = new AtlasRegions();
        skins = new Skins();
        animations = new Animations();
        particles = new Particles();
        models = new Models();
        bitmapFonts = new BitmapFonts();
        distanceFieldFonts = new DistanceFieldFonts();
        sounds = new Sounds();
    }

    public final class TextureAtlases
    {
        public Asset<TextureAtlas> gameTextures;

        private TextureAtlases()
        {
            TextureAtlasLoader.TextureAtlasParameter parameter = new TextureAtlasLoader.TextureAtlasParameter(true);
            gameTextures = new Asset<TextureAtlas>(atlas, TextureAtlas.class, parameter);
        }
    }

    public final class AtlasRegions
    {
        public AtlasRegionAsset background;
        public AtlasRegionAsset pixel;
        public AtlasRegionAsset libGdx;
        public AtlasRegionAsset titleSmashing;
        public AtlasRegionAsset titleCrystals;
        public AtlasRegionAsset ground;
        public AtlasRegionAsset buttonGlow;
        public AtlasRegionAsset grass1;
        public AtlasRegionAsset grass2;
        public AtlasRegionAsset grass3;
        public AtlasRegionAsset star;
        public AtlasRegionAsset ball;
        public AtlasRegionAsset redDot;
        public AtlasRegionAsset rope;
        public AtlasRegionAsset crystalGlow;
        public AtlasRegionAsset crystalWaveBorder;
        public AtlasRegionAsset crystalWaveFill;
        public AtlasRegionAsset spikesGlow;
        public AtlasRegionAsset hudCrystal;
        public AtlasRegionAsset hudBar;
        public AtlasRegionAsset hudBarCorner;
        public AtlasRegionAsset hudChanceActive;
        public AtlasRegionAsset hudChanceInactive;
        public AtlasRegionAsset uiHeaderBackground;
        public AtlasRegionAsset uiHeaderLine;
        public AtlasRegionAsset uiMessageLine;

        private AtlasRegions()
        {
            background = new AtlasRegionAsset("background", atlas);
            pixel = new AtlasRegionAsset("pixel", atlas);
            libGdx = new AtlasRegionAsset("libGDX", atlas);
            titleSmashing = new AtlasRegionAsset("title-smashing", atlas);
            titleCrystals = new AtlasRegionAsset("title-crystals", atlas);
            buttonGlow = new AtlasRegionAsset("ui-button-glow", atlas);
            ground = new AtlasRegionAsset("ground", atlas);
            grass1 = new AtlasRegionAsset("grass1", atlas);
            grass2 = new AtlasRegionAsset("grass2", atlas);
            grass3 = new AtlasRegionAsset("grass3", atlas);
            star = new AtlasRegionAsset("backgroundStar", atlas);
            ball = new AtlasRegionAsset("ball", atlas);
            redDot = new AtlasRegionAsset("red-dot", atlas);
            rope = new AtlasRegionAsset("rope", atlas);
            crystalGlow = new AtlasRegionAsset("crystalGlow", atlas);
            crystalWaveBorder = new AtlasRegionAsset("crystalWaveBorder", atlas);
            crystalWaveFill = new AtlasRegionAsset("crystalWaveFill", atlas);
            spikesGlow = new AtlasRegionAsset("spikesGlow", atlas);
            hudCrystal = new AtlasRegionAsset("hud-crystal", atlas);
            hudBar = new AtlasRegionAsset("hud-bar", atlas);
            hudBarCorner = new AtlasRegionAsset("hud-bar-corner", atlas);
            hudChanceActive = new AtlasRegionAsset("hud-chance-active", atlas);
            hudChanceInactive = new AtlasRegionAsset("hud-chance-inactive", atlas);
            uiHeaderBackground = new AtlasRegionAsset("ui-header-background", atlas);
            uiHeaderLine = new AtlasRegionAsset("ui-header-line", atlas);
            uiMessageLine = new AtlasRegionAsset("ui-message-line", atlas);
        }
    }

    public final class Skins
    {
        public SkinAsset ui;

        private Skins()
        {
            ui = new SkinAsset("skins/ui.json", atlas);
        }
    }

    public final class Animations
    {
        private AnimationLoader.AnimationLoaderParameter parameters;

        public Asset<AnimationPlayer> catapult;
        public Asset<AnimationPlayer> tulipan;
        public Asset<AnimationPlayer> grassFlower;
        public Asset<AnimationPlayer> flower;
        public Asset<AnimationPlayer> fireFly;

        private Animations()
        {
            parameters = new AnimationLoader.AnimationLoaderParameter("textures");

            catapult = new Asset<AnimationPlayer>("animations/catapult.xml", AnimationPlayer.class, parameters);
            tulipan = new Asset<AnimationPlayer>("animations/tulipan.xml", AnimationPlayer.class, parameters);
            grassFlower = new Asset<AnimationPlayer>("animations/grassflower.xml", AnimationPlayer.class, parameters);
            flower = new Asset<AnimationPlayer>("animations/flower.xml", AnimationPlayer.class, parameters);
            fireFly = new Asset<AnimationPlayer>("animations/firefly.xml", AnimationPlayer.class, parameters);
        }
    }

    public final class Particles
    {
        private ParticleEffectLoader.ParticleEffectParameter parameters;

        public Asset<ParticleEffect> ballExplosion;
        public Asset<ParticleEffect> crystalBreakNormal;
        public Asset<ParticleEffect> crystalBreakSpecial;
        public Asset<ParticleEffect> spikeBreak;

        private Particles()
        {
            parameters = new ParticleEffectLoader.ParticleEffectParameter();
            parameters.atlasFile = atlas;

            ballExplosion = new Asset<ParticleEffect>("particles/ballExplosion.p", ParticleEffect.class, parameters);
            crystalBreakNormal = new Asset<ParticleEffect>("particles/crystalBreakNormal.p", ParticleEffect.class, parameters);
            crystalBreakSpecial = new Asset<ParticleEffect>("particles/crystalBreakSpecial.p", ParticleEffect.class, parameters);
            spikeBreak = new Asset<ParticleEffect>("particles/spikeBreak.p", ParticleEffect.class, parameters);
        }
    }

    public final class Models
    {
        public Asset<Model> crystal;
        public Asset<Model> bomb;

        private Models()
        {
            crystal = new Asset<Model>("models/crystal.g3dj", Model.class);
            bomb = new Asset<Model>("models/bomb.g3db", Model.class);
        }
    }

    public final class BitmapFonts
    {
        private BitmapFontLoader.BitmapFontParameter parameters;

        public Asset<BitmapFont> furore;

        private BitmapFonts()
        {
            parameters = new BitmapFontLoader.BitmapFontParameter();
            parameters.minFilter = Texture.TextureFilter.Linear;
            parameters.magFilter = Texture.TextureFilter.Linear;
            parameters.flip = true;

            furore = new Asset<BitmapFont>("fonts/furore.fnt", BitmapFont.class, parameters);
        }
    }

    public final class DistanceFieldFonts
    {
        public DistanceFieldFontAsset furore;

        private DistanceFieldFonts()
        {
            furore = new DistanceFieldFontAsset("fonts/furore.fnt", Global.TEXT_SPREAD, Global.TEXT_THICKNESS);
        }
    }

    public final class Sounds
    {
        public Asset<Sound> crystalBreak;
        public Asset<Sound> shoot;

        private Sounds()
        {
            crystalBreak = new Asset<Sound>("sounds/break.ogg", Sound.class);
            shoot = new Asset<Sound>("sounds/shoot.ogg", Sound.class);
        }
    }
}
