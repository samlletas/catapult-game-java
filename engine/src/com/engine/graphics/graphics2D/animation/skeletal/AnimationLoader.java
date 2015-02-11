package com.engine.graphics.graphics2D.animation.skeletal;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

import java.io.IOException;

public class AnimationLoader extends AsynchronousAssetLoader<AnimationPlayer,
        AnimationLoader.AnimationLoaderParameter>
{
    private XmlReader reader;
    private String atlasFile;

    public AnimationLoader(FileHandleResolver resolver)
    {
        super(resolver);

        reader = new XmlReader();
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file,
                                                  AnimationLoaderParameter parameter)
    {
        Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();

        try
        {
            XmlReader.Element root = reader.parse(file);

            if (parameter != null && parameter.atlasFolder != null)
            {
                atlasFile = parameter.atlasFolder + "/" + root.get("atlasFile");
            }
            else
            {
                atlasFile = file.parent().name() + "/" + root.get("atlasFile");
            }

            dependencies.add(new AssetDescriptor<TextureAtlas>(atlasFile,
                    TextureAtlas.class));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return dependencies;
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
        TextureAtlas atlas = manager.get(atlasFile, TextureAtlas.class);
        player.load(reader, atlas, file);

        return player;
    }

    public static class AnimationLoaderParameter extends AssetLoaderParameters<AnimationPlayer>
    {
        String atlasFolder;

        public AnimationLoaderParameter(String atlasFolder)
        {
            this.atlasFolder = atlasFolder;
        }
    }
}
