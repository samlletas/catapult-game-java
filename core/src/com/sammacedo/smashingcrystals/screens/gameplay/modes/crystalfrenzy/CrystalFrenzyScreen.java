package com.sammacedo.smashingcrystals.screens.gameplay.modes.crystalfrenzy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.GameTime;
import com.engine.actors.TextureRegionActor;
import com.engine.events.EventsArgs;
import com.engine.events.IEventHandler;
import com.engine.graphics.GraphicsSettings;
import com.sammacedo.smashingcrystals.Common;
import com.sammacedo.smashingcrystals.Global;
import com.sammacedo.smashingcrystals.gamelogic.targets.Crystal;
import com.sammacedo.smashingcrystals.gamelogic.targets.Spike;
import com.sammacedo.smashingcrystals.gamelogic.targets.patterns.TargetCollisionArgs;
import com.sammacedo.smashingcrystals.screens.gameplay.*;

public class CrystalFrenzyScreen extends BaseGameplayScreen
{
    private static final String LABEL_CRYSTAL_NORMAL = "+1";
    private static final String LABEL_CRYSTAL_SPECIAL = "+3";
    private static final String LABEL_SPIKE = "MISS!";

    private CrystalFrenzyData data;
    private CrystalFrenzyHUD hud;

    private Runnable increaseScoreRunnable;
    private Runnable decreaseChanceRunnable;
    private Runnable finishGameRunnable;

    public CrystalFrenzyScreen(GraphicsSettings settings, Viewport viewport2D,
                            Viewport viewport3D, Batch batch, Common common,
                            GameInstances gameInstances)
    {
        super(Global.ScreenNames.CRYSTAL_FRENZY, settings, viewport2D,
                viewport3D, batch, common, gameInstances);
    }

    @Override
    protected BaseGameplayData createGameplayData()
    {
        data = new CrystalFrenzyData(common.settings);
        return data;
    }

    @Override
    protected BaseGameHUD createHUD()
    {
        hud = new CrystalFrenzyHUD(common, data);
        return hud;
    }

    @Override
    protected void onInitialize()
    {
        increaseScoreRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                data.increaseScore(1);
                hud.bloomScore();
            }
        };

        decreaseChanceRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                hud.disolveChance();
                hud.animateCurrentChance();
            }
        };

        finishGameRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                gameInstances.crystalManager.deactivateSpecial();
                hud.deactivateSpecialBar();
            }
        };

        data.onSpecialActivated.subscribe(new IEventHandler<EventsArgs>()
        {
            @Override
            public void onAction(EventsArgs args)
            {
                gameInstances.crystalManager.activateSpecial();
                hud.activateSpeciarBar();
            }
        });

        data.onSpecialFinalized.subscribe(new IEventHandler<EventsArgs>()
        {
            @Override
            public void onAction(EventsArgs args)
            {
                gameInstances.crystalManager.deactivateSpecial();
                hud.deactivateSpecialBar();
            }
        });
    }

    @Override
    protected void onReset()
    {

    }

    @Override
    protected void onStart()
    {

    }

    @Override
    protected void onTargetCollision(TargetCollisionArgs args)
    {
        if (args.target instanceof Crystal)
        {
            data.increaseSpecial(0.1f);
            gameInstances.gameLabelContainer.showNormal(LABEL_CRYSTAL_NORMAL, 0.3f,
                    args.target.getX(), args.target.getY(),
                    BaseGameHUD.SCORE_TEXT_X, BaseGameHUD.SCORE_TEXT_Y,
                    increaseScoreRunnable);
        }
        else if (args.target instanceof Spike)
        {
            int chances = data.getLives();

            if (chances > 0)
            {
                TextureRegionActor chanceActor = hud.getChance(chances - 1);
                gameInstances.gameLabelContainer.showFlashing(LABEL_SPIKE, 0.2f,
                        args.target.getX(), args.target.getY(),
                        chanceActor.getX(), chanceActor.getY(),
                        decreaseChanceRunnable, Global.Colors.LABEL_BAD);
                data.decreaseLive();
            }

            gameInstances.cameraShaker.shake(2);
        }
    }

    @Override
    protected void onUpdate(GameTime gameTime)
    {
        if (gameState == GameStates.Playing)
        {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
            {
                data.increaseSpecial(100f);
            }
        }
    }

    @Override
    protected void onDispose()
    {

    }
}
