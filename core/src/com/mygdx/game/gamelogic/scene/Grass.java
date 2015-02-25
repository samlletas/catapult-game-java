package com.mygdx.game.gamelogic.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.engine.GameTime;
import com.engine.collision2d.GamePolygon;
import com.engine.graphics.GraphicsSettings;
import com.engine.graphics.graphics2D.animation.skeletal.AnimationPlayer;
import com.mygdx.game.assets.GameAssets;

public final class Grass
{
    private static final int GROUND_TEXTURE_REGION_WIDTH = 27;
    private static final int GROUND_TEXTURE_REGION_HEIGHT = 35;
    private static final float GROUND_TEXTURE_REGION_SCALE = 2f;

    private GraphicsSettings settings;

    private TextureRegion[][] groundRegions;
    private Array<Vector2> groundPositions;

    private Array<GrassData> grassDatas;
    private Array<AnimationPlayer> tulipans;
    private Array<AnimationPlayer> flowers;
    private Array<AnimationPlayer> grassFlowers;

    private GamePolygon leftSidePolygon;
    private GamePolygon rightSidePolygon;

    private float elapsedTime = 0f;

    public Grass(GameAssets assets, GraphicsSettings settings)
    {
        this.settings = settings;

        assets.atlasRegions.ground.getInstance().flip(false, true);
        this.groundRegions = assets.atlasRegions.ground.getInstance().split(
                GROUND_TEXTURE_REGION_WIDTH, GROUND_TEXTURE_REGION_HEIGHT);
        this.groundPositions = new Array<Vector2>(16);
        this.grassDatas = new Array<GrassData>(32);
        this.tulipans = new Array<AnimationPlayer>(3);
        this.flowers = new Array<AnimationPlayer>(2);
        this.grassFlowers = new Array<AnimationPlayer>(6);

        initializeGround();
        initializeGrassDatas(assets);
        initializeTulipans(assets);
        initializeFlowers(assets);
        initializeGrassFlowers(assets);
    }

    private void initializeGround()
    {
        setGroundPolygons();
        setGroundPositions();
        flipGroundRegions();
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
        float startX = -5;
        float bottomY = settings.virtualHeight + 5;

        float width = GROUND_TEXTURE_REGION_WIDTH * GROUND_TEXTURE_REGION_SCALE;
        float height = GROUND_TEXTURE_REGION_HEIGHT * GROUND_TEXTURE_REGION_SCALE;
        float x;
        float y;

        int size = groundRegions[0].length;

        for (int i = 0; i < size; i++)
        {
            x = startX + (i * width);
            y = bottomY - height;

            groundPositions.add(new Vector2(x, y));
        }
    }

    private void flipGroundRegions()
    {
        TextureRegion[] regions = groundRegions[0];

        for (int i = 0, n = regions.length; i < n; i++)
        {
            regions[i].flip(false, true);
        }
    }

