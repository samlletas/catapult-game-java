package com.engine.graphics.animation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.engine.GameTime;
import com.engine.Interpolation.IInterpolator;
import com.engine.Interpolation.Interpolators;
import org.xml.sax.XMLReader;

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
        for (Animation animation : animations)
        {
            if (animation.name.equals(name))
            {
                if (animation != currentAnimation)
                {
                    currentAnimation = animation;
                    currentAnimation.start();
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
            currentAnimation.start();
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
            for (Bone bone : bones)
            {
                bone.draw(spriteBatch);
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
        for(Animation animation : animations)
        {
            if (animation.name.equals(name))
            {
                return animation;
            }
        }

        throw new GdxRuntimeException("The animation: \"" + name + "\" does not exists");
    }

    public Bone getBone(int id)
    {
        for (Bone bone : bones)
        {
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

            for (XmlReader.Element b : boneElements)
            {
                Bone newBone = new Bone(
                        atlas.findRegion(b.get("region")),
                        b.getInt("id"),
                        b.getFloat("pivotX"),
                        b.getFloat("pivotY"),
                        b.getFloat("offsetX"),
                        b.getFloat("offsetY"));

                addBone(newBone);
            }

            initializeBones(boneElements);

            // Animaciones
            Array<XmlReader.Element> animationElements = root.getChildByName("animations").getChildrenByName("animation");
            Array<XmlReader.Element> frameElements;
            Array<XmlReader.Element> frameDataElements;
            boolean loop;

            for (XmlReader.Element a : animationElements)
            {
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

                for (XmlReader.Element f : frameElements)
                {
                    Frame newFrame = new Frame(f.getInt("duration"));

                    // FramesData
                    frameDataElements = f.getChildrenByName("framedata");

                    for (XmlReader.Element fd : frameDataElements)
                    {
                        String interpolatorValue = fd.get("interpolator");
                        IInterpolator interpolator;

                        if (interpolatorValue.equals("linear"))
                        {
                            interpolator = Interpolators.LinearInterpolator;
                        }
                        else if (interpolatorValue.equals("cosine"))
                        {
                            interpolator = Interpolators.CosineInterpolator;
                        }
                        else
                        {
                            throw new GdxRuntimeException("Invalid value for interpolator attribute: " +
                                    interpolatorValue);
                        }

                        FrameData newFrameData = new FrameData(
                                fd.getInt("boneID"),
                                fd.getFloat("rotation"),
                                fd.getFloat("scaleX"),
                                fd.getFloat("scaleY"),
                                fd.getFloat("offsetDirection"),
                                fd.getFloat("offsetDistance"),
                                interpolator
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

    private void initializeBones(Array<XmlReader.Element> boneElements)
    {
        for (int i = 0; i < bones.size; i++)
        {
            Bone bone = bones.get(i);
            int parentID = boneElements.get(i).getInt("parent");
            Bone parent = getBone(parentID);

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
}
