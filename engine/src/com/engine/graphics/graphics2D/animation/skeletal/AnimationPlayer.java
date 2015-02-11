package com.engine.graphics.graphics2D.animation.skeletal;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.engine.GameTime;
import java.io.IOException;

public class AnimationPlayer
{
    private Array<Bone> bones;
    private Bone root;

    private Array<Animation> animations;
    private Animation currentAnimation;

    public float speed;
    public Vector2 position;
    public float rotation;
    public float scale;

    public AnimationPlayer()
    {
        bones = new Array<Bone>();
        root = null;

        animations = new Array<Animation>();
        currentAnimation = null;

        speed = 1f;
        position = new Vector2(0f, 0f);
        rotation = 0f;
        scale = 1f;
    }

    public void play(String name)
    {
        play(name, 0f);
    }

    public void play(String name, float frameOffset)
    {
        Array<Animation> localAnimations = animations;
        Animation animation;

        for (int i = 0, n = localAnimations.size; i < n; i++)
        {
            animation = localAnimations.get(i);

            if (animation.name.equals(name))
            {
                if (animation != currentAnimation)
                {
                    currentAnimation = animation;
                    currentAnimation.start(frameOffset);
                }

                return;
            }
        }

        throw new GdxRuntimeException("The animation: \"" + name + "\" does not exists");
    }

    /**
     * Pone en pausa la animación actual
     */
    public void pause()
    {
        if (currentAnimation != null)
        {
            currentAnimation.pause();
        }
    }

    /**
     * Continua con la ejecución la animación actual
     */
    public void resume()
    {
        if (currentAnimation != null)
        {
            currentAnimation.resume();
        }
    }

    /**
     * Reinicia la ejecución la animación actual desde el primer frame
     */
    public void reset()
    {
        if (currentAnimation != null)
        {
            currentAnimation.start(0);
        }
    }

    public void update(GameTime gameTime)
    {
        if (currentAnimation != null)
        {
            root.offsetX = position.x;
            root.offsetY = position.y;

            currentAnimation.update(gameTime, root, speed, rotation, scale);
        }
    }

    public void draw(SpriteBatch spriteBatch)
    {
        if (currentAnimation != null)
        {
            Array<Bone> localBones = bones;

            for (int i = 0, n = localBones.size; i < n; i++)
            {
                localBones.get(i).draw(spriteBatch);
            }
        }
    }

    public void addBone(Bone bone)
    {
        bones.add(bone);
    }

    public void addAnimation(Animation animation)
    {
        animations.add(animation);
    }

    public Animation getAnimation(String name)
    {
        Array<Animation> localAnimations = animations;
        Animation animation;

        for (int i = 0, n = localAnimations.size; i < n; i++)
        {
            animation = localAnimations.get(i);

            if (animation.name.equals(name))
            {
                return animation;
            }
        }

        throw new GdxRuntimeException("The animation: \"" + name + "\" does not exists");
    }

    public Bone getBone(int id)
    {
        Array<Bone> localBones = bones;
        Bone bone;

        for (int i = 0, n = localBones.size; i < n; i++)
        {
            bone = localBones.get(i);

            if (bone.id == id)
            {
                return bone;
            }
        }

        return null;
    }

    public void load(XmlReader reader, TextureAtlas atlas, FileHandle file)
    {
        try
        {
            XmlReader.Element root = reader.parse(file);

            // Bones
            Array<XmlReader.Element> boneElements = root.getChildByName("bones").getChildrenByName("bone");
            XmlReader.Element b;

            for (int i = 0, n = boneElements.size; i < n; i++)
            {
                b = boneElements.get(i);

                Bone newBone = new Bone(
                        atlas.findRegion(b.get("region")),
                        b.getInt("id"),
                        b.getInt("parent"),
                        b.getFloat("pivotX"),
                        b.getFloat("pivotY"),
                        b.getFloat("offsetX"),
                        b.getFloat("offsetY"));

                addBone(newBone);
            }

            initializeBones();

            // Animaciones
            Array<XmlReader.Element> animationElements = root.getChildByName("animations").getChildrenByName("animation");
            XmlReader.Element a;
            Array<XmlReader.Element> frameElements;
            XmlReader.Element f;
            Array<XmlReader.Element> frameDataElements;
            XmlReader.Element fd;
            boolean loop;

            for (int i = 0, n = animationElements.size; i < n; i++)
            {
                a = animationElements.get(i);

                if (a.get("loop").equals("true"))
                {
                    loop = true;
                }
                else
                {
                    loop = false;
                }

                Animation newAnimation = new Animation(
                        a.get("name"),
                        loop);

                // Frames
                frameElements = a.getChildrenByName("frame");

                for (int j = 0, jn = frameElements.size; j < jn; j++)
                {
                    f = frameElements.get(j);

                    Frame newFrame = new Frame(f.getInt("duration"));

                    // FramesData
                    frameDataElements = f.getChildrenByName("framedata");

                    for (int k = 0, kn = frameDataElements.size; k < kn; k++)
                    {
                        fd = frameDataElements.get(k);

                        FrameData newFrameData = new FrameData(
                                fd.getInt("boneID"),
                                fd.getFloat("rotation"),
                                fd.getFloat("scaleX"),
                                fd.getFloat("scaleY"),
                                fd.getFloat("offsetDirection"),
                                fd.getFloat("offsetDistance"),
                                getInterpolation(fd.get("interpolation"))
                        );

                        newFrame.addFrameData(newFrameData);
                    }

                    newAnimation.addFrame(newFrame);
                }

                addAnimation(newAnimation);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private Interpolation getInterpolation(String interpolation)
    {
        if (interpolation.equals("linear"))
        {
            return Interpolation.linear;
        }
        else if (interpolation.equals("sine"))
        {
            return Interpolation.sine;
        }
        else
        {
            throw new GdxRuntimeException("Invalid value for interpolation attribute: " + interpolation);
        }
    }

    private void initializeBones()
    {
        Bone bone;
        Bone parent;

        for (int i = 0; i < bones.size; i++)
        {
            bone = bones.get(i);
            parent = getBone(bone.parentId);

            if (parent != null)
            {
                parent.addChild(bone);
            }
            else
            {
                root = bone;

                position.x = root.offsetX;
                position.y = root.offsetY;
            }
        }
    }

    public AnimationPlayer copy()
    {
        AnimationPlayer copy = new AnimationPlayer();
        Array<Bone> localBones = bones;
        Array<Animation> localAnimations = animations;

        for (int i = 0, n = localBones.size; i < n; i++)
        {
            copy.addBone(localBones.get(i).copy());
        }

        copy.initializeBones();

        for (int i = 0, n = localAnimations.size; i < n; i++)
        {
            copy.addAnimation(localAnimations.get(i).copy());
        }

        return copy;
    }
}
