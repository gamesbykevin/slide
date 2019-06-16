package com.gamesbykevin.slide.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gamesbykevin.slide.exception.ScreenException;

import static com.gamesbykevin.slide.MyGdxGame.FRAME_MS;
import static com.gamesbykevin.slide.screen.MenuScreen.URL_MORE;
import static com.gamesbykevin.slide.screen.MenuScreen.URL_RATE;
import static com.gamesbykevin.slide.screen.ScreenHelper.SCREEN_GAME;
import static com.gamesbykevin.slide.screen.ScreenHelper.SCREEN_OPTIONS;
import static com.gamesbykevin.slide.screen.TemplateScreen.*;

public class GameScreenHelper {


    protected static void maintainFps(long start) {

        //how long  did that take?
        long duration = (System.currentTimeMillis() - start);

        //how much time is remaining
        long remaining = (long)(FRAME_MS - duration);

        if (remaining < 1)
            remaining = 1;

        try {
            //pause so the game can run at a consistent speed
            Thread.sleep(remaining);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Create the buttons for our game over screen
     */
    protected static void createGameover(final GameScreen screen) {

        //Create Table
        Table table = new Table();

        //Set table to fill stage
        table.setFillParent(true);

        //Set alignment of contents in the table.
        table.center();

        //Create buttons
        TextButton buttonPlay = new TextButton("Play", screen.getSkin());
        TextButton buttonOptions = new TextButton("Options", screen.getSkin());
        TextButton buttonRate = new TextButton("Rate", screen.getSkin());
        TextButton buttonMore = new TextButton("More", screen.getSkin());
        TextButton buttonExit = new TextButton("Exit", screen.getSkin());

        //Add listeners to buttons
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    screen.getGame().getScreenHelper().changeScreen(SCREEN_GAME);
                } catch (ScreenException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonOptions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    screen.getGame().getScreenHelper().changeScreen(SCREEN_OPTIONS);
                } catch (ScreenException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonRate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI(URL_RATE);
            }
        });

        buttonMore.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI(URL_MORE);
            }
        });

        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                //exit the game
                Gdx.app.exit();
            }
        });

        //Add buttons to table
        table.add(buttonPlay).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
        table.row();
        table.add(buttonOptions).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
        table.row();
        table.add(buttonRate).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
        table.row();
        table.add(buttonMore).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);

        switch(Gdx.app.getType()) {

            //only the desktop application we can exit
            case Desktop:
                table.row();
                table.add(buttonExit).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
                break;
        }

        //Add table to stage
        screen.getStage().addActor(table);

        //add our social media icons
        screen.addSocialIcons();
    }
}