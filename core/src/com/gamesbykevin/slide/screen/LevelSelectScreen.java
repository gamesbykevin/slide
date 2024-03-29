package com.gamesbykevin.slide.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.audio.GameAudio;
import com.gamesbykevin.slide.exception.ScreenException;
import com.gamesbykevin.slide.level.Level;

import static com.gamesbykevin.slide.MyGdxGame.getMyBundle;
import static com.gamesbykevin.slide.level.LevelHelper.LEVEL_COUNT;
import static com.gamesbykevin.slide.preferences.AppPreferences.hasLevelCompleted;
import static com.gamesbykevin.slide.screen.ScreenHelper.SCREEN_GAME;

public class LevelSelectScreen extends CustomSelectScreen {

    //how big is each button
    private static final float BUTTON_SIZE = 80f;

    //how many columns per row
    private static final int COLUMNS = 5;

    //provide some space between each selection
    private static final int PADDING = 10;

    public LevelSelectScreen(MyGdxGame game) {
        super(game, false);
        super.setButtonSize(BUTTON_SIZE);
        super.setColumns(COLUMNS);
        super.setPadding(PADDING);
        super.setTotal(LEVEL_COUNT);
    }

    @Override
    public void show() {
        super.show();
        getScroll().layout();
        getScroll().setScrollY(getScrollY());

        //play music
        GameAudio.playMusic(GameAudio.SoundMusic.Theme, false);
    }

    @Override
    public void resume() {
        super.resume();

        //play music
        GameAudio.playMusic(GameAudio.SoundMusic.Theme, false);
    }

    @Override
    public void handleClick(int index) {

        try {

            //remember our position
            setScrollY(getScroll().getScrollY());

            //assign the selected level
            Level.LEVEL_INDEX = index;

            //we are playing the games levels
            GameScreen.CUSTOM_LEVEL = false;

            //switch to the game screen
            getGame().setPaused(false);
            getGame().getScreenHelper().changeScreen(SCREEN_GAME);

        } catch (ScreenException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getButtonText(int index) {

        if (hasLevelCompleted(index)) {
            return getMyBundle().get("levelSelectScreenSolved");
        } else {
            return (index + 1) + "";
        }
    }

    @Override
    public void setButtonTextFontSize(TextButton button, int index) {
        //do anything here?
    }

    @Override
    public String getTitleText() {
        return getMyBundle().get("levelSelectScreenTitleText");
    }
}