package com.gamesbykevin.slide.level.objects;

import com.gamesbykevin.slide.textures.Textures;

import static com.gamesbykevin.slide.level.Level.getStartX;
import static com.gamesbykevin.slide.level.Level.getStartY;
import static com.gamesbykevin.slide.level.objects.LevelObject.DEFAULT_HEIGHT;
import static com.gamesbykevin.slide.level.objects.LevelObject.DEFAULT_WIDTH;

public class LevelObjectHelper {

    public static LevelObject create(Textures.Key key, float col, float row) {
        LevelObject object = create(key);
        object.setCol(col);
        object.setRow(row);
        object.setX((col * DEFAULT_WIDTH)  + getStartX());
        object.setY((row * DEFAULT_HEIGHT) + getStartY());
        object.setW(DEFAULT_WIDTH);
        object.setH(DEFAULT_HEIGHT);

        switch (key) {
            case WallLeft:
            case WallRight:
            case WallUp:
            case WallDown:
                ((PartialWall)object).setResetCol(col);
                ((PartialWall)object).setResetRow(row);
                break;
        }

        return object;
    }

    private static LevelObject create(Textures.Key key) {

        //our level object
        LevelObject object = null;

        switch (key) {

            case Bomb:
                object = new Bomb();
                break;

            case Danger:
                object = new Danger();
                break;

            case Teleporter:
                object = new Teleporter();
                break;

            case Player:
                object = new Player();
                break;

            case Indicator:
                object = new Indicator();
                break;

            case WallLeft:
            case WallRight:
            case WallUp:
            case WallDown:
                object = new PartialWall();
                break;

            case WallConnectorH:
            case WallConnectorV:
                object = new WallConnector();
                break;

            default:
                object = new GenericObject();
                break;
        }

        //assign the texture key
        object.setKey(key);

        return object;
    }
}