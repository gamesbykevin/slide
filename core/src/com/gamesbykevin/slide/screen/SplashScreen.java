package com.gamesbykevin.slide.screen;

import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.exception.ScreenException;

import java.util.Date;

import static com.gamesbykevin.slide.screen.ScreenHelper.SCREEN_MENU;

public class SplashScreen extends ParentScreen {

    //track how much time lapsed
    private Date time;

    //how long to display the screen
    private static final long DELAY = 1000L;

    public SplashScreen(MyGdxGame game) {
        super(game);

        //get the current time
        this.time = new Date();
    }

    @Override
    public void render(float delta) {

        //call parent
        super.render(delta);

        //draw the splash screen
        drawBackground();
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