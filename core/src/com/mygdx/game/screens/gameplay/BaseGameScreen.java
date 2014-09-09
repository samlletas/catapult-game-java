package com.mygdx.game.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.BaseShaderProvider;
import com.engine.GameSettings;
import com.engine.GameTime;
import com.engine.camera.CameraShaker2D;
import com.engine.graphics.shaders.shaders3d.Custom3DShader;
import com.engine.screens.GameScreen;
import com.mygdx.game.assets.GameAssets;
import com.mygdx.game.gamelogic.*;

public class BaseGameScreen extends GameScreen
{
    protected CameraShaker2D cameraShaker;
    protected Background background;
    protected Grass grass;
    protected Ball ball;
    protected Catapult catapult;

    protected CollisionTester collisionTester;
    protected Crystal crystal;
    protected ModelBatch modelBatch;

    // Para 60 FPS casi constantes(Android-Sam): 30 máx
    protected int updates = 1;

    // Para 60 FPS casi constantes(Android-Sam): 7 máx
    protected int draws = 1;

    public BaseGameScreen(GameSettings settings,
                          OrthographicCamera orthographicCamera,
                          PerspectiveCamera perspectiveCamera)
    {
        super(settings, orthographicCamera, perspectiveCamera);
    }

    @Override
    public void initialize()
    {
        cameraShaker = new CameraShaker2D(orthographicCamera, 80, 0, 0, 0.75f, 0.99f);
        background = new Background();
        grass = new Grass(settings);
        ball = new Ball(settings, cameraShaker, grass, orthographicCamera);
        catapult = new Catapult(ball);

        collisionTester = new CollisionTester(grass);
        crystal = new Crystal();
        modelBatch = new ModelBatch(new TestShaderProvider());
    }

    @Override
    public void update(GameTime gameTime)
    {
        for (int i = 0; i < updates; i++)
        {
            cameraShaker.update(gameTime);
            background.update(gameTime);

            catapult.update(gameTime);
            ball.update(gameTime);

            grass.update(gameTime);

            collisionTester.update(gameTime);
            crystal.update(gameTime);
        }
    }

    @Override
    public void draw(GameTime gameTime, SpriteBatch spriteBatch)
    {
        for (int i = 0; i < draws; i++)
        {
            cameraShaker.beginDraw(spriteBatch);

            spriteBatch.begin();
            background.draw(spriteBatch);
            spriteBatch.end();

            ball.draw(spriteBatch);

            spriteBatch.begin();
            catapult.draw(spriteBatch);
            grass.draw(spriteBatch);
            collisionTester.draw(spriteBatch);

            spriteBatch.draw(GameAssets.AtlasRegions.crystal.instance, 478, 172, 44, 55);
            spriteBatch.draw(GameAssets.AtlasRegions.crystal.instance, 600, 130, 44, 55);

            spriteBatch.end();

            crystal.draw(modelBatch, orthographicCamera);

            cameraShaker.endDraw(spriteBatch);
        }
    }

    class TestShader extends Custom3DShader
    {
        public TestShader(Renderable renderable)
        {
            super(renderable);
        }

        @Override
        protected String getCustomVertexShader()
        {
            return Gdx.files.internal("shaders/crystal.vert.glsl").readString();
        }

        @Override
        protected String getCustomFragmentShader()
        {
            return Gdx.files.internal("shaders/crystal.frag.glsl").readString();
        }

        @Override
        protected void addCustomGlobalUniforms()
        {

        }

        @Override
        protected void addCustomLocalUniforms()
        {

        }
    }

    class TestShaderProvider extends BaseShaderProvider
    {
        @Override
        protected Shader createShader(Renderable renderable)
        {
            return new TestShader(renderable);
        }
    }
}