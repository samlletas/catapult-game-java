package com.mygdx.game;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
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
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
    ShaderProgram blurShader;

    public Game()
    {
        super(800, 480);
    }

    @Override
    public void initialize()
    {
        // Linear Filtering
        TextureLoader.TextureParameter parameter = new TextureLoader.TextureParameter();
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.genMipMaps = true;

        assetManager = new AssetManager();
        assetManager.load("textures/badlogic.jpg", Texture.class, parameter);
        assetManager.load("textures/test.png", Texture.class, parameter);
        assetManager.load("models/cube.g3db", Model.class);
        assetManager.load("models/cubeflat.g3db", Model.class);
        assetManager.load("models/envelope.g3db", Model.class);
        assetManager.load("models/ship.obj", Model.class);
        assetManager.finishLoading();

//        img = assetManager.get("textures/badlogic.jpg");
        img = assetManager.get("textures/test.png");

        model = assetManager.get("models/cube.g3db");
//        model = assetManager.get("models/cubeflat.g3db");
//        model = assetManager.get("models/envelope.g3db");
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
//        animationController = new AnimationController(modelInstance);
//        animationController.setAnimation("Open", 5);
        blurShader = new ShaderProgram(
                Gdx.files.internal("shaders/blur.vert.glsl").readString(),
                Gdx.files.internal("shaders/blur.frag.glsl").readString());
    }

    @Override
    protected void update(GameTime gameTime)
    {
//        animationController.update((float)gameTime.delta);
    }

    @Override
    protected void draw(GameTime gameTime)
    {
        spriteBatch.setShader(blurShader);
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
