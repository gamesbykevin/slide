package com.gamesbykevin.slide.level.objects;

import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.textures.Textures;
import com.gamesbykevin.slide.audio.GameAudio;

import static com.gamesbykevin.slide.level.Level.updateCoordinates;

public class Goal extends LevelObject {

    //is the goal locked
    private boolean locked;

    public Goal(boolean locked) {
        super((locked) ? Type.LockedGoal : Type.Goal);
        setLocked(locked);
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public void updateCollision(Level level) {

        Player player = level.getPlayer();

        if (isLocked()) {

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

            //play sound effect
            GameAudio.playSfx(GameAudio.SoundEffect.Wall);

        } else {

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

                //place on the goal
                player.setCol(getCol());
                player.setRow(getRow());

                //update x,y render
                updateCoordinates(player);

                //stop motion
                player.stop();
            }
        }
    }

    @Override
    public void reset(Level level) {

        //if there is a key, we need to lock the goal
        if (level.getLevelObject(Type.Key) != null) {
            setTextureKey(Textures.Key.Locked);
            setLocked(true);
        } else {
            setTextureKey(Textures.Key.Goal);
            setLocked(false);
        }
    }
}