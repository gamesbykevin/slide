package com.gamesbykevin.slide.level.objects;

import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.textures.Textures;

public class Redirect extends LevelObject {

    public enum RedirectType {
        NW, NE, SE, SW
    }

    private RedirectType redirectType;

    public Redirect(RedirectType redirectType) {
        super(Type.Redirect);
        setRedirectType(redirectType);
    }

    public RedirectType getRedirectType() {
        return this.redirectType;
    }

    public void setRedirectType(RedirectType redirectType) {
        this.redirectType = redirectType;
    }

    @Override
    public void updateCollision(Level level) {

        Player player = level.getPlayer();

        switch (getRedirectType()) {

            case NE:
                if (player.getDX() > 0 && player.getCol() >= getCol()) {
                    player.setCol(getCol());
                    player.stopVelocity();
                    player.setMoveDown(true);
                } else if (player.getDX() < 0 && player.getCol() > getCol()) {
                    player.setCol(getCol() + 1);
                    player.stop();
                } else if (player.getDY() > 0 && player.getRow() >= getRow()) {
                    player.setRow(getRow());
                    player.stopVelocity();
                    player.setMoveLeft(true);
                } else if (player.getDY() < 0 && player.getRow() > getRow()) {
                    player.setRow(getRow() + 1);
                    player.stop();
                }
                break;

            case NW:
                if (player.getDX() > 0 && player.getCol() < getCol()) {
                    player.setCol(getCol() - 1);
                    player.stop();
                } else if (player.getDX() < 0 && player.getCol() <= getCol()) {
                    player.setCol(getCol());
                    player.stopVelocity();
                    player.setMoveDown(true);
                } else if (player.getDY() > 0 && player.getRow() >= getRow()) {
                    player.setRow(getRow());
                    player.stopVelocity();
                    player.setMoveRight(true);
                } else if (player.getDY() < 0 && player.getRow() > getRow()) {
                    player.setRow(getRow() + 1);
                    player.stop();
                }
                break;

            case SE:
                if (player.getDX() > 0 && player.getCol() >= getCol()) {
                    player.setCol(getCol());
                    player.stopVelocity();
                    player.setMoveUp(true);
                } else if (player.getDX() < 0 && player.getCol() > getCol()) {
                    player.setCol(getCol() + 1);
                    player.stop();
                } else if (player.getDY() > 0 && player.getRow() < getRow()) {
                    player.setRow(getRow() - 1);
                    player.stop();
                } else if (player.getDY() < 0 && player.getRow() <= getRow()) {
                    player.setRow(getRow());
                    player.stopVelocity();
                    player.setMoveLeft(true);
                }
                break;

            case SW:
                if (player.getDX() > 0 && player.getCol() < getCol()) {
                    player.setCol(getCol() - 1);
                    player.stop();
                } else if (player.getDX() < 0 && player.getCol() <= getCol()) {
                    player.setCol(getCol());
                    player.stopVelocity();
                    player.setMoveUp(true);
                } else if (player.getDY() > 0 && player.getRow() < getRow()) {
                    player.setRow(getRow() - 1);
                    player.stop();
                } else if (player.getDY() < 0 && player.getRow() <= getRow()) {
                    player.setRow(getRow());
                    player.stopVelocity();
                    player.setMoveRight(true);
                }
                break;
        }
    }

    @Override
    public void update(Level level) {
        //nothing to update
    }

    @Override
    public void reset(Level level) {
        //nothing to reset
    }
}