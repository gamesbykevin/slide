package com.gamesbykevin.slide.preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.gamesbykevin.slide.audio.GameAudio;

/**
 * Use the application preferences to store the app settings
 */
public class AppPreferences {

    private static final String PREF_MUSIC_ENABLED = "music.enabled";
    private static final String PREF_SOUND_ENABLED = "sound.enabled";
    private static final String PREF_VIBRATE_ENABLED = "vibrate.enabled";
    private static final String PREF_SCREEN_SHAKE_ENABLED = "screen.shake.enabled";
    private static final String PREF_LEVEL_COMPLETE = "level_complete_";
    private static final String PREF_LEVEL_SAVE = "level_save_";
    public static final String PREF_LANGUAGE = "language.index";
    private static final String PREFS_NAME = "slide_game_options";

    //how many levels can we save in our shared preferences
    public static final int MAX_LEVEL_SAVE = 20;

    private static Boolean ENABLED_MUSIC;
    private static Boolean ENABLED_SOUND;
    private static Boolean ENABLED_VIBRATE;
    private static Boolean ENABLED_SHAKE;

    private static Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    private static boolean isEnabled(String name) {
        return getPrefs().getBoolean(name, true);
    }

    public static boolean hasLevelCompleted(int index) {
        return getPrefs().getBoolean(PREF_LEVEL_COMPLETE + index, false);
    }

    public static boolean hasEnabledScreenShake() {

        if (ENABLED_SHAKE == null)
            ENABLED_SHAKE = isEnabled(PREF_SCREEN_SHAKE_ENABLED);

        return ENABLED_SHAKE;
    }

    public static boolean hasEnabledVibrate() {

        if (ENABLED_VIBRATE == null)
            ENABLED_VIBRATE = isEnabled(PREF_VIBRATE_ENABLED);

        return ENABLED_VIBRATE;
    }

    public static boolean hasEnabledSfx() {

        if (ENABLED_SOUND == null)
            ENABLED_SOUND = isEnabled(PREF_SOUND_ENABLED);

        return ENABLED_SOUND;
    }

    public static boolean hasEnabledMusic() {
        if (ENABLED_MUSIC == null)
            ENABLED_MUSIC = isEnabled(PREF_MUSIC_ENABLED);

        return ENABLED_MUSIC;
    }

    public static void setLevelCompleted(int index, boolean value) {

        //grab single instance to update and write
        Preferences preferences = getPrefs();

        //update the setting
        preferences.putBoolean(PREF_LEVEL_COMPLETE + index, value);

        //save the value
        preferences.flush();
    }

    public static boolean hasLevelSave(int index) {

        //get the saved level data
        String data = getLevelSave(index);

        //if we have data, then we have a saved level
        return (data != null && data.length() > 5);
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

    public static void setPreferenceMusic(boolean value) {

        //if not enabled stop
        if (!value)
            GameAudio.stopMusic();

        ENABLED_MUSIC = value;
        setPreference(PREF_MUSIC_ENABLED, value);
    }

    public static void setPreferenceSound(boolean value) {

        //if not enabled stop
        if (!value)
            GameAudio.stopSfx();

        ENABLED_SOUND = value;
        setPreference(PREF_SOUND_ENABLED, value);
    }

    public static void setPreferenceVibrate(boolean value) {
        ENABLED_VIBRATE = value;
        setPreference(PREF_VIBRATE_ENABLED, value);
    }

    public static void setPreferenceScreenShake(boolean value) {
        ENABLED_SHAKE = value;
        setPreference(PREF_SCREEN_SHAKE_ENABLED, value);
    }

    private static void setPreference(String name, boolean value) {

        //grab single instance to update and write
        Preferences preferences = getPrefs();

        //update the setting
        preferences.putBoolean(name, value);

        //write change to make it final
        preferences.flush();
    }

    public static void setPreference(String name, int value) {

        //grab single instance to update and write
        Preferences preferences = getPrefs();

        //update the setting
        preferences.putInteger(name, value);

        //write change to make it final
        preferences.flush();
    }

    public static int getPreferenceValue(String name) {

        //get value from shared preferences
        return getPrefs().getInteger(name, -1);
    }
}