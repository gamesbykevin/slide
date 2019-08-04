package com.gamesbykevin.slide.level.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.audio.GameAudio;

import static com.gamesbykevin.slide.level.objects.PartialWall.CLOSE_VELOCITY;

public class WallConnector extends LevelObject {

    boolean hide = false;

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
    }

    @Override
    public void updateCollision(Level level) {

        //if not hidden, check further
        if (!hide) {

            if (isVertical()) {

                LevelObject up = level.getLevelObject(Type.PartialWall, (int) getCol(), (int) getRow() + 1);
                LevelObject down = level.getLevelObject(Type.PartialWall, (int) getCol(), (int) getRow() - 1);

                //up wall moves down
                up.setDY(-CLOSE_VELOCITY);
                ((PartialWall) up).setTargetCol(getCol());
                ((PartialWall) up).setTargetRow(getRow());

                //down wall moves up
                down.setDY(CLOSE_VELOCITY);
                ((PartialWall) down).setTargetCol(getCol());
                ((PartialWall) down).setTargetRow(getRow());

            } else {

                LevelObject right = level.getLevelObject(Type.PartialWall, (int) getCol() + 1, (int) getRow());
                LevelObject left = level.getLevelObject(Type.PartialWall, (int) getCol() - 1, (int) getRow());

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

            //hide it now
            setHide(true);

            //play sound effect
            GameAudio.playSfx(GameAudio.SoundEffect.Connect);
        }
    }

    @Override
    public void reset(Level level) {

        //do we need to reset anything here
        setHide(false);
    }

    @Override
    public void render(SpriteBatch batch) {

        if (!isHide()) {

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

    public boolean isHide() {
        return this.hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }
}