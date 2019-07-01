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
                if (player.getDX() > 0) {
                    player.setCol(getCol());
                    player.setRow(getRow() - 1);
                    player.stop();
                    player.setDY(-DEFAULT_VELOCITY_Y);
                } else if (player.getDX() < 0) {
                    player.setCol(getCol() + 1);
                    player.stop();
                } else if (player.getDY() > 0) {
                    player.setRow(getRow());
                    player.setCol(getCol() - 1);
                    player.stop();
                    player.setDX(-DEFAULT_VELOCITY_X);
                } else if (player.getDY() < 0) {
                    player.setRow(getRow() - 1);
                    player.stop();
                }
                break;

            case NW:
                if (player.getDX() > 0) {
                    player.setCol(getCol() - 1);
                    player.stop();
                } else if (player.getDX() < 0) {
                    player.setCol(getCol());
                    player.setRow(getRow() - 1);
                    player.stop();
                    player.setDY(-DEFAULT_VELOCITY_Y);
                } else if (player.getDY() > 0) {
                    player.setCol(getCol() + 1);
                    player.setRow(getRow());
                    player.stop();
                    player.setDX(DEFAULT_VELOCITY_X);
                } else if (player.getDY() < 0) {
                    player.setRow(getRow() + 1);
                    player.stop();
                }
                break;

            case SE:
                if (player.getDX() > 0) {
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
                } else if (player.getDY() < 0) {
                    player.setRow(getRow());
                    player.setCol(getCol() - 1);
                    player.stop();
                    player.setDX(-DEFAULT_VELOCITY_X);
                }
                break;

            case SW:
                if (player.getDX() > 0) {
                    player.setCol(getCol() - 1);
                    player.stop();
                } else if (player.getDX() < 0) {
                    player.setCol(getCol());
                    player.setRow(getRow() + 1);
                    player.stop();
                    player.setDY(DEFAULT_VELOCITY_Y);
                } else if (player.getDY() > 0) {
                    player.setRow(getRow() - 1);
                    player.stop();
                } else if (player.getDY() < 0) {
                    player.setCol(getCol() + 1);
                    player.setRow(getRow());
                    player.stop();
                    player.setDX(DEFAULT_VELOCITY_X);
                }
                break;
        }
    }

    @Override
    public void reset(Level level) {

    }
}