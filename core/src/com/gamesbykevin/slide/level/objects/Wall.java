package com.gamesbykevin.slide.level.objects;

import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.audio.GameAudio;

public class Wall extends LevelObject {

    public Wall() {
        super(Type.Wall);
    }

    @Override
    public void updateCollision(Level level) {

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

        //play collision sound effect
        GameAudio.playSfx(GameAudio.SoundEffect.Wall);
    }

    @Override
    public void reset(Level level) {

    }
}