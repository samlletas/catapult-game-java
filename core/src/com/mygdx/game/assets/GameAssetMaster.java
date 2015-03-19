package com.mygdx.game.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.engine.assets.BaseAssetMaster;
import com.engine.assets.IAsset;
import com.engine.assets.loaders.BitmapFontAtlasLoader;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;

public class GameAssetMaster extends BaseAssetMaster
{
    private GameAssets assets;

    public GameAssetMaster(GameAssets assets)
    {
        this.assets = assets;
    }

    @Override
    protected void addCustomLoaders(AssetManager manager)
    {
    }

    /*************************************************************************
     * Carga Síncrona
     *************************************************************************/

    @Override
    protected void addToSyncQueue(Array<IAsset> queue)
    {
        queue.add(assets.textureAtlases.gameTextures);

        queue.add(assets.atlasRegions.background);
        queue.add(assets.atlasRegions.pixel);
        queue.add(assets.atlasRegions.libGdx);
        queue.add(assets.atlasRegions.titleSmashing);
        queue.add(assets.atlasRegions.titleCrystals);
        queue.add(assets.atlasRegions.buttonGlow);
        queue.add(assets.atlasRegions.ground);
        queue.add(assets.atlasRegions.grass1);
        queue.add(assets.atlasRegions.grass2);
        queue.add(assets.atlasRegions.grass3);
        queue.add(assets.atlasRegions.star);
        queue.add(assets.atlasRegions.ball);
        queue.add(assets.atlasRegions.redDot);
        queue.add(assets.atlasRegions.rope);
        queue.add(assets.atlasRegions.crystalGlow);
        queue.add(assets.atlasRegions.crystalWaveBorder);
        queue.add(assets.atlasRegions.crystalWaveFill);
        queue.add(assets.atlasRegions.spikesGlow);
        queue.add(assets.atlasRegions.hudCrystal);
        queue.add(assets.atlasRegions.hudBar);
        queue.add(assets.atlasRegions.hudBarCorner);
        queue.add(assets.atlasRegions.hudChanceActive);
        queue.add(assets.atlasRegions.hudChanceInactive);
        queue.add(assets.atlasRegions.uiHeaderBackground);
        queue.add(assets.atlasRegions.uiHeaderLine);

        queue.add(assets.skins.ui);

        queue.add(assets.animations.catapult);
        queue.add(assets.animations.tulipan);
        queue.add(assets.animations.grassFlower);
        queue.add(assets.animations.flower);
        queue.add(assets.animations.fireFly);

        queue.add(assets.particles.ballExplosion);
        queue.add(assets.particles.crystalBreakNormal);
        queue.add(assets.particles.crystalBreakSpecial);
        queue.add(assets.particles.spikeBreak);

        queue.add(assets.models.crystal);
        queue.add(assets.models.bomb);

        queue.add(assets.bitmapFonts.furore);
        queue.add(assets.distanceFieldFonts.furore);

        queue.add(assets.sounds.crystalBreak);
        queue.add(assets.sounds.shoot);
    }

    @Override
    protected void onSyncLoadCompleted(AssetManager manager)
    {
        // Preparación de texto
        DistanceFieldFont furore = assets.distanceFieldFonts.furore.getInstance();
        furore.setUseIntegerPositions(false);
        furore.setFixedWidthGlyphs("1234567890");
    }

    /*************************************************************************
     * Carga Asíncrona
     *************************************************************************/

    @Override
    protected void addToASyncQueue(Array<IAsset> queue)
    {

    }

    @Override
    protected void onAsyncLoadCompleted(AssetManager manager)
    {

    }
}