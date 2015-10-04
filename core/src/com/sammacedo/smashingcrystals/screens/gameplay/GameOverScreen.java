package com.sammacedo.smashingcrystals.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.GameTime;
import com.engine.actors.*;
import com.engine.events.EventsArgs;
import com.engine.events.IEventHandler;
import com.engine.graphics.GraphicsSettings;
import com.engine.graphics.graphics2D.text.DistanceFieldFont;
import com.engine.graphics.graphics2D.text.DistanceFieldRenderer;
import com.engine.input.BackInputProcessor;
import com.engine.screens.Overlay;
import com.engine.screens.Screen;
import com.engine.text.IntegerSequence;
import com.engine.utilities.ColorUtilities;
import com.engine.utilities.StageUtilities;
import com.engine.utilities.Timer;
import com.sammacedo.smashingcrystals.Common;
import com.sammacedo.smashingcrystals.Global;
import com.sammacedo.smashingcrystals.gamelogic.scene.Background;
import com.sammacedo.smashingcrystals.screens.OverlayedScreen;
import com.sammacedo.smashingcrystals.screens.ui.GameButton;
import com.sammacedo.smashingcrystals.screens.ui.GameSlider;
import com.sammacedo.smashingcrystals.screens.ui.Header;
import com.sammacedo.smashingcrystals.screens.ui.TextBlock;

public class GameOverScreen extends OverlayedScreen
{
    private final static float SCORE_COUNT_DURATION = 60f;
    private final static int MAX_INCREMENTS = 25;

    private final Common common;
    private final DistanceFieldRenderer distanceFieldRenderer;
    private final DistanceFieldFont distanceFieldFont;

    private Timer delayTimer;
    private Timer scoreTimer;
    private float scoreProgress;
    private float scoreOffset;
    private boolean finishedCount;

    private Stage stage;
    private Overlay overlay;
    private Header header;

    private IntegerSequence scoreSequence;
    private IntegerSequence bestSequence;

    private TextBlock scoreBlock;
    private TextBlock bestBlock;
    private GameButton homeButton;
    private GameButton replayButton;
    private GameButton scoresButton;

    private DistanceFieldFontActor newBestActor;
    private NewBestActionContainer newBestActionContainer;
    private Color newBestColor = ColorUtilities.createColor(255, 60, 60, 255);

    private InputMultiplexer inputMultiplexer;
    private BaseGameplayScreen gameplayScreen;

    public GameOverScreen(GraphicsSettings graphicsSettings, Viewport viewport2D,
                          Viewport viewport3D, Batch batch, Common common)
    {
        super(Global.ScreenNames.GAME_OVER, graphicsSettings, viewport2D,
                viewport3D, batch);

        this.common = common;
        this.distanceFieldRenderer = common.distanceFieldRenderer;
        this.distanceFieldFont = common.assets.distanceFieldFonts.furore.getInstance();

        this.delayTimer = new Timer(500);
        this.scoreTimer = new Timer(0);
    }

