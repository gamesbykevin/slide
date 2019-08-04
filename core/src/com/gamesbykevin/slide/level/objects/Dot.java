package com.gamesbykevin.slide.level.objects;

import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.textures.Textures;

public class Dot extends LevelObject {

    //did we collide with the player
    private boolean collision = false;

    //did the player exit the dot
    private boolean exit = false;

    //when to determine we are in collision with this object
    private static final float COLLISION_DISTANCE = 1.0f;

    public Dot() {
        super(Type.Dot);
        setTextureKey(Textures.Key.Dot);
    }

    @Override
    public void update(Level level) {
        super.update(level);

        //if we have collided with the player, but we haven't exited just yet
        if (isCollision() && !isExit()) {

            //if we no longer have collision the player has exited
            if (!hasCollision(level.getPlayer(), COLLISION_DISTANCE)) {

                //flag exit true
                setExit(true);

                //expand to wall
                setTextureKey(Textures.Key.Wall);
            }
        }
    }

    @Override
    public void updateCollision(Level level) {

        //if we have expanded stop the player
        if (isCollision() && isExit()) {

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
            
        } else {

            //flag that we have collision
            setCollision(true);
        }
    }

    @Override
    public void reset(Level level) {

        setCollision(false);
        setExit(false);
        setTextureKey(Textures.Key.Dot);
    }

    public boolean isCollision() {
        return this.collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public boolean isExit() {
        return this.exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }
}