package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.engine.utilities.ColorUtilities;

public final class Global
{
    public static final boolean DEBUG_FPS = true;
    public static final boolean DEBUG_OPENGL = false;
    public static final boolean DEBUG_MEMORY = false;
    public static final boolean DEBUG_POLYGONS = false;

    public static final float TIME_STEP = 1f / 60f;

    public static final float OVERLAY_ALPHA = 0.7f;
    public static final float OVERLAY_PAUSE_ALPHA = 0.8f;
    public static final Vector2 TEXT_SHADOW_OFFSET = new Vector2(-0.005f, -0.01f);

    public static final class Colors
    {
        public static final Color SPLASH_BACKGROUND = Color.WHITE;
        public static final Color MAIN_BACKGROUND = ColorUtilities.createColor(36, 82, 130, 255);
        public static final Color DEFAULT_TEXT = Color.WHITE;
        public static final Color OVERLAY = Color.BLACK;
        public static final Color NO_OVERLAY = ColorUtilities.createColor(0, 0, 0, 0);
        public static final Color TEXT_SHADOW = ColorUtilities.createColor(0, 0, 0, 100);
    }

    public static final class ScreenNames
    {
        public static final String POWERED_BY_SCREEN = "Powered By";
        public static final String MAIN_MENU_SCREEN  = "Main Menu";
        public static final String GAMEPLAY_SCREEN   = "Game";
        public static final String SETTINGS_SCREEN   = "Settings";
        public static final String INFO_SCREEN       = "Info";
    }

    public static final class ButtonStyles
    {
        public static final String PLAY     = "play";
        public static final String SETTINGS = "settings";
        public static final String SCORES   = "scores";
        public static final String HOME     = "home";
        public static final String QUESTION = "question";
        public static final String POWER    = "power";
        public static final String INFO     = "info";
        public static final String BACK     = "back";
        public static final String PAUSE    = "pause";
    }
}
