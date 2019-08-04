package com.gamesbykevin.slide.screen;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.audio.GameAudio;
import com.gamesbykevin.slide.exception.ScreenException;
import com.gamesbykevin.slide.preferences.AppPreferences;

import static com.gamesbykevin.slide.MyGdxGame.getMyBundle;
import static com.gamesbykevin.slide.screen.ScreenHelper.SCREEN_CREATE;

public class CreateSelectScreen extends CustomSelectScreen {

    //how big is each button
    private static final float BUTTON_SIZE = 150f;

    //how many columns per row
    private static final int COLUMNS = 2;

    //provide some space between each selection
    private static final int PADDING = 40;

    public CreateSelectScreen(MyGdxGame game) {
        super(game, false);
        super.setButtonSize(BUTTON_SIZE);
        super.setColumns(COLUMNS);
        super.setPadding(PADDING);
        super.setTotal(AppPreferences.MAX_LEVEL_SAVE);
    }

    @Override
    public void show() {
        super.show();

        //play music
        GameAudio.playMusic(GameAudio.SoundMusic.Editing, true);
    }

    @Override
    public void resume() {
        super.resume();

        //play music
        GameAudio.playMusic(GameAudio.SoundMusic.Editing, true);
    }

    @Override
    public void handleClick(int index) {

        try {

            //load the selected level
            getGame().getScreenHelper().getCreateScreen().setSaveIndex(index);
            getGame().getScreenHelper().getCreateScreen().create();

            //switch to the game screen
            getGame().getScreenHelper().changeScreen(SCREEN_CREATE);

        } catch (ScreenException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getButtonText(int index) {

        if (AppPreferences.hasLevelSave(index)) {
            return (index + 1) + "";
        } else {
            return getMyBundle().get("createSelectScreenNew");
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
        return getMyBundle().get("createSelectScreenTitleText");
    }
}