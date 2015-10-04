package com.sammacedo.smashingcrystals.gamelogic.targets.patterns;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.RandomXS128;
import com.engine.collision2d.IPhysicsObject;
import com.engine.events.IEventHandler;
import com.engine.utilities.FastArray;
import com.engine.GameTime;
import com.engine.events.Event;
import com.engine.events.EventsArgs;
import com.engine.utilities.Timer;
import com.sammacedo.smashingcrystals.Common;
import com.sammacedo.smashingcrystals.gamelogic.Ball;
import com.sammacedo.smashingcrystals.gamelogic.targets.Crystal;
import com.sammacedo.smashingcrystals.gamelogic.targets.CrystalTypes;
import com.sammacedo.smashingcrystals.gamelogic.targets.Spike;
import com.sammacedo.smashingcrystals.helpers.SoundPlayer;

public abstract class BasePattern implements IPhysicsObject
{
    private static final float TIMER_DISAPPEAR_DURATION = 900f;
    private static final RandomXS128 random = new RandomXS128();

    protected FastArray<Crystal> crystals;
    protected FastArray<Spike> spikes;
    protected SoundPlayer soundPlayer;

    protected final int crystalCount;
    protected final int spikeCount;

    protected final int minStartX;
    protected final int maxStartX;
    protected final int minStartY;
    protected final int maxStartY;

    protected int activeCrystals;
    protected int activeSpikes;
    protected boolean isActive;
    protected Timer disappearTimer;

    // Eventos
    public Event<EventsArgs> onDissapearTimerReachedZero;
    public Event<TargetCollisionArgs> onTargetCollision;
    public TargetCollisionArgs targetCollisionArgs;

    public BasePattern(Common common, FastArray<Crystal> crystals, FastArray<Spike> spikes,
                       int crystalCount, int spikeCount,
                       int minStartX, int maxStartX,
                       int minStartY, int maxStartY)
    {
        this.crystals = crystals;
        this.spikes = spikes;
        this.soundPlayer = common.soundPlayer;

        this.crystalCount = crystalCount;
        this.spikeCount = spikeCount;

        this.minStartX = minStartX;
        this.maxStartX = maxStartX;
        this.minStartY = minStartY;
        this.maxStartY = maxStartY;
        this.isActive = false;
        this.disappearTimer = new Timer(TIMER_DISAPPEAR_DURATION);

        initializeEvents();
    }

    private void initializeEvents()
    {
        this.onDissapearTimerReachedZero = disappearTimer.timerReachedZero;
        this.onDissapearTimerReachedZero.subscribe(new IEventHandler<EventsArgs>()
        {
            @Override
            public void onAction(EventsArgs args)
            {
                isActive = false;
            }
        });

        this.onTargetCollision = new Event<TargetCollisionArgs>();
        this.targetCollisionArgs = new TargetCollisionArgs();
    }

