package com.gamesbykevin.slide.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.exception.ScreenException;
import com.gamesbykevin.slide.preferences.AppPreferences;

import static com.gamesbykevin.slide.screen.ScreenHelper.SCREEN_MENU;

public class OptionsScreen extends TemplateScreen {

    public OptionsScreen(MyGdxGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        //Create buttons
        final CheckBox checkboxMusic = new CheckBox(null, getSkin());
        checkboxMusic.setTransform(true);
        checkboxMusic.setScale(CHECKBOX_SCALE);
        checkboxMusic.setChecked( getGame().getPreferences().isEnabled(AppPreferences.PREF_MUSIC_ENABLED) );
        checkboxMusic.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = checkboxMusic.isChecked();
                getGame().getPreferences().setPreference(AppPreferences.PREF_MUSIC_ENABLED, enabled );
                return false;
            }
        });

        final CheckBox checkboxSound = new CheckBox(null, getSkin());
        checkboxSound.setTransform(true);
        checkboxSound.setScale(CHECKBOX_SCALE);
        checkboxSound.setChecked( getGame().getPreferences().isEnabled(AppPreferences.PREF_SOUND_ENABLED) );
        checkboxSound.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = checkboxSound.isChecked();
                getGame().getPreferences().setPreference(AppPreferences.PREF_SOUND_ENABLED, enabled );
                return false;
            }
        });

        final CheckBox checkboxVibrate = new CheckBox(null, getSkin());
        checkboxVibrate.setTransform(true);
        checkboxVibrate.setScale(CHECKBOX_SCALE);
        checkboxVibrate.setChecked( getGame().getPreferences().isEnabled(AppPreferences.PREF_VIBRATE_ENABLED) );
        checkboxVibrate.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = checkboxVibrate.isChecked();
                getGame().getPreferences().setPreference(AppPreferences.PREF_VIBRATE_ENABLED, enabled );
                return false;
            }
        });

        //create our back button
        TextButton buttonBack = new TextButton("Back", getSkin());

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
        Label labelMusic = new Label(  "Music", getSkin());
        Label labelSound = new Label(  "Sound", getSkin());
        Label labelVibrate = new Label("Vibrate", getSkin());
        labelMusic.setFontScale(FONT_SCALE);
        labelSound.setFontScale(FONT_SCALE);
        labelVibrate.setFontScale(FONT_SCALE);

        //Create Table
        Table table = new Table();

        //Set table to fill stage
        table.setFillParent(true);

        //Set alignment of contents in the table.
        table.center();
        addRow(table, labelMusic, checkboxMusic);
        addRow(table, labelSound, checkboxSound);

        //mobile phones can vibrate
        switch(Gdx.app.getType()) {

            case iOS:
            case Android:
                addRow(table, labelVibrate, checkboxVibrate);
                break;

            default:
                table.row().pad(DEFAULT_PADDING);
                table.add(buttonBack).colspan(2).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
                break;
        }

        //Add table to stage
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