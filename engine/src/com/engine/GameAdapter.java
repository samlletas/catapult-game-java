package com.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.viewport.*;
import com.engine.graphics.GraphicsSettings;
import com.engine.graphics.shaders.shaders2d.Custom2DShader;
import com.engine.graphics.shaders.shaders2d.Default2DShader;
import com.engine.screens.ScreenManager;
import com.engine.utilities.ColorUtilities;

public abstract class GameAdapter extends ApplicationAdapter
{
    protected GraphicsSettings graphicsSettings;

    protected Custom2DShader defaultShader;
    protected OrthographicCamera orthographicCamera;
    protected PerspectiveCamera perspectiveCamera;
    protected Viewport viewport2D;
    protected Viewport viewport3D;

    protected SpriteBatch spriteBatch;
    protected ModelBatch modelBatch;

    private GameTime gameTime;
    private Texture pixelTexture;

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

        spriteBatch = new SpriteBatch(1000, defaultShader);
        modelBatch = new ModelBatch();

        gameTime = new GameTime();
        pixelTexture = new Texture(Gdx.files.classpath("com/engine/dot.png"));

        initialize();
    }

    /**
     * Crea el shader por default que utilizará el SpriteBatch. Esta función es
     * llamada en el GameAdapter.create().
     */
    protected Custom2DShader createDefaultShader()
    {
        return new Default2DShader();
    }

    /**
     * Crea la cámara ortográfica. Esta función es llamada en el
     * GameAdapter.create().
     */
    private OrthographicCamera createOrthoGraphicCamera()
    {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(true, graphicsSettings.virtualWidth,
                graphicsSettings.virtualHeight);
        camera.far = 300f;

        return camera;
    }

    /**
     * Crea la cámara de perspectiva. Esta función es llamada en el
     * GameAdapter.create().
     */
    private PerspectiveCamera createPerspectiveCamera()
    {
        PerspectiveCamera camera = new PerspectiveCamera(75,
                graphicsSettings.virtualWidth, graphicsSettings.virtualHeight);
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
        return new StretchViewport(graphicsSettings.virtualWidth,
                graphicsSettings.virtualHeight, orthographicCamera);
    }

    /**
     * Crea el viewport 3D (Por default StretchViewport). Esta función es
     * llamada en el GameAdapter.create().
     */
    protected Viewport create3DViewport()
    {
        return new StretchViewport(graphicsSettings.virtualWidth,
                graphicsSettings.virtualHeight, perspectiveCamera);
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
        spriteBatch.getTransformMatrix().idt();
//        spriteBatch.setProjectionMatrix(orthographicCamera.projection);
//        spriteBatch.setTransformMatrix(orthographicCamera.view);

        // Limpieza de pantalla
        Gdx.gl.glClearColor(graphicsSettings.clearColor.r,
                graphicsSettings.clearColor.g,
                graphicsSettings.clearColor.b,
                1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        draw(gameTime);
        drawBlackBars();
    }

    private void drawBlackBars()
    {
        int leftBarWidth = viewport2D.getLeftGutterWidth();
        int rightBarWidth = viewport2D.getRightGutterWidth();
        int topBarHeight = viewport2D.getTopGutterHeight();
        int bottomBarHeight = viewport2D.getBottomGutterHeight();

        // Solo dibujar en caso de ser necesario
        if (leftBarWidth > 0 || rightBarWidth > 0 || topBarHeight > 0 ||
                bottomBarHeight > 0)
        {
            int screenWidth = Gdx.graphics.getWidth();
            int screenHeight = Gdx.graphics.getHeight();

            // Habilitación de dibujado en toda la pantalla
            Gdx.gl.glViewport(0, 0, screenWidth, screenHeight);

            // Reseteo de matrices del spritebatch
            spriteBatch.getProjectionMatrix().idt().setToOrtho2D(0, 0,
                    screenWidth, screenHeight);
            spriteBatch.getTransformMatrix().idt();

            // Reinicio al default defaultShader
            spriteBatch.setShader(null);

            spriteBatch.disableBlending();
            spriteBatch.begin();
            ColorUtilities.setColor(spriteBatch, 255, 0, 0, 255);

            // Barras horizontales
            if (leftBarWidth > 0)
            {
                spriteBatch.draw(pixelTexture, 0, 0,
                        leftBarWidth, screenHeight);
            }
            if (rightBarWidth > 0)
            {
                spriteBatch.draw(pixelTexture, viewport2D.getRightGutterX(), 0,
                        rightBarWidth, screenHeight);
            }

            // Barras verticales
            if (bottomBarHeight > 0)
            {
                spriteBatch.draw(pixelTexture, 0, 0, screenWidth, bottomBarHeight);
            }
            if (topBarHeight > 0)
            {
                spriteBatch.draw(pixelTexture, 0, viewport2D.getTopGutterY(),
                        screenWidth, topBarHeight);
            }

            ColorUtilities.resetColor(spriteBatch);
            spriteBatch.end();
            spriteBatch.enableBlending();

            // Reseteo del viewport
            viewport2D.update(screenWidth, screenHeight, true);
        }
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
        spriteBatch.dispose();
        modelBatch.dispose();
        defaultShader.dispose();
        pixelTexture.dispose();
    }

    protected abstract void initialize();
    protected abstract void update(GameTime gameTime);
    protected abstract void draw(GameTime gameTime);
}