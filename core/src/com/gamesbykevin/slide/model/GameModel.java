package com.gamesbykevin.slide.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.level.LevelHelper;
import com.gamesbykevin.slide.level.objects.Player;
import com.gamesbykevin.slide.textures.Textures;

import java.io.IOException;

import static com.gamesbykevin.slide.level.objects.Bomb.TEXT_WIDTH;

public class GameModel {

    //keep our game reference
    private final MyGdxGame game;

    //our level
    private Level level;

    //our game textures
    private Textures textures;

    //our bitmap font to render text in our game
    private BitmapFont font;

    //which level are we on?
    public static int LEVEL_INDEX = 0;

    public GameModel(MyGdxGame game) {

        //store game object reference
        this.game = game;

        //create new font
        this.font = new BitmapFont();

        //create our font metrics for reference
        GlyphLayout layout = new GlyphLayout(getFont(), "9");

        //update the text width
        TEXT_WIDTH = layout.width;

        layout = null;

        try {
            //create our level
            this.level = LevelHelper.create(Gdx.files.internal("levels").list()[LEVEL_INDEX].name());
            this.level.reset();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //create our game textures
        this.textures = new Textures();
    }

    public void update() {
        getLevel().update();
    }

    public void render(SpriteBatch batch) {
        getLevel().render(this, batch);
    }

    public Player getPlayer() {
        return getLevel().getPlayer();
    }

    public BitmapFont getFont() {
        return this.font;
    }

    public Level getLevel() {
        return this.level;
    }

    public MyGdxGame getGame() {
        return game;
    }

    public Textures getTextures() {
        return textures;
    }
}