package com.gamesbykevin.slide.screen;

import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.level.objects.*;
import com.gamesbykevin.slide.textures.Textures;

import static com.gamesbykevin.slide.level.Level.updateCoordinates;
import static com.gamesbykevin.slide.level.LevelHelper.MAX_COLS;
import static com.gamesbykevin.slide.level.LevelHelper.MAX_ROWS;
import static com.gamesbykevin.slide.textures.Textures.Key.*;

public class CreateScreenHelper {

    private static LevelObject getLevelObject(CreateScreen screen) {

        if (screen.getSelectedId() != null) {

            return screen.getLevel().getLevelObject(screen.getSelectedId());

        } else {

            //get our touch coordinates
            final float x = screen.getGame().getController().getTouchPos().x;
            final float y = screen.getGame().getController().getTouchPos().y;

            return screen.getLevel().getLevelObject(x, y);
        }
    }

    private static LevelObject getCreateObject(CreateScreen screen) {

        for (int i = 0; i < screen.getCreateObjects().size(); i++) {

            LevelObject obj = screen.getCreateObjects().get(i);

            if (obj.getId().equals(screen.getSelectedId()))
                return obj;
        }

        return null;
    }

    protected static void checkInput(CreateScreen screen) {

        //get our touch coordinates
        final float x = screen.getGame().getController().getTouchPos().x;
        final float y = screen.getGame().getController().getTouchPos().y;

        //current location
        int col = Level.getColumn(x);
        int row = Level.getRow(y);

        if (screen.isPressed()) {

            //make sure we don't already have something selected
            if (screen.getSelectedId() == null) {

                //check to see what we have selected
                for (int i = 0; i < screen.getCreateObjects().size(); i++) {

                    LevelObject obj = screen.getCreateObjects().get(i);

                    //skip if we didn't select
                    if (!obj.contains(x, y))
                        continue;

                    //reset the level to prevent placing an object on another
                    screen.getLevel().reset();

                    //store the selected item
                    screen.setSelectedId(obj.getId());
                }

                //if still null, check if we selected an item from the level
                if (screen.getSelectedId() == null) {

                    LevelObject obj = screen.getLevel().getLevelObject(x, y);

                    if (obj != null) {

                        //reset the level to prevent placing an object on another
                        screen.getLevel().reset();

                        //store the selected item
                        screen.setSelectedId(obj.getId());
                    }
                }

            }

        } else if (screen.isDragged()) {

            if (screen.getSelectedId() != null) {

                //move the selected create object
                LevelObject obj = getCreateObject(screen);

                if (obj != null) {

                    //update coordinates
                    obj.setX(x - (obj.getW() / 2));
                    obj.setY(y - (obj.getH() / 2));

                } else {

                    //check if we selected a level object
                    obj = getLevelObject(screen);

                    //update only if we have something
                    if (obj != null) {
                        obj.setX(x - (obj.getW() / 2));
                        obj.setY(y - (obj.getH() / 2));
                    }
                }
            }

        } else if (screen.isReleased()) {

            //we let go of a selected item
            if (screen.getSelectedId() != null) {

                //get the create item
                LevelObject obj = getCreateObject(screen);

                if (obj != null) {

                    releasedCreateObject(screen, obj, col, row);

                } else {

                    //get the level object
                    obj = getLevelObject(screen);

                    //release it accordingly
                    if (obj != null)
                        releasedLevelObject(screen, obj, col, row);
                }

                //reset object location
                if (obj != null)
                    updateCoordinates(obj);

                screen.setSelectedId(null);
            }
        }
    }

