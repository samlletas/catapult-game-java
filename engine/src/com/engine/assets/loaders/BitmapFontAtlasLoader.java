package com.engine.assets.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class BitmapFontAtlasLoader extends AsynchronousAssetLoader<BitmapFont, BitmapFontLoader.BitmapFontParameter>
{
    public BitmapFontAtlasLoader (FileHandleResolver resolver)
    {
        super(resolver);
    }

    BitmapFont.BitmapFontData data;

    @Override
    public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file,
                                                   BitmapFontLoader.BitmapFontParameter parameter)
    {
        Array<AssetDescriptor> deps = new Array();

        if (parameter != null && parameter.bitmapFontData != null)
        {
            data = parameter.bitmapFontData;
            return deps;
        }

        data = new BitmapFont.BitmapFontData(file, parameter != null ? parameter.flip : false);

        if (parameter != null && parameter.atlasName != null)
        {
            deps.add(new AssetDescriptor(parameter.atlasName, TextureAtlas.class));
        }
        else
        {
            for (int i = 0; i < data.getImagePaths().length; i++)
            {
                String path = data.getImagePath(i);
                FileHandle resolved = resolve(path);

                TextureLoader.TextureParameter textureParams = new TextureLoader.TextureParameter();

                if (parameter != null)
                {
                    textureParams.genMipMaps = parameter.genMipMaps;
                    textureParams.minFilter = parameter.minFilter;
                    textureParams.magFilter = parameter.magFilter;
                }

                AssetDescriptor descriptor = new AssetDescriptor(resolved, Texture.class, textureParams);
                deps.add(descriptor);
            }
        }

        return deps;
    }

    @Override
    public void loadAsync (AssetManager manager, String fileName, FileHandle file, BitmapFontLoader.BitmapFontParameter parameter)
    {
    }

    @Override
    public BitmapFont loadSync (AssetManager manager, String fileName, FileHandle file, BitmapFontLoader.BitmapFontParameter parameter)
    {
        if (parameter != null && parameter.atlasName != null)
        {
            TextureAtlas atlas = manager.get(parameter.atlasName, TextureAtlas.class);
            String name = file.sibling(data.imagePaths[0]).nameWithoutExtension().toString();
            TextureAtlas.AtlasRegion region = atlas.findRegion(name);

            if (parameter.flip && region.isFlipY())
            {
                region.flip(false, true);
            }

            if (region == null) throw new GdxRuntimeException("Could not find font region " + name + " in atlas " + parameter.atlasName);
            return new BitmapFont(file, region, parameter.flip);
        }
        else
        {
            TextureRegion[] regs = new TextureRegion[data.getImagePaths().length];

            for (int i = 0; i < regs.length; i++)
            {
                regs[i] = new TextureRegion(manager.get(data.getImagePath(i), Texture.class));
            }

            return new BitmapFont(data, regs, true);
        }
    }
}
