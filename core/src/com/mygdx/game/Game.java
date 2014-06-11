package com.mygdx.game;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.UBJsonReader;
import com.engine.GameAdapter;
import com.engine.GameTime;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.engine.shaders.Base3DShader;

public final class Game extends GameAdapter
{
    private Texture img;
    private Model model;
    private ModelInstance modelInstance;
    private Environment environment;
    private CameraInputController controller;

    private Base3DShader shader;

    public Game()
    {
        super(800, 480);
    }

    @Override
    public void initialize()
    {
        img = new Texture("textures/badlogic.jpg");

        UBJsonReader reader = new UBJsonReader();
        ModelLoader loader = new G3dModelLoader(reader);

        model = loader.loadModel(Gdx.files.getFileHandle("models/cube.g3db",
                Files.FileType.Internal));
        modelInstance = new ModelInstance(model);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight,
                242f / 255f, 36f / 255f, 95f / 255f, 1f));

        controller = new CameraInputController(perspectiveCamera);
        Gdx.input.setInputProcessor(controller);

        shader = new Base3DShader();
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
        modelBatch.render(modelInstance, environment);
        modelBatch.end();
    }
}
