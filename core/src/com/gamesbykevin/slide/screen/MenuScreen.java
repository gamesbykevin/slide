package com.gamesbykevin.slide.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.audio.GameAudio;
import com.gamesbykevin.slide.exception.ScreenException;

import static com.gamesbykevin.slide.MyGdxGame.getMyBundle;
import static com.gamesbykevin.slide.preferences.AppPreferences.MAX_LEVEL_SAVE;
import static com.gamesbykevin.slide.preferences.AppPreferences.hasLevelSave;
import static com.gamesbykevin.slide.screen.ScreenHelper.*;

public class MenuScreen extends TemplateScreen {

    public static final String URL_MORE = "http://gamesbykevin.com";
    public static final String URL_RATE = "https://play.google.com/store/apps/details?id=com.gamesbykevin.slide";

    public MenuScreen(MyGdxGame game) {

        //call parent
        super(game);
    }

    @Override
    public void show() {

        //call parent
        super.show();

        //if session is not active, then we need to login
        if (!getGame().getGsClient().isSessionActive())
            getGame().getGsClient().logIn();

        //free our resources
        MyGdxGame.resetTextures();

        //capture the menu input
        captureInputMenu();

        //Create Table
        Table table = new Table();

        //Set table to fill stage
        table.setFillParent(true);

        //Set alignment of contents in the table.
        table.center();

        //Create buttons
        TextButton buttonPlay = new TextButton(getMyBundle().get("menuScreenPlay"), getSkin());
        TextButton buttonPlayCustom = new TextButton(getMyBundle().get("menuScreenPlayCustom"), getSkin());
        TextButton buttonCreate = new TextButton(getMyBundle().get("menuScreenCreate"), getSkin());
        TextButton buttonOptions = new TextButton(getMyBundle().get("menuScreenOptions"), getSkin());
        TextButton buttonRate = new TextButton(getMyBundle().get("menuScreenRate"), getSkin());
        TextButton buttonMore = new TextButton(getMyBundle().get("menuScreenMore"), getSkin());
        TextButton buttonExit = new TextButton(getMyBundle().get("menuScreenExit"), getSkin());

        //Add listeners to buttons
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {

                    //stop all sound
                    GameAudio.stop();

                    //now switch screens
                    getGame().getScreenHelper().changeScreen(SCREEN_SELECT_LEVEL);

                } catch (ScreenException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonPlayCustom.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    getGame().getScreenHelper().changeScreen(SCREEN_SELECT_LEVEL_CUSTOM);
                } catch (ScreenException ex) {
                    ex.printStackTrace();
                }
            }
        });

        //Add listeners to buttons
        buttonCreate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    getGame().getScreenHelper().changeScreen(SCREEN_SELECT_CREATE);
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
                MyGdxGame.exit(getGame());
            }
        });

        boolean hasCustom = false;

        //if we don't have any levels created we shouldn't be able to play them
        for (int index = 0; index < MAX_LEVEL_SAVE; index++) {

            if (hasLevelSave(index)) {
                hasCustom = true;
                break;
            }
        }

        //Add buttons to table
        table.add(buttonPlay).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
        table.row();

        if (hasCustom) {
            table.add(buttonPlayCustom).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
            table.row();
        }

        table.add(buttonCreate).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
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
        getStage().clear();
        getStage().addActor(table);

        //add our social media icons
        super.addSocialIcons();

        //play music
        GameAudio.playMusic(GameAudio.SoundMusic.Menu, true);
    }

    @Override
    public void resume() {
        super.resume();

        //play music
        GameAudio.playMusic(GameAudio.SoundMusic.Menu, true);
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
        drawLogo(getStage().getBatch());
        getStage().getBatch().end();

        //draw the remaining of the stage
        getStage().draw();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}