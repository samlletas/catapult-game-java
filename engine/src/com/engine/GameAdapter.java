package com.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.viewport.*;
import com.engine.graphics.GraphicsSettings;
import com.engine.graphics.shaders.shaders2d.Custom2DShader;
import com.engine.graphics.shaders.shaders2d.Default2DShader;
import com.engine.screens.ScreenManager;
import com.engine.utilities.ColorUtilities;

public abstract class GameAdapter extends ApplicationAdapter
{
    protected GraphicsSettings graphicsSettings;

    protected ShaderProgram defaultShader;
    protected OrthographicCamera orthographicCamera;
    protected PerspectiveCamera perspectiveCamera;
    protected Viewport viewport2D;
    protected Viewport viewport3D;

    protected Batch spriteBatch;
    protected ModelBatch modelBatch;
    private GameTime gameTime;

    public GameAdapter(GraphicsSettings graphicsSettings)
    {
        this.graphicsSettings = graphicsSettings;
    }

    @Override
    public final void create()
    {
        defaultShader = createDefaultShader();
        orthographicCamera = createOrthoGraphicCamera();
        perspectiveCamera = createPerspectiveCamera();
        viewport2D = create2DViewport();
        viewport3D = create3DViewport();

        spriteBatch = getSpriteBatch();
        modelBatch = getModelBatch();
        gameTime = new GameTime();

        initialize();
    }

    protected Batch getSpriteBatch()
    {
        return new SpriteBatch(1000, defaultShader);
    }

    protected ModelBatch getModelBatch()
    {
        return new ModelBatch();
    }

    /**
     * Crea el shader por default que utilizará el SpriteBatch. Esta función es
     * llamada en el GameAdapter.create().
     */
    protected ShaderProgram createDefaultShader()
    {
        return new Default2DShader();
    }

    /**
     * Crea la cámara ortográfica. Esta función es llamada en el
     * GameAdapter.create().
     */
    protected OrthographicCamera createOrthoGraphicCamera()
    {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(true, graphicsSettings.virtualWidth, graphicsSettings.virtualHeight);
        camera.far = 300f;

        return camera;
    }

    /**
     * Crea la cámara de perspectiva. Esta función es llamada en el
     * GameAdapter.create().
     */
    protected PerspectiveCamera createPerspectiveCamera()
    {
        PerspectiveCamera camera = new PerspectiveCamera(75, graphicsSettings.virtualWidth,
                graphicsSettings.virtualHeight);
        camera.near = -1f;
        camera.far = 300f;
        camera.position.set(0f, 0f, 5f);
        camera.lookAt(0f, 0f, 0f);

        return camera;
    }

    /**
     * Crea el viewport 2D (Por default StretchViewport). Esta función es
     * llamada en el GameAdapter.create().
     */
    protected Viewport create2DViewport()
    {
        return new StretchViewport(graphicsSettings.virtualWidth, graphicsSettings.virtualHeight, orthographicCamera);
    }

    /**
     * Crea el viewport 3D (Por default StretchViewport). Esta función es
     * llamada en el GameAdapter.create().
     */
    protected Viewport create3DViewport()
    {
        return new StretchViewport(graphicsSettings.virtualWidth, graphicsSettings.virtualHeight, perspectiveCamera);
    }

    @Override
    public final void render()
    {
        gameTime.update();
        update(gameTime);

        orthographicCamera.update();
        perspectiveCamera.update();

        // Asignación de matrices para respetar el viewport
        spriteBatch.setProjectionMatrix(orthographicCamera.combined);

        // Limpieza de pantalla
        Gdx.gl.glClearColor(graphicsSettings.clearColor.r, graphicsSettings.clearColor.g,
                graphicsSettings.clearColor.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        draw(gameTime);
    }

    @Override
    public void resize(int width, int height)
    {
        viewport2D.update(width, height);
        viewport3D.update(width, height);
    }

    @Override
    public void dispose()
    {
        if (spriteBatch != null) spriteBatch.dispose();
        if (modelBatch != null) modelBatch.dispose();
        if (defaultShader != null) defaultShader.dispose();
    }

    protected abstract void initialize();
    protected abstract void update(GameTime gameTime);
    protected abstract void draw(GameTime gameTime);
}