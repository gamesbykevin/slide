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
    private static final String PREFS_NAME = "game_options";

    private Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    public boolean isEnabled(String name) {
        return getPrefs().getBoolean(name, true);
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