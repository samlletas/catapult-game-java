package com.engine.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.engine.GameTime;
import com.engine.utilities.Timer;
import com.engine.events.EventsArgs;
import com.engine.events.IEventHandler;

public final class CameraShaker2D
{
    private OrthographicCamera camera;
    private Timer timer;

    private float currentDisplacementX;
    private float currentDisplacementY;
    private float currentRotation;
    private float currentZoom;
    private float originalCameraZoom;

    private float maxDisplacementX;
    private float maxDisplacementY;
    private float maxRotation;
    private float maxZoom;

    private int repetitions;

    private boolean shaking;
    private boolean recovering;
    private boolean beginCalled;

    public CameraShaker2D(OrthographicCamera camera)
    {
        this(camera, 100, 2, 2, 0f, 1f);
    }

    public CameraShaker2D(OrthographicCamera camera, float shakeTime,
                          int maxDisplacementX, int maxDisplacementY)
    {
        this(camera, shakeTime, maxDisplacementX, maxDisplacementY,
                0f, 1f);
    }

    public CameraShaker2D(OrthographicCamera camera, float shakeTime,
                          int maxDisplacementX, int maxDisplacementY,
                          float maxRotation)
    {
        this(camera, shakeTime, maxDisplacementX, maxDisplacementY,
                maxRotation, 1f);
    }

    /**
     *
     * @param camera
     * @param shakeTime duracion en ms de cada vibración
     * @param maxDisplacementX desplazamiento máximo en X durante la vibración
     * @param maxDisplacementY desplazamiento máximo en Y durante la vibración
     * @param maxRotation rotación máxima durante la vibración
     * @param maxZoom zoom máximo durante la vibración
     */
    public CameraShaker2D(OrthographicCamera camera, float shakeTime,
                          int maxDisplacementX, int maxDisplacementY,
                          float maxRotation, float maxZoom)
    {
        this.camera = camera;

        initializeTimer(shakeTime);

        this.maxDisplacementX = maxDisplacementX;
        this.maxDisplacementY = maxDisplacementY;
        this.maxRotation = maxRotation;
        this.maxZoom = maxZoom;
        this.currentZoom = 1f;

        this.shaking = false;
        this.recovering = false;
        this.beginCalled = false;
    }

    private void initializeTimer(float shakeTime)
    {
        timer = new Timer(shakeTime / 2f);

        timer.timerReachedZero.subscribe(new IEventHandler<EventsArgs>()
        {
            @Override
            public void onAction(EventsArgs args)
            {
                if (!recovering)
                {
                    timer.start();
                    recovering = true;
                } else
                {
                    repetitions--;

                    if (repetitions <= 0)
                    {
                        currentDisplacementX = 0f;
                        currentDisplacementY = 0f;
                        currentRotation = 0f;
                        currentZoom = 1f;

                        shaking = false;
                    } else
                    {
                        timer.start();
                    }

                    recovering = false;
                }
            }
        });
    }

    /**
     * Inicia una vibración de 1 repetición
     */
    public void shake()
    {
        shake(1);
    }

    /**
     * Inicia una vibración con la cantidad de repeticiones especificada
     * @param repetitions Cantidad de repeticiones
     */
    public void shake(int repetitions)
    {
        if (shaking)
        {
            this.repetitions += repetitions;
        }
        else
        {
            this.repetitions = repetitions;
            timer.start();

            shaking = true;
            recovering = false;
        }
    }

    /**
     * Detiene la vibración actual permitiendo su recuperación
     */
    public void stop()
    {
        if (shaking)
        {
            this.repetitions = 1;
        }
    }

    /**
     * Detiene por completo la vibración actual sin permitir su recuperación
     */
    public void forceStop()
    {
        if (shaking)
        {
            currentDisplacementX = 0f;
            currentDisplacementY = 0f;
            currentRotation = 0f;
            currentZoom = 1f;

            shaking = false;
            recovering = false;

            timer.stop();
        }
    }

    public void update(GameTime gameTime)
    {
        if (shaking)
        {
            float elapsedTimePercentage = timer.elapsedTimePercentage();

            if (!recovering)
            {
                currentDisplacementX = Interpolation.sine.apply(0f,
                        maxDisplacementX, elapsedTimePercentage);

                currentDisplacementY = Interpolation.sine.apply(0f,
                        maxDisplacementY, elapsedTimePercentage);

                currentRotation = Interpolation.sine.apply(0f, maxRotation,
                        elapsedTimePercentage);

                currentZoom = Interpolation.sine.apply(1f, maxZoom,
                        elapsedTimePercentage);
            }
            else
            {
                currentDisplacementX = Interpolation.sine.apply(
                        maxDisplacementX, 0f, elapsedTimePercentage);

                currentDisplacementY = Interpolation.sine.apply(
                        maxDisplacementY, 0f, elapsedTimePercentage);

                currentRotation = Interpolation.sine.apply(maxRotation, 0f,
                        elapsedTimePercentage);

                currentZoom = Interpolation.sine.apply(maxZoom, 1f,
                        elapsedTimePercentage);
            }
        }

        timer.update(gameTime);
    }

    /**
     * Realiza transformaciones correspondients a la vibración en la cámara
     * para preparar el dibujado
     * @param batch
     */
    public void beginDraw(Batch batch)
    {
        if (!beginCalled)
        {
            originalCameraZoom = camera.zoom;

            camera.rotate(currentRotation);
            camera.zoom = currentZoom;
            camera.translate(currentDisplacementX, currentDisplacementY, 0f);
            camera.update();

            batch.setProjectionMatrix(camera.combined);
            beginCalled = true;
        }
        else
        {
            throw new GdxRuntimeException("CameraShaker2D.beginDraw must be proceeded by endDraw");
        }
    }

    /**
     * Realiza transformaciones para reiniciar la cámara a su estado original
     * @param batch
     */
    public void endDraw(Batch batch)
    {
        if (beginCalled)
        {
            camera.translate(-currentDisplacementX, -currentDisplacementY, 0f);
            camera.zoom = originalCameraZoom;
            camera.rotate(-currentRotation);
            camera.update();

            batch.setProjectionMatrix(camera.combined);
            beginCalled = false;
        }
        else
        {
            throw new GdxRuntimeException("CameraShaker2D.endDraw must be called after beginDraw");
        }
    }
}