    private static void releasedLevelObject(CreateScreen screen, LevelObject obj, int col, int row) {

        if (obj == null)
            return;

        //if position is out of bounds, let's try to remove the object
        if (col < 0 || col >= MAX_COLS || row < 0 || row >= MAX_ROWS) {
            removeReleasedLevelObject(screen, obj);
            return;
        }

        //we can't place an object on top of another
        if (screen.getLevel().getLevelObject(col, row) != null)
            return;

        LevelObject obj1;
        LevelObject obj2;

        switch (obj.getKey()) {

            case WallLeft:
                if (col >= 0 && col < MAX_COLS - 2 && screen.getLevel().getLevelObject(col + 1, row) == null && screen.getLevel().getLevelObject(col + 2, row) == null) {

                    obj1 = screen.getLevel().getLevelObject((int) (obj.getCol() + 1), (int) (obj.getRow()));
                    obj1.setCol(col + 1);
                    obj1.setRow(row);
                    updateCoordinates(obj1);

                    obj2 = screen.getLevel().getLevelObject((int) (obj.getCol() + 2), (int) (obj.getRow()));
                    obj2.setCol(col + 2);
                    obj2.setRow(row);
                    updateCoordinates(obj2);
                    ((PartialWall)obj2).setResetCol(obj2.getCol());
                    ((PartialWall)obj2).setResetRow(obj2.getRow());

                    obj.setCol(col);
                    obj.setRow(row);
                    ((PartialWall)obj).setResetCol(col);
                    ((PartialWall)obj).setResetRow(row);
                    updateCoordinates(obj);
                }
                break;

            case WallRight:
                if (col > 1 && col < MAX_COLS && screen.getLevel().getLevelObject(col - 1, row) == null && screen.getLevel().getLevelObject(col - 2, row) == null) {

                    obj1 = screen.getLevel().getLevelObject((int) (obj.getCol() - 1), (int) (obj.getRow()));
                    obj1.setCol(col - 1);
                    obj1.setRow(row);
                    updateCoordinates(obj1);

                    obj2 = screen.getLevel().getLevelObject((int) (obj.getCol() - 2), (int) (obj.getRow()));
                    obj2.setCol(col - 2);
                    obj2.setRow(row);
                    updateCoordinates(obj2);
                    ((PartialWall)obj2).setResetCol(obj2.getCol());
                    ((PartialWall)obj2).setResetRow(obj2.getRow());

                    obj.setCol(col);
                    obj.setRow(row);
                    updateCoordinates(obj);
                    ((PartialWall)obj).setResetCol(obj.getCol());
                    ((PartialWall)obj).setResetRow(obj.getRow());
                }
                break;

            case WallConnectorH:
                if (col > 0 && col < MAX_COLS - 1 && screen.getLevel().getLevelObject(col - 1, row) == null && screen.getLevel().getLevelObject(col + 1, row) == null) {

                    obj1 = screen.getLevel().getLevelObject((int) (obj.getCol() - 1), (int) (obj.getRow()));
                    obj1.setCol(col - 1);
                    obj1.setRow(row);
                    updateCoordinates(obj1);
                    ((PartialWall)obj1).setResetCol(obj1.getCol());
                    ((PartialWall)obj1).setResetRow(obj1.getRow());

                    obj2 = screen.getLevel().getLevelObject((int) (obj.getCol() + 1), (int) (obj.getRow()));
                    obj2.setCol(col + 1);
                    obj2.setRow(row);
                    updateCoordinates(obj2);
                    ((PartialWall)obj2).setResetCol(obj2.getCol());
                    ((PartialWall)obj2).setResetRow(obj2.getRow());

                    obj.setCol(col);
                    obj.setRow(row);
                    updateCoordinates(obj);
                }
                break;

            case WallUp:
                if (row > 1 && row < MAX_ROWS && screen.getLevel().getLevelObject(col, row - 1) == null && screen.getLevel().getLevelObject(col, row - 2) == null) {

                    obj1 = screen.getLevel().getLevelObject((int) (obj.getCol()), (int) (obj.getRow() - 1));
                    obj1.setCol(col);
                    obj1.setRow(row - 1);
                    updateCoordinates(obj1);

                    obj2 = screen.getLevel().getLevelObject((int) (obj.getCol()), (int) (obj.getRow() - 2));
                    obj2.setCol(col);
                    obj2.setRow(row - 2);
                    updateCoordinates(obj2);
                    ((PartialWall)obj2).setResetCol(obj2.getCol());
                    ((PartialWall)obj2).setResetRow(obj2.getRow());

                    obj.setCol(col);
                    obj.setRow(row);
                    updateCoordinates(obj);
                    ((PartialWall)obj).setResetCol(obj.getCol());
                    ((PartialWall)obj).setResetRow(obj.getRow());
                }
                break;

            case WallDown:
                if (row >= 0 && row < MAX_ROWS - 2 && screen.getLevel().getLevelObject(col, row + 1) == null && screen.getLevel().getLevelObject(col, row + 2) == null) {

                    obj1 = screen.getLevel().getLevelObject((int) (obj.getCol()), (int) (obj.getRow() + 1));
                    obj1.setCol(col);
                    obj1.setRow(row + 1);
                    updateCoordinates(obj1);

                    obj2 = screen.getLevel().getLevelObject((int) (obj.getCol()), (int) (obj.getRow() + 2));
                    obj2.setCol(col);
                    obj2.setRow(row + 2);
                    updateCoordinates(obj2);
                    ((PartialWall)obj2).setResetCol(obj2.getCol());
                    ((PartialWall)obj2).setResetRow(obj2.getRow());

                    obj.setCol(col);
                    obj.setRow(row);
                    updateCoordinates(obj);
                    ((PartialWall)obj).setResetCol(obj.getCol());
                    ((PartialWall)obj).setResetRow(obj.getRow());
                }
                break;

            case WallConnectorV:
                if (row > 0 && row < MAX_ROWS - 1 && screen.getLevel().getLevelObject(col, row + 1) == null && screen.getLevel().getLevelObject(col, row - 1) == null) {

                    obj1 = screen.getLevel().getLevelObject((int) (obj.getCol()), (int) (obj.getRow() + 1));
                    obj1.setCol(col);
                    obj1.setRow(row + 1);
                    updateCoordinates(obj1);
                    ((PartialWall)obj1).setResetCol(obj1.getCol());
                    ((PartialWall)obj1).setResetRow(obj1.getRow());

                    obj2 = screen.getLevel().getLevelObject((int) (obj.getCol()), (int) (obj.getRow() - 1));
                    obj2.setCol(col);
                    obj2.setRow(row - 1);
                    updateCoordinates(obj2);
                    ((PartialWall)obj2).setResetCol(obj2.getCol());
                    ((PartialWall)obj2).setResetRow(obj2.getRow());

                    obj.setCol(col);
                    obj.setRow(row);
                    updateCoordinates(obj);
                }
                break;

            case Player:
                obj.setCol(col);
                obj.setRow(row);
                ((Player)obj).setStartCol(col);
                ((Player)obj).setStartRow(row);
                updateCoordinates(obj);
                screen.getLevel().reset();
                break;

            default:
                obj.setCol(col);
                obj.setRow(row);
                updateCoordinates(obj);
                break;
        }
    }

