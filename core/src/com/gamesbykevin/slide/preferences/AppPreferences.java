package com.gamesbykevin.slide.preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Use the application preferences to store the app settings
 */
public class AppPreferences {

    public static final String PREF_MUSIC_ENABLED = "music.enabled";
    public static final String PREF_SOUND_ENABLED = "sound.enabled";
    public static final String PREF_VIBRATE_ENABLED = "vibrate.enabled";
    public static final String PREF_SCREEN_SHAKE_ENABLED = "screen.shake.enabled";
    public static final String PREF_LEVEL_SAVE = "level_save_";
    private static final String PREFS_NAME = "slide_game_options";

    //how many levels can we save in our shared preferences
    public static final int MAX_LEVEL_SAVE = 10;

    private static Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    public static boolean isEnabled(String name) {
        return getPrefs().getBoolean(name, true);
    }

    public static boolean hasLevelSave(int index) {
        return (getLevelSave(index) != null && getLevelSave(index).length() > 5);
    }

    public static String getLevelSave(int index) {
        return getPrefs().getString(PREF_LEVEL_SAVE + index);
    }

    public static void setLevelSave(int index, String value) {

        //grab single instance to update and write
        Preferences preferences = getPrefs();

        //update the setting
        preferences.putString(PREF_LEVEL_SAVE + index, value);

        //save the value
        preferences.flush();
    }

    public void setPreference(String name, boolean value) {

        //grab single instance to update and write
        Preferences preferences = getPrefs();

        //update the setting
        preferences.putBoolean(name, value);

        //write change to make it final
        preferences.flush();
    }
}