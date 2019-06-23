package com.gamesbykevin.slide.level.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.textures.Textures;

import static com.gamesbykevin.slide.MyGdxGame.FRAME_MS;

public class Indicator extends LevelObject {

    //how long do we render the indicator to complete a level (in milliseconds)
    public static final float LEVEL_INDICATOR_DELAY = 3000;

    //how much time has passed
    private float lapsed = 0;

    //the min/max y-coordinate
    private float minY, maxY;

    //which direction do we go?
    private boolean ascending = true;

    //how fast can the indicator move?
    private static final float VELOCITY = 2.5f;

    /**
     * Default constructor
     */
    public Indicator() {
        //do anything here
    }

    private boolean isAscending() {
        return ascending;
    }

    private void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    private float getMinY() {
        return this.minY;
    }

    private void setMinY(float minY) {
        this.minY = minY;
    }

    private float getMaxY() {
        return this.maxY;
    }

    private void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    public float getLapsed() {
        return this.lapsed;
    }

    public void setLapsed(float lapsed) {
        this.lapsed = lapsed;
    }

    @Override
    public void update() {

        setLapsed(getLapsed() + FRAME_MS);

        //if time has not passed, move the indicator
        if (getLapsed() <= LEVEL_INDICATOR_DELAY) {

            if (isAscending()) {
                setY(getY() + VELOCITY);
            } else {
                setY(getY() - VELOCITY);
            }

            if (getY() > getMaxY()) {
                setY(getMaxY());
                setAscending(false);
            } else if (getY() < getMinY()) {
                setY(getMinY());
                setAscending(true);
            }
        }
    }

    @Override
    public void updateCollision(Level level) {
        //we don't need to check collision for this
    }

    @Override
    public void reset(Level level) {
        setLapsed(0);

        LevelObject obj = level.getLevelObject(Textures.Key.Goal);

        //if null, then the goal is locked
        if (obj == null)
            obj = level.getLevelObject(Textures.Key.Locked);

        //this object should always exist
        setMinY(obj.getY() + (getH() * .65f));
        setMaxY(obj.getY() + (getH() * 1.5f));
    }

    @Override
    public void render(SpriteBatch batch) {

        if (getLapsed() <= LEVEL_INDICATOR_DELAY) {

            //render if time has not passed
            super.render(batch);
        }
    }
}