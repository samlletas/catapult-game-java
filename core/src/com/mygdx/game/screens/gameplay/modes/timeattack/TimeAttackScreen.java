package com.mygdx.game.screens.gameplay.modes.timeattack;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.GameTime;
import com.engine.graphics.GraphicsSettings;
import com.mygdx.game.Common;
import com.mygdx.game.Global;
import com.mygdx.game.gamelogic.targets.Crystal;
import com.mygdx.game.gamelogic.targets.Spike;
import com.mygdx.game.gamelogic.targets.patterns.TargetCollisionArgs;
import com.mygdx.game.screens.gameplay.*;

public final class TimeAttackScreen extends BaseGameplayScreen
{
    private static final String LABEL_CRYSTAL_NORMAL = "+1";
    private static final String LABEL_SPIKE = "MISS!";

    private TimeAttackData data;
    private TimeAttackHUD hud;

    private Runnable increaseScoreRunnable;

    public TimeAttackScreen(GraphicsSettings settings, Viewport viewport2D,
                            Viewport viewport3D, Batch batch, Common common,
                            GameInstances gameInstances)
    {
        super(Global.ScreenNames.TIME_ATTACK_SCREEN, settings, viewport2D,
                viewport3D, batch, common, gameInstances);
    }

    @Override
    protected BaseGameplayData createGameplayData()
    {
        data = new TimeAttackData();
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
            }
        };
    }

    @Override
    protected void onReset()
    {

    }

    @Override
    protected void onTargetCollision(TargetCollisionArgs args)
    {
        if (args.target instanceof Crystal)
        {
            gameInstances.gameLabelContainer.show(LABEL_CRYSTAL_NORMAL,
                    args.target.getX(), args.target.getY(),
                    BaseGameHUD.SCORE_TEXT_X, BaseGameHUD.SCORE_TEXT_Y,
                    increaseScoreRunnable);
        }
        else if (args.target instanceof Spike)
        {
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
