package com.mygdx.game.assets;

import com.engine.assets.Asset;
import com.engine.graphics.animation.AnimationPlayer;

public final class GameAssets
{
    public final static class Animations
    {
        public static Asset<AnimationPlayer> catapult =
                new Asset<AnimationPlayer>("animations/catapult.anim", AnimationPlayer.class);
    }
}
