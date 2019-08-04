package com.gamesbykevin.slide.screen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.rumble.Rumble;
import com.gamesbykevin.slide.textures.Textures;

import static com.gamesbykevin.slide.MyGdxGame.getTextures;

public abstract class LevelScreen extends TemplateScreen {

    //current level
    private Level level;

    //keep track to maintain fps
    private long start;

    //how fast do we scroll?
    private int scrollSpeed;

    //how big is the go back button
    public static final int GO_BACK_SIZE = 36;
    public static final int GO_BACK_X = 0;
    public static final int GO_BACK_Y = 0;

    //zoom in when level solved
    private float zoomRate = ZOOM_DEFAULT;

    //how fast do we zoom in
    public static final float ZOOM_RATE = .02f;

    //the closest we can get
    public static final float ZOOM_MIN = 0.05f;

    //the farthest we can get
    public static final float ZOOM_MAX = 1.0f;

    //do we zoom in?
    private boolean zoom = true;

    //normal zoom
    public static final float ZOOM_DEFAULT = 1f;

    public LevelScreen(MyGdxGame game) {
        super(game);

        //how fast does the background move...
        setScrollSpeed((int)(SCREEN_HEIGHT * .01));
    }

    public void resetCameraOrigin() {
        //position camera back to origin
        getCamera().position.x = (SCREEN_WIDTH / 2);
        getCamera().position.y = (SCREEN_HEIGHT / 2);
    }

    public void setScrollSpeed(final int scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    public int getScrollSpeed() {
        return this.scrollSpeed;
    }

    public long getStart() {
        return this.start;
    }

    public Level getLevel() {
        return this.level;
    }

    protected void setLevel(Level level) {
        this.level = level;
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

    @Override
    public void show() {
        super.show();

        Sprite sprite = getTextures().getSprite(Textures.Key.GoBackMenu);

        if (sprite != null) {
            sprite.setPosition(GO_BACK_X, GO_BACK_Y);
            sprite.setSize(GO_BACK_SIZE, GO_BACK_SIZE);
        }

        //make sure camera is in the correct location
        resetCameraOrigin();
    }

    @Override
    public void render(float delta) {

        //get the current start time
        this.start = System.currentTimeMillis();

        //call parent
        super.render(delta);

        //draw a few backgrounds so it appears continuous
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                getBatch().draw(
                    getBackgroundImage(),
                    getBackgroundRect().x - (getBackgroundRect().width * x),
                    getBackgroundRect().y - (getBackgroundRect().height * y),
                    getBackgroundRect().width,
                    getBackgroundRect().height
                );
            }
        }

        //move the background
        getBackgroundRect().y += getScrollSpeed();

        //keep the background in bounds
        if (getBackgroundRect().y >= SCREEN_HEIGHT)
            getBackgroundRect().y = 0;

        //now we can shake the screen for everything else
        Rumble.tick(getCamera(), getPositionReset(), delta);

        //render the game objects
        getLevel().update();
        getLevel().render(getBatch());

        //get the go back button
        Sprite sprite = getTextures().getSprite(Textures.Key.GoBackMenu);

        //only render if it exists
        if (sprite != null) {
            sprite.draw(getBatch());
        }
    }

    public void drawGameover() {

        //reset zoom
        getCamera().zoom = ZOOM_DEFAULT;
        setZoomRate(ZOOM_DEFAULT);

        //position camera back to origin
        resetCameraOrigin();

        //make sure we capture menu input
        captureInputMenu();

        //draw an overlay
        getOverlay().draw(getBatch());

        //draw logo on screen
        getStage().getBatch().begin();
        drawLogo(getStage().getBatch());
        getStage().getBatch().end();

        //act the stage
        getStage().act();

        //draw the remaining of the stage
        getStage().draw();
    }
}