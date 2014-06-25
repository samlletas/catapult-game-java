package com.engine.graphics.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.engine.GameTime;

public class AnimationPlayer
{
    private Array<Bone> bones;
    private Bone root;

    private Array<Animation> animations;
    private Animation currentAnimation;

    public AnimationPlayer()
    {
        bones = new Array<Bone>();
        root = null;

        animations = new Array<Animation>();
        currentAnimation = null;
    }

    public void play(String name)
    {
        for (Animation animation : animations)
        {
            if (animation.name.equals(name))
            {
                currentAnimation = animation;
                currentAnimation.start();
                return;
            }
        }

        throw new GdxRuntimeException("The animation: \"" + name + "\" does not exists");
    }

    public void pause()
    {

    }

    public void update(GameTime gameTime)
    {
        if (currentAnimation != null)
        {
            currentAnimation.update(gameTime, root);
        }
    }

    public void draw(SpriteBatch spriteBatch)
    {
        if (currentAnimation != null)
        {
            for (Bone bone : bones)
            {
                bone.draw(spriteBatch);
            }
        }
    }

    public void addBone(Bone bone)
    {
        if (bone.isRoot())
        {
            root = bone;
        }

        bones.add(bone);
    }

    public void addAnimation(Animation animation)
    {
        animations.add(animation);
    }

    public Animation getAnimation(String name)
    {
        for(Animation animation : animations)
        {
            if (animation.name.equals(name))
            {
                return animation;
            }
        }

        throw new GdxRuntimeException("The animation: \"" + name + "\" does not exists");
    }
}
