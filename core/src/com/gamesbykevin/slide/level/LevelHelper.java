package com.gamesbykevin.slide.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.gamesbykevin.slide.level.objects.*;
import com.gamesbykevin.slide.textures.Textures;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.slide.level.Level.updateCoordinates;
import static com.gamesbykevin.slide.screen.CreateScreenHelper.TELEPORTER_KEYS;

public class LevelHelper {

    //different dimension sizes
    public static final int DIMENSION_SMALL = 24;
    public static final int DIMENSION_NORMAL = 32;
    public static final int DIMENSION_LARGE = 48;

    //if the level size is different we can display different sized objects
    public static final int SMALL_SIZE_COLS = 20;
    public static final int SMALL_SIZE_ROWS = 25;

    //if the level size is different we can display different sized objects
    public static final int NORMAL_SIZE_COLS = 15;
    public static final int NORMAL_SIZE_ROWS = 20;

    //if the level size is different we can display different sized objects
    public static final int LARGE_SIZE_COLS = 10;
    public static final int LARGE_SIZE_ROWS = 15;

    //how many teleporters can we have in 1 level
    public static final int TELEPORTER_LIMIT = TELEPORTER_KEYS.length;

    //each line will be separated by this character
    public static final String NEW_LINE_CHAR = "\n";

    public static Level create(String filename) throws IOException {

        //read text file to create level
        FileHandle handle = Gdx.files.internal("levels/" + filename);
        InputStream is = handle.read();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        List<String> lines = new ArrayList<String>();
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        //don't need these objects anymore
        reader.close();
        reader = null;
        is.close();
        is = null;
        handle = null;

        return create(lines);
    }

    public static Level create(List<String> lines) {

        int rows = lines.size();
        int cols = 0;

        //figure out how many columns, so we can fit the level on the screen
        for (String tmp : lines) {
            if (tmp.length() > cols)
                cols = tmp.length();
        }

        //our level reference
        Level level;

        //create list for our teleporters
        List<TeleportLocation> teleportLocations = new ArrayList<>();

        //if the columns are longer, rotate the level
        final boolean flip = (cols > rows);

        if (flip) {

            //create our level
            level = new Level(rows, cols);

            //start at the end
            int row = cols - 1;

            //continue until we check everything
            while (row >= 0) {

                for (int col = 0; col < rows; col++) {

                    //get the current line
                    String line = lines.get(col);

                    //stay in bounds
                    if (row < line.length()) {

                        //get the current character
                        String tmp = line.substring(row, row + 1);

                        //create the level object
                        createLevelObject(level, true, teleportLocations, tmp, col, row);
                    }
                }

                //decrease the row
                row--;
            }

        } else {

            //create our level
            level = new Level(cols, rows);

            int row = lines.size() - 1;

            while (row >= 0) {

                String line = lines.get(row);

                for (int col = 0; col < line.length(); col++) {

                    //get the current character
                    String tmp = line.substring(col, col + 1);

                    //create the level object
                    createLevelObject(level, false, teleportLocations, tmp, col, (lines.size()-1) - row);
                }

                //decrease the row
                row--;
            }
        }

        //let's connect the teleporters now
        for (int i = 0; i < teleportLocations.size(); i++) {

            TeleportLocation tmp1 = teleportLocations.get(i);

            for (int j = 0; j < teleportLocations.size(); j++) {

                //don't check self
                if (j == i)
                    continue;

                TeleportLocation tmp2 = teleportLocations.get(j);

                //if these match, link them together
                if (tmp1.charKey.equalsIgnoreCase(tmp2.charKey)) {
                    Teleporter tele1 = (Teleporter)level.getLevelObject(tmp1.id);
                    Teleporter tele2 = (Teleporter)level.getLevelObject(tmp2.id);
                    tele1.setFileCharKey(tmp1.charKey);
                    tele2.setFileCharKey(tmp2.charKey);
                    tele1.setLinkId(tmp2.id);
                    tele2.setLinkId(tmp1.id);
                }
            }
        }

        teleportLocations.clear();
        teleportLocations = null;

        return level;
    }

