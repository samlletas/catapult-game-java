package com.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.viewport.*;
import com.engine.utilities.ColorUtilities;

public abstract class GameAdapter extends ApplicationAdapter
{
    protected SpriteBatch spriteBatch;
    protected ModelBatch modelBatch;

    // Cámara 2D
    protected Viewport viewport2D;
    protected OrthographicCamera orthographicCamera;

    // Cámara 3D
    protected Viewport viewport3D;
    protected PerspectiveCamera perspectiveCamera;

    // Tamaño virtual del área de renderizado
    protected int virtualWidth;
    protected int virtualHeight;

    protected Color clearColor;
    private GameTime gameTime;
    private Texture blackBarTexture;

    public GameAdapter(int virtualWidth, int virtualHeight)
    {
        this.virtualWidth = virtualWidth;
        this.virtualHeight = virtualHeight;
    }

    @Override
    public final void create()
    {
        spriteBatch = new SpriteBatch();
        modelBatch = new ModelBatch();

        // Color por default "CornFlowerBlue"
        clearColor = new Color(
                ColorUtilities.ByteToFloat(100), // R
                ColorUtilities.ByteToFloat(149), // G
                ColorUtilities.ByteToFloat(237), // B
                1f);                             // A

        gameTime = new GameTime();

        initialize2DCamera();
        initialize3DCamera();
        createBlackBarTexture();
        initialize();
    }

    private void initialize2DCamera()
    {
        orthographicCamera = new OrthographicCamera();
        orthographicCamera.setToOrtho(true, virtualWidth, virtualHeight);

        // Por default se utiliza el StretchViewport
        viewport2D = new StretchViewport(virtualWidth, virtualHeight,
                orthographicCamera);
    }

    private void initialize3DCamera()
    {
        perspectiveCamera = new PerspectiveCamera(75,
                virtualWidth, virtualHeight);
        perspectiveCamera.near = -1f;
        perspectiveCamera.far = 300f;
        perspectiveCamera.position.set(0f, 0f, 5f);
        perspectiveCamera.lookAt(0f, 0f, 0f);

        // Por default se utiliza el StretchViewport
        viewport3D = new StretchViewport(virtualWidth, virtualHeight,
                perspectiveCamera);
    }

    private void createBlackBarTexture()
    {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        blackBarTexture = new Texture(pixmap);
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
        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        draw(gameTime);

        drawBlackBars();
    }

    private void drawBlackBars()
    {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        int leftBarWidth = viewport2D.getLeftGutterWidth();
        int rightBarWidth = viewport2D.getRightGutterWidth();
        int topBarHeight = viewport2D.getTopGutterHeight();
        int bottomBarHeight = viewport2D.getBottomGutterHeight();

        // Habilitación de dibujado en toda la pantalla
        Gdx.gl.glViewport(0, 0, screenWidth, screenHeight);
        spriteBatch.getProjectionMatrix().idt().setToOrtho2D(0, 0,
                screenWidth, screenHeight);
        spriteBatch.getTransformMatrix().idt();

        spriteBatch.begin();

        // Barras horizontales
        if (leftBarWidth > 0)
        {
            spriteBatch.draw(blackBarTexture, 0, 0,
                    leftBarWidth, screenHeight);
        }
        if (rightBarWidth > 0)
        {
            spriteBatch.draw(blackBarTexture, viewport2D.getRightGutterX(), 0,
                    rightBarWidth, screenHeight);
        }

        // Barras verticales
        if (bottomBarHeight > 0)
        {
            spriteBatch.draw(blackBarTexture, 0, 0,
                    screenWidth, bottomBarHeight);
        }
        if (topBarHeight > 0)
        {
            spriteBatch.draw(blackBarTexture, 0, viewport2D.getTopGutterY(),
                    screenWidth, topBarHeight);
        }

        spriteBatch.end();

        // Reseteo del viewport
        viewport2D.update(screenWidth, screenHeight, true);
    }

    @Override
    public void resize(int width, int height)
    {
        viewport2D.update(width, height);
        viewport3D.update(width, height);
    }

    protected abstract void initialize();
    protected abstract void update(GameTime gameTime);
    protected abstract void draw(GameTime gameTime);
}
