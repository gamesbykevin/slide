package com.gamesbykevin.slide.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.slide.graphics.Overlay;
import com.gamesbykevin.slide.level.objects.*;
import com.gamesbykevin.slide.level.objects.LevelObject;
import com.gamesbykevin.slide.rumble.Rumble;
import com.gamesbykevin.slide.audio.GameAudio;

import java.util.ArrayList;

import static com.gamesbykevin.slide.MyGdxGame.*;
import static com.gamesbykevin.slide.graphics.Overlay.OVERLAY_DURATION_GAMEPLAY;
import static com.gamesbykevin.slide.level.LevelHelper.*;
import static com.gamesbykevin.slide.level.objects.LevelObject.DEFAULT_DIMENSION;
import static com.gamesbykevin.slide.preferences.AppPreferences.*;
import static com.gamesbykevin.slide.screen.CreateScreen.EDITING;
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

    //do we reset the level
    private boolean reset = false;

    //how much time has passed since we completed the level
    private float lapsedComplete = 0;

    //how long to wait when we complete the level (in milliseconds)
    public static final float LEVEL_COMPLETE_DELAY = 3500;

    //show user where they need to go
    private LevelObject indicator;

    //how big is our level?
    private final int cols, rows;

    //what is the level objective
    private Objective objective = Objective.Goal;

    //keep track of the number of items we impacted
    private int countBomb = 0, countGem = 0;

    //keep track of the total
    private int totalBomb = 0, totalGem = 0;

    //obtain player reference for performance reasons
    private Player player;

    //the goal we are trying to get to
    private Goal goal;

    //how long have we been playing the level
    private float duration;

    //how do we display the duration
    private String durationDesc = null;

    public Level() {
        this(SMALL_SIZE_COLS, SMALL_SIZE_ROWS);
    }

    public Level(int cols, int rows) {

        //create new textures for this level
        resetTextures();

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

    public String getDurationDesc() {

        //format duration date if we haven't set it yet
        if (this.durationDesc == null) {

            //do it the old school way so it will work with html
            long millis = (long)getDuration() % 1000;
            long second = ((long)getDuration() / 1000) % 60;
            long minute = ((long)getDuration() / (1000 * 60)) % 60;

            String secondDesc = (second < 10) ? "0" + second : "" + second;
            String minuteDesc = (minute < 10) ? "0" + minute : "" + minute;

            //String.format("%02d:%02d.%d", minute, second, millis);
            this.durationDesc = minuteDesc + ":" + secondDesc + "." + millis;
        }

        return this.durationDesc;
    }

    public float getDuration() {
        return this.duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public boolean isReset() {
        return this.reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public int getCountBomb() {
        return this.countBomb;
    }

    public void setCountBomb(int countBomb) {
        this.countBomb = countBomb;
    }

    public int getCountGem() {
        return this.countGem;
    }

    public void setCountGem(int countGem) {
        this.countGem = countGem;
    }

    public int getTotalBomb() {
        return this.totalBomb;
    }

    public void setTotalBomb(int totalBomb) {
        this.totalBomb = totalBomb;
    }

    public int getTotalGem() {
        return this.totalGem;
    }

    public void setTotalGem(int totalGem) {
        this.totalGem = totalGem;
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

        //store player reference
        if (this.player == null)
            this.player = (Player)getLevelObject(LevelObject.Type.Player);

        return this.player;
    }

    public Goal getGoal() {

        if (this.goal == null) {

            switch (getObjective()) {
                case Goal:
                    this.goal = (Goal) getLevelObject(LevelObject.Type.Goal);
                    break;

                case Unlock:
                    this.goal = (Goal) getLevelObject(LevelObject.Type.LockedGoal);
                    break;
            }
        }

        return this.goal;
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

        //if we are marking this solved for the first time
        if (!EDITING && !isSolved() && solved) {

            //record that we beat the level
            setLevelCompleted(LEVEL_INDEX, true);
        }

        this.solved = solved;
    }

    @Override
    public void update() {

        //reset if we have the flag
        if (isReset()) {
            setReset(false);
            reset();
            return;
        }

        //no need to do anything else here
        if (isSolved()) {
            setLapsedComplete(getLapsedComplete() + FRAME_MS);
            return;
        }

        //keep updating the game timer
        setDuration(getDuration() + FRAME_MS);

        //update the indicator
        if (getIndicator() != null)
            getIndicator().update(this);

        //get the player
        Player player = getPlayer();

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

            if (player.hasCollision(tmp))
                tmp.updateCollision(this);
        }

        //if we went off screen reset the level
        if (!player.hasBounds()) {
            Rumble.reset();
            reset();

            //play sound effect
            GameAudio.playSfx(GameAudio.SoundEffect.Explosion);

            //vibrate if the option is enabled
            if (hasEnabledVibrate())
                Gdx.input.vibrate(DURATION_VIBRATE);
        }

        //check if we meet the level objective
        if (!EDITING)
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

        //reset playing time
        setDuration(0);

        //reset the time lapsed
        setLapsedComplete(0);

        //the level has not been solved (yet)
        setSolved(false);

        //reset the count back to 0
        setCountBomb(0);
        setCountGem(0);

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

            //count the number of bombs in case the objective is to destroy all bombs
            case Bomb:
                setTotalBomb(getTotalBomb() + 1);
                break;

            //count the number of gems in case the objective is to collect all the gems
            case Gem:
                setTotalGem(getTotalGem() + 1);
                break;

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

    public void dispose() {

        if (this.levelObjects != null) {
            for (int i = 0; i < this.levelObjects.size(); i++) {

                if (this.levelObjects.get(i) != null) {
                    this.levelObjects.get(i).dispose();
                    this.levelObjects.set(i, null);
                }
            }
            this.levelObjects.clear();
            this.levelObjects = null;
        }

        if (this.overlay != null) {
            this.overlay.dispose();
            this.overlay = null;
        }

        if (this.indicator != null) {
            this.indicator.dispose();
            this.indicator = null;
        }

        if (this.player != null) {
            this.player.dispose();
            this.player = null;
        }
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