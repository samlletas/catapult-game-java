package com.engine.graphics.graphics2D.animation.basic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.engine.GameTime;
import com.engine.events.Event;
import com.engine.events.EventsArgs;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.utilities.ColorUtilities;

public class BasicAnimation
{
    private Array<BasicFrame> frames;
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
            this.frames = new Array<BasicFrame>();

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

    public void update(GameTime gameTime)
    {
        if (!isPaused)
        {
            if (startDelay > 0f)
            {
                startDelay -= (gameTime.delta * 1000f) * speed;

                if (startDelay <= 0f)
                {
                    startDelay = 0f;
                }
            }

            if (isPlaying && startDelay == 0f)
            {
                currentFrameTime -= (gameTime.delta * 1000f) * speed;

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
     * @param spriteBatch spriteBatch
     * @param texture textura
     * @param x posición en x
     * @param y posición en y
     * @param originX origen de la textura en x
     * @param originY origen de la textura en y
     */
    public void draw(SpriteBatch spriteBatch, Texture texture, float x, float y,
                     float originX, float originY)
    {
        Color originalColor = spriteBatch.getColor();
        float finalOpacity = originalColor.a * alpha;
        float drawX = (x - originX) + offsetX;
        float drawY = (y - originY) + offsetY;

        ColorUtilities.setAlpha(spriteBatch, finalOpacity);
        spriteBatch.draw(texture, drawX, drawY, originX, originY,
                texture.getWidth(), texture.getHeight(),
                scaleX, scaleY, rotation, 0, 0, texture.getWidth(),
                texture.getHeight(), false, false);
        ColorUtilities.setColor(spriteBatch, originalColor);
    }

    /**
     * Dibuja una región de textura con los valores del frame actual
     *
     * @param spriteBatch spriteBatch
     * @param region region
     * @param x posición en x
     * @param y posición en y
     * @param originX origen de la textura en x
     * @param originY origen de la textura en y
     */
    public void draw(SpriteBatch spriteBatch, TextureRegion region,
                     float x, float y, float originX, float originY)
    {
        Color originalColor = spriteBatch.getColor();
        float finalOpacity = originalColor.a * alpha;
        float drawX = (x - originX) + offsetX;
        float drawY = (y - originY) + offsetY;

        ColorUtilities.setAlpha(spriteBatch, finalOpacity);
        spriteBatch.draw(region, drawX, drawY, originX, originY,
                region.getRegionWidth(), region.getRegionHeight(),
                scaleX, scaleY, rotation);
        ColorUtilities.setColor(spriteBatch, originalColor);
    }

    /*************************************************************************
     * Dibujado de Fuentes DistanceField
     *************************************************************************/

    /**
     * Dibuja una fuente DistanceField con los valores del frame actual
     * Nota: La rotación del frame actual no se toma en cuenta
     *
     * @param renderer renderer
     * @param font font
     * @param str string
     * @param x posición en x
     * @param y posición en y
     * @param centered si la fuente debe de dibujarse centrada
     * @return los bounds de la fuente al ser dibujada
     */
    public BitmapFont.TextBounds draw(DistanceFieldRenderer renderer,
                                      DistanceFieldFont font, CharSequence str,
                                      float x, float y, boolean centered)
    {
        float originalScaleX = font.getScaleX();
        float originalScaleY = font.getScaleY();
        float originalAlpha = font.getAlpha();

        float finalX = x + offsetX;
        float finalY = y + offsetY;

        font.setScale(scaleX, scaleY);
        font.setAlpha(alpha);

        if (centered)
        {
            BitmapFont.TextBounds bounds = font.getFont().getBounds(str);
            finalX -= bounds.width / 2f;
            finalY -= bounds.height / 2f;
        }

        BitmapFont.TextBounds bounds = renderer.draw(str, finalX, finalY);

        font.setScale(originalScaleX, originalScaleY);
        font.setAlpha(originalAlpha);

        return bounds;
    }

    /**
     * Dibuja una fuente DistanceField con los valores del frame actual
     * Nota: La rotación del frame actual no se toma en cuenta
     *
     * @param renderer renderer
     * @param font font
     * @param str string
     * @param x posición en x
     * @param y posición en y
     * @param start posición del caracter inicial
     * @param end posición del caracter final
     * @param centered si la fuente debe de dibujarse centrada
     * @return los bounds de la fuente al ser dibujada
     */
    public BitmapFont.TextBounds draw(DistanceFieldRenderer renderer,
                                      DistanceFieldFont font, CharSequence str,
                                      float x, float y, int start, int end,
                                      boolean centered)
    {
        float originalScaleX = font.getScaleX();
        float originalScaleY = font.getScaleY();
        float originalAlpha = font.getAlpha();

        float finalX = x + offsetX;
        float finalY = y + offsetY;

        font.setScale(scaleX, scaleY);
        font.setAlpha(alpha);

        if (centered)
        {
            BitmapFont.TextBounds bounds = font.getFont().getBounds(str);
            finalX -= bounds.width / 2f;
            finalY -= bounds.height / 2f;
        }

        BitmapFont.TextBounds bounds = renderer.draw(str, finalX, finalY,
                start, end);

        font.setScale(originalScaleX, originalScaleY);
        font.setAlpha(originalAlpha);

        return bounds;
    }

    /**
     * Dibuja una fuente DistanceField con los valores del frame actual
     * Nota: La rotación del frame actual no se toma en cuenta
     *
     * @param renderer renderer
     * @param font font
     * @param str string
     * @param x posición en x
     * @param y posición en y
     * @param centered si la fuente debe de dibujarse centrada
     * @return los bounds de la fuente al ser dibujada
     */
    public BitmapFont.TextBounds drawMultiline(DistanceFieldRenderer renderer,
                                      DistanceFieldFont font, CharSequence str,
                                      float x, float y, boolean centered)
    {
        float originalScaleX = font.getScaleX();
        float originalScaleY = font.getScaleY();
        float originalAlpha = font.getAlpha();

        float finalX = x + offsetX;
        float finalY = y + offsetY;

        font.setScale(scaleX, scaleY);
        font.setAlpha(alpha);

        if (centered)
        {
            BitmapFont.TextBounds bounds = font.getFont().getMultiLineBounds(str);
            finalX -= bounds.width / 2f;
            finalY -= bounds.height / 2f;
        }

        BitmapFont.TextBounds bounds = renderer.drawMultiLine(str, finalX, finalY);

        font.setScale(originalScaleX, originalScaleY);
        font.setAlpha(originalAlpha);

        return bounds;
    }
}
