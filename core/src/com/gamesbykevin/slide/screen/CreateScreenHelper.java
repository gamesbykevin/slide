package com.gamesbykevin.slide.screen;

import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.level.objects.*;
import com.gamesbykevin.slide.textures.Textures;

import static com.gamesbykevin.slide.level.Level.updateCoordinates;
import static com.gamesbykevin.slide.level.LevelHelper.MAX_COLS;
import static com.gamesbykevin.slide.level.LevelHelper.MAX_ROWS;

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

        if (screen.isPressed()) {

            if (screen.getSelectedId() == null) {

                //check to see what we have selected
                for (int i = 0; i < screen.getCreateObjects().size(); i++) {

                    LevelObject obj = screen.getCreateObjects().get(i);

                    //skip if we didn't select
                    if (!obj.contains(x, y))
                        continue;

                    screen.setSelectedId(obj.getId());
                }

                //if still null, check if we selected an item from the level
                if (screen.getSelectedId() == null) {

                    LevelObject obj = screen.getLevel().getLevelObject(x, y);

                    if (obj != null) {

                        //if we selected the player, reset the level
                        if (obj.getKey() == Textures.Key.Player)
                            screen.getLevel().reset();

                        screen.setSelectedId(obj.getId());
                    }
                }

            } else {

                //move the selected object
                LevelObject obj = getCreateObject(screen);

                if (obj != null) {
                    obj.setX(x - (obj.getW() / 2));
                    obj.setY(y - (obj.getH() / 2));
                } else {

                    //check if we selected a level object
                    obj = getLevelObject(screen);

                    if (obj != null) {
                        obj.setX(x - (obj.getW() / 2));
                        obj.setY(y - (obj.getH() / 2));
                    }
                }
            }

        } else {

            //we let go of a selected item
            if (screen.getSelectedId() != null) {

                //current location
                int col = Level.getColumn(x);
                int row = Level.getRow(y);

                //get the create item
                LevelObject obj = getCreateObject(screen);

                if (obj != null) {

                    //make sure we place on the screen
                    if (col >= 0 && col < MAX_COLS && row >= 0 && row < MAX_ROWS) {

                        //make sure we aren't placing on top of existing objects
                        if (screen.getLevel().getLevelObject(col, row) == null) {

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
                                        screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallConnectorV, col, row - 1));
                                        screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallDown, col, row - 2));
                                    }
                                    break;

                                case WallDown:
                                    if (row < MAX_ROWS - 2 && screen.getLevel().getLevelObject(col, row + 1) == null && screen.getLevel().getLevelObject(col, row + 2) == null) {
                                        screen.getLevel().add(LevelObjectHelper.create(obj.getKey(), col, row));
                                        screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallConnectorV, col, row + 1));
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
                                    tmp = screen.getLevel().getLevelObject(Textures.Key.Locked);

                                    if (tmp != null) {
                                        tmp.setKey(Textures.Key.Goal);
                                        tmp.setCol(col);
                                        tmp.setRow(row);
                                        updateCoordinates(tmp);
                                        screen.getLevel().remove(Textures.Key.Key);
                                    } else {
                                        tmp = screen.getLevel().getLevelObject(Textures.Key.Goal);

                                        if (tmp != null) {
                                            tmp.setCol(col);
                                            tmp.setRow(row);
                                            updateCoordinates(tmp);
                                        } else {
                                            //this shouldn't happen
                                        }
                                    }
                                    break;

                                //check if there is an unlocked goal, then add key
                                case Locked:
                                    tmp = screen.getLevel().getLevelObject(Textures.Key.Goal);

                                    if (tmp != null) {
                                        tmp.setKey(Textures.Key.Locked);
                                        tmp.setCol(col);
                                        tmp.setRow(row);
                                        updateCoordinates(tmp);
                                        LevelObject levelObject = LevelObjectHelper.create(Textures.Key.Key, col, row);
                                        screen.getLevel().assignLocation(levelObject, col, row);
                                        screen.getLevel().add(levelObject);
                                    } else {

                                        tmp = screen.getLevel().getLevelObject(Textures.Key.Locked);

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
                                    Teleporter teleporter1 = (Teleporter)LevelObjectHelper.create(obj.getKey(), col, row);
                                    Teleporter teleporter2 = (Teleporter)LevelObjectHelper.create(obj.getKey(), col, row);
                                    teleporter1.setLinkId(teleporter2.getId());
                                    teleporter2.setLinkId(teleporter1.getId());
                                    screen.getLevel().add(teleporter1);

                                    screen.getLevel().assignLocation(teleporter2, col, row);
                                    screen.getLevel().add(teleporter2);
                                    break;

                                default:
                                    screen.getLevel().add(LevelObjectHelper.create(obj.getKey(), col, row));
                                    break;
                            }
                        }
                    }

                } else {

                    //check to see if it is a level object
                    obj = getLevelObject(screen);

                    if (obj != null) {

                        if (col >= 0 && col < MAX_COLS && row >= 0 && row < MAX_ROWS) {

                            //make sure we aren't placing items on top of other items and stay in bounds
                            if (screen.getLevel().getLevelObject(col, row) == null) {

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

                        } else {

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
                                    //we can't remove these
                                    break;

                                default:
                                    screen.getLevel().remove(obj.getId());
                                    break;
                            }
                        }
                    }
                }

                //reset object location
                if (obj != null)
                    updateCoordinates(obj);

                screen.setSelectedId(null);
            }
        }
    }
}