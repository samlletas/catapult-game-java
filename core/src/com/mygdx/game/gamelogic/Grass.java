package com.mygdx.game.gamelogic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.engine.GameSettings;
import com.engine.GameTime;
import com.engine.assets.Asset;
import com.mygdx.game.assets.GameAssets;

public final class Grass
{
    private GameSettings settings;
    private Array<Vector2> groundPositions;
    private Array<GrassData> grassDatas;

    public Grass(GameSettings settings)
    {
        this.settings = settings;
        this.groundPositions = new Array<Vector2>();
        this.grassDatas = new Array<GrassData>();

        setGroundPositions();
        setGrassDatas();
    }

    private void setGroundPositions()
    {
        int width = 54;
        float startX = -5;
        float bottomY = settings.virtualHeight + 5;

        float x;
        float y;

        Array<Asset<TextureAtlas.AtlasRegion>> groundRegions =
                GameAssets.AtlasRegions.groundRegions;
        TextureAtlas.AtlasRegion region;

        int size = groundRegions.size;

        for (int i = 0; i < size; i++)
        {
            region = groundRegions.get(i).instance;

            x = startX + (i * width);
            y = bottomY - region.getRegionHeight();

            groundPositions.add(new Vector2(x, y));
        }
    }

    private void setGrassDatas()
    {
        TextureAtlas.AtlasRegion grass1 = GameAssets.AtlasRegions.grass1.instance;
        TextureAtlas.AtlasRegion grass2 = GameAssets.AtlasRegions.grass2.instance;
        TextureAtlas.AtlasRegion grass3 = GameAssets.AtlasRegions.grass3.instance;

        float grass1pivotX = 5f;
        float grass1pivotY = 34f;

        float grass2pivotX = 4f;
        float grass2pivotY = 35f;

        float grass3pivotX = 4f;
        float grass3pivotY = 28f;

        // Grass1
        grassDatas.add(new GrassData(grass1, 839f, 453f, grass1pivotX, grass1pivotY, 90f, 0f));
        grassDatas.add(new GrassData(grass1, 760f, 453f, grass1pivotX, grass1pivotY, 90f, 500f));
        grassDatas.add(new GrassData(grass1, 676f, 452f, grass1pivotX, grass1pivotY, 90f, 1500f));
        grassDatas.add(new GrassData(grass1, 584f, 450f, grass1pivotX, grass1pivotY, 90f, 200f));
        grassDatas.add(new GrassData(grass1, 493f, 444f, grass1pivotX, grass1pivotY, 85f, 700f));
        grassDatas.add(new GrassData(grass1, 396f, 433f, grass1pivotX, grass1pivotY, 86f, 2000f));
        grassDatas.add(new GrassData(grass1, 306f, 427f, grass1pivotX, grass1pivotY, 87f, 400f));
        grassDatas.add(new GrassData(grass1, 215f, 422f, grass1pivotX, grass1pivotY, 89f, 1800f));
        grassDatas.add(new GrassData(grass1, 117f, 425f, grass1pivotX, grass1pivotY, 92f, 1200f));
        grassDatas.add(new GrassData(grass1, 13f, 436f, grass1pivotX, grass1pivotY, 94f, 100f));

        // Grass2
        grassDatas.add(new GrassData(grass2, 808f, 452f, grass2pivotX, grass2pivotY, 90f, 800f));
        grassDatas.add(new GrassData(grass2, 728f, 451f, grass2pivotX, grass2pivotY, 90f, 5000f));
        grassDatas.add(new GrassData(grass2, 643f, 450f, grass2pivotX, grass2pivotY, 90f, 2500f));
        grassDatas.add(new GrassData(grass2, 552f, 446f, grass2pivotX, grass2pivotY, 90f, 800f));
        grassDatas.add(new GrassData(grass2, 459f, 439f, grass2pivotX, grass2pivotY, 85f, 200f));
        grassDatas.add(new GrassData(grass2, 365f, 429f, grass2pivotX, grass2pivotY, 86f, 100f));
        grassDatas.add(new GrassData(grass2, 274f, 423f, grass2pivotX, grass2pivotY, 87f, 1800f));
        grassDatas.add(new GrassData(grass2, 186f, 420f, grass2pivotX, grass2pivotY, 89f, 1500f));
        grassDatas.add(new GrassData(grass2, 165f, 420f, grass2pivotX, grass2pivotY, 92f, 1000f));
        grassDatas.add(new GrassData(grass2, 88f, 425f, grass2pivotX, grass2pivotY, 94f, 100f));
        grassDatas.add(new GrassData(grass2, 47f, 428f, grass2pivotX, grass2pivotY, 94f, 500f));

        // Grass3
        grassDatas.add(new GrassData(grass3, 786f, 451f, grass3pivotX, grass3pivotY, 90f, 500f));
        grassDatas.add(new GrassData(grass3, 703f, 451f, grass3pivotX, grass3pivotY, 90f, 1500f));
        grassDatas.add(new GrassData(grass3, 614f, 449f, grass3pivotX, grass3pivotY, 90f, 1000f));
        grassDatas.add(new GrassData(grass3, 523f, 445f, grass3pivotX, grass3pivotY, 90f, 1200f));
        grassDatas.add(new GrassData(grass3, 435f, 435f, grass3pivotX, grass3pivotY, 85f, 1700f));
        grassDatas.add(new GrassData(grass3, 415f, 435f, grass3pivotX, grass3pivotY, 85f, 200f));
        grassDatas.add(new GrassData(grass3, 335f, 428f, grass3pivotX, grass3pivotY, 86f, 200f));
        grassDatas.add(new GrassData(grass3, 247f, 423f, grass3pivotX, grass3pivotY, 87f, 1400f));
        grassDatas.add(new GrassData(grass3, 227f, 423f, grass3pivotX, grass3pivotY, 87f, 1000f));
        grassDatas.add(new GrassData(grass3, 143f, 422f, grass3pivotX, grass3pivotY, 89f, 180f));
        grassDatas.add(new GrassData(grass3, 68f, 428f, grass3pivotX, grass3pivotY, 92f, 2000f));
    }

