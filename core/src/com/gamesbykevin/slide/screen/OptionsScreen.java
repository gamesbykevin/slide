package com.gamesbykevin.slide.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.exception.ScreenException;
import com.gamesbykevin.slide.preferences.AppPreferences;
import com.gamesbykevin.slide.util.Language;

import static com.gamesbykevin.slide.MyGdxGame.getMyBundle;
import static com.gamesbykevin.slide.preferences.AppPreferences.*;
import static com.gamesbykevin.slide.screen.ScreenHelper.SCREEN_MENU;

public class OptionsScreen extends TemplateScreen {

    public OptionsScreen(MyGdxGame game) {
        super(game);
    }

    @Override
    public void show() {

        super.show();

        //capture the menu input
        captureInputMenu();

        //Create buttons
        final CheckBox checkboxMusic = new CheckBox(null, getSkin());
        checkboxMusic.setTransform(true);
        checkboxMusic.setScale(CHECKBOX_SCALE);
        checkboxMusic.setChecked( hasEnabledMusic());
        checkboxMusic.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = checkboxMusic.isChecked();
                setPreferenceMusic(enabled );
                return false;
            }
        });

        final CheckBox checkboxSound = new CheckBox(null, getSkin());
        checkboxSound.setTransform(true);
        checkboxSound.setScale(CHECKBOX_SCALE);
        checkboxSound.setChecked( hasEnabledSfx());
        checkboxSound.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = checkboxSound.isChecked();
                setPreferenceSound(enabled );
                return false;
            }
        });

        final CheckBox checkboxScreenShake = new CheckBox(null, getSkin());
        checkboxScreenShake.setTransform(true);
        checkboxScreenShake.setScale(CHECKBOX_SCALE);
        checkboxScreenShake.setChecked( hasEnabledScreenShake());
        checkboxScreenShake.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = checkboxScreenShake.isChecked();
                setPreferenceScreenShake(enabled );
                return false;
            }
        });

        //create our back button
        TextButton buttonBack = new TextButton(getMyBundle().get("optionsScreenBack"), getSkin());

        //Add listeners to buttons
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    getGame().getScreenHelper().changeScreen(SCREEN_MENU);
                } catch (ScreenException ex) {
                    ex.printStackTrace();
                }
            }
        });

        //create the labels and size them
        Label labelMusic = new Label(getMyBundle().get("optionsScreenMusic"), getSkin());
        Label labelSound = new Label(getMyBundle().get("optionsScreenSound"), getSkin());
        Label labelShakeScreen = new Label(getMyBundle().get("optionsScreenShake"), getSkin());
        labelMusic.setFontScale(FONT_SCALE);
        labelSound.setFontScale(FONT_SCALE);
        labelShakeScreen.setFontScale(FONT_SCALE);

        //Create Table
        Table table = new Table();

        //Set table to fill stage
        table.setFillParent(true);

        //Set alignment of contents in the table.
        table.center();
        addRow(table, labelMusic, checkboxMusic);
        addRow(table, labelSound, checkboxSound);
        addRow(table, labelShakeScreen, checkboxScreenShake);

        //html and web application the user will select their language
        switch(Gdx.app.getType()) {
            case WebGL:
            case Applet:
            case Desktop:
            case HeadlessDesktop:
                Label labelLanguage = new Label("Language", getSkin());
                labelLanguage.setFontScale(FONT_SCALE);
                final Language.Languages[] languages = Language.Languages.values();

                int languageIndexDefault = 0;

                String[] items = new String[languages.length];
                for (int i = 0; i < languages.length; i++) {
                    items[i] = languages[i].getDesc();

                    //default language to english
                    if (languages[i].getLanguageCode().equalsIgnoreCase("EN"))
                        languageIndexDefault = i;
                }

                //create our drop down
                final SelectBox<String> dropdown = new SelectBox<>(getSkin());

                //add our item selections
                dropdown.setItems(items);

                //pre select the value if it exists
                final int languageIndex = getPreferenceValue(AppPreferences.PREF_LANGUAGE);
                if (languageIndex >= 0) {
                    dropdown.setSelectedIndex(languageIndex);
                } else {
                    dropdown.setSelectedIndex(languageIndexDefault);
                }

                dropdown.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {

                        //change the language
                        MyGdxGame.changeMyBundle(dropdown.getSelectedIndex());

                        //store language in the preferences
                        setPreference(AppPreferences.PREF_LANGUAGE, dropdown.getSelectedIndex());
                    }
                });

                table.add(labelLanguage).colspan(1).top().center().pad(DEFAULT_PADDING);
                table.add(dropdown).colspan(1).bottom().pad(DEFAULT_PADDING);
                table.row();
                break;
        }

        //mobile phones can vibrate
        switch(Gdx.app.getType()) {

            case iOS:
            case Android:
                Label labelVibrate = new Label(getMyBundle().get("optionsScreenVibrate"), getSkin());
                labelVibrate.setFontScale(FONT_SCALE);

                final CheckBox checkboxVibrate = new CheckBox(null, getSkin());
                checkboxVibrate.setTransform(true);
                checkboxVibrate.setScale(CHECKBOX_SCALE);
                checkboxVibrate.setChecked( hasEnabledVibrate());
                checkboxVibrate.addListener( new EventListener() {
                    @Override
                    public boolean handle(Event event) {
                        boolean enabled = checkboxVibrate.isChecked();
                        setPreferenceVibrate(enabled );
                        return false;
                    }
                });

                addRow(table, labelVibrate, checkboxVibrate);
                break;
        }

        //html and web application do not have a "back button"
        switch (Gdx.app.getType()) {
            case Desktop:
            case Applet:
            case WebGL:
            case HeadlessDesktop:
                table.row().pad(DEFAULT_PADDING);
                table.add(buttonBack).colspan(2).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
                break;
        }

        //Add table to stage
        getStage().clear();
        getStage().addActor(table);

        //add our social media icons
        super.addSocialIcons();
    }

    private void addRow(Table table, Label label, CheckBox checkBox) {
        table.add(label).colspan(1).top().center().pad(DEFAULT_PADDING);
        table.add(checkBox).colspan(1).bottom().pad(DEFAULT_PADDING);
        table.row();
    }

    @Override
    public void render(float delta) {

        getStage().act();

        //draw background with the stage
        getStage().getBatch().begin();
        getStage().getBatch().draw(getBackgroundImage(), getBackgroundRect().x, getBackgroundRect().y, getBackgroundRect().width, getBackgroundRect().height);
        getStage().getBatch().end();

        getStage().draw();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}