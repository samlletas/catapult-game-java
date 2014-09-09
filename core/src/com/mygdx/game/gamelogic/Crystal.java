package com.mygdx.game.gamelogic;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.engine.GameTime;
import com.mygdx.game.assets.GameAssets;

public class Crystal
{
    private ModelInstance instance;

    public Crystal()
    {
        instance = new ModelInstance(GameAssets.Models.crystal.instance);
        instance.transform.translate(500, 200, 100);
        instance.transform.scl(13f);
        instance.transform.rotate(1f, 0f, 0f, 15f);
    }

    public void update(GameTime gameTime)
    {
        // TODO: mult time delta
        instance.transform.rotate(0f, 1f, 0f, 1.2f);
    }

    public void draw(ModelBatch modelBatch, Camera camera)
    {
        modelBatch.begin(camera);
        modelBatch.render(instance);
        modelBatch.end();
    }
}