    private void initializeGrassDatas(GameAssets assets)
    {
        TextureAtlas.AtlasRegion grass1 = assets.atlasRegions.grass1.getInstance();
        TextureAtlas.AtlasRegion grass2 = assets.atlasRegions.grass2.getInstance();
        TextureAtlas.AtlasRegion grass3 = assets.atlasRegions.grass3.getInstance();

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

    private void initializeTulipans(GameAssets assets)
    {
        AnimationPlayer tulipan1 = assets.animations.tulipan.getInstance();
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

    private void initializeFlowers(GameAssets assets)
    {
        AnimationPlayer flower1 = assets.animations.flower.getInstance();
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

    private void initializeGrassFlowers(GameAssets assets)
    {
        AnimationPlayer grassFlower1 = assets.animations.grassFlower.getInstance();
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
        return polygon.onCollision(leftSidePolygon) ||
                polygon.onCollision(rightSidePolygon);
    }

    public void update(GameTime gameTime)
    {
        elapsedTime += gameTime.delta;

        updateGrassDatas();
        updateTulipans(gameTime);
        updateGrassFlowers(gameTime);
        updateFlowers(gameTime);
    }

    private void updateGrassDatas()
    {
        Array<GrassData> localGrassDatas = grassDatas;

        for (int i = 0, n = localGrassDatas.size; i < n; i++)
        {
            localGrassDatas.get(i).update(elapsedTime);
        }
    }

    private void updateTulipans(GameTime gameTime)
    {
        Array<AnimationPlayer> localTulipans = tulipans;

        for (int i = 0, n = localTulipans.size; i < n; i++)
        {
            localTulipans.get(i).update(gameTime);
        }
    }

    private void updateGrassFlowers(GameTime gameTime)
    {
        Array<AnimationPlayer> localGrassFlowers = grassFlowers;

        for (int i = 0, n = localGrassFlowers.size; i < n; i++)
        {
            localGrassFlowers.get(i).update(gameTime);
        }
    }

    private void updateFlowers(GameTime gameTime)
    {
        Array<AnimationPlayer> localFlowers = flowers;

        for (int i = 0, n = localFlowers.size; i < n; i++)
        {
            localFlowers.get(i).update(gameTime);
        }
    }

    public void draw(Batch batch)
    {
        drawTulipans(batch);
        drawGrassFlowers(batch);
        drawFlowers(batch);
        drawGrassDatas(batch);
        drawGround(batch);
    }

    private void drawTulipans(Batch batch)
    {
        Array<AnimationPlayer> localTulipans = tulipans;

        for (int i = 0, n = localTulipans.size; i < n; i++)
        {
            localTulipans.get(i).draw(batch);
        }
    }

    private void drawGrassFlowers(Batch batch)
    {
        Array<AnimationPlayer> localGrassFlowers = grassFlowers;

        for (int i = 0, n = localGrassFlowers.size; i < n; i++)
        {
            localGrassFlowers.get(i).draw(batch);
        }
    }

    private void drawFlowers(Batch batch)
    {
        Array<AnimationPlayer> localFlowers = flowers;

        for (int i = 0, n = localFlowers.size; i < n; i++)
        {
            localFlowers.get(i).draw(batch);
        }
    }

    private void drawGrassDatas(Batch batch)
    {
        Array<GrassData> localGrassDatas = grassDatas;

        for (int i = 0, n = localGrassDatas.size; i < n; i++)
        {
            localGrassDatas.get(i).draw(batch);
        }
    }

    private void drawGround(Batch batch)
    {
        float width = GROUND_TEXTURE_REGION_WIDTH * GROUND_TEXTURE_REGION_SCALE;
        float height = GROUND_TEXTURE_REGION_HEIGHT * GROUND_TEXTURE_REGION_SCALE;

        Array<Vector2> localGroundPositions = groundPositions;
        TextureRegion[] regions = groundRegions[0];
        Vector2 position;

        for (int i = 0, n = regions.length; i < n; i++)
        {
            position = localGroundPositions.get(i);
            batch.draw(regions[i], position.x, position.y, width, height);
        }
    }

    public void drawPolygons(ShapeRenderer shapeRenderer)
    {
        leftSidePolygon.draw(shapeRenderer, Color.MAGENTA);
        rightSidePolygon.draw(shapeRenderer, Color.MAGENTA);
    }

    class GrassData
    {
        private static final float SPEED = 2.5f;
        private static final float ROTATION_DIFFERENCE = 12.5f;
        private static final float INITIAL_SCALE = 1.05f;
        private static final float SCALE_DIFFERENCE = 0.05f;

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
                         float pivoyX, float pivotY, float initialAngle,
                         float seed)
        {
            this.region = region;

            this.x = x - pivoyX;
            this.y = y - pivotY;
            this.pivotX = pivoyX;
            this.pivotY = pivotY;
            this.initialAngle = initialAngle;
            this.seed = seed;
        }

        void update(float elapsedTime)
        {
            float sin = MathUtils.sin((elapsedTime * SPEED) + seed);

            rotation = -initialAngle + (ROTATION_DIFFERENCE * sin);
            scale = INITIAL_SCALE + (SCALE_DIFFERENCE * sin);
        }

        void draw(Batch batch)
        {
            batch.draw(region, x, y, pivotX, pivotY,
                    region.getRegionWidth(), region.getRegionHeight(),
                    scale, scale, rotation);
        }
    }
}
