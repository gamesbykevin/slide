package com.gamesbykevin.slide.screen;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.audio.GameAudio;
import com.gamesbykevin.slide.exception.ScreenException;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.preferences.AppPreferences;

import static com.gamesbykevin.slide.MyGdxGame.getMyBundle;

public class LevelSelectCustomScreen extends CustomSelectScreen {

    //how big is each button
    private static final float BUTTON_SIZE = 150f;

    //how many columns per row
    private static final int COLUMNS = 2;

    //provide some space between each selection
    private static final int PADDING = 40;

    public LevelSelectCustomScreen(MyGdxGame game) {
        super(game, true);
        super.setButtonSize(BUTTON_SIZE);
        super.setColumns(COLUMNS);
        super.setPadding(PADDING);
        super.setTotal(AppPreferences.MAX_LEVEL_SAVE);
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

            //if we don't have a level saved, don't do anything
            if (!AppPreferences.hasLevelSave(index))
                return;

            //remember our position
            setScrollY(getScroll().getScrollY());

            //assign the selected level
            Level.LEVEL_INDEX = index;

            //we are loading a custom level
            GameScreen.CUSTOM_LEVEL = true;

            //switch to the game screen
            getGame().setPaused(false);
            getGame().getScreenHelper().changeScreen(ScreenHelper.SCREEN_GAME);

        } catch (ScreenException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getButtonText(int index) {

        if (AppPreferences.hasLevelSave(index)) {
            return (index + 1) + "";
        } else {
            return "";
        }
    }

    @Override
    public void setButtonTextFontSize(TextButton button, int index) {

        if (AppPreferences.hasLevelSave(index)) {
            button.getLabel().setFontScale(2.0f);
        } else {
            button.getLabel().setFontScale(1.0f);
        }
    }

    @Override
    public String getTitleText() {
        return getMyBundle().get("levelSelectCustomScreenTitleText");
    }
}
