package com.gamesbykevin.slide.level.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.audio.GameAudio;

import static com.gamesbykevin.slide.level.objects.PartialWall.CLOSE_VELOCITY;

public class WallConnector extends LevelObject {

    //did we collide with the wall connector
    boolean collide = false;

    //did we exit the wall connector
    boolean exit = false;

    //is the connector vertical
    private boolean vertical;

    public WallConnector(boolean vertical) {
        super(Type.WallConnector);
        setVertical(vertical);
    }

    public boolean isVertical() {
        return this.vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    @Override
    public void update(Level level) {

        //call parent
        super.update(level);

        if (isCollide() && !isExit()) {

            //if we no longer have collision with the player we can close the connector
            if (!hasCollision(level.getPlayer())) {

                //flag that we have exited
                setExit(true);

                //close walls depending on orientation
                if (isVertical()) {

                    LevelObject up = level.getLevelObject(Type.PartialWall, (int)getCol(), (int)getRow() + 1);
                    LevelObject down = level.getLevelObject(Type.PartialWall, (int)getCol(), (int)getRow() - 1);

                    //up wall moves down
                    up.setDY(-CLOSE_VELOCITY);
                    ((PartialWall) up).setTargetCol(getCol());
                    ((PartialWall) up).setTargetRow(getRow());

                    //down wall moves up
                    down.setDY(CLOSE_VELOCITY);
                    ((PartialWall) down).setTargetCol(getCol());
                    ((PartialWall) down).setTargetRow(getRow());

                } else {

                    LevelObject right = level.getLevelObject(Type.PartialWall, (int)getCol() + 1, (int)getRow());
                    LevelObject left = level.getLevelObject(Type.PartialWall, (int)getCol() - 1, (int)getRow());

                    //if the walls exist, let's see if we can close them
                    if (right != null && left != null) {

                        //right wall moves left
                        right.setDX(-CLOSE_VELOCITY);
                        ((PartialWall) right).setTargetCol(getCol());
                        ((PartialWall) right).setTargetRow(getRow());

                        //left wall moves right
                        left.setDX(CLOSE_VELOCITY);
                        ((PartialWall) left).setTargetCol(getCol());
                        ((PartialWall) left).setTargetRow(getRow());
                    }
                }

                //play sound effect
                GameAudio.playSfx(GameAudio.SoundEffect.Connect);
            }
        }
    }

    @Override
    public void updateCollision(Level level) {

        //if we already collided, don't continue
        if (isCollide())
            return;

        //flag we have collision
        setCollide(true);
    }

    public boolean isCollide() {
        return this.collide;
    }

    public void setCollide(boolean collide) {
        this.collide = collide;
    }

    public boolean isExit() {
        return this.exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    @Override
    public void reset(Level level) {

        //do we need to reset anything here
        setExit(false);
        setCollide(false);
    }

    @Override
    public void render(SpriteBatch batch) {

        //only render if we didn't exit the connector
        if (!isExit()) {

            final float x = getX();
            final float y = getY();
            final float w = getW();
            final float h = getH();

            //wall connector will be slightly bigger
            float size = (DEFAULT_DIMENSION * 2);

            //change position so it appears that blocks are connected
            setX(x - (size / 4));
            setY(y - (size / 4));
            setW(size);
            setH(size);

            super.render(batch);

            //restore previous values
            setX(x);
            setY(y);
            setW(w);
            setH(h);
        }
    }
}