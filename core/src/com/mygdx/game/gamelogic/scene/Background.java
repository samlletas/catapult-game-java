package com.mygdx.game.gamelogic.scene;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.engine.GameTime;
import com.engine.graphics.GraphicsSettings;
import com.engine.graphics.graphics2D.animation.skeletal.AnimationPlayer;
import com.engine.utilities.FastArray;
import com.mygdx.game.assets.GameAssets;

public final class Background
{
    private GraphicsSettings graphicsSettings;
    private TextureRegion backgroundRegion;
    private FastArray<StarData> stars;
    private float elapsedTime = 0f;

    AnimationPlayer firefly;

    public Background(GraphicsSettings settings, GameAssets assets)
    {
        graphicsSettings = settings;
        backgroundRegion = assets.atlasRegions.background.getInstance();

        stars = new FastArray<StarData>(17);
        stars.add(new StarData(assets, 774f, 53f, 0f, 0.8f, 1000f, false));
        stars.add(new StarData(assets, 570f, 75f, 90f, 0.4f, 500f, true));
        stars.add(new StarData(assets, 452f, 42f, 45f, 0.45f, 1500f, false));
        stars.add(new StarData(assets, 302f, 65f, 180f, 0.5f, 2500f, true));
        stars.add(new StarData(assets, 197f, 147f, 0f, 0.55f, 3000f, false));
        stars.add(new StarData(assets, 48f, 37f, 90f, 0.55f, 2000f, true));
        stars.add(new StarData(assets, 61f, 193f, 45f, 0.7f, 200f, false));
        stars.add(new StarData(assets, 749f, 174f, 0f, 0.45f, 500f, true));
        stars.add(new StarData(assets, 588f, 223f, 90f, 0.4f, 1500f, false));
        stars.add(new StarData(assets, 419f, 205f, 180f, 0.5f, 2500f, true));
        stars.add(new StarData(assets, 729f, 304f, 0f, 0.55f, 200f, false));
        stars.add(new StarData(assets, 257f, 315f, 90f, 0.4f, 1200f, true));
        stars.add(new StarData(assets, 509f, 347f, 180f, 0.4f, 100f, false));
        stars.add(new StarData(assets, 813f, 372f, 0f, 0.6f, 1000f, true));
        stars.add(new StarData(assets, 616f, 430f, 90f, 0.55f, 0f, false));
        stars.add(new StarData(assets, 387f, 419f, 180f, 0.4f, 100f, true));
        stars.add(new StarData(assets, 97f, 405f, 45f, 0.5f, 800f, false));

        firefly = assets.animations.fireFly.getInstance();
        firefly.position.set(630f, 350f);
        firefly.play("fly");
    }

    public void update(GameTime gameTime)
    {
        elapsedTime += gameTime.delta;

        for (StarData star : stars)
        {
            star.update(gameTime, elapsedTime);
        }

        firefly.update(gameTime);
    }

    public void draw(Batch batch)
    {
        batch.disableBlending();
        batch.draw(backgroundRegion, 0f, 0f, graphicsSettings.virtualWidth,
                graphicsSettings.virtualHeight);
        batch.enableBlending();

        for (StarData star : stars)
        {
            star.draw(batch);
        }

        firefly.draw(batch);
    }

    class StarData
    {
        private static final float PIVOTX = 20f;
        private static final float PIVOTY = 20f;

        private float x;
        private float y;
        private float rotation;
        private boolean clockwise;

        private float initialScale;
        private float seed;
        private float scale;

        private TextureAtlas.AtlasRegion region;

        public StarData(GameAssets assets, float x, float y, float initialAngle,
                        float initialScale, float seed, boolean clockwise)
        {
            this.x = x - PIVOTX;
            this.y = y - PIVOTY;
            this.rotation = -initialAngle;
            this.clockwise = clockwise;

            this.initialScale = initialScale;
            this.seed = seed;

            this.region = assets.atlasRegions.star.getInstance();
        }

        void update(GameTime gameTime, float elapsedTime)
        {
            if (clockwise)
            {
                rotation += 15f * gameTime.delta;
            }
            else
            {
                rotation -= 15f * gameTime.delta;
            }

            scale = initialScale + (initialScale * 0.3f) * MathUtils.sin((elapsedTime * 3f) + seed);
        }

        void draw(Batch batch)
        {
            batch.draw(region, x, y, PIVOTX, PIVOTY,
                    region.getRegionWidth(), region.getRegionHeight(),
                    scale, scale, rotation);
        }
    }
}