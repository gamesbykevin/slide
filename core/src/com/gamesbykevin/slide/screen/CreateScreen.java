package com.gamesbykevin.slide.screen;

import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.level.LevelHelper;
import com.gamesbykevin.slide.level.objects.LevelObject;
import com.gamesbykevin.slide.level.objects.LevelObjectHelper;
import com.gamesbykevin.slide.level.objects.Player;
import com.gamesbykevin.slide.level.objects.WallConnector;
import com.gamesbykevin.slide.textures.Textures;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.slide.level.LevelHelper.MAX_COLS;
import static com.gamesbykevin.slide.level.LevelHelper.MAX_ROWS;
import static com.gamesbykevin.slide.level.objects.LevelObject.DEFAULT_WIDTH;
import static com.gamesbykevin.slide.screen.CreateScreenHelper.checkInput;
import static com.gamesbykevin.slide.screen.GameScreenHelper.maintainFps;

public class CreateScreen extends LevelScreen {

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

    private boolean released = false;

    //what type did we select
    private String selectedId = null;

    //keep track of the teleporter key
    private int teleporterKeyIndex = 0;

    public CreateScreen(MyGdxGame game) {
        super(game);

        //create our list
        this.createObjects = new ArrayList<>();

        int row = -1;

        Level.START_X = LEVEL_X;
        Level.START_Y = LEVEL_Y;

        //create the divider
        this.divider = (WallConnector)LevelObjectHelper.create(Textures.Key.WallConnectorH, 0, -1);

        //add our customized objects
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.Teleporter,     1f,    row - 1));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.Wall,           3f,    row - 1));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.Danger,         5f,    row - 1));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.Bomb,           7f,    row - 1));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.Goal,           9f,    row - 1));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.Locked,         11f,   row - 1));

        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.WallUp,         13,    row - 1));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.WallConnectorV, 13,    row - 2));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.WallDown,       13,    row - 3));

        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.WallLeft,       1,     row - 3));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.WallConnectorH, 2,     row - 3));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.WallRight,      3,     row - 3));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.RedirectNW,     5,     row - 3));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.RedirectNE,     7,     row - 3));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.RedirectSE,     9,     row - 3));
        getCreateObjects().add(LevelObjectHelper.create(Textures.Key.RedirectSW,     11,    row - 3));

        //create an empty level
        super.setLevel(LevelHelper.create(new ArrayList<>()));

        //every level has to have a player
        Player player = (Player)LevelObjectHelper.create(Textures.Key.Player, (MAX_COLS / 2), (MAX_ROWS / 2));
        player.setStartCol((int)player.getCol());
        player.setStartRow((int)player.getRow());
        getLevel().add(player);

        //every level has to have a goal
        LevelObject goal = LevelObjectHelper.create(Textures.Key.Goal, (MAX_COLS / 2) + 2, (MAX_ROWS / 2));
        getLevel().add(goal);

        //make sure the level start coordinates are correct
        Level.START_X = LEVEL_X;
        Level.START_Y = LEVEL_Y;
        getLevel().reset();

        //start at the beginning
        setTeleporterKeyIndex(0);
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
        getLevel().reset();
    }

    @Override
    public void show() {
        super.show();

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

        //call parent
        super.render(delta);

        for (int col = 0; col < MAX_COLS; col++) {
            getDivider().setX(col * DEFAULT_WIDTH);
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