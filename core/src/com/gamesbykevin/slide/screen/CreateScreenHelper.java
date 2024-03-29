package com.gamesbykevin.slide.screen;

import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.level.LevelHelper;
import com.gamesbykevin.slide.level.objects.*;
import com.gamesbykevin.slide.textures.Textures;

import static com.gamesbykevin.slide.level.Level.updateCoordinates;
import static com.gamesbykevin.slide.level.LevelHelper.*;
import static com.gamesbykevin.slide.level.objects.LevelObject.Type;
import static com.gamesbykevin.slide.textures.Textures.Key.*;

public class CreateScreenHelper {

    //valid characters to use for our teleporters
    public static final String[] TELEPORTER_KEYS = {
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "!", "@", "#", "$", "%", "^", "&", "*", "(", ")"
    };

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
        if (col < 0 || col >= SMALL_SIZE_COLS || row < 0 || row >= SMALL_SIZE_ROWS) {
            removeReleasedLevelObject(screen, obj);
            return;
        }

        //we can't place an object on top of another
        if (screen.getLevel().getLevelObject(col, row) != null)
            return;

        LevelObject obj1;
        LevelObject obj2;

        switch (obj.getTextureKey()) {

            case WallLeft:
                if (col >= 0 && col < SMALL_SIZE_COLS - 2 && screen.getLevel().getLevelObject(col + 1, row) == null && screen.getLevel().getLevelObject(col + 2, row) == null) {

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
                if (col > 1 && col < SMALL_SIZE_COLS && screen.getLevel().getLevelObject(col - 1, row) == null && screen.getLevel().getLevelObject(col - 2, row) == null) {

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
                if (col > 0 && col < SMALL_SIZE_COLS - 1 && screen.getLevel().getLevelObject(col - 1, row) == null && screen.getLevel().getLevelObject(col + 1, row) == null) {

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
                if (row > 1 && row < SMALL_SIZE_ROWS && screen.getLevel().getLevelObject(col, row - 1) == null && screen.getLevel().getLevelObject(col, row - 2) == null) {

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
                if (row >= 0 && row < SMALL_SIZE_ROWS - 2 && screen.getLevel().getLevelObject(col, row + 1) == null && screen.getLevel().getLevelObject(col, row + 2) == null) {

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
                if (row > 0 && row < SMALL_SIZE_ROWS - 1 && screen.getLevel().getLevelObject(col, row + 1) == null && screen.getLevel().getLevelObject(col, row - 1) == null) {

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
        switch (obj.getTextureKey()) {

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

            case Teleporter0:
            case Teleporter1:
            case Teleporter2:
            case Teleporter3:
            case Teleporter4:
            case Teleporter5:
            case Teleporter6:
            case Teleporter7:
            case Teleporter8:
            case Teleporter9:
            case Teleporter10:
            case Teleporter11:
            case Teleporter12:
            case Teleporter13:
            case Teleporter14:
            case Teleporter15:
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

        //reset the level
        screen.getLevel().reset();
    }

    private static void releasedCreateObject(CreateScreen screen, LevelObject obj, int col, int row) {

        if (obj == null)
            return;

        //if we didn't release within the level, no need to continue
        if (col < 0 || col >= SMALL_SIZE_COLS || row < 0 || row >= SMALL_SIZE_ROWS)
            return;

        //also make sure we aren't placing the object on top of another
        if (screen.getLevel().getLevelObject(col, row) != null)
            return;

        LevelObject tmp;

        //let's see what is added so we can act accordingly
        switch (obj.getTextureKey()) {

            //add 3 items to the level
            case WallLeft:
                if (col < SMALL_SIZE_COLS - 2 && screen.getLevel().getLevelObject(col + 1, row) == null && screen.getLevel().getLevelObject(col + 2, row) == null) {
                    screen.getLevel().add(LevelObjectHelper.create(obj.getTextureKey(), col, row));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallConnectorH,col + 1, row));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallRight,     col + 2, row));
                }
                break;

            case WallRight:
                if (col > 1 && screen.getLevel().getLevelObject(col - 1, row) == null && screen.getLevel().getLevelObject(col - 2, row) == null) {
                    screen.getLevel().add(LevelObjectHelper.create(obj.getTextureKey(), col, row));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallConnectorH,col - 1, row));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallLeft,col - 2, row));
                }
                break;

            case WallConnectorH:
                if (col > 0 && col < SMALL_SIZE_COLS - 1 && screen.getLevel().getLevelObject(col - 1, row) == null && screen.getLevel().getLevelObject(col + 1, row) == null) {
                    screen.getLevel().add(LevelObjectHelper.create(obj.getTextureKey(), col, row));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallLeft,col - 1, row));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallRight,col + 1, row));
                }
                break;

            //add 3 items to the level
            case WallUp:
                if (row > 1 && screen.getLevel().getLevelObject(col, row - 1) == null && screen.getLevel().getLevelObject(col, row - 2) == null) {
                    screen.getLevel().add(LevelObjectHelper.create(obj.getTextureKey(), col, row));
                    screen.getLevel().add(LevelObjectHelper.create(WallConnectorV, col, row - 1));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallDown, col, row - 2));
                }
                break;

            case WallDown:
                if (row < SMALL_SIZE_ROWS - 2 && screen.getLevel().getLevelObject(col, row + 1) == null && screen.getLevel().getLevelObject(col, row + 2) == null) {
                    screen.getLevel().add(LevelObjectHelper.create(obj.getTextureKey(), col, row));
                    screen.getLevel().add(LevelObjectHelper.create(WallConnectorV, col, row + 1));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallUp, col, row + 2));
                }
                break;

            case WallConnectorV:
                if (row > 0 && row < SMALL_SIZE_ROWS - 1 && screen.getLevel().getLevelObject(col, row + 1) == null && screen.getLevel().getLevelObject(col, row - 1) == null) {
                    screen.getLevel().add(LevelObjectHelper.create(obj.getTextureKey(), col, row));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallDown, col, row - 1));
                    screen.getLevel().add(LevelObjectHelper.create(Textures.Key.WallUp, col, row + 1));
                }
                break;

            //check if there is a locked goal
            case Goal:
                tmp = screen.getLevel().getLevelObject(Type.LockedGoal);

                if (tmp != null) {
                    tmp.setType(Type.Goal);
                    tmp.setTextureKey(Goal);
                    tmp.setCol(col);
                    tmp.setRow(row);
                    updateCoordinates(tmp);

                    //we don't need the key anymore
                    screen.getLevel().remove(Type.Key);
                } else {
                    tmp = screen.getLevel().getLevelObject(Type.Goal);

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
                tmp = screen.getLevel().getLevelObject(Type.Goal);

                //if there is a goal, we can update the existing because there can be only 1
                if (tmp != null) {
                    tmp.setTextureKey(Locked);
                    tmp.setType(Type.LockedGoal);
                    tmp.setCol(col);
                    tmp.setRow(row);
                    updateCoordinates(tmp);

                    //we also need to add a key to solve the level
                    LevelObject levelObject = LevelObjectHelper.create(Textures.Key.Key, col, row);
                    LevelHelper.assignLocation(screen.getLevel(), levelObject, col, row);
                    screen.getLevel().add(levelObject);
                } else {

                    tmp = screen.getLevel().getLevelObject(Type.LockedGoal);

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
            case Teleporter0:
            case Teleporter1:
            case Teleporter2:
            case Teleporter3:
            case Teleporter4:
            case Teleporter5:
            case Teleporter6:
            case Teleporter7:
            case Teleporter8:
            case Teleporter9:
            case Teleporter10:
            case Teleporter11:
            case Teleporter12:
            case Teleporter13:
            case Teleporter14:
            case Teleporter15:

                //make sure the telporters are linked together
                Teleporter teleporter1 = (Teleporter)LevelObjectHelper.create(obj.getTextureKey(), col, row);
                Teleporter teleporter2 = (Teleporter)LevelObjectHelper.create(obj.getTextureKey(), col, row);

                //make sure the teleporters are linked to each other
                teleporter1.setLinkId(teleporter2.getId());
                teleporter2.setLinkId(teleporter1.getId());

                //make sure we don't use the same key
                if (screen.getTeleporterKeyIndex() >= TELEPORTER_KEYS.length ||
                        screen.getLevel().hasFileCharKey(TELEPORTER_KEYS[screen.getTeleporterKeyIndex()])) {

                    //look for a valid character not in use starting from the beginning
                    for (int index = 0; index < TELEPORTER_KEYS.length; index++) {

                        //if we don't have this then set the index here!!!
                        if (!screen.getLevel().hasFileCharKey(TELEPORTER_KEYS[index])) {
                            screen.setTeleporterKeyIndex(index);
                            break;
                        }
                    }
                }

                switch (screen.getTeleporterKeyIndex()) {
                    case 0:
                    default:
                        teleporter1.setTextureKey(Teleporter0);
                        teleporter2.setTextureKey(Teleporter0);
                        break;

                    case 1:
                        teleporter1.setTextureKey(Teleporter1);
                        teleporter2.setTextureKey(Teleporter1);
                        break;

                    case 2:
                        teleporter1.setTextureKey(Teleporter2);
                        teleporter2.setTextureKey(Teleporter2);
                        break;

                    case 3:
                        teleporter1.setTextureKey(Teleporter3);
                        teleporter2.setTextureKey(Teleporter3);
                        break;

                    case 4:
                        teleporter1.setTextureKey(Teleporter4);
                        teleporter2.setTextureKey(Teleporter4);
                        break;

                    case 5:
                        teleporter1.setTextureKey(Teleporter5);
                        teleporter2.setTextureKey(Teleporter5);
                        break;

                    case 6:
                        teleporter1.setTextureKey(Teleporter6);
                        teleporter2.setTextureKey(Teleporter6);
                        break;

                    case 7:
                        teleporter1.setTextureKey(Teleporter7);
                        teleporter2.setTextureKey(Teleporter7);
                        break;

                    case 8:
                        teleporter1.setTextureKey(Teleporter8);
                        teleporter2.setTextureKey(Teleporter8);
                        break;

                    case 9:
                        teleporter1.setTextureKey(Teleporter9);
                        teleporter2.setTextureKey(Teleporter9);
                        break;

                    case 10:
                        teleporter1.setTextureKey(Teleporter10);
                        teleporter2.setTextureKey(Teleporter10);
                        break;

                    case 11:
                        teleporter1.setTextureKey(Teleporter11);
                        teleporter2.setTextureKey(Teleporter11);
                        break;

                    case 12:
                        teleporter1.setTextureKey(Teleporter12);
                        teleporter2.setTextureKey(Teleporter12);
                        break;

                    case 13:
                        teleporter1.setTextureKey(Teleporter13);
                        teleporter2.setTextureKey(Teleporter13);
                        break;

                    case 14:
                        teleporter1.setTextureKey(Teleporter14);
                        teleporter2.setTextureKey(Teleporter14);
                        break;

                    case 15:
                        teleporter1.setTextureKey(Teleporter15);
                        teleporter2.setTextureKey(Teleporter15);
                        break;
                }

                //assign the char key
                teleporter1.setFileCharKey(TELEPORTER_KEYS[screen.getTeleporterKeyIndex()]);
                teleporter2.setFileCharKey(TELEPORTER_KEYS[screen.getTeleporterKeyIndex()]);

                //move to the next index
                screen.setTeleporterKeyIndex(screen.getTeleporterKeyIndex() + 1);

                //add the teleporter to the level
                screen.getLevel().add(teleporter1);

                //let's find an open spot for the other teleporter
                LevelHelper.assignLocation(screen.getLevel(), teleporter2, col, row);

                //and add that to the level as well
                screen.getLevel().add(teleporter2);
                break;

            default:
                //anything else we can just add to the level
                screen.getLevel().add(LevelObjectHelper.create(obj.getTextureKey(), col, row));
                break;
        }

        //reset the level
        screen.getLevel().reset();
    }
}