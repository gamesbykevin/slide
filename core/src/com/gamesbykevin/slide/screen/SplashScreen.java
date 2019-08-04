package com.gamesbykevin.slide.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.exception.ScreenException;
import com.gamesbykevin.slide.audio.GameAudio;

import java.util.Date;

import static com.gamesbykevin.slide.screen.ScreenHelper.SCREEN_MENU;

public class SplashScreen extends ParentScreen {

    //track how much time lapsed
    private Date time;

    //how long to display the screen
    private static final long DELAY = 1500L;

    //our screen images
    private Texture website, loading;

    private final float websiteX, websiteY;
    private final float loadingX, loadingY;

    public SplashScreen(MyGdxGame game) {

        //call parent
        super(game);

        //create our background
        this.website = new Texture(Gdx.files.internal("website.png"));
        this.loading = new Texture(Gdx.files.internal("loading.png"));

        //position our image
        this.websiteX = (SCREEN_WIDTH / 2) - (getWebsite().getWidth() / 2);
        this.websiteY = SCREEN_HEIGHT - getWebsite().getHeight() - 150;

        //position our image
        this.loadingX = (SCREEN_WIDTH / 2) - (getLoading().getWidth() / 2);
        this.loadingY = websiteY - getLoading().getHeight() - 75;

        //get the current time
        this.time = new Date();

        //load our music/sfx
        GameAudio.load();
    }

    @Override
    public void dispose() {

        //call parent
        super.dispose();

        if (this.website != null) {
            this.website.dispose();
            this.website = null;
        }

        if (this.loading != null) {
            this.loading.dispose();
            this.loading = null;
        }
    }

    public Texture getWebsite() {
        return this.website;
    }

    public Texture getLoading() {
        return this.loading;
    }

    @Override
    public void render(float delta) {

        //call parent
        super.render(delta);

        //draw the splash screen
        drawBackground();

        //draw our images
        getBatch().draw(getWebsite(), this.websiteX, this.websiteY);
        getBatch().draw(getLoading(), this.loadingX, this.loadingY);

        //we are done rendering items
        getBatch().end();

        //when enough time has passed go to the next screen
        if (new Date().getTime() - time.getTime() >= DELAY) {

            try {

                //now we can go to our menu screen
                getGame().getScreenHelper().changeScreen(SCREEN_MENU);

            } catch (ScreenException ex) {
                ex.printStackTrace();
            }
        }
    }
}