package com.gamesbykevin.slide.level.objects;

import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.textures.Textures;

import static com.gamesbykevin.slide.level.Level.updateCoordinates;
import static com.gamesbykevin.slide.level.objects.PartialWall.CLOSE_VELOCITY;

public class GenericObject extends LevelObject {

    @Override
    public void update() {
        //nothing here
    }

    @Override
    public void updateCollision(Level level) {

        Player player = level.getPlayer();

        switch (getKey()) {

            case RedirectNE:
                if (player.getDX() > 0 && player.hasCollisionClose(this)) {
                    player.setCol(getCol());
                    player.setRow(getRow() + 1);
                    player.stop();
                    player.setDY(-DEFAULT_VELOCITY_Y);
                } else if (player.getDX() < 0) {
                    player.setCol(getCol() + 1);
                    player.stop();
                } else if (player.getDY() > 0 && player.hasCollisionClose(this)) {
                    player.setRow(getRow());
                    player.setCol(getCol() - 1);
                    player.stop();
                    player.setDX(-DEFAULT_VELOCITY_X);
                } else if (player.getDY() < 0) {
                    player.setRow(getRow() - 1);
                    player.stop();
                }
                break;

            case RedirectNW:
                if (player.getDX() > 0) {
                    player.setCol(getCol() - 1);
                    player.stop();
                } else if (player.getDX() < 0 && player.hasCollisionClose(this)) {
                    player.setCol(getCol());
                    player.setRow(getRow() - 1);
                    player.stop();
                    player.setDY(-DEFAULT_VELOCITY_Y);
                } else if (player.getDY() > 0 && player.hasCollisionClose(this)) {
                    player.setCol(getCol() + 1);
                    player.setRow(getRow());
                    player.stop();
                    player.setDX(DEFAULT_VELOCITY_X);
                } else if (player.getDY() < 0) {
                    player.setRow(getRow() + 1);
                    player.stop();
                }
                break;

            case RedirectSE:
                if (player.getDX() > 0 && player.hasCollisionClose(this)) {
                    player.setCol(getCol());
                    player.setRow(getRow() + 1);
                    player.stop();
                    player.setDY(DEFAULT_VELOCITY_Y);
                } else if (player.getDX() < 0) {
                    player.setCol(getCol() + 1);
                    player.stop();
                } else if (player.getDY() > 0) {
                    player.setRow(getRow() - 1);
                    player.stop();
                } else if (player.getDY() < 0 && player.hasCollisionClose(this)) {
                    player.setRow(getRow());
                    player.setCol(getCol() - 1);
                    player.stop();
                    player.setDX(-DEFAULT_VELOCITY_X);
                }
                break;

            case RedirectSW:
                if (player.getDX() > 0) {
                    player.setCol(getCol() - 1);
                    player.stop();
                } else if (player.getDX() < 0 && player.hasCollisionClose(this)) {
                    player.setCol(getCol());
                    player.setRow(getRow() + 1);
                    player.stop();
                    player.setDY(DEFAULT_VELOCITY_Y);
                } else if (player.getDY() > 0) {
                    player.setRow(getRow() - 1);
                    player.stop();
                } else if (player.getDY() < 0 && player.hasCollisionClose(this)) {
                    player.setCol(getCol() + 1);
                    player.setRow(getRow());
                    player.stop();
                    player.setDX(DEFAULT_VELOCITY_X);
                }
                break;

            case Wall:
                if (player.getDX() > 0) {
                    player.setCol(getCol() - 1);
                } else if (player.getDX() < 0) {
                    player.setCol(getCol() + 1);
                } else if (player.getDY() > 0) {
                    player.setRow(getRow() - 1);
                } else if (player.getDY() < 0) {
                    player.setRow(getRow() + 1);
                }

                //stop motion
                player.stop();
                break;

            case Goal:
            case Key:

                boolean done = false;

                if (player.getDX() > 0) {
                    if (player.getCol() >= getCol())
                        done = true;
                } else if (player.getDX() < 0) {
                    if (player.getCol() <= getCol())
                        done = true;
                } else if (player.getDY() > 0) {
                    if (player.getRow() >= getRow())
                        done = true;
                } else if (player.getDY() < 0) {
                    if (player.getRow() <= getRow())
                        done = true;
                }

                if (done) {

                    if (getKey() == Textures.Key.Goal) {

                        //place on the goal
                        player.setCol(getCol());
                        player.setRow(getRow());

                        //update x,y render
                        updateCoordinates(player);

                        //stop motion
                        player.stop();

                        //we solved the level!!!
                        level.setSolved(true);

                    } else if (getKey() == Textures.Key.Key) {

                        //the key was collected
                        setKey(Textures.Key.Collected);

                        //we now unlock the goal and switch textures
                        level.getLevelObject(Textures.Key.Locked).setKey(Textures.Key.Goal);

                        //play sound effect here?
                    }
                }
                break;

            case TransparentSpace:

                //if we are close, then we close the walls on both sides
                if (player.hasCollisionClose(this)) {

                    LevelObject right = level.getLevelObject(Textures.Key.WallRight, (int)getCol() + 1, (int)getRow());
                    LevelObject left = level.getLevelObject(Textures.Key.WallLeft, (int)getCol() - 1, (int)getRow());
                    LevelObject up = level.getLevelObject(Textures.Key.WallUp, (int)getCol(), (int)getRow() + 1);
                    LevelObject down = level.getLevelObject(Textures.Key.WallDown, (int)getCol(), (int)getRow() - 1);

                    //if the walls exist, let's see if we can close them
                    if (right != null && left != null) {
                        right.setDX(-CLOSE_VELOCITY);
                        ((PartialWall)right).setTargetCol(getCol());
                        ((PartialWall)right).setTargetRow(getRow());
                        left.setDX(CLOSE_VELOCITY);
                        ((PartialWall)left).setTargetCol(getCol());
                        ((PartialWall)left).setTargetRow(getRow());
                    } else if (up != null && down != null) {
                        up.setDY(-CLOSE_VELOCITY);
                        ((PartialWall)up).setTargetCol(getCol());
                        ((PartialWall)up).setTargetRow(getRow());
                        down.setDY(CLOSE_VELOCITY);
                        ((PartialWall)down).setTargetCol(getCol());
                        ((PartialWall)down).setTargetRow(getRow());
                    }
                }
                break;
        }
    }

    @Override
    public void reset(Level level) {

        switch (getKey()) {

            case Collected:
                setKey(Textures.Key.Key);
                break;

            case Goal:

                if (level.getLevelObject(Textures.Key.Collected) != null || level.getLevelObject(Textures.Key.Key) != null)
                    setKey(Textures.Key.Locked);

                break;
        }
    }
}