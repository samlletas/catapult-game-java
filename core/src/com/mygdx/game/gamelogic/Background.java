package com.mygdx.game.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.engine.GameTime;
import com.mygdx.game.assets.GameAssets;

public final class Background
{
    private Array<StarData> stars;

    public Background()
    {
        stars = new Array<StarData>();

        TextureAtlas.AtlasRegion region = GameAssets.AtlasRegions.star.instance;

        stars.add(new StarData(region, 774f, 53f, 0f, 0.8f, 1000f, false));
        stars.add(new StarData(region, 570f, 75f, 90f, 0.4f, 500f, true));
        stars.add(new StarData(region, 452f, 42f, 45f, 0.45f, 1500f, false));
        stars.add(new StarData(region, 302f, 65f, 180f, 0.5f, 2500f, true));
        stars.add(new StarData(region, 197f, 147f, 0f, 0.55f, 3000f, false));
        stars.add(new StarData(region, 78f, 42f, 90f, 0.55f, 2000f, true));
        stars.add(new StarData(region, 61f, 193f, 45f, 0.7f, 200f, false));
        stars.add(new StarData(region, 749f, 174f, 0f, 0.45f, 500f, true));
        stars.add(new StarData(region, 588f, 223f, 90f, 0.4f, 1500f, false));
        stars.add(new StarData(region, 419f, 205f, 180f, 0.5f, 2500f, true));
        stars.add(new StarData(region, 729f, 304f, 0f, 0.55f, 200f, false));
        stars.add(new StarData(region, 257f, 315f, 90f, 0.4f, 1200f, true));
        stars.add(new StarData(region, 509f, 347f, 180f, 0.4f, 100f, false));
        stars.add(new StarData(region, 813f, 372f, 0f, 0.6f, 1000f, true));
        stars.add(new StarData(region, 616f, 430f, 90f, 0.55f, 0f, false));
        stars.add(new StarData(region, 387f, 419f, 180f, 0.4f, 100f, true));
        stars.add(new StarData(region, 97f, 405f, 45f, 0.5f, 800f, false));
    }

    public void update(GameTime gameTime)
    {
        Array<StarData> localStars = stars;
        int size = localStars.size;

        StarData star;

        for (int i = 0; i < size; i++)
        {
            star = localStars.get(i);
            star.update(gameTime);
        }
    }

    public void draw(SpriteBatch spriteBatch)
    {
        Array<StarData> localStars = stars;
        int size = localStars.size;

        StarData star;

        for (int i = 0; i < size; i++)
        {
            star = localStars.get(i);
            star.draw(spriteBatch);
        }
    }
}

class StarData
{
    private static final float PIVOTX = 20f;
    private static final float PIVOTY = 20f;

    private TextureAtlas.AtlasRegion region;

    private float x;
    private float y;

    private float initialAngle;
    private float initialScale;
    private float seed;

    private float rotation;
    private float scale;

    private boolean clockwise;

    public StarData(TextureAtlas.AtlasRegion region, float x, float y,
                     float initialAngle, float initialScale, float seed,
                     boolean clockwise)
    {
        this.region = region;

        this.x = x - PIVOTX;
        this.y = y - PIVOTY;

        this.initialAngle = initialAngle;
        this.initialScale = initialScale;
        this.seed = seed;
        this.clockwise = clockwise;

        this.rotation = -initialAngle;
    }

    void update(GameTime gameTime)
    {
        if (clockwise)
        {
            rotation += 15f * gameTime.delta;
        }
        else
        {
            rotation -= 15f * gameTime.delta;
        }

        scale = initialScale + (initialScale * 0.3f) * MathUtils.sin((gameTime.elapsed * 3f) + seed);
    }

    void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(region, x, y, PIVOTX, PIVOTY,
                region.getRegionWidth(), region.getRegionHeight(),
                scale, scale, rotation);
    }
}
