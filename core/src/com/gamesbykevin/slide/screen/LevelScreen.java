package com.gamesbykevin.slide.screen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.rumble.Rumble;
import com.gamesbykevin.slide.textures.Textures;

import static com.gamesbykevin.slide.MyGdxGame.getTextures;

public abstract class LevelScreen extends TemplateScreen {

    //current level
    private Level level;

    //keep track to maintain fps
    private long start;

    //how fast do we scroll?
    private int scrollSpeed;

    //how big is the go back button
    public static final int GO_BACK_SIZE = 36;
    public static final int GO_BACK_X = 0;
    public static final int GO_BACK_Y = 0;

    public LevelScreen(MyGdxGame game) {
        super(game);

        //how fast does the background move...
        setScrollSpeed((int)(SCREEN_HEIGHT * .01));
    }

    public void setScrollSpeed(final int scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    public int getScrollSpeed() {
        return this.scrollSpeed;
    }

    public long getStart() {
        return this.start;
    }

    public Level getLevel() {
        return this.level;
    }

    protected void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public void show() {
        super.show();

        Sprite sprite = getTextures().getSprite(Textures.Key.GoBackMenu);

        if (sprite != null) {
            sprite.setPosition(GO_BACK_X, GO_BACK_Y);
            sprite.setSize(GO_BACK_SIZE, GO_BACK_SIZE);
        }
    }

    @Override
    public void render(float delta) {

        //get the current start time
        this.start = System.currentTimeMillis();

        //call parent
        super.render(delta);

        //draw a few backgrounds so it appears continuous
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                getBatch().draw(
                    getBackgroundImage(),
                    getBackgroundRect().x - (getBackgroundRect().width * x),
                    getBackgroundRect().y - (getBackgroundRect().height * y),
                    getBackgroundRect().width,
                    getBackgroundRect().height
                );
            }
        }

        //move the background
        getBackgroundRect().y += getScrollSpeed();

        //keep the background in bounds
        if (getBackgroundRect().y >= SCREEN_HEIGHT)
            getBackgroundRect().y = 0;

        //now we can shake the screen for everything else
        Rumble.tick(getCamera(), delta);

        //render the game objects
        getLevel().update();
        getLevel().render(getBatch());

        //get the go back button
        Sprite sprite = getTextures().getSprite(Textures.Key.GoBackMenu);

        //only render if it exists
        if (sprite != null) {
            sprite.draw(getBatch());
        }
    }
}