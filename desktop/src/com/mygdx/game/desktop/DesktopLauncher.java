package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Game;
import org.lwjgl.util.Display;

public class DesktopLauncher
{
    public static void main(String[] arg)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 854;
        config.height = 480;

        int targetFPS = 60;

        config.vSyncEnabled = false;
        config.foregroundFPS = targetFPS;
        config.backgroundFPS = targetFPS;

        new LwjglApplication(new Game(), config);
    }
}
