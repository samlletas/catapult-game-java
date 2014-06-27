package com.engine.graphics.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.engine.GameTime;

public class AnimationPlayer
{
    private Array<Bone> bones;
    private Bone root;

    private Array<Animation> animations;
    private Animation currentAnimation;

    public Vector2 position;
    public float rotation;
    public float scale;

    public AnimationPlayer()
    {
        bones = new Array<Bone>();
        root = null;

        animations = new Array<Animation>();
        currentAnimation = null;

        position = new Vector2(0f, 0f);
        rotation = 0f;
        scale = 1f;
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
            root.offsetX = position.x;
            root.offsetY = position.y;

            currentAnimation.update(gameTime, root, rotation, scale);
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

            position.x = root.offsetX;
            position.y = root.offsetY;
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