    public boolean hasActiveCrystals()
    {
        return activeCrystals > 0f;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void reset()
    {
        setCrystalsType(CrystalTypes.Normal);
        isActive = false;
        disappearTimer.stop();
    }

    public final void start(boolean onSpecial)
    {
        activeCrystals = crystalCount;
        activeSpikes = spikeCount;

        FastArray<Crystal> localCrystals = crystals;
        FastArray<Spike> localSpikes = spikes;
        CrystalTypes crystalType;

        if (onSpecial)
        {
            crystalType = CrystalTypes.Special;
        }
        else
        {
            crystalType = CrystalTypes.Normal;
        }

        for (int i = 0; i < crystalCount; i++)
        {
            Crystal crystal = localCrystals.get(i);
            crystal.setType(crystalType);
            crystal.start();
        }

        for (int i = 0; i < spikeCount; i++)
        {
            Spike spike = localSpikes.get(i);
            spike.start();
        }

        int diffX = maxStartX - minStartX;
        int diffY = maxStartY - minStartY;

        int startX = minStartX + random.nextInt(diffX);
        int startY = minStartY + random.nextInt(diffY);

        initializePositions(startX, startY);
        isActive = true;
    }

    public void setCrystalsType(CrystalTypes type)
    {
        FastArray<Crystal> localCrystals = crystals;

        for (int i = 0; i < crystalCount; i++)
        {
            Crystal crystal = localCrystals.get(i);
            crystal.setType(type);
        }
    }

    public void startWaveAnimations()
    {
        FastArray<Crystal> localCrystals = crystals;

        for (int i = 0; i < crystalCount; i++)
        {
            localCrystals.get(i).startWaveAnimation();
        }
    }

    public final void update(GameTime gameTime)
    {
        if (isActive)
        {
            FastArray<Crystal> localCrystals = crystals;
            FastArray<Spike> localSpikes = spikes;

            for (int i = 0; i < crystalCount; i++)
            {
                localCrystals.get(i).update(gameTime);
            }

            for (int i = 0; i < spikeCount; i++)
            {
                localSpikes.get(i).update(gameTime);
            }

            disappearTimer.update(gameTime);
        }
    }

    @Override
    public final void step(float elapsed, float delta)
    {
        if (isActive)
        {
            onStepStart(elapsed, delta);

            FastArray<Crystal> localCrystals = crystals;
            FastArray<Spike> localSpikes = spikes;

            Crystal crystal;
            Spike spike;

            for (int i = 0; i < crystalCount; i++)
            {
                crystal = localCrystals.get(i);
                crystal.step(elapsed, delta);
                stepCrystal(elapsed, delta, crystal, i);
            }

            for (int i = 0; i < spikeCount; i++)
            {
                spike = localSpikes.get(i);
                spike.step(elapsed, delta);
                stepSpike(elapsed, delta, localSpikes.get(i), i);
            }
        }
    }

    public final void checkCrystalCollisions(Ball ball)
    {
        if (isActive)
        {
            FastArray<Crystal> localCrystals = crystals;
            boolean collided;

            for (int i = 0; i < crystalCount; i++)
            {
                Crystal crystal = localCrystals.get(i);
                collided = crystal.onCollision(ball.polygon);
                crystal.move();

                if (collided)
                {
                    crystal.destroy();
                    soundPlayer.play(soundPlayer.crystalBreak);
                    activeCrystals--;

                    if (activeCrystals == 0)
                    {
                        disappear();
                    }

                    targetCollisionArgs.sender = this;
                    targetCollisionArgs.target = crystal;
                    onTargetCollision.invoke(targetCollisionArgs);
                }
            }
        }
    }

    public final void checkSpikeCollisions(Ball ball)
    {
        if (isActive)
        {
            FastArray<Spike> localSpikes = spikes;
            boolean collided;

            for (int i = 0; i < spikeCount; i++)
            {
                Spike spike = localSpikes.get(i);
                collided = spike.onCollision(ball.polygon);
                spike.move();

                if (collided)
                {
                    spike.destroy();
                    soundPlayer.play(soundPlayer.crystalBreak);

                    targetCollisionArgs.sender = this;
                    targetCollisionArgs.target = spike;
                    onTargetCollision.invoke(targetCollisionArgs);
                }
            }
        }
    }

    private void disappear()
    {
        disappearTimer.start();
        FastArray<Spike> localSpikes = spikes;

        for (int i = 0; i < spikeCount; i++)
        {
            localSpikes.get(i).disappear();
        }
    }

    public final void drawGlow(Batch batch)
    {
        if (isActive)
        {
            FastArray<Crystal> localCrystals = crystals;
            FastArray<Spike> localSpikes = spikes;

            for (int i = 0; i < crystalCount; i++)
            {
                Crystal crystal = localCrystals.get(i);
                crystal.drawGlow(batch);
            }

            for (int i = 0; i < spikeCount; i++)
            {
                Spike spike = localSpikes.get(i);
                spike.drawGlow(batch);
            }
        }
    }

    public final void drawModels(ModelBatch modelBatch)
    {
        if (isActive)
        {
            FastArray<Crystal> localCrystals = crystals;
            FastArray<Spike> localSpikes = spikes;

            for (int i = 0; i < crystalCount; i++)
            {
                Crystal crystal = localCrystals.get(i);
                crystal.drawModel(modelBatch);
            }

            for (int i = 0; i < spikeCount; i++)
            {
                Spike spike = localSpikes.get(i);
                spike.drawModel(modelBatch);
            }
        }
    }

    public final void drawEffets(Batch batch)
    {
        if (isActive)
        {
            FastArray<Crystal> localCrystals = crystals;
            FastArray<Spike> localSpikes = spikes;

            for (int i = 0; i < crystalCount; i++)
            {
                Crystal crystal = localCrystals.get(i);
                crystal.drawEffects(batch);
            }

            for (int i = 0; i < spikeCount; i++)
            {
                Spike spike = localSpikes.get(i);
                spike.drawEffects(batch);
            }
        }
    }

    public final void drawPolygons(ShapeRenderer shapeRenderer)
    {
        if (isActive)
        {
            FastArray<Crystal> localCrystals = crystals;
            FastArray<Spike> localSpikes = spikes;

            for (int i = 0; i < crystalCount; i++)
            {
                Crystal crystal = localCrystals.get(i);
                crystal.drawPolygon(shapeRenderer);
            }

            for (int i = 0; i < spikeCount; i++)
            {
                Spike spike = localSpikes.get(i);
                spike.drawPolygon(shapeRenderer);
            }
        }
    }

    protected abstract void initializePositions(int startX, int startY);
    protected abstract void onStepStart(float elapsed, float delta);
    protected abstract void stepCrystal(float elapsed, float delta, Crystal crystal, int index);
    protected abstract void stepSpike(float elapsed, float delta, Spike spike, int index);
}
