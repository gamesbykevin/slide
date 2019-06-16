package com.gamesbykevin.slide.level;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.slide.level.objects.*;
import com.gamesbykevin.slide.level.objects.LevelObject;
import com.gamesbykevin.slide.model.GameModel;
import com.gamesbykevin.slide.textures.Textures;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.gamesbykevin.slide.level.objects.LevelObject.*;
import static com.gamesbykevin.slide.MyGdxGame.FRAME_MS;
import static com.gamesbykevin.slide.screen.ParentScreen.SCREEN_HEIGHT;
import static com.gamesbykevin.slide.screen.ParentScreen.SCREEN_WIDTH;

public class Level implements ILevel {

    //list of objects in the level
    private ArrayList<LevelObject> levelObjects;

    //start position of level
    private static int START_X = 64;
    private static int START_Y = 64;

    //is the level solved
    private boolean solved = false;

    //player we interact with
    private Player player;

    //how much time has passed since we completed the level
    private float lapsedComplete = 0;

    //how long to wait when we complete the level (in milliseconds)
    public static final float LEVEL_COMPLETE_DELAY = 2000;

    //show user where they need to go
    private LevelObject indicator;

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

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
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

        //update the player
        getPlayer().update();

        //update the indicator
        getIndicator().update();

        //update the (x, y) coordinates
        updateCoordinates(getPlayer());

        //update our objects
        for (int j = 0; j < getLevelObjects().size(); j++) {

            //get current level object
            LevelObject tmp = getLevelObjects().get(j);

            //update the (x, y) coordinates
            updateCoordinates(tmp);

            //update the level object accordingly
            tmp.update();

            //if there is collision update accordingly
            if (getPlayer().hasCollisionNormal(tmp))
                tmp.updateCollision(this);
        }

        //if we went off screen reset the level
        if (!getPlayer().hasBounds())
            reset();
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

        //reset the player
        getPlayer().reset(this);

        for (int i = 0; i < getLevelObjects().size(); i++) {

            //get the current level object so it can be reset
            getLevelObjects().get(i).reset(this);
        }

        //reset the indicator
        getIndicator().reset(this);
    }

    public LevelObject getLevelObject(UUID id) {

        for (int i = 0; i < getLevelObjects().size(); i++) {

            LevelObject object = getLevelObjects().get(i);

            if (object.getId().equals(id))
                return object;
        }

        return null;
    }

    public LevelObject getLevelObject(Textures.Key key, int col, int row) {

        for (int i = 0; i < getLevelObjects().size(); i++) {

            LevelObject object = getLevelObjects().get(i);

            if (object.getCol() != col || object.getRow() != row)
                continue;

            if (object.getKey() != key)
                continue;

            return object;
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

    private ArrayList<LevelObject> getLevelObjects() {
        return this.levelObjects;
    }

    public void render(GameModel model, SpriteBatch batch) {

        for (int i = 0; i < getLevelObjects().size(); i++) {

            //get the level object
            LevelObject obj = getLevelObjects().get(i);

            //get the sprite containing our texture
            Sprite sprite = model.getTextures().getSprite(obj.getKey());

            //render the level object
            obj.render(batch, sprite, model.getFont());
        }

        //render the player as well
        getPlayer().render(batch, model.getTextures().getSprite(getPlayer().getKey()), model.getFont());

        //render our goal indicator
        getIndicator().render(batch, model.getTextures().getSprite(getIndicator().getKey()), null);
    }
}