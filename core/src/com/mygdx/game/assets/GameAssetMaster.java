package com.mygdx.game.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.engine.assets.BaseAssetMaster;
import com.engine.assets.IAsset;

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

        queue.add(assets.atlasRegions.pixel);
        queue.add(assets.atlasRegions.libGdx);
        queue.add(assets.atlasRegions.buttonGlow);
        queue.add(assets.atlasRegions.ground);
        queue.add(assets.atlasRegions.grass1);
        queue.add(assets.atlasRegions.grass2);
        queue.add(assets.atlasRegions.grass3);
        queue.add(assets.atlasRegions.star);
        queue.add(assets.atlasRegions.ball);
        queue.add(assets.atlasRegions.rope);
        queue.add(assets.atlasRegions.crystalGlow);
        queue.add(assets.atlasRegions.crystalWaveBorder);
        queue.add(assets.atlasRegions.crystalWaveFill);
        queue.add(assets.atlasRegions.spikesGlow);
        queue.add(assets.atlasRegions.hudCrystal);
        queue.add(assets.atlasRegions.hudBarCorner);
        queue.add(assets.atlasRegions.hudBarBorder);
        queue.add(assets.atlasRegions.hudBar);
        queue.add(assets.atlasRegions.hudChance);
        queue.add(assets.atlasRegions.uiHeaderBackground);
        queue.add(assets.atlasRegions.uiHeaderLine);
        queue.add(assets.atlasRegions.titleGLow);

        queue.add(assets.skins.ui);

        queue.add(assets.animations.catapult);
        queue.add(assets.animations.tulipan);
        queue.add(assets.animations.grassFlower);
        queue.add(assets.animations.flower);

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
        // Se utilizan posiciones flotantes para permitir un movimiento más
        // fluido del texto
        assets.distanceFieldFonts.furore.getInstance().setUseIntegerPositions(false);
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
