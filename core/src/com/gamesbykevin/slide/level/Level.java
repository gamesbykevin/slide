package com.gamesbykevin.slide.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.slide.graphics.Overlay;
import com.gamesbykevin.slide.level.objects.*;
import com.gamesbykevin.slide.level.objects.LevelObject;
import com.gamesbykevin.slide.preferences.AppPreferences;
import com.gamesbykevin.slide.rumble.Rumble;

import java.util.ArrayList;

import static com.gamesbykevin.slide.MyGdxGame.*;
import static com.gamesbykevin.slide.graphics.Overlay.OVERLAY_DURATION_GAMEPLAY;
import static com.gamesbykevin.slide.level.LevelHelper.*;
import static com.gamesbykevin.slide.level.objects.LevelObject.DEFAULT_DIMENSION;
import static com.gamesbykevin.slide.preferences.AppPreferences.PREF_VIBRATE_ENABLED;
import static com.gamesbykevin.slide.screen.ParentScreen.SCREEN_HEIGHT;
import static com.gamesbykevin.slide.screen.ParentScreen.SCREEN_WIDTH;

public class Level implements ILevel {

    public enum Objective {
        Goal,
        Unlock,
        Bomb,
        Gem
    }

    //which level are we on?
    public static int LEVEL_INDEX = 0;

    //list of objects in the level
    private ArrayList<LevelObject> levelObjects;

    //display what the level objective is
    private Overlay overlay;

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

    //how big is our level?
    private final int cols, rows;

    //what is the level objective
    private Objective objective = Objective.Goal;

    public Level() {
        this(SMALL_SIZE_COLS, SMALL_SIZE_ROWS);
    }

    public Level(int cols, int rows) {

        //save the level size
        this.cols = cols;
        this.rows = rows;

        //we can render different size objects depending on how large the level is
        if (getCols() <= LARGE_SIZE_COLS && getRows() <= LARGE_SIZE_ROWS ) {
            DEFAULT_DIMENSION = DIMENSION_LARGE;
        } else if (getCols() <= NORMAL_SIZE_COLS && getRows() <= NORMAL_SIZE_ROWS) {
            DEFAULT_DIMENSION = DIMENSION_NORMAL;
        } else if (getCols() <= SMALL_SIZE_COLS && getRows() <= SMALL_SIZE_ROWS) {
            DEFAULT_DIMENSION = DIMENSION_SMALL;
        }

        START_X = (int)((SCREEN_WIDTH - (getCols() * DEFAULT_DIMENSION))   / 2);
        START_Y = (int)((SCREEN_HEIGHT - (getRows() * DEFAULT_DIMENSION)) / 2);

        //create our list of level objects
        this.levelObjects = new ArrayList<>();
    }

    public void setObjective(Objective objective) {

        this.objective = objective;

        if (getOverlay() == null) {

            //our level objective overlay
            switch (objective) {
                case Goal:
                    this.overlay = new Overlay(Overlay.OVERLAY_TEXT_GAMEPLAY_GOAL, Overlay.OVERLAY_TRANSPARENCY_GAMEPLAY, OVERLAY_DURATION_GAMEPLAY);
                    break;

                case Bomb:
                    this.overlay = new Overlay(Overlay.OVERLAY_TEXT_GAMEPLAY_BOMB, Overlay.OVERLAY_TRANSPARENCY_GAMEPLAY, OVERLAY_DURATION_GAMEPLAY);
                    break;

                case Gem:
                    this.overlay = new Overlay(Overlay.OVERLAY_TEXT_GAMEPLAY_GEM, Overlay.OVERLAY_TRANSPARENCY_GAMEPLAY, OVERLAY_DURATION_GAMEPLAY);
                    break;

                case Unlock:
                    this.overlay = new Overlay(Overlay.OVERLAY_TEXT_GAMEPLAY_LOCKED, Overlay.OVERLAY_TRANSPARENCY_GAMEPLAY, OVERLAY_DURATION_GAMEPLAY);
                    break;
            }
        }
    }

    public Objective getObjective() {
        return this.objective;
    }

