package com.sammacedo.smashingcrystals.gamelogic.scene;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.engine.GameTime;
import com.engine.graphics.GraphicsSettings;
import com.engine.utilities.FastArray;
import com.sammacedo.smashingcrystals.assets.GameAssets;

public final class Background
{
    private GraphicsSettings graphicsSettings;
    private TextureRegion backgroundRegion;
    private FastArray<StarData> stars;

    private Mode mode;
    private float elapsedTime = 0f;

    public enum Mode
    {
        Main,
        Menus,
        Gameplay,
    }

    public Background(GraphicsSettings settings, GameAssets assets)
    {
        graphicsSettings = settings;
        backgroundRegion = assets.atlasRegions.background.getInstance();

        stars = new FastArray<StarData>(18);
        stars.add(new StarData(assets, 774f, 53f, 0f, 0.65f, 1000f, false));
        stars.add(new StarData(assets, 570f, 75f, 90f, 0.4f, 500f, true));
        stars.add(new StarData(assets, 452f, 22f, 45f, 0.45f, 1500f, false, Mode.Gameplay));
        stars.add(new StarData(assets, 280f, 65f, 180f, 0.5f, 2500f, true));
        stars.add(new StarData(assets, 197f, 147f, 0f, 0.55f, 3000f, false));
        stars.add(new StarData(assets, 48f, 37f, 90f, 0.55f, 2000f, true, Mode.Gameplay));
        stars.add(new StarData(assets, 61f, 193f, 45f, 0.7f, 200f, false, Mode.Main));
        stars.add(new StarData(assets, 749f, 174f, 0f, 0.45f, 500f, true));
        stars.add(new StarData(assets, 588f, 223f, 90f, 0.4f, 1500f, false, Mode.Main));
        stars.add(new StarData(assets, 419f, 205f, 180f, 0.5f, 2500f, true));
        stars.add(new StarData(assets, 729f, 304f, 0f, 0.55f, 200f, false));
        stars.add(new StarData(assets, 257f, 315f, 90f, 0.4f, 1200f, true, Mode.Main));
        stars.add(new StarData(assets, 525f, 317f, 180f, 0.4f, 700f, false));
        stars.add(new StarData(assets, 813f, 372f, 0f, 0.4f, 1000f, true));
        stars.add(new StarData(assets, 616f, 430f, 90f, 0.55f, 0f, false));
        stars.add(new StarData(assets, 387f, 419f, 180f, 0.4f, 100f, true));
        stars.add(new StarData(assets, 97f, 405f, 45f, 0.5f, 800f, false));
        stars.add(new StarData(assets, 86f, 325f, 25f, 0.5f, 300f, false, Mode.Gameplay));

        mode = Mode.Menus;
    }

    public Mode getMode()
    {
        return mode;
    }

    public void setMode(Mode mode)
    {
        this.mode = mode;
    }

    public void update(GameTime gameTime)
    {
        elapsedTime += gameTime.delta;

        for (StarData star : stars)
        {
            star.update(gameTime, elapsedTime);
        }
    }

    public void draw(Batch batch)
    {
        batch.disableBlending();
        batch.draw(backgroundRegion, 0f, 0f, graphicsSettings.virtualWidth,
                graphicsSettings.virtualHeight);
        batch.enableBlending();

        for (StarData star : stars)
        {
            if (star.hideOnMode == null || !(mode ==  star.hideOnMode))
            {
                star.draw(batch);
            }
        }
    }

    class StarData
    {
        private static final float PIVOTX = 20f;
        private static final float PIVOTY = 20f;

        private float x;
        private float y;
        private float rotation;
        private boolean clockwise;
        private Mode hideOnMode;

        private float initialScale;
        private float seed;
        private float scale;

        private TextureAtlas.AtlasRegion region;

        StarData(GameAssets assets, float x, float y, float initialAngle,
                 float initialScale, float seed, boolean clockwise)
        {
            this(assets, x, y, initialAngle, initialScale, seed, clockwise, null);
        }

        StarData(GameAssets assets, float x, float y, float initialAngle,
                        float initialScale, float seed, boolean clockwise,
                        Mode hideOnMode)
        {
            this.x = x - PIVOTX;
            this.y = y - PIVOTY;
            this.rotation = -initialAngle;
            this.clockwise = clockwise;
            this.hideOnMode = hideOnMode;

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

            scale = initialScale + (initialScale * 0.35f) * MathUtils.sin((elapsedTime * 3.5f) + seed);
        }

        void draw(Batch batch)
        {
            batch.draw(region, x, y, PIVOTX, PIVOTY,
                    region.getRegionWidth(), region.getRegionHeight(),
                    scale, scale, rotation);
        }
    }
}