package com.mygdx.game.gamelogic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.engine.GameSettings;
import com.engine.GameTime;
import com.engine.assets.Asset;
import com.engine.collision2d.GamePolygon;
import com.engine.graphics.animation.AnimationPlayer;
import com.mygdx.game.assets.GameAssets;

public final class Grass
{
    private GameSettings settings;
    private Array<Vector2> groundPositions;
    private Array<GrassData> grassDatas;
    private Array<AnimationPlayer> tulipans;
    private Array<AnimationPlayer> flowers;
    private Array<AnimationPlayer> grassFlowers;

    private GamePolygon leftSidePolygon;
    private GamePolygon rightSidePolygon;

    public Grass(GameSettings settings)
    {
        this.settings = settings;
        this.groundPositions = new Array<Vector2>();
        this.grassDatas = new Array<GrassData>();
        this.tulipans = new Array<AnimationPlayer>();
        this.flowers = new Array<AnimationPlayer>();
        this.grassFlowers = new Array<AnimationPlayer>();

        setGroundPolygons();
        setGroundPositions();
        setGrassDatas();

        initializeTulipans();
        initializeFlowers();
        initializeGrassFlowers();
    }

    private void setGroundPolygons()
    {
        leftSidePolygon = new GamePolygon(new float[]
        {
                -215f, -38f,
                 -27f, -50f,
                 114f, -48f,
                 235f, -30f,
                 235f,  20f,
                -215f,  20f
        });

        rightSidePolygon = new GamePolygon(new float[]
        {
                -198, -30,
                 202, -17,
                 202,  20,
                -198,  20
        });

        leftSidePolygon.setPosition(217, 459);
        rightSidePolygon.setPosition(650, 459);

        leftSidePolygon.isSolid = true;
        rightSidePolygon.isSolid = true;
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

    private void initializeTulipans()
    {
        AnimationPlayer tulipan1 = GameAssets.Animations.tulipan.instance;
        AnimationPlayer tulipan2 = tulipan1.copy();
        AnimationPlayer tulipan3 = tulipan1.copy();

        tulipan1.position.x = 634;
        tulipan1.position.y = 445;
        tulipan1.rotation = 10f;
        tulipan1.play("blow", 100);

        tulipan2.position.x = 644;
        tulipan2.position.y = 441;
        tulipan2.rotation = 350f;
        tulipan2.play("blow", 400);

        tulipan3.position.x = 652;
        tulipan3.position.y = 444;
        tulipan3.rotation = 330f;
        tulipan3.play("blow", 700);

        tulipans.add(tulipan1);
        tulipans.add(tulipan2);
        tulipans.add(tulipan3);
    }

    private void initializeFlowers()
    {
        AnimationPlayer flower1 = GameAssets.Animations.flower.instance;
        AnimationPlayer flower2 = flower1.copy();

        flower1.position.x = 310;
        flower1.position.y = 405;
        flower1.play("blow");

        flower2.position.x = 347;
        flower2.position.y = 408;
        flower2.rotation = 345f;
        flower2.play("blow", 500);

        flowers.add(flower1);
        flowers.add(flower2);
    }

    private void initializeGrassFlowers()
    {
        AnimationPlayer grassFlower1 = GameAssets.Animations.grassFlower.instance;
        AnimationPlayer grassFlower2 = grassFlower1.copy();
        AnimationPlayer grassFlower3 = grassFlower1.copy();
        AnimationPlayer grassFlower4 = grassFlower1.copy();
        AnimationPlayer grassFlower5 = grassFlower1.copy();
        AnimationPlayer grassFlower6 = grassFlower1.copy();

        grassFlower1.position.x = 240f;
        grassFlower1.position.y = 427f;
        grassFlower1.rotation = 0f;
        grassFlower1.play("blow");

        grassFlower2.position.x = 271;
        grassFlower2.position.y = 429;
        grassFlower2.rotation = 0f;
        grassFlower2.play("blow", 200);

        grassFlower3.position.x = 276;
        grassFlower3.position.y = 428;
        grassFlower3.rotation = 350f;
        grassFlower3.play("blow", 600);

        grassFlower4.position.x = 309;
        grassFlower4.position.y = 427;
        grassFlower4.rotation = 350f;
        grassFlower4.play("blow", 400);

        grassFlower5.position.x = 337;
        grassFlower5.position.y = 435;
        grassFlower5.rotation = 345f;
        grassFlower5.play("blow", 1000);

        grassFlower6.position.x = 367;
        grassFlower6.position.y = 442;
        grassFlower6.rotation = 340f;
        grassFlower6.play("blow", 800);

        grassFlowers.add(grassFlower1);
        grassFlowers.add(grassFlower2);
        grassFlowers.add(grassFlower3);
        grassFlowers.add(grassFlower4);
        grassFlowers.add(grassFlower5);
        grassFlowers.add(grassFlower6);
    }

    public boolean onCollision(GamePolygon polygon)
    {
        return rightSidePolygon.onCollision(polygon) ||
                leftSidePolygon.onCollision(polygon);
    }

    public void update(GameTime gameTime)
    {
        updateGrassDatas(gameTime);
        updateTulipans(gameTime);
        updateGrassFlowers(gameTime);
        updateFlowers(gameTime);
    }

    private void updateGrassDatas(GameTime gameTime)
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

    private void updateTulipans(GameTime gameTime)
    {
        Array<AnimationPlayer> localTulipans = tulipans;
        int size = localTulipans.size;

        AnimationPlayer tulipan;

        for (int i = 0; i < size; i++)
        {
            tulipan = localTulipans.get(i);
            tulipan.update(gameTime);
        }
    }

    private void updateGrassFlowers(GameTime gameTime)
    {
        Array<AnimationPlayer> localGrassFlowers = grassFlowers;
        int size = localGrassFlowers.size;

        AnimationPlayer grassFlower;

        for (int i = 0; i < size; i++)
        {
            grassFlower = localGrassFlowers.get(i);
            grassFlower.update(gameTime);
        }
    }

    private void updateFlowers(GameTime gameTime)
    {
        Array<AnimationPlayer> localFlowers = flowers;
        int size = localFlowers.size;

        AnimationPlayer flower;

        for (int i = 0; i < size; i++)
        {
            flower = localFlowers.get(i);
            flower.update(gameTime);
        }
    }

    public void draw(SpriteBatch spriteBatch)
    {
        drawTulipans(spriteBatch);
        drawGrassFlowers(spriteBatch);
        drawFlowers(spriteBatch);
        drawGrassDatas(spriteBatch);
        drawGround(spriteBatch);
    }

    private void drawTulipans(SpriteBatch spriteBatch)
    {
        Array<AnimationPlayer> localTulipans = tulipans;
        int size = localTulipans.size;

        AnimationPlayer tulipan;

        for (int i = 0; i < size; i++)
        {
            tulipan = localTulipans.get(i);
            tulipan.draw(spriteBatch);
        }
    }

    private void drawGrassFlowers(SpriteBatch spriteBatch)
    {
        Array<AnimationPlayer> localGrassFlowers = grassFlowers;
        int size = localGrassFlowers.size;

        AnimationPlayer grassFlower;

        for (int i = 0; i < size; i++)
        {
            grassFlower = localGrassFlowers.get(i);
            grassFlower.draw(spriteBatch);
        }
    }

    private void drawFlowers(SpriteBatch spriteBatch)
    {
        Array<AnimationPlayer> localFlowers = flowers;
        int size = localFlowers.size;

        AnimationPlayer flower;

        for (int i = 0; i < size; i++)
        {
            flower = localFlowers.get(i);
            flower.draw(spriteBatch);
        }
    }

    private void drawGrassDatas(SpriteBatch spriteBatch)
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

        leftSidePolygon.draw(spriteBatch, Color.MAGENTA);
        rightSidePolygon.draw(spriteBatch, Color.MAGENTA);
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

            this.initialAngle -= 10f;
        }

        void update(GameTime gameTime)
        {
            float sin = MathUtils.sin((gameTime.elapsed * 2.5f) + seed);

            rotation = -initialAngle + 12.5f * sin;
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