    private static void removeReleasedLevelObject(CreateScreen screen, LevelObject obj) {

        if (obj == null)
            return;

        //if an items is out of bounds let's see if we can remove it
        switch (obj.getKey()) {

            case WallLeft:
                screen.getLevel().remove(obj.getId());
                screen.getLevel().remove(screen.getLevel().getLevelObject((int)(obj.getCol() + 1), (int)(obj.getRow())).getId());
                screen.getLevel().remove(screen.getLevel().getLevelObject((int)(obj.getCol() + 2), (int)(obj.getRow())).getId());
                break;

            case WallRight:
                screen.getLevel().remove(obj.getId());
                screen.getLevel().remove(screen.getLevel().getLevelObject((int)(obj.getCol() - 1), (int)(obj.getRow())).getId());
                screen.getLevel().remove(screen.getLevel().getLevelObject((int)(obj.getCol() - 2), (int)(obj.getRow())).getId());
                break;

            case WallConnectorH:
                screen.getLevel().remove(obj.getId());
                screen.getLevel().remove(screen.getLevel().getLevelObject((int)(obj.getCol() - 1), (int)(obj.getRow())).getId());
                screen.getLevel().remove(screen.getLevel().getLevelObject((int)(obj.getCol() + 1), (int)(obj.getRow())).getId());
                break;

            case WallUp:
                screen.getLevel().remove(obj.getId());
                screen.getLevel().remove(screen.getLevel().getLevelObject((int)(obj.getCol()), (int)(obj.getRow() - 1)).getId());
                screen.getLevel().remove(screen.getLevel().getLevelObject((int)(obj.getCol()), (int)(obj.getRow() - 2)).getId());
                break;

            case WallDown:
                screen.getLevel().remove(obj.getId());
                screen.getLevel().remove(screen.getLevel().getLevelObject((int)(obj.getCol()), (int)(obj.getRow() + 1)).getId());
                screen.getLevel().remove(screen.getLevel().getLevelObject((int)(obj.getCol()), (int)(obj.getRow() + 2)).getId());
                break;

            case WallConnectorV:
                screen.getLevel().remove(obj.getId());
                screen.getLevel().remove(screen.getLevel().getLevelObject((int)(obj.getCol()), (int)(obj.getRow() - 1)).getId());
                screen.getLevel().remove(screen.getLevel().getLevelObject((int)(obj.getCol()), (int)(obj.getRow() + 1)).getId());
                break;

            case Teleporter:
                screen.getLevel().remove(obj.getId());

                //get the teleporter linked to the one we removed
                LevelObject tmp1 = screen.getLevel().getLevelObject(((Teleporter)obj).getLinkId());

                //remove the linked teleporter as well
                screen.getLevel().remove(tmp1.getId());
                break;

            case Goal:
            case Locked:
            case Key:
            case Player:
                //we can never remove these items
                break;

            default:
                screen.getLevel().remove(obj.getId());
                break;
        }
    }

