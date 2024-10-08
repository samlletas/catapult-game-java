package com.engine.graphics.graphics2D.animation.basic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.engine.events.Event;
import com.engine.events.EventsArgs;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.utilities.ColorUtilities;
import com.engine.utilities.FastArray;

public class BasicAnimation
{
    private FastArray<BasicFrame> frames;
    private boolean loop;
    private boolean isPlaying;
    private boolean isPaused;
    private float speed;

    private int currentFramePosition;
    private int nextFramePosition;
    private float totalFrameTime;
    private float currentFrameTime;
    private float startDelay;

    private float rotation;
    private float scaleX;
    private float scaleY;
    private float offsetX;
    private float offsetY;
    private float alpha;

    // Eventos
    public Event<EventsArgs> onAnimationEnd;
    private EventsArgs onAnimationEndArgs;

    public BasicAnimation(boolean loop, BasicFrame... frames)
    {
        this.loop = loop;
        this.isPlaying = false;
        this.isPaused = false;
        this.speed = 1f;

        this.currentFramePosition = -1;
        this.nextFramePosition = -1;
        this.totalFrameTime = 0.0f;
        this.currentFrameTime = 0.0f;
        this.startDelay = 0f;

        this.scaleX = 0f;
        this.scaleY = 0f;
        this.offsetX = 0f;
        this.offsetY = 0f;
        this.alpha = 0f;

        this.onAnimationEnd = new Event<EventsArgs>();
        this.onAnimationEndArgs = new EventsArgs();

        initializeFrames(frames);
    }

    private void initializeFrames(BasicFrame[] frames)
    {
        if (frames.length > 0)
        {
            this.frames = new FastArray<BasicFrame>();

            for (BasicFrame frame : frames)
            {
                this.frames.add(frame);
            }

            // Iniciar con el primer frame
            currentFramePosition = 0;
            nextFramePosition = 0;
            interpolateFrames(0f);
        }
        else
        {
            throw new GdxRuntimeException("The animation must have at least one frame");
        }
    }

    public boolean isPlaying()
    {
        return isPlaying;
    }

    public float getRotation()
    {
        return rotation;
    }

    public float getScaleX()
    {
        return scaleX;
    }

    public float getScaleY()
    {
        return scaleY;
    }

    public float getOffsetX()
    {
        return offsetX;
    }

    public float getOffsetY()
    {
        return offsetY;
    }

    public float getAlpha()
    {
        return alpha;
    }

    public int getCurrentFramePosition()
    {
        return currentFramePosition;
    }

    public void setSpeed(float speed)
    {
        this.speed = speed;
    }

    public void start()
    {
        start(0f, 0f);
    }

    public void start(float delay)
    {
        start(delay, 0f);
    }

    public void start(float delay, float timeOffset)
    {
        if (startDelay >= 0f)
        {
            currentFramePosition = 0;
            nextFramePosition = advanceFramePosition(currentFramePosition);
            interpolateFrames(0f);

            totalFrameTime = frames.get(currentFramePosition).duration;
            currentFrameTime = Math.max(0f, totalFrameTime - timeOffset);
            startDelay = delay;

            isPlaying = true;
            isPaused = false;
        }
        else
        {
            throw new GdxRuntimeException("The delay must be positive");
        }
    }

    public void pause()
    {
        if (isPlaying)
        {
            isPaused = true;
        }
    }

    public void resume()
    {
        if (isPlaying)
        {
            isPaused = false;
        }
    }

    public void update(float delta)
    {
        if (!isPaused)
        {
            if (startDelay > 0f)
            {
                startDelay -= (delta * 1000f) * speed;

                if (startDelay <= 0f)
                {
                    startDelay = 0f;
                }
            }

            if (isPlaying && startDelay == 0f)
            {
                currentFrameTime -= (delta * 1000f) * speed;

                if (currentFrameTime <= 0f)
                {
                    currentFramePosition = advanceFramePosition(currentFramePosition);
                    nextFramePosition = advanceFramePosition(nextFramePosition);

                    if (!loop && currentFramePosition == frames.size - 1)
                    {
                        isPlaying = false;
                        currentFrameTime = 0f;

                        onAnimationEndArgs.sender = this;
                        onAnimationEnd.invoke(onAnimationEndArgs);
                    } else
                    {
                        totalFrameTime = frames.get(currentFramePosition).duration;
                        currentFrameTime = totalFrameTime;
                    }
                }

                float factor = 1f - (currentFrameTime / totalFrameTime);
                interpolateFrames(factor);
            }
        }
    }

