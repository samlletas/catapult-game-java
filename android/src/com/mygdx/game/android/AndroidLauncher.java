package com.mygdx.game.android;

import android.os.Bundle;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.Game;

public class AndroidLauncher extends AndroidApplication
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        // Deshabilitación para ahorro de batería
        config.useAccelerometer = false;
        config.useCompass = false;
        config.useImmersiveMode = true;

        initialize(new Game(), config);
    }

    @Override
    public void initialize(ApplicationListener listener, AndroidApplicationConfiguration config)
    {
        super.initialize(listener, config);
    }
}
