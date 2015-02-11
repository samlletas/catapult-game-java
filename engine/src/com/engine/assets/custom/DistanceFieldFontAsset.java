package com.engine.assets.custom;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.engine.assets.IAsset;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;

public class DistanceFieldFontAsset implements IAsset<DistanceFieldFont>
{
    private DistanceFieldFont distanceFieldFont;

    private String bitmapFontName;
    private float spread;
    private float thickness;
    private float sharpness;

    public DistanceFieldFontAsset(String bitmapFontNamet, float spread)
    {
        this(bitmapFontNamet, spread, 0.5f);
    }

    public DistanceFieldFontAsset(String bitmapFontName, float spread, float thickness)
    {
        this(bitmapFontName, spread, thickness, 0.25f);
    }

    public DistanceFieldFontAsset(String bitmapFontName, float spread, float thickness, float sharpness)
    {
        this.bitmapFontName = bitmapFontName;
        this.spread = spread;
        this.thickness = thickness;
        this.sharpness = sharpness;
    }

    @Override
    public DistanceFieldFont getInstance()
    {
        return distanceFieldFont;
    }

    @Override
    public void load(AssetManager manager)
    {
        // No se realiza carga debido a que la fuente depende de un BitmapFont
    }

    @Override
    public void retrieveInstance(AssetManager manager)
    {
        BitmapFont bitmapFont = manager.get(bitmapFontName, BitmapFont.class);
        distanceFieldFont = new DistanceFieldFont(bitmapFont, spread, thickness, sharpness);
    }
}
