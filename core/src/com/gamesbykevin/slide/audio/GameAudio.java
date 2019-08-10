package com.gamesbykevin.slide.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.gamesbykevin.slide.preferences.AppPreferences;

import java.util.HashMap;

public class GameAudio {

    //here is where our sound effects are located
    public static final String DIRECTORY_SFX = "audio/sfx/";

    //here is where our music is located
    public static final String DIRECTORY_MUSIC = "audio/music/";

    //our collection of game sound effects
    private static HashMap<SoundEffect, Sound> SFX;

    //our collection of game music
    private static HashMap<SoundMusic, Music> MUSIC;

    public enum SoundMusic {
        Editing("editing.mp3"),
        Menu("menu.mp3"),
        Theme("theme.mp3"),
        Victory("victory.mp3");

        private final String path;

        SoundMusic(String path) {
            this.path = path;
        }

        public String getPath() {
            return this.path;
        }
    }

    public enum SoundEffect {

        Activate("activate.wav"),
        Connect("connect.wav"),
        Danger("danger.wav"),
        Explosion("explosion.wav"),
        Gem("gem.wav"),
        Holder("holder.wav"),
        Grow("grow.wav"),
        Key("key.wav"),
        Redirect("redirect.wav"),
        Teleport("teleport.wav"),
        Wall("wall.wav");

        private final String path;

        SoundEffect(String path) {
            this.path = path;
        }

        public String getPath() {
            return this.path;
        }
    }

    public static void load() {
        loadSfx();
        loadMusic();
    }

    private static void loadSfx() {

        if (SFX == null)
            SFX = new HashMap<>();

        for (SoundEffect effect : SoundEffect.values()) {

            if (SFX.get(effect) == null) {
                SFX.put(effect, Gdx.audio.newSound(Gdx.files.internal(DIRECTORY_SFX + effect.getPath())));
            }
        }
    }

    private static void loadMusic() {

        if (MUSIC == null)
            MUSIC = new HashMap<>();

        for (SoundMusic effect : SoundMusic.values()) {

            if (MUSIC.get(effect) == null) {
                MUSIC.put(effect, Gdx.audio.newMusic(Gdx.files.internal(DIRECTORY_MUSIC + effect.getPath())));
            }
        }
    }

    public static void recycle() {
        recycleMusic();
        recycleSfx();
    }

    private static void recycleMusic() {

        if (MUSIC != null) {

            for (SoundMusic effect : MUSIC.keySet()) {

                if (MUSIC.get(effect) != null) {
                    MUSIC.get(effect).stop();
                    MUSIC.get(effect).dispose();
                    MUSIC.put(effect, null);
                }
            }

            MUSIC.clear();
            MUSIC = null;
        }
    }

    private static void recycleSfx() {

        if (SFX != null) {

            for (SoundEffect effect : SFX.keySet()) {

                if (SFX.get(effect) != null) {
                    SFX.get(effect).dispose();
                    SFX.put(effect, null);
                }
            }

            SFX.clear();
            SFX = null;
        }
    }

    public static void playMusic(SoundMusic effect, boolean restart) {

        //we can only play if enabled
        if (AppPreferences.hasEnabledMusic()) {

            Music music = MUSIC.get(effect);

            if (!music.isPlaying()) {

                //stop all music
                stopMusic();

                //only play the music if we aren't already
                music.setLooping(true);

                if (restart)
                    music.setPosition(0f);

                music.play();
            }
        }
    }

    public static void playSfx(SoundEffect effect) {

        //we can only play if enabled
        if (AppPreferences.hasEnabledSfx()) {
            SFX.get(effect).play(1.0f);
        }
    }

    public static void stop() {
        stopMusic();
        stopSfx();
    }

    public static void stopMusic() {

        //we can only pause if enabled
        if (AppPreferences.hasEnabledMusic()) {

            if (MUSIC != null) {
                for (SoundMusic effect : MUSIC.keySet()) {
                    MUSIC.get(effect).pause();
                }
            }
        }
    }

    public static void stopSfx() {

        //we can only stop if enabled
        if (AppPreferences.hasEnabledSfx()) {

            if (SFX != null) {
                for (SoundEffect effect : SFX.keySet()) {
                    SFX.get(effect).stop();
                }
            }
        }
    }

}