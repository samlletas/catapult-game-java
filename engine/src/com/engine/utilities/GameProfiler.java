package com.engine.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.TimeUtils;
import com.engine.GameSpriteBatch;
import com.engine.text.IntegerSequence;
import com.engine.text.LabelSequence;

public class GameProfiler
{
    private static final int FPS_DISPLAY_DELAY = 1000;
    private static final int VERTEXCOUNT_DISPLAY_DELAY = 500;
    private static final float LINE_OFFSET = 15f;

    public boolean profileFPS = true;
    public boolean profileOpenGL = true;
    public boolean profileMemory = true;

    public float x;
    public float y;

    private BitmapFont font;
    private float drawY;

    private long fpsStartTime;
    private long vertexCountStartTime;

    private LabelSequence<IntegerSequence> fpsLabel;
    private LabelSequence<IntegerSequence> glCallsLabel;
    private LabelSequence<IntegerSequence> drawCallsLabel;
    private LabelSequence<IntegerSequence> textureBindsLabel;
    private LabelSequence<IntegerSequence> shaderSwitchesLabel;
    private LabelSequence<IntegerSequence> vertexCountLabel;
    private LabelSequence<IntegerSequence> maxSpritesLabel;
    private LabelSequence<IntegerSequence> javaHeapLabel;
    private LabelSequence<IntegerSequence> nativeHeapLabel;

    public GameProfiler()
    {
        this(10f, 10f);
    }

    public GameProfiler(float x, float y)
    {
        this.x = x;
        this.y = y;

        font = new BitmapFont(true);

        fpsStartTime = 0L;
        vertexCountStartTime = 0L;

        fpsLabel = new LabelSequence<IntegerSequence>("FPS: ", new IntegerSequence());
        glCallsLabel = new LabelSequence<IntegerSequence>("Gl Calls: ", new IntegerSequence());
        drawCallsLabel = new LabelSequence<IntegerSequence>("Draw Calls: ", new IntegerSequence());
        textureBindsLabel = new LabelSequence<IntegerSequence>("Texture Binds: ", new IntegerSequence());
        shaderSwitchesLabel = new LabelSequence<IntegerSequence>("Shader Switches: ", new IntegerSequence());
        vertexCountLabel = new LabelSequence<IntegerSequence>("Vertex Count: ", new IntegerSequence());
        maxSpritesLabel = new LabelSequence<IntegerSequence>("Sprites Batched: ", new IntegerSequence());
        javaHeapLabel = new LabelSequence<IntegerSequence>("Java Heap (MB): ", new IntegerSequence());
        nativeHeapLabel = new LabelSequence<IntegerSequence>("Native Heap (MB): ", new IntegerSequence());

        GLProfiler.enable();
    }

    public void profile(Batch batch)
    {
        drawY = 0f;
        batch.begin();

        if (profileFPS)
        {
            if ((TimeUtils.millis() - fpsStartTime) > FPS_DISPLAY_DELAY)
            {
                fpsLabel.getValue().set(Gdx.graphics.getFramesPerSecond());
                fpsStartTime = TimeUtils.millis();
            }

            drawSequence(batch, fpsLabel);
        }

        if (profileOpenGL)
        {
            if ((TimeUtils.millis() - vertexCountStartTime) > VERTEXCOUNT_DISPLAY_DELAY)
            {
                vertexCountLabel.getValue().set((int)GLProfiler.vertexCount.total);
                vertexCountStartTime = TimeUtils.millis();
            }

            glCallsLabel.getValue().set(GLProfiler.calls);
            drawCallsLabel.getValue().set(GLProfiler.drawCalls);
            textureBindsLabel.getValue().set(GLProfiler.textureBindings);
            shaderSwitchesLabel.getValue().set(GLProfiler.shaderSwitches);

            boolean drawMaxSpritesInBatch = false;

            if (batch instanceof SpriteBatch)
            {
                maxSpritesLabel.getValue().set(((SpriteBatch)batch).maxSpritesInBatch);
                drawMaxSpritesInBatch = true;
            }
            else if (batch instanceof GameSpriteBatch)
            {
                maxSpritesLabel.getValue().set(((GameSpriteBatch)batch).maxSpritesInBatch);
                drawMaxSpritesInBatch = true;
            }

            drawSequence(batch, glCallsLabel);
            drawSequence(batch, drawCallsLabel);
            drawSequence(batch, textureBindsLabel);
            drawSequence(batch, shaderSwitchesLabel);
            drawSequence(batch, vertexCountLabel);
            if (drawMaxSpritesInBatch) drawSequence(batch, maxSpritesLabel);
        }

        if (profileMemory)
        {
            javaHeapLabel.getValue().set((int)((float)Gdx.app.getJavaHeap() / 1000000f));
            nativeHeapLabel.getValue().set((int)((float)Gdx.app.getNativeHeap() / 1000000f));

            drawSequence(batch, javaHeapLabel);
            drawSequence(batch, nativeHeapLabel);
        }

        batch.end();
        GLProfiler.reset();
    }

    private void drawSequence(Batch batch, CharSequence sequence)
    {
        font.draw(batch, sequence, x, y + drawY);
        drawY += LINE_OFFSET;
    }
}
