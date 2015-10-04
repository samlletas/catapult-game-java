package com.sammacedo.smashingcrystals.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sammacedo.smashingcrystals.Game;

public class DesktopLauncher
{
    public static void main(String[] arg)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 854;
        config.height = 480;

//        config.width = 480;
//        config.height = 320;
//
//        config.width = 320;
//        config.height = 240;

        int targetFPS = 60;

        config.vSyncEnabled = false;
        config.foregroundFPS = targetFPS;
        config.backgroundFPS = targetFPS;

        new LwjglApplication(new Game(), config);
    }
}
