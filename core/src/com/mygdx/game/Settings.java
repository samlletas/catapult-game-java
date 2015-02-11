package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings
{
    private final static String PREFERENCES_NAME = "game settings";

    private final static String KEY_MUSIC_VOLUME  = "music volume";
    private final static String KEY_SOUNDS_VOLUME = "sounds volume";
    private final static String KEY_LOCAL_SCORE   = "local score";

    private final static float DEFAULT_MUSIC_VOLUME  = 80f;
    private final static float DEFAULT_SOUNDS_VOLUME = 70f;
    private final static int   DEFAULT_LOCAL_SCORE   = 0;

    private float musicVolume;
    private float soundsVolume;
    private int localScore;

    private Preferences preferences;
    private boolean upToDate;

    public Settings()
    {
        preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
        upToDate = false;
    }

    public float getMusicVolume()
    {
        return musicVolume;
    }

    public void setMusicVolume(float musicVolume)
    {
        if (this.musicVolume != musicVolume)
        {
            this.musicVolume = musicVolume;
            upToDate = false;
        }
    }

    public float getSoundsVolume()
    {
        return soundsVolume;
    }

    public void setSoundsVolume(float soundsVolume)
    {
        if (this.soundsVolume != soundsVolume)
        {
            this.soundsVolume = soundsVolume;
            upToDate = false;
        }
    }

    public int getLocalScore()
    {
        return localScore;
    }

    public void setLocalScore(int localScore)
    {
        if (this.localScore != localScore)
        {
            this.localScore = localScore;
            upToDate = false;
        }
    }

    public void load()
    {
        musicVolume = preferences.getFloat(KEY_MUSIC_VOLUME, DEFAULT_MUSIC_VOLUME);
        soundsVolume = preferences.getFloat(KEY_SOUNDS_VOLUME, DEFAULT_SOUNDS_VOLUME);
        localScore = preferences.getInteger(KEY_LOCAL_SCORE, DEFAULT_LOCAL_SCORE);

        upToDate = true;
    }

    public void save()
    {
        if (!upToDate)
        {
            preferences.putFloat(KEY_MUSIC_VOLUME, musicVolume);
            preferences.putFloat(KEY_SOUNDS_VOLUME, soundsVolume);
            preferences.putInteger(KEY_LOCAL_SCORE, localScore);

            preferences.flush();
        }
    }
}
