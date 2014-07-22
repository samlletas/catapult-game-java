package com.engine.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.TimeUtils;

public class GameProfiler
{
    public float x;
    public float y;

    private boolean profileFPS;
    private boolean profileOpenGL;

    private BitmapFont font;

    private int fps;
    private float totalVertexCount;

    private long fpsStartTime;
    private long vertexCountStartTime;

    private static final int FPS_DISPLAY_DELAY = 1000;
    private static final int VERTEXCOUNT_DISPLAY_DELAY = 500;

    public GameProfiler(boolean profileFPS, boolean profileOpenGL)
    {
        x = 10f;
        y = 10f;

        this.profileFPS = profileFPS;
        this.profileOpenGL = profileOpenGL;

        font = new BitmapFont(true);

        fps = 0;
        totalVertexCount = 0;

        fpsStartTime = 0;
        vertexCountStartTime = 0;

        GLProfiler.enable();
    }

    public void profile(SpriteBatch spriteBatch)
    {
        spriteBatch.begin();

        if (profileFPS)
        {
            if ((TimeUtils.millis() - fpsStartTime) > FPS_DISPLAY_DELAY)
            {
                fps = Gdx.graphics.getFramesPerSecond();
                fpsStartTime = TimeUtils.millis();
            }

            font.draw(spriteBatch, "FPS: " + fps, x, y);
        }

        if (profileOpenGL)
        {
            if ((TimeUtils.millis() - vertexCountStartTime) > VERTEXCOUNT_DISPLAY_DELAY)
            {
                totalVertexCount = GLProfiler.vertexCount.total;
                vertexCountStartTime = TimeUtils.millis();
            }

            font.draw(spriteBatch, "Gl Calls: " + GLProfiler.calls, x, y + 15);
            font.draw(spriteBatch, "Draw Calls: " + GLProfiler.drawCalls, x, y + 30);
            font.draw(spriteBatch, "Texture Binds: " + GLProfiler.textureBindings, x, y + 45);
            font.draw(spriteBatch, "Shader Switches: " + GLProfiler.shaderSwitches, x, y + 60);
            font.draw(spriteBatch, "Vertex Count: " + totalVertexCount, x, y + 75);
        }

        spriteBatch.end();
        GLProfiler.reset();
    }
}
