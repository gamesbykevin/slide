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
import java.util.UUID;

public class LevelHelper {

    //since our width is limited what is the max # of columns?
    private static final int MAX_COLS = 15;

    public static Level create(String filename) throws IOException {

        //read text file to create level
        FileHandle handle = Gdx.files.internal("levels/" + filename);
        InputStream is = handle.read();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        List<String> lines = new ArrayList<>();
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

        if (cols >= MAX_COLS) {

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
                    createLevelObject(level, false, teleportLocations, tmp, col, lines.size() - row);
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
     * @param level
     * @param flip Are we rotating the level to fit on the screen?
     * @param teleportLocations
     * @param tmp
     * @param col
     * @param row
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
                    level.setPlayer(((Player)obj));
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

        private final UUID id;
        private final String charKey;

        private TeleportLocation(String charKey, UUID id) {
            this.id = id;
            this.charKey = charKey;
        }
    }
}