    public void update(GameTime gameTime)
    {
        updateGrassDatas(gameTime);
    }

    public void updateGrassDatas(GameTime gameTime)
    {
        Array<GrassData> localGrassDatas = grassDatas;
        int size = localGrassDatas.size;

        GrassData grassData;

        for (int i = 0; i < size; i++)
        {
            grassData = localGrassDatas.get(i);
            grassData.update(gameTime);
        }
    }

    public void draw(SpriteBatch spriteBatch)
    {
        drawGrassDatas(spriteBatch);
        drawGround(spriteBatch);
    }

    public void drawGrassDatas(SpriteBatch spriteBatch)
    {
        Array<GrassData> localGrassDatas = grassDatas;
        int size = localGrassDatas.size;

        GrassData grassData;

        for (int i = 0; i < size; i++)
        {
            grassData = localGrassDatas.get(i);
            grassData.draw(spriteBatch);
        }
    }

    private void drawGround(SpriteBatch spriteBatch)
    {
        Array<Vector2> localGroundPositions = groundPositions;
        Array<Asset<TextureAtlas.AtlasRegion>> groundRegions =
                GameAssets.AtlasRegions.groundRegions;
        int size = localGroundPositions.size;

        Vector2 position;
        TextureAtlas.AtlasRegion region;

        for (int i = 0; i < size; i++)
        {
            position = localGroundPositions.get(i);
            region = groundRegions.get(i).instance;

            spriteBatch.draw(region, position.x, position.y, 55,
                    region.getRegionHeight());
        }
    }

    class GrassData
    {
        private TextureAtlas.AtlasRegion region;

        private float x;
        private float y;

        private float pivotX;
        private float pivotY;

        private float initialAngle;
        private float seed;

        private float rotation;
        private float scale;

        public GrassData(TextureAtlas.AtlasRegion region, float x, float y,
                         float pivoyX, float pivotY, float initialAngle, float seed)
        {
            this.region = region;

            this.x = x - pivoyX;
            this.y = y - pivotY;
            this.pivotX = pivoyX;
            this.pivotY = pivotY;
            this.initialAngle = initialAngle;
            this.seed = seed;
        }

        void update(GameTime gameTime)
        {
            float sin = MathUtils.sin((gameTime.elapsed * 2.5f) + seed);

            rotation = -initialAngle + 10f * sin;
//            rotation = -initialAngle;

            scale = 1.05f + 0.05f * sin;
//            scale = 1f;
        }

        void draw(SpriteBatch spriteBatch)
        {
            spriteBatch.draw(region, x, y, pivotX, pivotY,
                    region.getRegionWidth(), region.getRegionHeight(),
                    scale, scale, rotation);
        }
    }
}
