package com.gamesbykevin.slide.level.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.slide.audio.GameAudio;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.textures.Textures;

public class Holder extends LevelObject {

    //is it holding the player
    private boolean hold = false;

    public Holder() {
        super(Type.Holder);
        super.setTextureKey(Textures.Key.Holder);
    }

    @Override
    public void update(Level level) {

        //call parent
        super.update(level);

        //if we are holding the player let's see if that is no longer the case
        if (isHold()) {

            Player player = level.getPlayer();

            //if we don't have collision
            if (!hasCollision(player)) {
                setHold(false);
            }
        }
    }

    @Override
    public void updateCollision(Level level) {

        //if we aren't holding the player, let's see if the can
        if (!isHold()) {

            Player player = level.getPlayer();

            boolean stick = false;

            //if in the right spot we will stick it
            if (player.getDX() > 0 && player.getCol() >= getCol()) {
                stick = true;
            } else if (player.getDX() < 0 && player.getCol() <= getCol()) {
                stick = true;
            } else if (player.getDY() > 0 && player.getRow() >= getRow()) {
                stick = true;
            } else if (player.getDY() < 0 && player.getRow() <= getRow()) {
                stick = true;
            }

            if (stick) {

                //if we stick, place the player on the holder
                player.stop();
                player.setCol(getCol());
                player.setRow(getRow());

                //hold the player
                setHold(true);

                //play sound effect
                GameAudio.playSfx(GameAudio.SoundEffect.Holder);
            }
        }
    }

    public boolean isHold() {
        return this.hold;
    }

    public void setHold(boolean hold) {
        this.hold = hold;
    }

    @Override
    public void reset(Level level) {
        //do anything here?
        setHold(false);
    }

    @Override
    public void render(SpriteBatch batch) {
        //call to parent
        super.render(batch);
    }

}