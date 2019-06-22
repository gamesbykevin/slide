package com.gamesbykevin.slide.level.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.textures.Textures;

import static com.gamesbykevin.slide.level.objects.PartialWall.CLOSE_VELOCITY;

public class WallConnector extends LevelObject {

    boolean hide = false;

    //how big is this when we render
    private static final int RENDER_SIZE = 64;

    @Override
    public void update() {

        //call parent
        super.update();
    }

    @Override
    public void updateCollision(Level level) {

        //if not hidden, check further
        if (!hide) {
            LevelObject right = level.getLevelObject(Textures.Key.WallRight, (int) getCol() + 1, (int) getRow());
            LevelObject left = level.getLevelObject(Textures.Key.WallLeft, (int) getCol() - 1, (int) getRow());

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

            } else {

                //if we don't have right/left walls, then we have down/up
                LevelObject up = level.getLevelObject(Textures.Key.WallUp, (int) getCol(), (int) getRow() + 1);
                LevelObject down = level.getLevelObject(Textures.Key.WallDown, (int) getCol(), (int) getRow() - 1);

                //up wall moves down
                up.setDY(-CLOSE_VELOCITY);
                ((PartialWall) up).setTargetCol(getCol());
                ((PartialWall) up).setTargetRow(getRow());

                //down wall moves up
                down.setDY(CLOSE_VELOCITY);
                ((PartialWall) down).setTargetCol(getCol());
                ((PartialWall) down).setTargetRow(getRow());
            }

            //hide it now
            setHide(true);

            //play sound effect?
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

            //change position so it appears that blocks are connected
            setX(x - (RENDER_SIZE / 4));
            setY(y - (RENDER_SIZE / 4));
            setW(RENDER_SIZE);
            setH(RENDER_SIZE);

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