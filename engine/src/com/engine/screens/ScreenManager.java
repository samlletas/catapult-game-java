package com.engine.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.engine.graphics.GraphicsSettings;
import com.engine.GameTime;
import com.engine.screens.transitions.InmediateTransition;
import com.engine.screens.transitions.Transition;

public final class ScreenManager implements Disposable
{
    private Array<Screen> screens;
    private GraphicsSettings graphicsSettings;
    private Transition defaultTransition;

    private Screen previousScreen;
    private Screen currentScreen;
    private Screen nextScreen;

    private Transition currentTransition;
    private boolean onTransition;
    private boolean swappedScreens;

    private Color tempColor;

    public ScreenManager(GraphicsSettings graphicsSettings)
    {
        this(graphicsSettings, new InmediateTransition());
    }

    public ScreenManager(GraphicsSettings graphicsSettings, Transition defaultTransition)
    {
        this.screens = new Array<Screen>(8);
        this.graphicsSettings = graphicsSettings;
        this.defaultTransition = defaultTransition;

        this.previousScreen = null;
        this.currentScreen = null;
        this.nextScreen = null;

        this.currentTransition = null;
        this.onTransition = false;
        this.swappedScreens = false;

        this.tempColor = new Color();
    }

    public void initialize()
    {
        Array<Screen> localScreens = this.screens;

        for (int i = 0, n = localScreens.size; i < n; i++)
        {
            localScreens.get(i).initialize();
        }
    }

    public void addScreen(Screen screen)
    {
        Array<Screen> localScreens = this.screens;
        boolean repeated = false;

        for (int i = 0, n = localScreens.size; i < n; i++)
        {
            if (localScreens.get(i).name.equals(screen.name))
            {
                repeated = true;
                break;
            }
        }

        if (!repeated)
        {
            screen.setScreenManager(this);
            screens.add(screen);
        }
        else
        {
            throw new GdxRuntimeException("Screen with name '" + screen.name + "' already exists");
        }
    }

    public Screen getScreen(String name)
    {
        Array<Screen> localScreens = this.screens;
        Screen screen;

        for (int i = 0, n = localScreens.size; i < n; i++)
        {
            screen = localScreens.get(i);

            if (screen.name.equals(name))
            {
                return screen;
            }
        }

        return null;
    }

    public boolean isOnTransition()
    {
        return onTransition;
    }

    public Transition getCurrentTransition()
    {
        return currentTransition;
    }

    public void transitionTo(String name)
    {
        transitionTo(name, defaultTransition);
    }

    public void transitionTo(String name, Transition transition)
    {
        if (!onTransition)
        {
            Screen screen = getScreen(name);

            if (screen != null)
            {
                transitionTo(screen, transition);
            }
            else
            {
                throw new GdxRuntimeException("Screen with name '" + name + "' was not found");
            }
        }
    }

    public void transitionTo(Screen screen)
    {
        transitionTo(screen, defaultTransition);
    }

    public void transitionTo(Screen screen, Transition transition)
    {
        if (!onTransition)
        {
            nextScreen = screen;

            if (currentScreen != null)
            {
                currentScreen.onTransitionToStart(nextScreen);
            }

            nextScreen.onTransitionFromStart(currentScreen);

            currentTransition = transition;
            currentTransition.start();

            previousScreen = currentScreen;
            onTransition = true;
            swappedScreens = false;
        }
    }

    public void update(GameTime gameTime)
    {
        if (onTransition)
        {
            currentTransition.update(gameTime);

            if (!swappedScreens && currentTransition.canRenderNextScreen())
            {
                if (previousScreen != null)
                {
                    previousScreen.onLeave(nextScreen);
                }

                nextScreen.onEnter(previousScreen);
                currentScreen = nextScreen;

                swappedScreens = true;
            }

            if (!currentTransition.isActive())
            {
                if (previousScreen != null)
                {
                    previousScreen.onTransitionToFinish(nextScreen);
                }

                nextScreen.onTransitionFromFinish(previousScreen);
                currentTransition = null;
                onTransition = false;
            }
        }

        if (currentScreen != null)
        {
            currentScreen.update(gameTime);

            tempColor.set(graphicsSettings.clearColor);
            graphicsSettings.clearColor.set(currentScreen.clearColor);
        }
    }

    public void draw(GameTime gameTime)
    {
        if (currentScreen != null)
        {
            currentScreen.draw(gameTime);
            graphicsSettings.clearColor.set(tempColor);
        }

        if (onTransition && currentTransition.isActive())
        {
            currentTransition.draw(gameTime);
        }
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
        if (currentScreen != null)
        {
            currentScreen.resize(width, height);
        }
    }

    /**
     * Llamado en Android cuando se presiona el botón Home o cuando entra una
     * llamada telefónica.
     * En Escritorio es llamado justo antes de dispose() cuando se está saliendo
     * de la aplicación.
     */
    public void pause()
    {
        if (currentScreen != null)
        {
            currentScreen.pause();
        }
    }

    /**
     * Llamado sólamente en Android cuando la aplicación regresa del estado de
     * pausa
     */
    public void resume()
    {
        if (currentScreen != null)
        {
            currentScreen.resume();
        }
    }

    @Override
    public void dispose()
    {
        Array<Screen> localScreens = this.screens;

        for (int i = 0, n = localScreens.size; i < n; i++)
        {
            localScreens.get(i).dispose();
        }

        defaultTransition.dispose();
    }
}
