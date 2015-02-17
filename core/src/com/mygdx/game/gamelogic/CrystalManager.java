package com.mygdx.game.gamelogic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.engine.GameTime;
import com.engine.events.DelayedEventHandler;
import com.engine.events.Event;
import com.engine.events.EventsArgs;
import com.engine.events.IEventHandler;
import com.engine.utilities.FastArray;
import com.mygdx.game.Common;
import com.mygdx.game.gamelogic.targets.Crystal;
import com.mygdx.game.gamelogic.targets.CrystalTypes;
import com.mygdx.game.gamelogic.targets.Spike;
import com.mygdx.game.gamelogic.targets.patterns.BasePattern;
import com.mygdx.game.gamelogic.targets.patterns.TargetCollisionArgs;
import com.mygdx.game.gamelogic.targets.patterns.ingame.CircularCrystalsPattern;
import com.mygdx.game.gamelogic.targets.patterns.ingame.CircularSpikesPattern;

public final class CrystalManager
{
    public static final int MAX_CRYSTALS = 8;
    public static final int MAX_SPIKES = 3;

    private Common common;
    private FastArray<Crystal> crystals;
    private FastArray<Spike> spikes;
    private FastArray<BasePattern> patterns;
    public BasePattern currentPattern;

    private int index = 0;
    private boolean onSpecial = false;

    // Eventos
    public Event<TargetCollisionArgs> onCrystalCollision;
    public Event<TargetCollisionArgs> onSpikeCollision;
    private DisappearTimerReachedZeroDelayedHandler disappearTimerReachedZeroDelayedHandler;

    public CrystalManager(Common common)
    {
        this.common = common;
        this.crystals = new FastArray<Crystal>();
        this.spikes = new FastArray<Spike>();

        this.onCrystalCollision = new Event<TargetCollisionArgs>();
        this.onSpikeCollision = new Event<TargetCollisionArgs>();
        this.disappearTimerReachedZeroDelayedHandler = new DisappearTimerReachedZeroDelayedHandler();

        initializeTargets();
        initializePatterns();
        subscribeToEvents();
    }

    private void initializeTargets()
    {
        for (int i = 0; i < MAX_CRYSTALS; i++)
        {
            Crystal crystal = new Crystal(common.assets);
            common.modelBatch.initializeRenderable(crystal.getModelInstance());
            crystals.add(crystal);
        }

        for (int i = 0; i < MAX_SPIKES; i++)
        {
            Spike spike = new Spike(common.assets);
            common.modelBatch.initializeRenderable(spike.getModelInstance());
            spikes.add(spike);
        }

        common.modelBatch.resetPool();
    }

    private void initializePatterns()
    {
        patterns = new FastArray<BasePattern>();
        patterns.add(new CircularCrystalsPattern(common, crystals, spikes, 8, 75f, 100f, false));
        patterns.add(new CircularSpikesPattern(common, crystals, spikes, 2, 75f, 150f, false));

        currentPattern = patterns.get(index);
        currentPattern.start(onSpecial);
    }

    private void subscribeToEvents()
    {
        for (BasePattern pattern : patterns)
        {
            pattern.onDissapearTimerReachedZero.subscribe(disappearTimerReachedZeroDelayedHandler);
            pattern.onCrystalCollision.subscribe(new CrystalCollisionHandler());
            pattern.onSpikeCollision.subscribe(new SpikeCollisionHandler());
        }
    }

    public void reset()
    {
        for(BasePattern pattern : patterns)
        {
            pattern.reset();
        }

        onSpecial = false;
        index = 0;
        currentPattern = patterns.get(index);
    }

    public void start()
    {
        currentPattern.start(false);
    }

    public void activateSpecial()
    {
        activateSpecial(currentPattern);
    }

    private void activateSpecial(BasePattern pattern)
    {
        onSpecial = true;

        if (pattern.hasActiveCrystals())
        {
            pattern.setCrystalsType(CrystalTypes.Special);
            pattern.startWaveAnimations();
        }
    }

    public void deactivateSpecial()
    {
        deactivateSpecial(currentPattern);
    }

    private void deactivateSpecial(BasePattern pattern)
    {
        onSpecial = false;

        if (pattern.hasActiveCrystals())
        {
            pattern.setCrystalsType(CrystalTypes.Normal);
            pattern.startWaveAnimations();
        }
    }

    public void update(GameTime gameTime)
    {
        disappearTimerReachedZeroDelayedHandler.resolve();
        currentPattern.update(gameTime);
    }

    public void checkCollisions(Ball ball)
    {
        currentPattern.checkCrystalCollisions(ball);
        currentPattern.checkSpikeCollisions(ball);
    }

    public void drawTextures(SpriteBatch spriteBatch)
    {
        currentPattern.drawGlow(spriteBatch);
    }

    public void drawModels(ModelBatch modelBatch)
    {
        currentPattern.drawModels(modelBatch);
    }

    public void drawEffects(SpriteBatch spriteBatch)
    {
        currentPattern.drawEffets(spriteBatch);
    }

    public void drawPolygons(ShapeRenderer shapeRenderer)
    {
        currentPattern.drawPolygons(shapeRenderer);
    }

    class DisappearTimerReachedZeroDelayedHandler extends DelayedEventHandler<EventsArgs>
    {
        @Override
        public void onResolve(EventsArgs args)
        {
            if (index == 0)
            {
                index = 1;
            }
            else
            {
                index = 0;
            }

            currentPattern = patterns.get(index);
            currentPattern.start(onSpecial);
        }
    }

    class CrystalCollisionHandler implements IEventHandler<TargetCollisionArgs>
    {
        @Override
        public void onAction(TargetCollisionArgs args)
        {
            onCrystalCollision.invoke(args);
        }
    }

    class SpikeCollisionHandler implements IEventHandler<TargetCollisionArgs>
    {
        @Override
        public void onAction(TargetCollisionArgs args)
        {
            onSpikeCollision.invoke(args);
        }
    }
}
