package com.mygdx.game.gamelogic;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.engine.GameTime;
import com.engine.collision2d.IPhysicsObject;
import com.engine.events.DelayedEventHandler;
import com.engine.events.Event;
import com.engine.events.EventsArgs;
import com.engine.events.IEventHandler;
import com.engine.graphics.graphics3D.FastModelBatch;
import com.engine.utilities.FastArray;
import com.mygdx.game.Common;
import com.mygdx.game.gamelogic.targets.Crystal;
import com.mygdx.game.gamelogic.targets.CrystalTypes;
import com.mygdx.game.gamelogic.targets.Spike;
import com.mygdx.game.gamelogic.targets.patterns.BasePattern;
import com.mygdx.game.gamelogic.targets.patterns.TargetCollisionArgs;
import com.mygdx.game.gamelogic.targets.patterns.ingame.CircularCrystalsPattern;
import com.mygdx.game.gamelogic.targets.patterns.ingame.CircularSpikesPattern;
import com.mygdx.game.shaders.CrystalShaderProvider;

public final class CrystalManager implements IPhysicsObject
{
    public static final int MAX_CRYSTALS = 20;
    public static final int MAX_SPIKES = 3;

    private Common common;
    private FastArray<Crystal> crystals;
    private FastArray<Spike> spikes;
    private FastArray<BasePattern> patterns;
    public BasePattern currentPattern;

    public CrystalShaderProvider crystalShaderProvider;
    public FastModelBatch modelBatch;

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
            crystals.add(crystal);
        }

        for (int i = 0; i < MAX_SPIKES; i++)
        {
            Spike spike = new Spike(common.assets);
            spikes.add(spike);
        }

        crystalShaderProvider = new CrystalShaderProvider();
        modelBatch = new FastModelBatch(crystalShaderProvider);

        // Creación de un shader por default para evitar crearlo al entrar
        // al GameplayScreen por primera vez
        ModelInstance crystalInstance = crystals.get(0).getModelInstance();
        crystalShaderProvider.getShader(crystalInstance.getRenderable(new Renderable(), crystalInstance.getNode("default")));

        for (Crystal crystal : crystals)
        {
            modelBatch.initializeRenderable(crystal.getModelInstance());
        }

        for (Spike spike : spikes)
        {
            modelBatch.initializeRenderable(spike.getModelInstance());
        }

        modelBatch.resetPool();
    }

    private void initializePatterns()
    {
        patterns = new FastArray<BasePattern>();
        patterns.add(new CircularCrystalsPattern(common, crystals, spikes, MAX_CRYSTALS, 85f, 100f, false));
        patterns.add(new CircularSpikesPattern(common, crystals, spikes, 3, 75f, 200f, false));

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

    @Override
    public void step(float elapsed, float delta)
    {
        currentPattern.step(elapsed, delta);
    }

    public void checkCollisions(Ball ball)
    {
        currentPattern.checkCrystalCollisions(ball);
        currentPattern.checkSpikeCollisions(ball);
    }

    public void drawTextures(Batch batch)
    {
        currentPattern.drawGlow(batch);
    }

    public void drawModels(Camera camera)
    {
        modelBatch.begin(camera);
        currentPattern.drawModels(modelBatch);
        modelBatch.end();
    }

    public void drawEffects(Batch batch)
    {
        currentPattern.drawEffets(batch);
    }

    public void drawPolygons(ShapeRenderer shapeRenderer)
    {
        currentPattern.drawPolygons(shapeRenderer);
    }
    
    public void setForegoundColor(Color color)
    {
        crystalShaderProvider.setForegroundColor(color);
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
