package com.gamesbykevin.slide.screen;

import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.rumble.Rumble;

public abstract class LevelScreen extends TemplateScreen {

    //current level
    private Level level;

    //keep track to maintain fps
    private long start;

    //how fast do we scroll?
    private int scrollSpeed;

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
    }
}