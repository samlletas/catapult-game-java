package com.mygdx.game;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.UBJsonReader;
import com.engine.GameAdapter;
import com.engine.GameTime;
import com.mygdx.game.shaders.TestShader;
import com.mygdx.game.shaders.TestShaderProvider;

public final class Game extends GameAdapter
{
    private ModelBatch customModelBatch;

    private Texture img;
    private Model model;
    private ModelInstance modelInstance;
    private Environment environment;
    private CameraInputController controller;
    private AssetManager assetManager;
    private AnimationController animationController;

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
        customModelBatch = new ModelBatch(new TestShaderProvider());
        animationController = new AnimationController(modelInstance);
        animationController.setAnimation("Open", 5);
    }

    @Override
    protected void update(GameTime gameTime)
    {
        animationController.update((float)gameTime.getDelta());
    }

    @Override
    protected void draw(GameTime gameTime)
    {
        spriteBatch.begin();
        spriteBatch.draw(img, 0, 0);
        spriteBatch.end();

//        modelBatch.begin(perspectiveCamera);
//        modelBatch.render(modelInstance, environment);
//        modelBatch.end();

        customModelBatch.begin(perspectiveCamera);
        customModelBatch.render(modelInstance);
        customModelBatch.end();
    }
}
