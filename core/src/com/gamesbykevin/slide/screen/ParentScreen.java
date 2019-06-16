package com.gamesbykevin.slide.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.gamesbykevin.slide.MyGdxGame;

public abstract class ParentScreen implements Screen {

    //screen size
    public static final int SCREEN_WIDTH = 480;
    public static final int SCREEN_HEIGHT = 800;

    //game object for reference
    private final MyGdxGame game;

    //texture for a single black pixel used for our overlay
    private Texture pixelMapTexture;

    //our background image
    private Texture backgroundImage;

    //how to draw our background
    private Rectangle backgroundRect;

    //font to draw text on screen
    private BitmapFont bitmapFont;

    //what to display when paused
    private String pauseText = "Paused";

    //where do we render the text
    private final float pauseX, pauseY;

    //font metrics
    private GlyphLayout glyphLayout;

    //render all objects simultaneously
    private SpriteBatch batch;

    //camera perspective to project etc... images
    private OrthographicCamera camera;

    //how visible is our overlay
    private static final float OVERLAY_TRANSPARENCY = .45f;

    public ParentScreen(MyGdxGame game) {

        //create a new sprite batch
        this.batch = new SpriteBatch();

        //store game reference
        this.game = game;

        //create our camera of specified size
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        //create our background
        this.backgroundImage = new Texture(Gdx.files.internal("background.jpg"));

        //our background will cover the screen
        this.backgroundRect = new Rectangle();
        this.backgroundRect.width = SCREEN_WIDTH;
        this.backgroundRect.height = SCREEN_HEIGHT;
        this.backgroundRect.x = 0;
        this.backgroundRect.y = 0;

        //create a single pixel
        Pixmap pixmap = new Pixmap( 1, 1, Pixmap.Format.RGBA8888 );
        pixmap.setColor( 0, 0, 0, OVERLAY_TRANSPARENCY);
        pixmap.fillRectangle(0, 0, 1, 1);

        //create new texture referencing the pixel
        this.pixelMapTexture = new Texture( pixmap );

        //we no longer need this
        pixmap.dispose();
        pixmap = null;

        //create our bitmap font object
        this.bitmapFont = new BitmapFont();

        //font will be white
        this.bitmapFont.setColor(1, 1, 1, 1);

        //change font size
        this.bitmapFont.getData().setScale(1.5f);

        //create our layout so we can calculate the font metrics
        this.glyphLayout = new GlyphLayout(this.bitmapFont, pauseText);

        //reference the font metrics so we can place our text in the middle of the screen
        this.pauseX = (SCREEN_WIDTH / 2) - (glyphLayout.width / 2);
        this.pauseY = (SCREEN_HEIGHT / 2) - (glyphLayout.height / 2);
    }

    public Texture getBackgroundImage() {
        return this.backgroundImage;
    }

    public Rectangle getBackgroundRect() {
        return this.backgroundRect;
    }

    public SpriteBatch getBatch() {
        return this.batch;
    }

    public MyGdxGame getGame() {
        return this.game;
    }

    public void clearScreen() {

        //clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void hide() {
        //do anything here?
    }

    @Override
    public void resize(int w, int h) {
        //do anything here?
    }

    @Override
    public void show() {
        //do anything here?
    }

    @Override
    public void pause() {
        //do anything here?
    }

    @Override
    public void resume() {
        //do anything here?
    }

    @Override
    public void render(float delta) {

        //clear the screen
        clearScreen();

        //good practice to update the camera at least once per frame
        getCamera().update();

        //set the screen projection coordinates
        getBatch().setProjectionMatrix(getCamera().combined);

        //start the batch
        getBatch().begin();
    }

    public void drawOverlay() {

        //draw overlay
        getBatch().draw(this.pixelMapTexture, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        //render "paused" text
        bitmapFont.draw(getBatch(), glyphLayout, pauseX, pauseY);
    }

    public void drawBackground() {
        drawBackground(getBatch());
    }

    public void drawBackground(Batch batch) {
        batch.draw(
            getBackgroundImage(),
            getBackgroundRect().x,
            getBackgroundRect().y,
            getBackgroundRect().width,
            getBackgroundRect().height
        );
    }

    @Override
    public void dispose() {

        if (bitmapFont != null)
            bitmapFont.dispose();
        if (batch != null)
            batch.dispose();
        if (backgroundImage != null)
            backgroundImage.dispose();

        this.backgroundRect = null;
        this.backgroundImage = null;
        this.batch = null;
        this.bitmapFont = null;
        this.glyphLayout = null;
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }
}