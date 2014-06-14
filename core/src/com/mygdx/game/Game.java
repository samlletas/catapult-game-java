package com.mygdx.game;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.UBJsonReader;
import com.engine.GameAdapter;
import com.engine.GameTime;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.mygdx.game.shaders.TestShader;

public final class Game extends GameAdapter
{
    private Texture img;
    private Model model;
    private ModelInstance modelInstance;
    private Environment environment;
    private CameraInputController controller;
    private TestShader shader;
    private AssetManager assetManager;

    public Game()
    {
        super(800, 480);
    }

    @Override
    public void initialize()
    {
        img = new Texture("textures/badlogic.jpg");

        assetManager = new AssetManager();
        assetManager.load("models/cube.g3db", Model.class);
        assetManager.load("models/envelope.g3db", Model.class);
        assetManager.load("models/ship.obj", Model.class);
        assetManager.finishLoading();

//        model = assetManager.get("models/cube.g3db");
        model = assetManager.get("models/envelope.g3db");
//        model = assetManager.get("models/ship.obj");
        modelInstance = new ModelInstance(model);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight,
                1f, 1f, 1f, 1f));

        controller = new CameraInputController(perspectiveCamera);
        Gdx.input.setInputProcessor(controller);

        test();
    }

    private void test()
    {
//        shader = new TestShader(VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
//                | VertexAttributes.Usage.TextureCoordinates);

        shader = new TestShader(VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        shader.init();
    }

    @Override
    protected void update(GameTime gameTime)
    {
    }

    @Override
    protected void draw(GameTime gameTime)
    {
        spriteBatch.begin();
        spriteBatch.draw(img, 0, 0);
        spriteBatch.end();

        modelBatch.begin(perspectiveCamera);
//        modelBatch.render(modelInstance, environment);
        modelBatch.render(modelInstance, shader);
        modelBatch.end();
    }
}
