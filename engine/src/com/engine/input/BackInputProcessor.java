package com.engine.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public abstract class BackInputProcessor extends InputAdapter
{
    @Override
    public final boolean keyDown(int keycode)
    {
        if (keycode == Input.Keys.BACK)
        {
            onBackDown();
        }

        return false;
    }

    @Override
    public final boolean scrolled(int amount)
    {
        return super.scrolled(amount);
    }

    @Override
    public final boolean mouseMoved(int screenX, int screenY)
    {
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public final boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public final boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public final boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public final boolean keyTyped(char character)
    {
        return super.keyTyped(character);
    }

    @Override
    public final boolean keyUp(int keycode)
    {
        return super.keyUp(keycode);
    }

    protected abstract void onBackDown();
}
