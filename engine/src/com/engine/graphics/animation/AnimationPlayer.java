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

    public void load(AssetManager manager, FileHandle file)
    {
        try
        {
            XmlReader reader = new XmlReader();
            XmlReader.Element root = reader.parse(file);

            // Bones
            Array<XmlReader.Element> bones = root.getChildByName("bones").getChildrenByName("bone");
            TextureAtlas atlas = manager.get(file.parent().name() + "/" + root.get("atlasFile"), TextureAtlas.class);

            for (XmlReader.Element b : bones)
            {
                Bone newBone = new Bone(
                        atlas,
                        b.getInt("id"),
                        b.get("name"),
                        b.getInt("parentID"),
                        b.getFloat("pivotX"),
                        b.getFloat("pivotY"),
                        b.getFloat("offsetX"),
                        b.getFloat("offsetY"));

                addBone(newBone);
            }

            initializeBones();

            // Animaciones
            Array<XmlReader.Element> animations = root.getChildByName("animations").getChildrenByName("animation");
            Array<XmlReader.Element> frames;
            Array<XmlReader.Element> framesData;
            boolean loop;

            for (XmlReader.Element a : animations)
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
                        a.getFloat("speed"),
                        loop);

                // Frames
                frames = a.getChildrenByName("frame");

                for (XmlReader.Element f : frames)
                {
                    Frame newFrame = new Frame(f.getInt("duration"));

                    // FramesData
                    framesData = f.getChildrenByName("framedata");

                    for (XmlReader.Element fd : framesData)
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

    private void initializeBones()
    {
        for(int i = 0; i < bones.size; i++)
        {
            Bone bone = bones.get(i);
            Bone parent = getBone(bone.parentID);

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
