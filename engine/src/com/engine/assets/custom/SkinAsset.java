package com.engine.assets.custom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.engine.assets.IAsset;

public class SkinAsset implements IAsset<Skin>
{
    private final String skinName;
    private final String textureAtlasName;

    private Skin skin;

    public SkinAsset(String skinName, String textureAtlasName)
    {
        this.skinName = skinName;
        this.textureAtlasName = textureAtlasName;
    }

    @Override
    public Skin getInstance()
    {
        return skin;
    }

    @Override
    public void load(AssetManager manager)
    {
        // No se realiza carga debido a que el skin depende de un TextureAtlas
    }

    @Override
    public void retrieveInstance(AssetManager manager)
    {
        TextureAtlas textureAtlas =  manager.get(textureAtlasName, TextureAtlas.class);
        skin = new Skin(Gdx.files.internal(skinName), textureAtlas);
    }
}
