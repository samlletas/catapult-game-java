package com.mygdx.game.helpers;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.Settings;
import com.mygdx.game.assets.GameAssets;

public class SoundPlayer
{
    private Settings settings;

    private Sound crystalBreak;
    private Sound shoot;
    private Sound point;

    public SoundPlayer(Settings settings, GameAssets assets)
    {
        this.settings = settings;

        crystalBreak = assets.sounds.crystalBreak.getInstance();
        shoot = assets.sounds.shoot.getInstance();
        point = assets.sounds.point.getInstance();
    }

    public float getVolume()
    {
        return settings.getSoundsVolume() / 100f;
    }

    public void playBreak()
    {
        crystalBreak.play(getVolume());
    }

    public void playShoot()
    {
        shoot.play(getVolume());
    }

    public void playPoint()
    {
        point.play(getVolume());
    }
}
