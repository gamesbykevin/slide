package com.gamesbykevin.slide.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.level.LevelHelper;
import com.gamesbykevin.slide.rumble.Rumble;
import com.gamesbykevin.slide.textures.Textures;

import java.io.IOException;

import static com.gamesbykevin.slide.level.Level.LEVEL_INDEX;
import static com.gamesbykevin.slide.level.objects.Bomb.TEXT_WIDTH;
import static com.gamesbykevin.slide.screen.GameScreenHelper.createGameover;
import static com.gamesbykevin.slide.screen.GameScreenHelper.maintainFps;
import static com.gamesbykevin.slide.level.Level.LEVEL_COMPLETE_DELAY;

public class GameScreen extends TemplateScreen {

    //how fast do we scroll?
    private int scrollSpeed;

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

    //our level
    private Level level;

    //our game textures
    private Textures textures;

    //our bitmap font to render text in our game
    private BitmapFont font;

    public GameScreen(MyGdxGame game) {
        super(game);

        //how fast does the background move...
        setScrollSpeed((int)(SCREEN_HEIGHT * .01));

        //create new font
        this.font = new BitmapFont();

        //create our font metrics for reference
        GlyphLayout layout = new GlyphLayout(getFont(), "9");

        //update the text width
        TEXT_WIDTH = layout.width;

        layout = null;

        //create our game textures
        this.textures = new Textures();
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
            this.level = LevelHelper.create(Gdx.files.internal("levels").list()[LEVEL_INDEX].name());
            this.level.reset();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        getLevel().reset();
    }

    public void setScrollSpeed(final int scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    public int getScrollSpeed() {
        return this.scrollSpeed;
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

        //draw a few backgrounds so it appears continuous
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                getBatch().draw(
                    getBackgroundImage(),
                    getBackgroundRect().x - (getBackgroundRect().width  * x),
                    getBackgroundRect().y - (getBackgroundRect().height * y),
                    getBackgroundRect().width,
                    getBackgroundRect().height
                );
            }
        }

        //now we can shake the screen for everything else
        Rumble.tick(getCamera(), Gdx.graphics.getDeltaTime());

        //render the game objects
        getLevel().update();
        getLevel().render(this, getBatch());

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

    public BitmapFont getFont() {
        return this.font;
    }

    public Level getLevel() {
        return this.level;
    }

    public Textures getTextures() {
        return textures;
    }
}