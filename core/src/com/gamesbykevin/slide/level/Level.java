package com.gamesbykevin.slide.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.slide.level.objects.*;
import com.gamesbykevin.slide.level.objects.LevelObject;
import com.gamesbykevin.slide.preferences.AppPreferences;
import com.gamesbykevin.slide.rumble.Rumble;
import com.gamesbykevin.slide.textures.Textures;

import java.util.ArrayList;

import static com.gamesbykevin.slide.MyGdxGame.*;
import static com.gamesbykevin.slide.level.LevelHelper.MAX_COLS;
import static com.gamesbykevin.slide.level.LevelHelper.MAX_ROWS;
import static com.gamesbykevin.slide.level.objects.LevelObject.*;
import static com.gamesbykevin.slide.preferences.AppPreferences.PREF_VIBRATE_ENABLED;
import static com.gamesbykevin.slide.screen.ParentScreen.SCREEN_HEIGHT;
import static com.gamesbykevin.slide.screen.ParentScreen.SCREEN_WIDTH;

public class Level implements ILevel {

    //which level are we on?
    public static int LEVEL_INDEX = 0;

    //list of objects in the level
    private ArrayList<LevelObject> levelObjects;

    //start position of level
    public static int START_X = 64;
    public static int START_Y = 64;

    //is the level solved
    private boolean solved = false;

    //how much time has passed since we completed the level
    private float lapsedComplete = 0;

    //how long to wait when we complete the level (in milliseconds)
    public static final float LEVEL_COMPLETE_DELAY = 2000;

    //show user where they need to go
    private LevelObject indicator;

    public Level() {
        this(MAX_COLS, MAX_ROWS);
    }

    public Level(int cols, int rows) {

        START_X = (int)((SCREEN_WIDTH - (cols * DEFAULT_WIDTH))   / 2);
        START_Y = (int)((SCREEN_HEIGHT - (rows * DEFAULT_HEIGHT)) / 2);

        this.levelObjects = new ArrayList<>();
    }

    public void setIndicator(LevelObject indicator) {
        this.indicator = indicator;
    }

    public LevelObject getIndicator() {
        return this.indicator;
    }

    public Player getPlayer() {
        return (Player)getLevelObject(Textures.Key.Player);
    }

    public static int getStartX() {
        return START_X;
    }

    public static int getStartY() {
        return START_Y;
    }