    @Override
    public void initialize()
    {
        stage = new Stage(viewport2D, batch);
        overlay = new Overlay(graphicsSettings, Global.Colors.OVERLAY, Global.OVERLAY_ALPHA);
        header = new Header(common, graphicsSettings, "GAME OVER");

        scoreSequence = new IntegerSequence();
        bestSequence = new IntegerSequence();

        scoreSequence.set(0);
        bestSequence.set(0);

        scoreBlock = new TextBlock(common, "SCORE", scoreSequence, 220f, false);
        bestBlock = new TextBlock(common, "BEST", bestSequence, 220f, true);
        homeButton = new GameButton(common, Global.ButtonStyles.HOME_SMALL);
        replayButton = new GameButton(common, Global.ButtonStyles.REPLAY);
        scoresButton = new GameButton(common, Global.ButtonStyles.SCORES_SMALL);

        newBestActor = new DistanceFieldFontActor("NEW");
        newBestActionContainer = new NewBestActionContainer();

        scoreBlock.setOriginalPosition(200f, 185f);
        bestBlock.setOriginalPosition(455f, 185f);
        homeButton.setOriginalPosition(graphicsSettings.virtualWidth / 2f - 150f, 325f);
        replayButton.setOriginalPosition(graphicsSettings.virtualWidth / 2f, 354f);
        scoresButton.setOriginalPosition(graphicsSettings.virtualWidth / 2f + 150f, 325f);

        scoreBlock.setTextOrigin(ActorOrigin.CenterRight);
        bestBlock.setTextOrigin(ActorOrigin.CenterLeft);
        newBestActor.setActorOrigin(ActorOrigin.Center);

        scoreBlock.setTitleFontScale(0.75f);
        scoreBlock.setTextFontScale(0.65f);

        bestBlock.setTitleFontScale(0.75f);
        bestBlock.setTextFontScale(0.65f);
        newBestActor.setFontBaseScale(0.55f);

        scoreBlock.setTitleOffset(95f, -2f);
        scoreBlock.setTextOffset(177f, 40f);
        bestBlock.setTitleOffset(10f, -2f);
        bestBlock.setTextOffset(10f, 40f);

        header.setShowDelay(0.200f);
        scoreBlock.setShowDelay(0.200f);
        bestBlock.setShowDelay(0.200f);
        homeButton.setShowDelay(0f);
        replayButton.setShowDelay(0.100f);
        scoresButton.setShowDelay(0f);

        header.setHideDelay(0f);
        scoreBlock.setHideDelay(0f);
        bestBlock.setHideDelay(0f);
        homeButton.setHideDelay(0.100f);
        replayButton.setHideDelay(0f);
        scoresButton.setHideDelay(0.100f);

        homeButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                StageUtilities.disableTouch(stage);
                screenManager.transitionTo(Global.ScreenNames.MAIN_MENU);
            }
        });

        replayButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                StageUtilities.disableTouch(stage);
                screenManager.transitionTo(gameplayScreen);
            }
        });

        scoresButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {

            }
        });

        stage.addActor(homeButton);
        stage.addActor(replayButton);
        stage.addActor(scoresButton);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new BackInputProcessor()
        {
            @Override
            protected void onBackDown()
            {
                if (finishedCount)
                {
                    StageUtilities.disableTouch(stage);
                    screenManager.transitionTo(Global.ScreenNames.MAIN_MENU);
                }
            }
        });
        inputMultiplexer.addProcessor(stage);

        delayTimer.timerReachedZero.subscribe(new IEventHandler<EventsArgs>()
        {
            @Override
            public void onAction(EventsArgs args)
            {
                startCount();
            }
        });

        scoreTimer.timerReachedZero.subscribe(new IEventHandler<EventsArgs>()
        {
            @Override
            public void onAction(EventsArgs args)
            {
                updateCount();
            }
        });
    }

    public void setGameplayScreen(BaseGameplayScreen gameplayScreen)
    {
        this.gameplayScreen = gameplayScreen;
    }

    @Override
    public void onEnter(Screen from)
    {
        header.show();
        scoreBlock.show();
        bestBlock.show();
        newBestActor.setVisible(false);

        homeButton.hideImmediately();
        replayButton.hideImmediately();
        scoresButton.hideImmediately();

        stage.unfocusAll();
        common.background.setMode(Background.Mode.Menus);

        prepareForScoreCount();
    }

    private void prepareForScoreCount()
    {
        int score = gameplayScreen.getGameplayData().getScore();
        int best = gameplayScreen.getGameplayData().getBest();

        scoreSequence.set(0);
        bestSequence.set(best);

        delayTimer.restart();

        scoreProgress = 0f;

        if (score <= MAX_INCREMENTS)
        {
            scoreOffset = 1f;
        }
        else
        {
            scoreOffset = (float)score / (float)MAX_INCREMENTS;
        }
    }

    @Override
    public void onTransitionFromFinish(Screen from)
    {
        enableInput();
    }

    @Override
    public void onTransitionToStart(Screen to)
    {
        // Deshabilitación del input
        Gdx.input.setInputProcessor(null);

        header.hide();
        scoreBlock.hide();
        bestBlock.hide();
        homeButton.hide();
        replayButton.hide();
        scoresButton.hide();

        finishedCount = false;

        if (newBestActor.isVisible())
        {
            newBestActor.addAction(newBestActionContainer.getHideAction());
        }
    }

    private void enableInput()
    {
        Gdx.input.setInputProcessor(inputMultiplexer);
        StageUtilities.enableTouch(stage);
    }

    @Override
    public void update(GameTime gameTime)
    {
        common.background.update(gameTime);
        common.grass.update(gameTime);
        stage.act(gameTime.delta);
        header.update(gameTime);
        scoreBlock.update(gameTime);
        bestBlock.update(gameTime);
        newBestActor.act(gameTime.delta);

        delayTimer.update(gameTime);
        scoreTimer.update(gameTime);
    }

    private void startCount()
    {
        scoreTimer.restart(SCORE_COUNT_DURATION);
    }

    private void updateCount()
    {
        int score = gameplayScreen.getGameplayData().getScore();
        scoreProgress = MathUtils.clamp(scoreProgress + scoreOffset, 0, score);

        if (scoreProgress == score)
        {
            homeButton.show();
            replayButton.show();
            scoresButton.show();

            finishedCount = true;

            if (score > gameplayScreen.getGameplayData().getBest())
            {
                showNewRecord();
                bestSequence.set(score);

                gameplayScreen.getGameplayData().saveBest(score);
                common.settings.save();
            }

            enableInput();
        }
        else
        {
            scoreTimer.restart();
        }

        scoreSequence.set((int)scoreProgress);

        if (score > 0)
        {
            common.soundPlayer.play(common.soundPlayer.count);
        }
    }

    private void showNewRecord()
    {
        newBestActor.setVisible(true);
        newBestActor.setColor(Color.WHITE);
        newBestActor.getColor().a = 0f;
        newBestActor.setScale(4.5f, 4.5f);
        newBestActor.setPosition(442f, 190f);

        newBestActor.clearActions();
        newBestActor.addAction(newBestActionContainer.getShowAction());

        common.soundPlayer.play(common.soundPlayer.success);
    }

    @Override
    public void draw(GameTime gameTime)
    {
//        // Reinicio del color ya que al parecer hay un bug en libgdx en el
//        // que no se recupera el alpha original durante la ejecución de un
//        // AlphaAction en un Actor
        ColorUtilities.resetColor(batch);

        common.shaders.defaultShader.setForegroundColor(getTransitionForeColor(overlay));
        batch.begin();
        common.background.draw(batch);
        common.grass.draw(batch);
        batch.end();

        common.shaders.defaultShader.setForegroundColor(getTransitionForeColor());
        stage.draw();

        batch.begin();
        header.drawTextures(batch);
        scoreBlock.drawTextures(batch);
        bestBlock.drawTextures(batch);
        batch.end();

        common.shaders.textShader.setForegroundColor(getTransitionForeColor());
        distanceFieldRenderer.begin(distanceFieldFont, GameSlider.FONT_SCALE);
        header.drawText(distanceFieldRenderer, distanceFieldFont);
        scoreBlock.drawText(distanceFieldRenderer, distanceFieldFont);
        bestBlock.drawText(distanceFieldRenderer, distanceFieldFont);
        newBestActor.draw(distanceFieldRenderer, distanceFieldFont);
        distanceFieldRenderer.end();
    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }

    class NewBestActionContainer
    {
        Action getShowAction()
        {
            return Actions.parallel(
                    Actions.repeat(RepeatAction.FOREVER,
                            Actions.sequence(
                                    Actions.color(newBestColor, 0.2f, Interpolation.sine),
                                    Actions.color(Color.WHITE, 0.2f, Interpolation.sine))),
                    Actions.parallel(
                            Actions.fadeIn(0.3f, Interpolation.pow4Out),
                            Actions.scaleTo(1f, 1f, 0.3f, Interpolation.pow4Out)));
        }

        public Action getHideAction()
        {
            return Actions.sequence(
                    Actions.delay(0.025f),
                    Actions.parallel(
                            Actions.moveBy(200f, 0f, 0.5f, Interpolation.exp5),
                            Actions.fadeOut(0.5f, Interpolation.linear)));
        }
    }
}
