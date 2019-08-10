package com.gamesbykevin.slide.level.objects;

import com.gamesbykevin.slide.exception.LevelObjectException;
import com.gamesbykevin.slide.textures.Textures;

import static com.gamesbykevin.slide.level.Level.*;
import static com.gamesbykevin.slide.level.objects.LevelObject.DEFAULT_DIMENSION;

public class LevelObjectHelper {

    public static LevelObject create(Textures.Key key, float col, float row) {

        LevelObject object = null;

        try {
            object = create(key);
            object.setCol(col);
            object.setRow(row);
            updateCoordinates(object);
            object.setW(DEFAULT_DIMENSION);
            object.setH(DEFAULT_DIMENSION);

            switch (object.getType()) {
                case PartialWall:
                    ((PartialWall) object).setResetCol(col);
                    ((PartialWall) object).setResetRow(row);
                    break;
            }
        } catch (LevelObjectException e) {
            e.printStackTrace();
        }

        return object;
    }

    private static LevelObject create(Textures.Key key) throws LevelObjectException {

        if (key == null)
            return null;

        //our level object
        LevelObject object;

        switch (key) {

            case Holder:
                object = new Holder();
                break;

            case Gem:
                object = new Gem();
                break;

            case Key:
                object = new Key();
                break;

            case Goal:
                object = new Goal(false);
                break;

            case Locked:
                object = new Goal(true);
                break;

            case Bomb:
                object = new Bomb();
                break;

            case Danger:
                object = new Danger();
                break;

            case Dot:
                object = new Dot();
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
                object = new Teleporter();
                break;

            case Player:
                object = new Player();
                break;

            case Indicator:
                object = new Indicator();
                break;

            case Wall:
                object = new Wall();
                break;

            case RedirectNE:
                object = new Redirect(Redirect.RedirectType.NE);
                break;

            case RedirectNW:
                object = new Redirect(Redirect.RedirectType.NW);
                break;

            case RedirectSE:
                object = new Redirect(Redirect.RedirectType.SE);
                break;

            case RedirectSW:
                object = new Redirect(Redirect.RedirectType.SW);
                break;

            case WallConnectorV:
                object = new WallConnector(true);
                break;

            case WallConnectorH:
                object = new WallConnector(false);
                break;

            case WallLeft:
            case WallRight:
                object = new PartialWall(false);
                break;

            case WallUp:
            case WallDown:
                object = new PartialWall(true);
                break;

            default:
                throw new LevelObjectException("Texture key not found here: " + key);
        }

        object.setTextureKey(key);

        //return the level object
        return object;
    }
}