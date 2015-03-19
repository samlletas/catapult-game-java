package com.mygdx.game.screens.gameplay.modes.timeattack;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.engine.GameTime;
import com.engine.actors.ActorOrigin;
import com.engine.actors.DistanceFieldFontActor;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.text.IntegerSequence;
import com.mygdx.game.Common;
import com.mygdx.game.screens.gameplay.BaseGameHUD;

public class TimeAttackHUD extends BaseGameHUD
{
    private static final float SECONDS_TEXT_SCALE = 1.15f;
    private static final float CENTISECONDS_TEXT_SCALE = 0.67f;

    private static final float SECONDS_X = 420f;
    private static final float SECONDS_Y = 35f;

    private static final float CENTISECONDS_X = 465f;
    private static final float CENTISECONDS_Y = 43f;

    private TimeAttackData data;

    private IntegerSequence secondsSequence;
    private IntegerSequence centisecondsSequence;

    private DistanceFieldFontActor secondsActor;
    private DistanceFieldFontActor centisecondsActor;

    public TimeAttackHUD(Common common, TimeAttackData timeAttackData)
    {
        super(common, timeAttackData);

        data = timeAttackData;

        secondsSequence = new IntegerSequence();
        centisecondsSequence = new IntegerSequence();

        secondsActor = new DistanceFieldFontActor(secondsSequence);
        centisecondsActor = new DistanceFieldFontActor(centisecondsSequence);

        secondsActor.setFontBaseScale(SECONDS_TEXT_SCALE);
        centisecondsActor.setFontBaseScale(CENTISECONDS_TEXT_SCALE);

        secondsActor.setActorOrigin(ActorOrigin.Center);
        centisecondsActor.setActorOrigin(ActorOrigin.Center);

        secondsActor.setPosition(SECONDS_X, SECONDS_Y);
        centisecondsActor.setPosition(CENTISECONDS_X, CENTISECONDS_Y);
    }

    @Override
    protected void onReset()
    {

    }

    @Override
    protected void onUpdate(GameTime gameTime)
    {

    }

    @Override
    protected void onDrawTextures(Batch batch)
    {

    }

    @Override
    protected void onDrawText(DistanceFieldRenderer renderer, DistanceFieldFont font)
    {
        int seconds = data.timer.remainingSeconds();
        int centiseconds = data.timer.remainingCentiseconds() % 100;

        secondsSequence.set(seconds);
        centisecondsSequence.set(centiseconds);

        if (seconds < 10)
        {
            secondsSequence.offset(1, '0');
        }

        if (centiseconds < 10)
        {
            centisecondsSequence.offset(1, '0');
        }

        secondsActor.draw(renderer, font);
        centisecondsActor.draw(renderer, font);
    }
}
