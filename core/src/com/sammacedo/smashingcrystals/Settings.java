package com.sammacedo.smashingcrystals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings
{
    private final static String PREFERENCES_NAME = "game settings";

    private final static String KEY_MUSIC_VOLUME   = "music volume";
    private final static String KEY_SOUNDS_VOLUME  = "sounds volume";
    private final static String KEY_TIME_ATTACK    = "time attack";
    private final static String KEY_CRYSTAL_FRENZY = "crystal frenzy";

    private final static float DEFAULT_MUSIC_VOLUME  = 80f;
    private final static float DEFAULT_SOUNDS_VOLUME = 70f;

    private float musicVolume;
    private float soundsVolume;
    private int localTimeAttackScore;
    private int localCrystalFrenzyScore;

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

    public int getLocalTimeAttackScore()
    {
        return localTimeAttackScore;
    }

    public void setLocalTimeAttackScore(int score)
    {
        if (localTimeAttackScore != score)
        {
            localTimeAttackScore = score;
            upToDate = false;
        }
    }

    public int getLocalCrystalFrenzyScore()
    {
        return localCrystalFrenzyScore;
    }

    public void setLocalCrystalFrenzyScore(int score)
    {
        if (localCrystalFrenzyScore != score)
        {
            localCrystalFrenzyScore = score;
            upToDate = false;
        }
    }

    public void load()
    {
        if (Global.DEBUG_CLEAR_SETTINGS)
        {
            preferences.clear();
            preferences.flush();
        }

        musicVolume = preferences.getFloat(KEY_MUSIC_VOLUME, DEFAULT_MUSIC_VOLUME);
        soundsVolume = preferences.getFloat(KEY_SOUNDS_VOLUME, DEFAULT_SOUNDS_VOLUME);
        localTimeAttackScore = preferences.getInteger(KEY_TIME_ATTACK, 0);
        localCrystalFrenzyScore = preferences.getInteger(KEY_CRYSTAL_FRENZY, 0);

        upToDate = true;
    }

    public void save()
    {
        if (!upToDate)
        {
            preferences.putFloat(KEY_MUSIC_VOLUME, musicVolume);
            preferences.putFloat(KEY_SOUNDS_VOLUME, soundsVolume);
            preferences.putInteger(KEY_TIME_ATTACK, localTimeAttackScore);
            preferences.putInteger(KEY_CRYSTAL_FRENZY, localCrystalFrenzyScore);

            preferences.flush();
        }
    }
}
