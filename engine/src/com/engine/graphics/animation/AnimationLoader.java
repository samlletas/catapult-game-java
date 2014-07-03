package com.engine.graphics.animation;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.engine.Interpolation.Interpolators;

import java.io.IOException;

public class AnimationLoader extends AsynchronousAssetLoader<AnimationPlayer,
        AnimationLoader.AnimationLoaderParameter>
{
    private XmlReader reader;

    public AnimationLoader(FileHandleResolver resolver)
    {
        super(resolver);

        reader = new XmlReader();
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName,
                          FileHandle file, AnimationLoaderParameter parameter)
    {
    }

    @Override
    public AnimationPlayer loadSync(AssetManager manager, String fileName,
                                    FileHandle file, AnimationLoaderParameter parameter)
    {
        AnimationPlayer player = new AnimationPlayer();
        player.load(reader, manager, file);

        return player;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file,
                                                  AnimationLoaderParameter parameter)
    {
        Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();

        try
        {
            XmlReader.Element root = reader.parse(file);
            String atlasFile = file.parent().name() + "/" + root.get("atlasFile");

            TextureAtlasLoader.TextureAtlasParameter textureAtlasParameter =
                    new TextureAtlasLoader.TextureAtlasParameter(true);

            dependencies.add(new AssetDescriptor<TextureAtlas>(atlasFile,
                    TextureAtlas.class, textureAtlasParameter));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return dependencies;
    }

    public static class AnimationLoaderParameter extends AssetLoaderParameters<AnimationPlayer>
    {
    }
}
