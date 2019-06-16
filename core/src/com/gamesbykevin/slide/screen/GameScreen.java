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
    private float zoom = ZOOM_DEFAULT;

    //how fast do we zoom in
    private static final float ZOOM_RATE = .00444f;

    //normal zoom
    private static final float ZOOM_DEFAULT = 1f;

    public GameScreen(MyGdxGame game) {
        super(game);

        //how fast does the background move...
        setScrollSpeed((int)(SCREEN_HEIGHT * .01));
    }

    public float getZoom() {
        return this.zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public void reset() {
        this.setZoom(ZOOM_DEFAULT);
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

            //draw the game over screen
            if (getModel().getLevel().getLapsedComplete() >= LEVEL_COMPLETE_DELAY) {
                drawGameover();
            } else {

                //continue to zoom in
                setZoom(getZoom() - ZOOM_RATE);

                //we don't want to zoom in too far
                if (getZoom() < .05f)
                    setZoom(.05f);

                //zoom in on camera
                getCamera().zoom = getZoom();
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
        setZoom(ZOOM_DEFAULT);

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