    /**
     *
     * @param level Level instance we are adding our object to
     * @param flip Are we rotating the level to fit it on the screen?
     * @param teleportLocations List of teleporters that we need to link together
     * @param tmp Character key of our level object
     * @param col column location
     * @param row row location
     */
    private static void createLevelObject(Level level, boolean flip, List<TeleportLocation> teleportLocations, String tmp, int col, int row) {

        //skip checking space or underscore
        if (tmp.equalsIgnoreCase(" ") || tmp.equalsIgnoreCase("_"))
            return;

        //get the appropriate key
        Textures.Key key = Textures.getKey(tmp);

        //our level object
        LevelObject obj;

        //if the key is null, it's one of our teleporters
        if (key == null) {

            //if the key doesn't match, we assume it is a teleporter
            obj = LevelObjectHelper.create(Textures.Key.Teleporter, col, row);

            //add our teleport location
            teleportLocations.add(new TeleportLocation(tmp, obj.getId()));

            //add object to the level list
            level.add(obj);

        } else {

            //if flipping we need to change some things here
            if (flip) {
                switch (key) {

                    case RedirectNE:
                        key = Textures.Key.RedirectNW;
                        break;

                    case RedirectSE:
                        key = Textures.Key.RedirectNE;
                        break;

                    case RedirectNW:
                        key = Textures.Key.RedirectSW;
                        break;

                    case RedirectSW:
                        key = Textures.Key.RedirectSE;
                        break;

                    case WallDown:
                        key = Textures.Key.WallRight;
                        break;

                    case WallUp:
                        key = Textures.Key.WallLeft;
                        break;

                    case WallLeft:
                        key = Textures.Key.WallDown;
                        break;

                    case WallRight:
                        key = Textures.Key.WallUp;
                        break;

                    case WallConnectorH:
                        key = Textures.Key.WallConnectorV;
                        break;

                    case WallConnectorV:
                        key = Textures.Key.WallConnectorH;
                        break;
                }
            }

            //else create the level object
            obj = LevelObjectHelper.create(key, col, row);

            //we need to do some extra stuff for a few level objects
            switch(key) {

                //if this is the player, mark this as the start of the level
                case Player:

                    //mark this as the start
                    ((Player)obj).setStartCol(col);
                    ((Player)obj).setStartRow(row);

                    //assign this player to our level
                    level.add(obj);
                    break;

                //if this is the goal we also need to create the indicator to show where the goal is
                case Goal:
                    level.setIndicator(LevelObjectHelper.create(Textures.Key.Indicator, (int)obj.getCol(), (int)obj.getRow() + 1));
                    level.add(obj);
                    break;

                //store the original location so we can reset
                case WallLeft:
                case WallRight:
                case WallUp:
                case WallDown:
                    ((PartialWall)obj).setResetCol(obj.getCol());
                    ((PartialWall)obj).setResetRow(obj.getRow());
                    level.add(obj);
                    break;

                default:
                    level.add(obj);
                    break;
            }
        }
    }

    private static class TeleportLocation {

        private final String id;
        private final String charKey;

        private TeleportLocation(String charKey, String id) {
            this.id = id;
            this.charKey = charKey;
        }
    }

    //here we will find an open location nearby for the specified level object
    public static void assignLocation(Level level, LevelObject object, int col, int row) {

        for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++) {

                if (x == 0 && y == 0)
                    continue;

                if (col + x < 0 || col + x >= level.getCols())
                    continue;
                if (row + y < 0 || row + y >= level.getRows())
                    continue;

                //make sure there are no existing objects here
                if (level.getLevelObject(col + x, row + y) == null) {
                    object.setCol(col + x);
                    object.setRow(row + y);
                    updateCoordinates(object);
                    return;
                }
            }
        }

        for (int x = 0; x < level.getCols(); x++) {
            for (int y = 0; y < level.getRows(); y++) {
                if (level.getLevelObject(col + x, row + y) == null) {
                    object.setCol(col + x);
                    object.setRow(row + y);
                    updateCoordinates(object);
                    return;
                }
            }
        }
    }

    public static String getLevelCode(Level level) {

        String line = "";
        System.out.println("-----------------START-----------------");

        //is the level empty? (meaning only a player and goal)
        boolean empty = true;

        for (int row = level.getRows() - 1; row >= 0; row--) {

            String tmp = "";

            for (int col = 0; col < level.getCols(); col++) {

                LevelObject obj = level.getLevelObject(col, row);

                String charKey = "";

                if (obj == null) {

                    //if null we will add a space
                    charKey = " ";

                } else {

                    switch (obj.getKey()) {

                        //teleporter keys are stored differently
                        case Teleporter:
                            charKey = ((Teleporter)obj).getFileCharKey();
                            empty = false;
                            break;

                        case Player:
                        case Goal:
                            charKey = obj.getKey().getFileCharKey();
                            break;

                        default:
                            charKey = obj.getKey().getFileCharKey();
                            empty = false;
                            break;
                    }
                }

                //concatenate the character key
                line += charKey;
                tmp += charKey;
            }

            System.out.println(tmp);

            if (row > 0)
                line += NEW_LINE_CHAR;
        }

        System.out.println("-----------------END-------------------");

        //if the level is empty we won't store anything
        if (empty)
            line = "";

        return line;
    }
}