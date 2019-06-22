package com.gamesbykevin.slide.screen;

import com.badlogic.gdx.Gdx;
import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.level.LevelHelper;
import com.gamesbykevin.slide.rumble.Rumble;

import java.io.IOException;

import static com.gamesbykevin.slide.level.Level.LEVEL_INDEX;
import static com.gamesbykevin.slide.screen.GameScreenHelper.createGameover;
import static com.gamesbykevin.slide.screen.GameScreenHelper.maintainFps;
import static com.gamesbykevin.slide.level.Level.LEVEL_COMPLETE_DELAY;

public class GameScreen extends LevelScreen {

    //zoom in when level solved
    private float zoomRate = ZOOM_DEFAULT;

    //how fast do we zoom in
    private static final float ZOOM_RATE = .02f;

    //the closest we can get
    private static final float ZOOM_MIN = 0.05f;

    //the farthest we can get
    private static final float ZOOM_MAX = 1.0f;

    //do we zoom in?
    private boolean zoom = true;

    //normal zoom
    private static final float ZOOM_DEFAULT = 1f;

    public GameScreen(MyGdxGame game) {
        super(game);
    }

    public boolean isZoom() {
        return this.zoom;
    }

    public void setZoom(boolean zoom) {
        this.zoom = zoom;
    }

    public float getZoomRate() {
        return this.zoomRate;
    }

    public void setZoomRate(float zoomRate) {
        this.zoomRate = zoomRate;
    }

    public void reset() {

        this.setZoomRate(ZOOM_DEFAULT);

        try {
            //create our level
            setLevel(LevelHelper.create(Gdx.files.internal("levels").list()[LEVEL_INDEX].name()));
            getLevel().reset();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        getLevel().reset();
    }

    @Override
    public void show() {
        super.show();
        
        //set zoom to normal
        getCamera().zoom = ZOOM_DEFAULT;

        //make sure game controller has input
        getGame().getController().setInput();

        //create the game over menu
        createGameover(this);

        //if the game isn't paused reset the level
        if (!getGame().isPaused())
            reset();
    }

    @Override
    public void pause() {
        getGame().setPaused(true);
    }

    @Override
    public void render(float delta) {

        //call parent
        super.render(delta);

        if (getLevel().isSolved()) {

            if (getLevel().getLapsedComplete() >= LEVEL_COMPLETE_DELAY) {

                //draw the game over screen
                drawGameover();

            } else {

                //continue to zoom in/out the meantime
                setZoomRate(isZoom() ? getZoomRate() - ZOOM_RATE : getZoomRate() + ZOOM_RATE);

                //watch how far we zoom
                if (getZoomRate() < ZOOM_MIN) {
                    setZoomRate(ZOOM_MIN);
                    setZoom(!isZoom());
                } else if (getZoomRate() > ZOOM_MAX) {
                    setZoomRate(ZOOM_MAX);
                    setZoom(!isZoom());
                }

                //zoom in on camera
                getCamera().zoom = getZoomRate();
            }

        } else if (getGame().isPaused()) {

            //if paused show the overlay
            super.drawOverlay();

        }

        //finished rendering
        getBatch().end();

        //keep the speed of the game steady
        maintainFps(getStart());
    }

    private void drawGameover() {

        //reset zoom
        getCamera().zoom = ZOOM_DEFAULT;
        setZoomRate(ZOOM_DEFAULT);

        //make sure we capture menu input
        super.captureMenuInput();

        //draw an overlay
        super.drawOverlay();

        //act the stage
        getStage().act();

        //draw the remaining of the stage
        getStage().draw();
    }
}