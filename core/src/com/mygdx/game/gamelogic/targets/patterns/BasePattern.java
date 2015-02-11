package com.mygdx.game.gamelogic.targets.patterns;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.RandomXS128;
import com.engine.events.IEventHandler;
import com.engine.utilities.FastArray;
import com.engine.GameTime;
import com.engine.events.Event;
import com.engine.events.EventsArgs;
import com.engine.utilities.Timer;
import com.mygdx.game.Common;
import com.mygdx.game.gamelogic.Ball;
import com.mygdx.game.gamelogic.targets.Crystal;
import com.mygdx.game.gamelogic.targets.CrystalTypes;
import com.mygdx.game.gamelogic.targets.Spike;
import com.mygdx.game.helpers.SoundPlayer;

public abstract class BasePattern
{
    private static final int CRYSTAL_NORMAL_SCORE = 1;
    private static final int CRYSTAL_SPECIAL_SCORE = 3;
    private static final float MAX_PATTERN_SPECIAL = 50f;
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

    private int scorePerCrystal;
    private float specialPerCrystal;

    // Eventos
    public Event<EventsArgs> onDissapearTimerReachedZero;
    public Event<TargetCollisionArgs> onCrystalCollision;
    public Event<TargetCollisionArgs> onSpikeCollision;
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

        this.onCrystalCollision = new Event<TargetCollisionArgs>();
        this.onSpikeCollision = new Event<TargetCollisionArgs>();
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
        setCrystalsScore(crystalType);

        specialPerCrystal = MAX_PATTERN_SPECIAL / crystalCount;
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

        setCrystalsScore(type);
    }

    private void setCrystalsScore(CrystalTypes type)
    {
        if (type == CrystalTypes.Normal)
        {
            scorePerCrystal = CRYSTAL_NORMAL_SCORE;
        }
        else
        {
            scorePerCrystal = CRYSTAL_SPECIAL_SCORE;
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
            onUpdateStart(gameTime);

            FastArray<Crystal> localCrystals = crystals;
            FastArray<Spike> localSpikes = spikes;

            for (int i = 0; i < crystalCount; i++)
            {
                Crystal crystal = localCrystals.get(i);
                crystal.update(gameTime);
                updateCrystal(gameTime, crystal, i);
            }

            for (int i = 0; i < spikeCount; i++)
            {
                Spike spike = localSpikes.get(i);
                spike.update(gameTime);
                updateSpike(gameTime, spike, i);
            }

            disappearTimer.update(gameTime);
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
                    soundPlayer.playBreak();
                    activeCrystals--;

                    if (activeCrystals == 0)
                    {
                        disappear();
                    }

                    targetCollisionArgs.sender = this;
                    targetCollisionArgs.target = crystal;
                    targetCollisionArgs.score = scorePerCrystal;
                    targetCollisionArgs.special = specialPerCrystal;
                    onCrystalCollision.invoke(targetCollisionArgs);
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
                    soundPlayer.playBreak();

                    targetCollisionArgs.sender = this;
                    targetCollisionArgs.target = spike;
                    targetCollisionArgs.score = 0;
                    targetCollisionArgs.special = 0f;
                    onSpikeCollision.invoke(targetCollisionArgs);
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

    public final void drawGlow(SpriteBatch spriteBatch)
    {
        if (isActive)
        {
            FastArray<Crystal> localCrystals = crystals;
            FastArray<Spike> localSpikes = spikes;

            for (int i = 0; i < crystalCount; i++)
            {
                Crystal crystal = localCrystals.get(i);
                crystal.drawGlow(spriteBatch);
            }

            for (int i = 0; i < spikeCount; i++)
            {
                Spike spike = localSpikes.get(i);
                spike.drawGlow(spriteBatch);
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

    public final void drawEffets(SpriteBatch spriteBatch)
    {
        if (isActive)
        {
            FastArray<Crystal> localCrystals = crystals;
            FastArray<Spike> localSpikes = spikes;

            for (int i = 0; i < crystalCount; i++)
            {
                Crystal crystal = localCrystals.get(i);
                crystal.drawEffects(spriteBatch);
            }

            for (int i = 0; i < spikeCount; i++)
            {
                Spike spike = localSpikes.get(i);
                spike.drawEffects(spriteBatch);
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
    protected abstract void onUpdateStart(GameTime gameTime);
    protected abstract void updateCrystal(GameTime gameTime, Crystal crystal, int index);
    protected abstract void updateSpike(GameTime gameTime, Spike spike, int index);
}
