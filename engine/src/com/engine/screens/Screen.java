package com.engine.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.graphics.GraphicsSettings;
import com.engine.GameTime;
import com.engine.utilities.ColorUtilities;

public abstract class Screen implements Disposable
{
    protected final String name;
    protected final GraphicsSettings graphicsSettings;
    protected final Viewport viewport2D;
    protected final Viewport viewport3D;
    protected final Batch batch;
    protected Color clearColor;
    protected ScreenManager screenManager;

    public Screen(String name, GraphicsSettings graphicsSettings,
                  Viewport viewport2D, Viewport viewport3D,
                  Batch batch)
    {
        this(name, graphicsSettings, viewport2D, viewport3D, batch,
                ColorUtilities.createColor(graphicsSettings.clearColor));
    }

    public Screen(String name, GraphicsSettings graphicsSettings,
                  Viewport viewport2D, Viewport viewport3D,
                  Batch batch, Color clearColor)
    {
        this.name = name;
        this.graphicsSettings = graphicsSettings;
        this.viewport2D = viewport2D;
        this.viewport3D = viewport3D;
        this.batch = batch;
        this.clearColor = clearColor;
        this.screenManager = null;
    }

    public final String getName()
    {
        return name;
    }

    public final void setScreenManager(ScreenManager screenManager)
    {
        this.screenManager = screenManager;
    }

    /**
     * Aquí las clases derivadas implementan su propia lógica de inicialización
     */
    public void initialize()
    {

    }

    /**
     * Llamado al comenzar la transición desde esta pantalla
     *
     * @param to pantalla a la que se realizará la transición
     */
    public void onTransitionToStart(Screen to)
    {

    }

    /**
     * LLamado al comenzar la transición hacia esta pantalla
     *
     * @param from pantalla que comenzará la transición
     */
    public void onTransitionFromStart(Screen from)
    {

    }

    /**
     * Llamado en el momento en que esta pantalla deja de ser la pantalla actual
     * durante una transición hacia otra pantalla
     *
     * @param to pantalla a la que se realizó la transición
     */
    public void onLeave(Screen to)
    {

    }

    /**
     * Llamado en el momento en que esta pantalla comienza a ser la pantalla actual
     * durante una transición desde otra pantalla
     *
     * @param from pantalla desde donde se comenzó la transición
     */
    public void onEnter(Screen from)
    {

    }

    /**
     * Llamado al finalizar completamente la transición desde esta pantalla
     *
     * @param to pantalla a la que se realizó la transición
     */
    public void onTransitionToFinish(Screen to)
    {

    }

    /**
     * LLamado al finalizar completamente la transición hacia esta pantalla
     *
     * @param from pantalla desde donde se comenzó la transición
     */
    public void onTransitionFromFinish(Screen from)
    {

    }

    /**
     * Aquí las clases derivadas implementan su propia lógica de actualización
     *
     * @param gameTime gameTime
     */
    public void update(GameTime gameTime)
    {

    }

    /**
     * Aquí las clases derivadas realizan el dibujado, cada pantalla es
     * responsable de llamar a SpriteBatch.begin() y SpriteBatch.end()
     *
     * @param gameTime gameTime
     */
    public void draw(GameTime gameTime)
    {

    }

    /**
     * Llamado cada vez que el tamaño de la pantalla del juego cambia y el
     * juego no se encuentra en el estado de pausa
     *
     * @param width nuevo ancho de la pantalla en pixeles
     * @param height nueva altura de la pantalla en pixeles
     */
    public void resize(int width, int height)
    {

    }

    /**
     * Llamado en Android cuando se presiona el botón Home o cuando entra una
     * llamada telefónica.
     * En Escritorio es llamado justo antes de dispose() cuando se está saliendo
     * de la aplicación.
     */
    public void pause()
    {

    }

    /**
     * Llamado sólamente en Android cuando la aplicación regresa del estado de
     * pausa
     */
    public void resume()
    {

    }

    /**
     * Aquí las clases derivadas se encargan de realizar la liberación de
     * memoria de sus elementos
     */
    public void dispose()
    {

    }
}
