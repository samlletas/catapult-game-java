package com.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.viewport.*;
import com.engine.assets.BaseAssetMaster;
import com.engine.graphics.shaders.shaders2d.Default2DShader;
import com.engine.utilities.ColorUtilities;

public abstract class GameAdapter extends ApplicationAdapter
{
    public static Texture dotTexture;

    protected GameSettings settings;
    protected SpriteBatch spriteBatch;
    protected ModelBatch modelBatch;
    private Default2DShader shader;
    private GameTime gameTime;

    // Cámara 2D
    protected Viewport viewport2D;
    protected OrthographicCamera orthographicCamera;

    // Cámara 3D
    protected Viewport viewport3D;
    protected PerspectiveCamera perspectiveCamera;

    public GameAdapter(GameSettings settings)
    {
        this.settings = settings;
    }

    @Override
    public final void create()
    {
        shader = new Default2DShader();
        shader.init();

        spriteBatch = new SpriteBatch(1000, shader.program);
        modelBatch = new ModelBatch();
        gameTime = new GameTime();

        initialize2DCamera();
        initialize3DCamera();

        setup2DViewport();
        setup3DViewport();

        dotTexture = new Texture(Gdx.files.classpath("com/engine/dot.png"));
        initialize();
    }

    private final void initialize2DCamera()
    {
        orthographicCamera = new OrthographicCamera();
        orthographicCamera.setToOrtho(true, settings.virtualWidth,
                settings.virtualHeight);
    }

    private final void initialize3DCamera()
    {
        perspectiveCamera = new PerspectiveCamera(75,
                settings.virtualWidth, settings.virtualHeight);
        perspectiveCamera.near = -1f;
        perspectiveCamera.far = 300f;
        perspectiveCamera.position.set(0f, 0f, 5f);
        perspectiveCamera.lookAt(0f, 0f, 0f);
    }

    /**
     * Inicializa el viewport 2D (Por default StretchViewport). Hacer override
     * en clase derivada para implementar un viewport personalizado
     */
    protected void setup2DViewport()
    {
        viewport2D = new StretchViewport(settings.virtualWidth,
                settings.virtualHeight, orthographicCamera);
    }

    /**
     * Inicializa el viewport 3D (Por default StretchViewport). Hacer override
     * en clase derivada para implementar un viewport personalizado
     */
    protected void setup3DViewport()
    {
        viewport3D = new StretchViewport(settings.virtualWidth,
                settings.virtualHeight, perspectiveCamera);
    }

    @Override
    public final void render()
    {
        gameTime.update();
        orthographicCamera.update();
        perspectiveCamera.update();

        // Asignación de matrices para respetar el viewport
        spriteBatch.setProjectionMatrix(orthographicCamera.projection);
        spriteBatch.setTransformMatrix(orthographicCamera.view);

        update(gameTime);
        Gdx.gl.glClearColor(settings.clearColor.r, settings.clearColor.g,
                settings.clearColor.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        draw(gameTime);

        drawBlackBars();
    }

    private final void drawBlackBars()
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

            // Reinicio al default shader
            spriteBatch.setShader(null);

            spriteBatch.begin();
            ColorUtilities.setTint(spriteBatch, 0, 0, 0, 255);

            // Barras horizontales
            if (leftBarWidth > 0)
            {
                spriteBatch.draw(dotTexture, 0, 0,
                        leftBarWidth, screenHeight);
            }
            if (rightBarWidth > 0)
            {
                spriteBatch.draw(dotTexture, viewport2D.getRightGutterX(), 0,
                        rightBarWidth, screenHeight);
            }

            // Barras verticales
            if (bottomBarHeight > 0)
            {
                spriteBatch.draw(dotTexture, 0, 0, screenWidth, bottomBarHeight);
            }
            if (topBarHeight > 0)
            {
                spriteBatch.draw(dotTexture, 0, viewport2D.getTopGutterY(),
                        screenWidth, topBarHeight);
            }

            ColorUtilities.resetTint(spriteBatch);
            spriteBatch.end();

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
    public void resume()
    {
    }

    @Override
    public void dispose()
    {
        spriteBatch.dispose();
        modelBatch.dispose();
        shader.dispose();
        dotTexture.dispose();
    }

    protected abstract void initialize();

    protected abstract void update(GameTime gameTime);

    protected abstract void draw(GameTime gameTime);
}
