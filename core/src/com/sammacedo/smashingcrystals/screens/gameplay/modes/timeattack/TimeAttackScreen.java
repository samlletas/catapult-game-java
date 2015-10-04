package com.sammacedo.smashingcrystals.screens.gameplay.modes.timeattack;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.GameTime;
import com.engine.events.EventsArgs;
import com.engine.events.IEventHandler;
import com.engine.graphics.GraphicsSettings;
import com.engine.screens.Screen;
import com.sammacedo.smashingcrystals.Common;
import com.sammacedo.smashingcrystals.Global;
import com.sammacedo.smashingcrystals.gamelogic.targets.Crystal;
import com.sammacedo.smashingcrystals.gamelogic.targets.Spike;
import com.sammacedo.smashingcrystals.gamelogic.targets.patterns.TargetCollisionArgs;
import com.sammacedo.smashingcrystals.screens.gameplay.*;

public final class TimeAttackScreen extends BaseGameplayScreen
{
    private static final String LABEL_CRYSTAL = "+1";
    private static final String LABEL_SPIKE = "MISS!";
    private static final String GAME_OVER_MESSAGE = "TIME'S UP";

    private TimeAttackData data;
    private TimeAttackHUD hud;

    private Runnable increaseScoreRunnable;
    private Runnable decreaseTimeRunnable;

    public TimeAttackScreen(GraphicsSettings settings, Viewport viewport2D,
                            Viewport viewport3D, Batch batch, Common common,
                            GameInstances gameInstances)
    {
        super(Global.ScreenNames.TIME_ATTACK, settings, viewport2D,
                viewport3D, batch, common, gameInstances);
    }

    @Override
    protected BaseGameplayData createGameplayData()
    {
        data = new TimeAttackData(common.settings);
        return data;
    }

    @Override
    protected BaseGameHUD createHUD()
    {
        hud = new TimeAttackHUD(common, data);
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
                common.soundPlayer.play(common.soundPlayer.point);
            }
        };

        decreaseTimeRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                data.timer.substract(5000);
                hud.hitTime();
                common.soundPlayer.play(common.soundPlayer.damage);
            }
        };

        data.timer.timerReachedZero.subscribe(new IEventHandler<EventsArgs>()
        {
            @Override
            public void onAction(EventsArgs args)
            {
                setGameOverFlag();
            }
        });
    }

    @Override
    public void onEnter(Screen from)
    {
        super.onEnter(from);
        common.assets.musics.timeAttack.getInstance().play();
    }

    @Override
    public void onLeave(Screen to)
    {
        super.onLeave(to);
        common.assets.musics.timeAttack.getInstance().stop();
    }

    @Override
    protected void onReset()
    {
        gameInstances.gameOverMessage.setText(GAME_OVER_MESSAGE);
    }

    @Override
    protected void onStart()
    {
        data.timer.resume();
    }

    @Override
    protected void onTargetCollision(TargetCollisionArgs args)
    {
        if (args.target instanceof Crystal)
        {
            gameInstances.gameLabelContainer.showNormal(LABEL_CRYSTAL, 0.3f,
                    args.target.getX(), args.target.getY(),
                    BaseGameHUD.SCORE_TEXT_X, BaseGameHUD.SCORE_TEXT_Y,
                    increaseScoreRunnable);
        }
        else if (args.target instanceof Spike)
        {
            gameInstances.gameLabelContainer.showFlashing(LABEL_SPIKE, 0.25f,
                    args.target.getX(), args.target.getY(),
                    TimeAttackHUD.SECONDS_X, TimeAttackHUD.SECONDS_Y,
                    decreaseTimeRunnable, Global.Colors.LABEL_BAD);

            gameInstances.cameraShaker.shake(2);
        }
    }

    @Override
    protected void onUpdate(GameTime gameTime)
    {

    }

    @Override
    protected void onDispose()
    {

    }
}
