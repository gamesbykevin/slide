package com.gamesbykevin.slide.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gamesbykevin.slide.exception.ScreenException;

import static com.gamesbykevin.slide.MyGdxGame.FRAME_MS;
import static com.gamesbykevin.slide.MyGdxGame.getMyBundle;
import static com.gamesbykevin.slide.screen.GameScreen.CUSTOM_LEVEL;
import static com.gamesbykevin.slide.screen.MenuScreen.URL_MORE;
import static com.gamesbykevin.slide.screen.MenuScreen.URL_RATE;
import static com.gamesbykevin.slide.screen.ScreenHelper.*;
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
        TextButton buttonSelect = new TextButton(getMyBundle().get("gameScreenHelperSelect"), screen.getSkin());
        TextButton buttonRate = new TextButton(getMyBundle().get("gameScreenHelperRate"), screen.getSkin());
        TextButton buttonMore = new TextButton(getMyBundle().get("gameScreenHelperMore"), screen.getSkin());
        TextButton buttonMenu = new TextButton(getMyBundle().get("gameScreenHelperMenu"), screen.getSkin());

        //Add listeners to buttons
        buttonSelect.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if (CUSTOM_LEVEL) {
                        screen.getGame().getScreenHelper().changeScreen(SCREEN_SELECT_LEVEL_CUSTOM);
                    } else {
                        screen.getGame().getScreenHelper().changeScreen(SCREEN_SELECT_LEVEL);
                    }
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

        buttonMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    screen.getGame().getScreenHelper().changeScreen(SCREEN_MENU);
                } catch (ScreenException ex) {
                    ex.printStackTrace();
                }
            }
        });

        //Add buttons to table
        table.add(buttonSelect).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
        table.row();
        table.add(buttonRate).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
        table.row();
        table.add(buttonMore).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
        table.row();
        table.add(buttonMenu).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);

        //Add table to stage
        screen.getStage().clear();
        screen.getStage().addActor(table);

        //add our social media icons
        screen.addSocialIcons();
    }
}