    private static void releasedCreateObject(CreateScreen screen, LevelObject obj, int col, int row) {

        if (obj == null)
            return;

        //if we didn't release within the level, no need to continue
        if (col < 0 || col >= MAX_COLS || row < 0 || row >= MAX_ROWS)
            return;

        //also make sure we aren't placing the object on top of another
        if (screen.getLevel().getLevelObject(col, row) != null)
            return;

        LevelObject tmp;

        //let's see what is added so we can act accordingly
        switch (obj.getKey()) {

            //add 3 items to the level
            case WallLeft:
                if (col < MAX_COLS - 2 && screen.getLevel().getLevelObject(col + 1, row) == null && screen.getLevel().getLevelObject(col + 2, row) == null) {
                    screen.getLevel().add(LevelObjectHelper.create(obj.getKey(), col, row));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallConnectorH,col + 1, row));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallRight,     col + 2, row));
                }
                break;

            case WallRight:
                if (col > 1 && screen.getLevel().getLevelObject(col - 1, row) == null && screen.getLevel().getLevelObject(col - 2, row) == null) {
                    screen.getLevel().add(LevelObjectHelper.create(obj.getKey(), col, row));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallConnectorH,col - 1, row));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallLeft,col - 2, row));
                }
                break;

            case WallConnectorH:
                if (col > 0 && col < MAX_COLS - 1 && screen.getLevel().getLevelObject(col - 1, row) == null && screen.getLevel().getLevelObject(col + 1, row) == null) {
                    screen.getLevel().add(LevelObjectHelper.create(obj.getKey(), col, row));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallLeft,col - 1, row));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallRight,col + 1, row));
                }
                break;

            //add 3 items to the level
            case WallUp:
                if (row > 1 && screen.getLevel().getLevelObject(col, row - 1) == null && screen.getLevel().getLevelObject(col, row - 2) == null) {
                    screen.getLevel().add(LevelObjectHelper.create(obj.getKey(), col, row));
                    screen.getLevel().add(LevelObjectHelper.create(WallConnectorV, col, row - 1));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallDown, col, row - 2));
                }
                break;

            case WallDown:
                if (row < MAX_ROWS - 2 && screen.getLevel().getLevelObject(col, row + 1) == null && screen.getLevel().getLevelObject(col, row + 2) == null) {
                    screen.getLevel().add(LevelObjectHelper.create(obj.getKey(), col, row));
                    screen.getLevel().add(LevelObjectHelper.create(WallConnectorV, col, row + 1));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallUp, col, row + 2));
                }
                break;

            case WallConnectorV:
                if (row > 0 && row < MAX_ROWS - 1 && screen.getLevel().getLevelObject(col, row + 1) == null && screen.getLevel().getLevelObject(col, row - 1) == null) {
                    screen.getLevel().add(LevelObjectHelper.create(obj.getKey(), col, row));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallDown, col, row - 1));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallUp, col, row + 1));
                }
                break;

            //check if there is a locked goal
            case Goal:
                tmp = screen.getLevel().getLevelObject(Locked);

                if (tmp != null) {
                    tmp.setKey(Goal);
                    tmp.setCol(col);
                    tmp.setRow(row);
                    updateCoordinates(tmp);

                    //we don't need the key anymore
                    screen.getLevel().remove(Textures.Key.Key);
                } else {
                    tmp = screen.getLevel().getLevelObject(Goal);

                    if (tmp != null) {
                        tmp.setCol(col);
                        tmp.setRow(row);
                        updateCoordinates(tmp);
                    } else {
                        //this shouldn't happen as there should already be a goal
                    }
                }
                break;

            //check if there is an unlocked goal, then add key
            case Locked:
                tmp = screen.getLevel().getLevelObject(Goal);

                //if there is a goal, we can update the existing because there can be only 1
                if (tmp != null) {
                    tmp.setKey(Locked);
                    tmp.setCol(col);
                    tmp.setRow(row);
                    updateCoordinates(tmp);

                    //we also need to add a key to solve the level
                    LevelObject levelObject = LevelObjectHelper.create(Textures.Key.Key, col, row);
                    screen.getLevel().assignLocation(levelObject, col, row);
                    screen.getLevel().add(levelObject);
                } else {

                    tmp = screen.getLevel().getLevelObject(Locked);

                    //if locked exists we only need to update it's location
                    if (tmp != null) {
                        tmp.setCol(col);
                        tmp.setRow(row);
                        updateCoordinates(tmp);
                    } else {
                        //this shouldn't happen
                    }
                }
                break;

            //add an extra teleporter
            case Teleporter:

                //make sure the telporters are linked togethr
                Teleporter teleporter1 = (Teleporter)LevelObjectHelper.create(obj.getKey(), col, row);
                Teleporter teleporter2 = (Teleporter)LevelObjectHelper.create(obj.getKey(), col, row);
                teleporter1.setLinkId(teleporter2.getId());
                teleporter2.setLinkId(teleporter1.getId());

                //add the teleporter to the level
                screen.getLevel().add(teleporter1);

                //let's find an open spot for the other teleporter
                screen.getLevel().assignLocation(teleporter2, col, row);

                //and add that to the level as well
                screen.getLevel().add(teleporter2);
                break;

            default:
                //anything else we can just add to the level
                screen.getLevel().add(LevelObjectHelper.create(obj.getKey(), col, row));
                break;
        }
    }
}