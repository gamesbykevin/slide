package com.gamesbykevin.slide.textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.HashMap;

public class Textures {

    //our game textures
    private HashMap<Key, Sprite> sprites;

    //list of different directories
    private String[] directories = "red/,yellow/,green/,blue/".split(",");

    /**
     * Different types of textures
     */
    public enum Key {
        Key("key.png", "A"),
        Locked("locked.png", "B"),
        RedirectNW("redirect_nw.png", "C"),
        RedirectNE("redirect_ne.png", "D"),
        RedirectSE("redirect_se.png", "E"),
        RedirectSW("redirect_sw.png", "F"),
        Bomb("bomb.png", "G"),
        Wall("wall.png", "H"),
        Teleporter("teleport.png", ""),
        Danger("danger.png", "J"),
        Goal("goal.png", "K"),
        Collected("collected.png", "L"),
        Player("player.png", "M"),
        Indicator("arrow.png", ""),
        WallLeft("wall_l.png", "N"),
        WallRight("wall_r.png", "O"),
        WallUp("wall_u.png", "Q"),
        WallDown("wall_d.png", "R"),
        WallConnectorH("pipe_h.png", "P"),
        WallConnectorV("pipe_v.png", "S");

        private final String filename;
        private final String fileCharKey;

        Key(String filename, String fileCharKey) {
            this.filename = filename;
            this.fileCharKey = fileCharKey;
        }

        private String getFilename() {
            return this.filename;
        }

        public String getFileCharKey() {
            return fileCharKey;
        }
    }

    public Textures() {

        //create new hash map
        this.sprites = new HashMap<Key, Sprite>();

        //pick random directory
        final String directory = directories[(int)(Math.random() * directories.length)];

        add(Key.Key, directory);
        add(Key.Locked, directory);
        add(Key.RedirectNW, directory);
        add(Key.RedirectNE, directory);
        add(Key.RedirectSW, directory);
        add(Key.RedirectSE, directory);
        add(Key.Bomb, directory);
        add(Key.Wall, directory);
        add(Key.Teleporter, directory);
        add(Key.Goal, directory);
        add(Key.WallLeft, directory);
        add(Key.WallRight, directory);
        add(Key.WallUp, directory);
        add(Key.WallDown, directory);
        add(Key.WallConnectorH);
        add(Key.WallConnectorV);
        add(Key.Player);
        add(Key.Collected);
        add(Key.Danger);
        add(Key.Indicator);
    }

    private void add(Key key) {
        add(key, "");
    }

    private void add(Key key, String directory) {

        //create the texture
        Texture texture = new Texture(Gdx.files.internal("sprites/" + directory + key.getFilename()));

        //create a sprite with the texture
        this.sprites.put(key, new Sprite(texture, 0, 0, texture.getWidth(), texture.getHeight()));
    }

    public Sprite getSprite(Key key) {
        return this.sprites.get(key);
    }

    public static Key getKey(String charKey) {

        for (Key key : Key.values()) {

            if (key.getFileCharKey().equalsIgnoreCase(charKey))
                return key;
        }

        return null;
    }
}