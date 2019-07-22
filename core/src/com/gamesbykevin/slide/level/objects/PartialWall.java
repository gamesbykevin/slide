package com.gamesbykevin.slide.level.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.slide.level.Level;

import static com.gamesbykevin.slide.level.Level.updateCoordinates;

public class PartialWall extends LevelObject {

    //where will the wall head to?
    private float targetCol, targetRow;

    //where do we reset the location?
    private float resetCol, resetRow;

    //how fast can the wall move
    public static final float CLOSE_VELOCITY = .1f;

    //is the wall vertical
    private boolean vertical;

    /**
     * Default constructor
     */
    public PartialWall(boolean vertical) {
        super(Type.PartialWall);
        setVertical(vertical);
    }

    public boolean isVertical() {
        return this.vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    public float getResetCol() {
        return this.resetCol;
    }

    public void setResetCol(float resetCol) {
        this.resetCol = resetCol;
    }

    public float getResetRow() {
        return this.resetRow;
    }

    public void setResetRow(float resetRow) {
        this.resetRow = resetRow;
    }

    public float getTargetCol() {
        return this.targetCol;
    }

    public void setTargetCol(float targetCol) {
        this.targetCol = targetCol;
    }

    public float getTargetRow() {
        return this.targetRow;
    }

    public void setTargetRow(float targetRow) {
        this.targetRow = targetRow;
    }

    @Override
    public void update(Level level) {

        //call parent
        super.update(level);

        if (getDX() != VELOCITY_NONE)
            setCol(getCol() + getDX());

        if (getDY() != VELOCITY_NONE)
            setRow(getRow() + getDY());

        if (getDX() > 0 && getCol() >= getTargetCol() || getDX() < 0 && getCol() <= getTargetCol()) {
            setDX(VELOCITY_NONE);
            setCol(getTargetCol());
            updateCoordinates(this);
        } else if (getDY() > 0 && getRow() >= getTargetRow() || getDY() < 0 && getRow() <= getTargetRow()) {
            setDY(VELOCITY_NONE);
            setRow(getTargetRow());
            updateCoordinates(this);
        }
    }

    @Override
    public void updateCollision(Level level) {

        //only check for player collision if not moving
        if (getDX() == VELOCITY_NONE && getDY() == VELOCITY_NONE) {

            Player player = level.getPlayer();

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
        }
    }

    @Override
    public void reset(Level level) {
        setCol(getResetCol());
        setRow(getResetRow());
        setDX(VELOCITY_NONE);
        setDY(VELOCITY_NONE);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }
}