    private void interpolateFrames(float factor)
    {
        BasicFrame currentFrame = frames.get(currentFramePosition);
        BasicFrame nextFrame = frames.get(nextFramePosition);
        Interpolation interpolation = currentFrame.interpolation;

        rotation = getRotationInterpolation(currentFrame.rotation, nextFrame.rotation, interpolation, factor);
        scaleX = interpolation.apply(currentFrame.scaleX, nextFrame.scaleX, factor);
        scaleY = interpolation.apply(currentFrame.scaleY, nextFrame.scaleY, factor);
        offsetX = interpolation.apply(currentFrame.offsetX, nextFrame.offsetX, factor);
        offsetY = interpolation.apply(currentFrame.offsetY, nextFrame.offsetY, factor);
        alpha = interpolation.apply(currentFrame.alpha, nextFrame.alpha, factor);
    }

    private int advanceFramePosition(int position)
    {
        position++;

        if (position >= frames.size)
        {
            if (loop)
            {
                position = 0;
            }
            else
            {
                position = frames.size - 1;
            }
        }

        return position;
    }

    private float getRotationInterpolation(float currentRotation,
                                           float nextRotation,
                                           Interpolation interpolation,
                                           float factor)
    {
        // Interpolación de rotación directa
        if (Math.abs(nextRotation - currentRotation) <= 180f)
        {
            return interpolation.apply(-currentRotation, -nextRotation, factor);
        }
        // Interpolación de rotación en la dirección que menos diferencia tiene
        // respecto al siguiente frame
        else
        {
            if (currentRotation > nextRotation)
            {
                return interpolation.apply(360f - currentRotation, -nextRotation, factor);
            }
            else
            {
                return interpolation.apply(-currentRotation, 360f - nextRotation, factor);
            }
        }
    }

    /*************************************************************************
     * Dibujado de Texturas
     *************************************************************************/

    /**
     * Dibuja una textura con los valores del frame actual
     *
     * @param batch batch
     * @param texture textura
     * @param x posición en x
     * @param y posición en y
     * @param originX origen de la textura en x
     * @param originY origen de la textura en y
     */
    public void draw(Batch batch, Texture texture, float x, float y,
                     float originX, float originY)
    {
        Color originalColor = batch.getColor();
        float finalOpacity = originalColor.a * alpha;
        float drawX = (x - originX) + offsetX;
        float drawY = (y - originY) + offsetY;

        ColorUtilities.setAlpha(batch, finalOpacity);
        batch.draw(texture, drawX, drawY, originX, originY,
                texture.getWidth(), texture.getHeight(),
                scaleX, scaleY, rotation, 0, 0, texture.getWidth(),
                texture.getHeight(), false, false);
        ColorUtilities.setColor(batch, originalColor);
    }

    /**
     * Dibuja una región de textura con los valores del frame actual
     *
     * @param batch batch
     * @param region region
     * @param x posición en x
     * @param y posición en y
     * @param originX origen de la textura en x
     * @param originY origen de la textura en y
     */
    public void draw(Batch batch, TextureRegion region,
                     float x, float y, float originX, float originY)
    {
        Color originalColor = batch.getColor();
        float finalOpacity = originalColor.a * alpha;
        float drawX = (x - originX) + offsetX;
        float drawY = (y - originY) + offsetY;

        ColorUtilities.setAlpha(batch, finalOpacity);
        batch.draw(region, drawX, drawY, originX, originY,
                region.getRegionWidth(), region.getRegionHeight(),
                scaleX, scaleY, rotation);
        ColorUtilities.setColor(batch, originalColor);
    }
}