    public Overlay getOverlay() {
        return this.overlay;
    }

    public int getCols() {
        return this.cols;
    }

    public int getRows() {
        return this.rows;
    }

    public void setIndicator(LevelObject indicator) {
        this.indicator = indicator;
    }

    public LevelObject getIndicator() {
        return this.indicator;
    }

    public Player getPlayer() {
        return (Player)getLevelObject(LevelObject.Type.Player);
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
            getIndicator().update(this);

        //get the player
        Player player = getPlayer();

        //check if we can detonate a bomb
        player.checkBombs(this);

        //update the player
        player.update(this);

        //update our objects
        for (int j = 0; j < getLevelObjects().size(); j++) {

            //get current level object
            LevelObject tmp = getLevelObjects().get(j);

            //if this is the player
            if (tmp.getId().equals(player.getId()))
                continue;

            //update the level object accordingly
            tmp.update(this);

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

        //check if we meet the level objective
        verifyObjective(this);
    }

    public static int getColumn(float x) {
        return (int)((x - getStartX()) / DEFAULT_DIMENSION);
    }

    public static int getRow(float y) {
        return (int)((y - getStartY()) / DEFAULT_DIMENSION);
    }

    public static void updateCoordinates(LevelObject object) {

        //update the (x, y) coordinates
        object.setX((object.getCol() * DEFAULT_DIMENSION) + getStartX());
        object.setY((object.getRow() * DEFAULT_DIMENSION) + getStartY());
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

        //reset the level overlay
        if (getOverlay() != null)
            getOverlay().reset();

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

    public LevelObject getLevelObject(LevelObject.Type type, int col, int row) {

        for (int i = 0; i < getLevelObjects().size(); i++) {

            LevelObject obj = getLevelObjects().get(i);

            if (obj.getCol() != col || obj.getRow() != row)
                continue;

            if (!obj.getType().equals(type))
                continue;

            return obj;
        }

        return null;
    }

    public int getCount(LevelObject.Type type) {

        int count = 0;

        //search the level objects for the type
        for (int i = 0; i < getLevelObjects().size(); i++) {

            //if the key matches keep track of the count
            if (getLevelObjects().get(i).getType().equals(type))
                count++;
        }

        return count;
    }

    public LevelObject getLevelObject(LevelObject.Type type) {

        //search the level objects for the type
        for (int i = 0; i < getLevelObjects().size(); i++) {
            if (getLevelObjects().get(i).getType().equals(type))
                return getLevelObjects().get(i);
        }

        return null;
    }

    public void add(LevelObject object) {

        //if adding a teleporter we have to stay within limits
        switch (object.getType()) {
            case Teleporter:
                //if we exceed the limit we won't add the object
                if (getCount(object.getType()) >= TELEPORTER_LIMIT)
                    return;
                break;
        }

        getLevelObjects().add(object);
    }

    public void remove(LevelObject.Type type) {

        for (int i = 0; i < getLevelObjects().size(); i++) {
            if (getLevelObjects().get(i).getType().equals(type)) {
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

    //looking for teleporters with this key
    public boolean hasFileCharKey(String key) {

        for (int i = 0; i < getLevelObjects().size(); i++) {
            LevelObject obj = getLevelObjects().get(i);

            if (!obj.getType().equals(LevelObject.Type.Teleporter))
                continue;

            if (((Teleporter)obj).getFileCharKey().equals(key))
                return true;
        }

        return false;
    }

    private ArrayList<LevelObject> getLevelObjects() {
        return this.levelObjects;
    }

    public void render(SpriteBatch batch) {

        //rotate the sprites here once
        getTextures().rotateSprites();

        for (int i = 0; i < getLevelObjects().size(); i++) {

            //render the level object
            getLevelObjects().get(i).render(batch);
        }

        //render the player here so it is on top of other objects
        if (getPlayer() != null)
            getPlayer().render(batch);

        //render our goal indicator
        if (getIndicator() != null)
            getIndicator().render(batch);

        //draw the overlay
        if (getOverlay() != null)
            getOverlay().draw(batch);
    }
}