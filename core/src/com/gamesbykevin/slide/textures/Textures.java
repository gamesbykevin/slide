package com.gamesbykevin.slide.textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.gamesbykevin.slide.level.objects.Danger;
import com.gamesbykevin.slide.level.objects.Teleporter;

import java.util.HashMap;

import static com.gamesbykevin.slide.MyGdxGame.getTextures;

public class Textures {

    //our game textures
    private HashMap<Key, Sprite> sprites;

    //list of different directories
    private String[] directories = "red/,yellow/,green/,blue/".split(",");

    private static final String TELEPORTER_DIRECTORY = "teleporters/";

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
        Teleporter0("0.png", ""),
        Teleporter1("1.png", ""),
        Teleporter2("2.png", ""),
        Teleporter3("3.png", ""),
        Teleporter4("4.png", ""),
        Teleporter5("5.png", ""),
        Teleporter6("6.png", ""),
        Teleporter7("7.png", ""),
        Teleporter8("8.png", ""),
        Teleporter9("9.png", ""),
        Teleporter10("10.png", ""),
        Teleporter11("11.png", ""),
        Teleporter12("12.png", ""),
        Teleporter13("13.png", ""),
        Teleporter14("14.png", ""),
        Teleporter15("15.png", ""),
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
        WallConnectorV("pipe_v.png", "S"),
        Dot("dot.png", "T"),
        Gem("gem.png", "U"),
        Holder("holder.png", "V"),
        GoBackMenu("back.png", ""),
        Restart("restart.png", "");

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
        this.sprites = new HashMap<>();

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
        add(Key.Teleporter0, TELEPORTER_DIRECTORY);
        add(Key.Teleporter1, TELEPORTER_DIRECTORY);
        add(Key.Teleporter2, TELEPORTER_DIRECTORY);
        add(Key.Teleporter3, TELEPORTER_DIRECTORY);
        add(Key.Teleporter4, TELEPORTER_DIRECTORY);
        add(Key.Teleporter5, TELEPORTER_DIRECTORY);
        add(Key.Teleporter6, TELEPORTER_DIRECTORY);
        add(Key.Teleporter7, TELEPORTER_DIRECTORY);
        add(Key.Teleporter8, TELEPORTER_DIRECTORY);
        add(Key.Teleporter9, TELEPORTER_DIRECTORY);
        add(Key.Teleporter10, TELEPORTER_DIRECTORY);
        add(Key.Teleporter11, TELEPORTER_DIRECTORY);
        add(Key.Teleporter12, TELEPORTER_DIRECTORY);
        add(Key.Teleporter13, TELEPORTER_DIRECTORY);
        add(Key.Teleporter14, TELEPORTER_DIRECTORY);
        add(Key.Teleporter15, TELEPORTER_DIRECTORY);
        add(Key.Holder, directory);
        add(Key.Goal, directory);
        add(Key.WallLeft, directory);
        add(Key.WallRight, directory);
        add(Key.WallUp, directory);
        add(Key.WallDown, directory);
        add(Key.Dot, directory);
        add(Key.WallConnectorH);
        add(Key.WallConnectorV);
        add(Key.Player);
        add(Key.Collected);
        add(Key.Danger);
        add(Key.Indicator);
        add(Key.Gem);

        //only certain platforms will have go back
        switch (Gdx.app.getType()) {
            case WebGL:
            case Desktop:
            case Applet:
            case HeadlessDesktop:
                add(Key.GoBackMenu);
                break;
        }

        //add option to restart
        add(Key.Restart);
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

    public static void rotateSprites() {

        //rotate the sprites here 1 time since the sprite instance is shared
        getTextures().getSprite(Textures.Key.Teleporter0).rotate(Teleporter.DEFAULT_ROTATION);
        getTextures().getSprite(Textures.Key.Teleporter1).rotate(Teleporter.DEFAULT_ROTATION);
        getTextures().getSprite(Textures.Key.Teleporter2).rotate(Teleporter.DEFAULT_ROTATION);
        getTextures().getSprite(Textures.Key.Teleporter3).rotate(Teleporter.DEFAULT_ROTATION);
        getTextures().getSprite(Textures.Key.Teleporter4).rotate(Teleporter.DEFAULT_ROTATION);
        getTextures().getSprite(Textures.Key.Teleporter5).rotate(Teleporter.DEFAULT_ROTATION);
        getTextures().getSprite(Textures.Key.Teleporter6).rotate(Teleporter.DEFAULT_ROTATION);
        getTextures().getSprite(Textures.Key.Teleporter7).rotate(Teleporter.DEFAULT_ROTATION);
        getTextures().getSprite(Textures.Key.Teleporter8).rotate(Teleporter.DEFAULT_ROTATION);
        getTextures().getSprite(Textures.Key.Teleporter9).rotate(Teleporter.DEFAULT_ROTATION);
        getTextures().getSprite(Textures.Key.Teleporter10).rotate(Teleporter.DEFAULT_ROTATION);
        getTextures().getSprite(Textures.Key.Teleporter11).rotate(Teleporter.DEFAULT_ROTATION);
        getTextures().getSprite(Textures.Key.Teleporter12).rotate(Teleporter.DEFAULT_ROTATION);
        getTextures().getSprite(Textures.Key.Teleporter13).rotate(Teleporter.DEFAULT_ROTATION);
        getTextures().getSprite(Textures.Key.Teleporter14).rotate(Teleporter.DEFAULT_ROTATION);
        getTextures().getSprite(Textures.Key.Teleporter15).rotate(Teleporter.DEFAULT_ROTATION);
        getTextures().getSprite(Textures.Key.Danger).rotate(Danger.DEFAULT_ROTATION);
    }

    public void dispose() {
        if (this.sprites != null) {

            for (Key key : sprites.keySet()) {
                this.sprites.put(key, null);
            }

            this.sprites.clear();
            this.sprites = null;
        }
    }
}