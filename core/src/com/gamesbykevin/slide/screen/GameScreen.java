package com.gamesbykevin.slide.screen;

import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.model.GameModel;

import static com.gamesbykevin.slide.screen.GameScreenHelper.createGameover;
import static com.gamesbykevin.slide.screen.GameScreenHelper.maintainFps;
import static com.gamesbykevin.slide.level.Level.LEVEL_COMPLETE_DELAY;

public class GameScreen extends TemplateScreen {

    //how fast do we scroll?
    private int scrollSpeed;

    //keep reference to the model for our game
    private GameModel model;

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

        //how fast does the background move...
        setScrollSpeed((int)(SCREEN_HEIGHT * .01));
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
        this.model = new GameModel(getGame());
    }

    public void setScrollSpeed(final int scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    public int getScrollSpeed() {
        return this.scrollSpeed;
    }

    public GameModel getModel() {
        return this.model;
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
    public void render(float delta) {

        //get the current start time
        long start = System.currentTimeMillis();

        //call parent
        super.render(delta);

        //draw background twice for endless scrolling
        drawBackground();

        //draw background again so it is continuous
        getBatch().draw(getBackgroundImage(), getBackgroundRect().x, getBackgroundRect().y - getBackgroundRect().height, getBackgroundRect().width, getBackgroundRect().height);

        //render the game objects
        getModel().update();
        getModel().render(getBatch());

        if (getModel().getLevel().isSolved()) {

            if (getModel().getLevel().getLapsedComplete() >= LEVEL_COMPLETE_DELAY) {

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

        } else {

            //move the background
            getBackgroundRect().y += getScrollSpeed();

            //keep the background in bounds
            if (getBackgroundRect().y >= SCREEN_HEIGHT)
                getBackgroundRect().y = 0;
        }

        //finished rendering
        getBatch().end();

        //keep the speed of the game steady
        maintainFps(start);
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

    @Override
    public void pause() {
        getGame().setPaused(true);
    }
}