package com.gamesbykevin.slide.screen;

import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.exception.ScreenException;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.level.LevelHelper;
import com.gamesbykevin.slide.level.objects.LevelObject;
import com.gamesbykevin.slide.level.objects.LevelObjectHelper;
import com.gamesbykevin.slide.level.objects.WallConnector;
import com.gamesbykevin.slide.preferences.AppPreferences;
import com.gamesbykevin.slide.textures.Textures;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.slide.level.LevelHelper.*;
import static com.gamesbykevin.slide.level.objects.LevelObject.DEFAULT_DIMENSION;
import static com.gamesbykevin.slide.screen.CreateScreenHelper.checkInput;
import static com.gamesbykevin.slide.screen.GameScreenHelper.maintainFps;

public class CreateScreen extends LevelScreen {

    //are we editing a level?
    public static boolean EDITING = false;

    //where do we want the level to start
    public static final int LEVEL_X = 0;
    public static final int LEVEL_Y = 160;

    //list of items we can add to our level
    private List<LevelObject> createObjects;

    //this will divide the level stage
    private WallConnector divider;

    //did we touch the screen to select a level object?
    private boolean pressed = false;

    //are we dragging something
    private boolean dragged = false;

    //did we release something
    private boolean released = false;

    //what type did we select
    private String selectedId = null;

    //keep track of the teleporter key
    private int teleporterKeyIndex = 0;

    //which save did we select
    private int saveIndex;

    //do we want to exit the screen?
    private boolean exit = false;

    public CreateScreen(MyGdxGame game) {
        super(game);
    }

    public boolean isExit() {
        return this.exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public void create() {

        //flag if we are editing a level
        EDITING = true;

        //we don't want to exit (yet...)
        setExit(false);

        //create our list
        this.createObjects = new ArrayList<>();

        final float row = -1.5f;

        //set start so we know where to render create objects
        Level.START_X = LEVEL_X;
        Level.START_Y = LEVEL_Y;

        //assume objects will be small
        DEFAULT_DIMENSION = DIMENSION_SMALL;

        //create the divider
        this.divider = (WallConnector)LevelObjectHelper.create(Textures.Key.WallConnectorH, 0, row);

        //add our customized objects
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.Teleporter0,     2f,    row - 1));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.Wall,           4f,    row - 1));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.Danger,         6f,    row - 1));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.Bomb,           8f,    row - 1));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.Goal,           10f,    row - 1));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.Locked,         12f,   row - 1));

        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.WallUp,         14f,    row - 1));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.WallConnectorV, 14f,    row - 2));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.WallDown,       14f,    row - 3));

        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.Dot,            16f,    row - 1));

        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.WallLeft,       2f,     row - 3));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.WallConnectorH, 3f,     row - 3));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.WallRight,      4f,     row - 3));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.RedirectNW,     6f,     row - 3));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.RedirectNE,     8f,     row - 3));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.RedirectSE,     10f,     row - 3));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.RedirectSW,     12f,    row - 3));

        //if we have the level saved, load it
        if (AppPreferences.hasLevelSave(getSaveIndex())) {

            //get the level data from our app preferences
            List<String> lines = LevelHelper.getCreatedLevelLines(getSaveIndex());

            //create an empty level
            super.setLevel(LevelHelper.create(lines));

        } else {

            List<String> lines = new ArrayList<>();

            for (int r = 0; r < SMALL_SIZE_ROWS; r++) {

                String line = "";

                for (int c = 0; c < SMALL_SIZE_COLS; c++) {

                    if (r == (SMALL_SIZE_ROWS / 2)) {

                        if (c == (SMALL_SIZE_COLS / 2) - 2) {
                            line += Textures.Key.Player.getFileCharKey();
                        } else if (c == (SMALL_SIZE_COLS / 2) + 2) {
                            line += Textures.Key.Goal.getFileCharKey();
                        } else {
                            line += " ";
                        }

                    } else {
                        line += " ";
                    }
                }

                //add the line to the list
                lines.add(line);
            }

            //create an empty level
            super.setLevel(LevelHelper.create(lines));

        }

        //start at the beginning
        setTeleporterKeyIndex(0);
    }

    public int getSaveIndex() {
        return this.saveIndex;
    }

    public void setSaveIndex(int saveIndex) {
        this.saveIndex = saveIndex;
    }

    public int getTeleporterKeyIndex() {
        return this.teleporterKeyIndex;
    }

    public void setTeleporterKeyIndex(int teleporterKeyIndex) {
        this.teleporterKeyIndex = teleporterKeyIndex;
    }

    public boolean isDragged() {
        return this.dragged;
    }

    public void setDragged(boolean dragged) {
        this.dragged = dragged;
    }

    public boolean isReleased() {
        return this.released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    public boolean isPressed() {
        return this.pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public WallConnector getDivider() {
        return this.divider;
    }

    public List<LevelObject> getCreateObjects() {
        return this.createObjects;
    }

    public void reset() {
        setPressed(false);
        setDragged(false);
        setReleased(false);
        Level.START_X = LEVEL_X;
        Level.START_Y = LEVEL_Y;
        getLevel().reset();
    }

    @Override
    public void show() {
        super.show();

        //reset to be safe
        reset();

        //make sure game controller has input
        getGame().getController().setInput();
    }

    public String getSelectedId() {
        return this.selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    @Override
    public void render(float delta) {

        if (isExit()) {

            //reset the level
            getLevel().reset();

            //then save the level data
            AppPreferences.setLevelSave(
                getGame().getScreenHelper().getCreateScreen().getSaveIndex(),
                    LevelHelper.getLevelCode(getGame().getScreenHelper().getCreateScreen().getLevel())
            );

            try {

                //go back to the menu screen
                getGame().getScreenHelper().changeScreen(ScreenHelper.SCREEN_SELECT_CREATE);

            } catch (ScreenException e) {
                e.printStackTrace();
            }

            //don't continue any further
            return;
        }

        //call parent
        super.render(delta);

        for (int col = SMALL_SIZE_COLS; col >= 0; col--) {
            getDivider().setX(col * DEFAULT_DIMENSION);
            getDivider().render(getBatch());
        }

        //check for any input
        checkInput(this);

        //render our customizable objects
        for (int i = 0; i < getCreateObjects().size(); i++) {
            getCreateObjects().get(i).render(getBatch());
        }

        //finished rendering
        getBatch().end();

        //keep the speed of the game steady
        maintainFps(getStart());
    }
}