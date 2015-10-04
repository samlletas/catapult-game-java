package com.sammacedo.smashingcrystals;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.engine.GameSpriteBatch;
import com.engine.graphics.GraphicsSettings;
import com.engine.graphics.shaders.shaders2d.AlphaBlended2DShader;
import com.engine.graphics.graphics2D.text.AlphaBlendedDistanceFieldShader;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.sammacedo.smashingcrystals.assets.GameAssets;
import com.sammacedo.smashingcrystals.gamelogic.scene.Background;
import com.sammacedo.smashingcrystals.gamelogic.scene.Grass;
import com.sammacedo.smashingcrystals.helpers.SoundPlayer;

public class Common
{
    public final GameAssets assets;
    public final Settings settings;

    public final GameSpriteBatch spriteBatch;
    public final Background background;
    public final Grass grass;
    public final Shaders shaders;

    public final DistanceFieldRenderer distanceFieldRenderer;
    public final SoundPlayer soundPlayer;
    public final ShapeRenderer shapeRenderer; // Para modo debug

    public Common(GameAssets assets, GraphicsSettings graphicsSettings,
                  Batch spriteBatch, AlphaBlended2DShader defaultShader)
    {
        this.assets = assets;
        this.settings = new Settings();
        this.settings.load();
        this.spriteBatch = (GameSpriteBatch)spriteBatch;
        this.background = new Background(graphicsSettings, assets);
        this.grass = new Grass(assets, graphicsSettings);

        this.shaders = new Shaders();
        this.shaders.defaultShader = defaultShader;
        this.shaders.textShader = new AlphaBlendedDistanceFieldShader();
        this.shaders.textShader.setShadowColor(Global.Colors.TEXT_SHADOW);
        this.shaders.textShader.setShadowOffset(Global.TEXT_SHADOW_OFFSET.x, Global.TEXT_SHADOW_OFFSET.y);

        this.distanceFieldRenderer = new DistanceFieldRenderer(spriteBatch, shaders.textShader);
        this.soundPlayer = new SoundPlayer(settings, assets);

        if (Global.DEBUG_POLYGONS)
        {
            this.shapeRenderer = new ShapeRenderer();
        }
        else
        {
            this.shapeRenderer = null;
        }
    }

    public void dispose()
    {
        distanceFieldRenderer.dispose();

        if (Global.DEBUG_POLYGONS)
        {
            shapeRenderer.dispose();
        }
    }

    public class Shaders
    {
        public AlphaBlended2DShader defaultShader;
        public AlphaBlendedDistanceFieldShader textShader;
    }
}