    public boolean isSolved() {
        return this.solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    @Override
    public void update() {

        //no need to do anything else here
        if (isSolved()) {
            setLapsedComplete(getLapsedComplete() + FRAME_MS);
            return;
        }

        //update the indicator
        if (getIndicator() != null)
            getIndicator().update();

        //get the player
        Player player = getPlayer();
        player.update();

        //update our objects
        for (int j = 0; j < getLevelObjects().size(); j++) {

            //get current level object
            LevelObject tmp = getLevelObjects().get(j);

            //don't check the player
            if (tmp.getId().equals(player.getId()))
                continue;

            //update the level object accordingly
            tmp.update();

            if (getPlayer().hasCollision(tmp))
                tmp.updateCollision(this);
        }

        //if we went off screen reset the level
        if (!player.hasBounds()) {
            Rumble.reset();
            reset();

            //vibrate if the option is enabled
            if (AppPreferences.isEnabled(PREF_VIBRATE_ENABLED))
                Gdx.input.vibrate(DURATION_VIBRATE);
        }
    }

    public static int getColumn(float x) {
        return (int)((x - getStartX()) / DEFAULT_WIDTH);
    }

    public static int getRow(float y) {
        return (int)((y - getStartY()) / DEFAULT_HEIGHT);
    }

    public static void updateCoordinates(LevelObject object) {

        //update the (x, y) coordinates
        object.setX((object.getCol() * DEFAULT_WIDTH) + getStartX());
        object.setY((object.getRow() * DEFAULT_HEIGHT) + getStartY());
    }

    public float getLapsedComplete() {
        return this.lapsedComplete;
    }

    public void setLapsedComplete(float lapsedComplete) {
        this.lapsedComplete = lapsedComplete;
    }

    /**
     * Reset the level
     */
    @Override
    public void reset() {

        //reset the time lapsed
        setLapsedComplete(0);

        //the level has not been solved (yet)
        setSolved(false);

        for (int i = 0; i < getLevelObjects().size(); i++) {

            //get the current level object so it can be reset
            getLevelObjects().get(i).reset(this);
            updateCoordinates(getLevelObjects().get(i));
        }

        //reset the indicator
        if (getIndicator() != null)
            getIndicator().reset(this);
    }

    public LevelObject getLevelObject(final String id) {

        for (int i = 0; i < getLevelObjects().size(); i++) {

            LevelObject object = getLevelObjects().get(i);

            if (object.getId().equals(id))
                return object;
        }

        return null;
    }

    public LevelObject getLevelObject(float x, float y) {

        for (int i = 0; i < getLevelObjects().size(); i++) {

            LevelObject obj = getLevelObjects().get(i);

            if (obj.contains(x, y))
                return obj;
        }

        //we didn't find it
        return null;
    }

    public LevelObject getLevelObject(int col, int row) {

        for (int i = 0; i < getLevelObjects().size(); i++) {

            LevelObject obj = getLevelObjects().get(i);

            if (obj.getCol() != col || obj.getRow() != row)
                continue;

            return obj;
        }

        return null;
    }

    public LevelObject getLevelObject(Textures.Key key, int col, int row) {

        for (int i = 0; i < getLevelObjects().size(); i++) {

            LevelObject obj = getLevelObjects().get(i);

            if (obj.getCol() != col || obj.getRow() != row)
                continue;

            if (obj.getKey() != key)
                continue;

            return obj;
        }

        return null;
    }

    public LevelObject getLevelObject(Textures.Key key) {

        //search the level objects for the type
        for (int i = 0; i < getLevelObjects().size(); i++) {
            if (getLevelObjects().get(i).getKey() == key)
                return getLevelObjects().get(i);
        }

        return null;
    }

    public void add(LevelObject object) {
        getLevelObjects().add(object);
    }

    public void assignLocation(LevelObject object, int col, int row) {

        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {

                if (x == 0 && y == 0)
                    continue;

                if (col + x < 0 || col + x >= MAX_COLS)
                    continue;
                if (row + y < 0 || row + y >= MAX_ROWS)
                    continue;

                //make sure there are no existing objects here
                if (getLevelObject(col + x, row + y) == null) {
                    object.setCol(col + x);
                    object.setRow(row + y);
                    updateCoordinates(object);
                    return;
                }
            }
        }

        for (int x = 0; x < MAX_COLS; x++) {
            for (int y = 0; y < MAX_ROWS; y++) {
                if (getLevelObject(col + x, row + y) == null) {
                    object.setCol(col + x);
                    object.setRow(row + y);
                    updateCoordinates(object);
                    return;
                }
            }
        }
    }

    public void remove(Textures.Key key) {

        for (int i = 0; i < getLevelObjects().size(); i++) {
            if (getLevelObjects().get(i).getKey() == key) {
                getLevelObjects().remove(i);
                i--;
            }
        }
    }

    public void remove(String id) {

        for (int i = 0; i < getLevelObjects().size(); i++) {
            if (getLevelObjects().get(i).getId().equals(id)) {
                getLevelObjects().remove(i);
                return;
            }
        }
    }

    private ArrayList<LevelObject> getLevelObjects() {
        return this.levelObjects;
    }

    public void render(SpriteBatch batch) {

        for (int i = 0; i < getLevelObjects().size(); i++) {

            //render the level object
            getLevelObjects().get(i).render(batch);
        }

        //render the player as well
        if (getPlayer() != null)
            getPlayer().render(batch);

        //render our goal indicator
        if (getIndicator() != null)
            getIndicator().render(batch);
    }
}