package com.gamesbykevin.slide.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.exception.ScreenException;

import static com.gamesbykevin.slide.screen.ScreenHelper.*;

public class MenuScreen extends TemplateScreen {

    public static final String URL_MORE = "http://gamesbykevin.com";
    public static final String URL_RATE = "https://play.google.com/store/apps/details?id=com.gamesbykevin.blocks";

    public MenuScreen(MyGdxGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        //Create Table
        Table table = new Table();

        //Set table to fill stage
        table.setFillParent(true);

        //Set alignment of contents in the table.
        table.center();

        //Create buttons
        TextButton buttonPlay = new TextButton("Play", getSkin());
        TextButton buttonOptions = new TextButton("Options", getSkin());
        TextButton buttonRate = new TextButton("Rate", getSkin());
        TextButton buttonMore = new TextButton("More", getSkin());
        TextButton buttonExit = new TextButton("Exit", getSkin());

        //Add listeners to buttons
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    getGame().getScreenHelper().changeScreen(SCREEN_SELECT);
                } catch (ScreenException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonOptions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    getGame().getScreenHelper().changeScreen(SCREEN_OPTIONS);
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
        getStage().addActor(table);

        //add our social media icons
        super.addSocialIcons();
    }

    @Override
    public void render(float delta) {

        //clear the screen
        super.clearScreen();

        //act the stage
        getStage().act();

        //draw background with the stage
        getStage().getBatch().begin();
        super.drawBackground(getStage().getBatch());
        getStage().getBatch().end();

        //draw the remaining of the stage
        getStage().draw();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}