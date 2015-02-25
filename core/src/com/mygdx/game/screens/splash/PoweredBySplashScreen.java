package com.mygdx.game.screens.splash;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.GameTime;
import com.engine.actors.ActorOrigin;
import com.engine.actors.DistanceFieldFontActor;
import com.engine.actors.TextureRegionActor;
import com.engine.graphics.GraphicsSettings;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.screens.Screen;
import com.engine.utilities.ColorUtilities;
import com.mygdx.game.Common;
import com.mygdx.game.Global;
import com.mygdx.game.helpers.ConfiguredAction;
import com.mygdx.game.screens.OverlayedScreen;
import com.engine.actors.Actions;

public class PoweredBySplashScreen extends OverlayedScreen
{
    private static final float SHOW_DELAY = 1.000f;
    private static final float SHOW_HIDE_DURATION = 0.500f;
    private static final float MIN_SPLASH_TIME = 2f;
    private static final float TEXT_SCALE = 0.55f;

    private static final float TEXT_OFFSET_X = -60f;
    private static final float TEXT_OFFSET_Y = -30f;

    private static final float HIDDEN_OFFSET_X = 100f;
    private static final float HIDDEN_OFFSET_Y = 0f;

    private Common common;
    private DistanceFieldRenderer distanceFieldRenderer;
    private DistanceFieldFont distanceFieldFont;

    private float x;
    private float y;
    private boolean finishedAnimation;

    private DistanceFieldFontActor textActor;
    private TextureRegionActor logoActor;

    private ConfiguredAction textConfiguredAction;
    private ConfiguredAction logoConfiguredAction;

    private AfterAction after;
    private SequenceAction sequence;
    private DelayAction delay;
    private RunnableAction runnable;

    private Runnable animationFinishedRunnable;

    public PoweredBySplashScreen(GraphicsSettings graphicsSettings,
                                 Viewport viewport2D, Viewport viewport3D,
                                 Batch batch, Common common)
    {
        super(Global.ScreenNames.POWERED_BY_SCREEN, graphicsSettings,
                viewport2D, viewport3D, batch);

        this.common = common;

        distanceFieldRenderer = common.distanceFieldRenderer;
        distanceFieldFont = common.assets.distanceFieldFonts.furore.getInstance();

        x = graphicsSettings.virtualWidth / 2f;
        y = graphicsSettings.virtualHeight / 2f;

        finishedAnimation = false;
    }

    @Override
    public void initialize()
    {
        textActor = new DistanceFieldFontActor("POWERED BY");
        logoActor = new TextureRegionActor(common.assets.atlasRegions.libGdx.getInstance());

        textActor.setActorOrigin(ActorOrigin.Center);
        logoActor.setActorOrigin(ActorOrigin.Center);

        textActor.setFontBaseScale(TEXT_SCALE);

        textConfiguredAction = new ConfiguredAction();
        logoConfiguredAction = new ConfiguredAction();

        after = new AfterAction();
        sequence = new SequenceAction();
        delay = new DelayAction();
        runnable = new RunnableAction();

        animationFinishedRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                PoweredBySplashScreen.this.finishedAnimation = true;
            }
        };
    }

    @Override
    public void onEnter(Screen from)
    {
        distanceFieldFont.setColor(
                ColorUtilities.intToFloat(150),
                ColorUtilities.intToFloat(150),
                ColorUtilities.intToFloat(150),
                ColorUtilities.intToFloat(255));

        textActor.getColor().a = 0f;
        textActor.setPosition(x + TEXT_OFFSET_X - HIDDEN_OFFSET_X, y + TEXT_OFFSET_Y - HIDDEN_OFFSET_Y);

        textActor.clearActions();
        textActor.addAction(textConfiguredAction.show(SHOW_DELAY, SHOW_HIDE_DURATION, x + TEXT_OFFSET_X, y + TEXT_OFFSET_Y));

        logoActor.getColor().a = 0f;
        logoActor.setPosition(x + HIDDEN_OFFSET_X, y + HIDDEN_OFFSET_Y);

        logoActor.clearActions();
        logoActor.addAction(logoConfiguredAction.show(SHOW_DELAY, SHOW_HIDE_DURATION, x, y));
        logoActor.addAction(
                Actions.after(after,
                        Actions.sequence(sequence,
                                Actions.delay(delay, MIN_SPLASH_TIME),
                                Actions.run(runnable, animationFinishedRunnable))));
    }

    @Override
    public void update(GameTime gameTime)
    {
        textActor.act(gameTime.delta);
        logoActor.act(gameTime.delta);

        if (finishedAnimation)
        {
            screenManager.transitionTo(Global.ScreenNames.MAIN_MENU_SCREEN);
            finishedAnimation = false;
        }
    }

    @Override
    public void draw(GameTime gameTime)
    {
        // Reinicio del color ya que al parecer hay un bug en libgdx en el
        // que no se recupera el alpha original durante la ejecución de un
        // AlphaAction en un Actor
        ColorUtilities.resetColor(batch);

        common.shaders.textShader.setForegroundColor(getTransitionForeColor());
        distanceFieldRenderer.begin(distanceFieldFont, TEXT_SCALE);
        textActor.draw(distanceFieldRenderer, distanceFieldFont);
        distanceFieldRenderer.end();

        common.shaders.defaultShader.setForegroundColor(getTransitionForeColor());
        batch.begin();
        logoActor.draw(batch, 1f);
        batch.end();
    }
}
