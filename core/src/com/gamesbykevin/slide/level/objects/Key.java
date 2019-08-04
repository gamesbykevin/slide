package com.gamesbykevin.slide.level.objects;

import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.textures.Textures;
import com.gamesbykevin.slide.audio.GameAudio;

public class Key extends LevelObject {

    //was the key collected
    private boolean collected = false;

    public Key() {
        super(Type.Key);
    }

    public boolean isCollected() {
        return this.collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    @Override
    public void updateCollision(Level level) {

        if (isCollected())
            return;

        Player player = level.getPlayer();

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

            //the key was collected
            setTextureKey(Textures.Key.Collected);

            //flag key has been collected
            setCollected(true);

            //we now unlock the goal and switch textures
            Goal levelObject = (Goal)level.getLevelObject(Type.LockedGoal);
            levelObject.setTextureKey(Textures.Key.Goal);
            levelObject.setLocked(false);

            //play sound effect here
            GameAudio.playSfx(GameAudio.SoundEffect.Key);
        }

    }

    @Override
    public void reset(Level level) {
        setTextureKey(Textures.Key.Key);
        setCollected(false);
    }
}