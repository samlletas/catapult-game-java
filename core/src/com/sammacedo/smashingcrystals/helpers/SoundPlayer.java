package com.sammacedo.smashingcrystals.helpers;

import com.badlogic.gdx.audio.Sound;
import com.sammacedo.smashingcrystals.Settings;
import com.sammacedo.smashingcrystals.assets.GameAssets;

public class SoundPlayer
{
    private Settings settings;

    public Sound crystalBreak;
    public Sound shoot;
    public Sound point;
    public Sound damage;
    public Sound count;
    public Sound success;

    public SoundPlayer(Settings settings, GameAssets assets)
    {
        this.settings = settings;

        crystalBreak = assets.sounds.crystalBreak.getInstance();
        shoot = assets.sounds.shoot.getInstance();
        point = assets.sounds.point.getInstance();
        damage = assets.sounds.damage.getInstance();
        count = assets.sounds.count.getInstance();
        success = assets.sounds.success.getInstance();
    }

    public float getVolume()
    {
        return settings.getSoundsVolume() / 100f;
    }

    public void play(Sound sound)
    {
        sound.play(getVolume());
    }
}
