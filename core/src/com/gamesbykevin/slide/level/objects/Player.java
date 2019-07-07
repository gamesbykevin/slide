package com.gamesbykevin.slide.level.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.textures.Textures;

import static com.gamesbykevin.slide.screen.ParentScreen.SCREEN_HEIGHT;
import static com.gamesbykevin.slide.screen.ParentScreen.SCREEN_WIDTH;

public class Player extends LevelObject {

    //start location needed to reset
    private int startCol, startRow;

    //which teleporter did we come from
    private String teleporterId;

    //where is our particle meta particles at?
    private static final String PARTICLE_FILE = "player_particle.p";

    private boolean moveRight = false, moveLeft = false, moveUp = false, moveDown = false;

    /**
     * Default constructor
     */
    public Player() {
        super(Type.Player);
        setTextureKey(Textures.Key.Player);
        super.createParticleEffect(Gdx.files.internal(PARTICLE_PATH + PARTICLE_FILE), Gdx.files.internal(PARTICLE_PATH));
    }

    public boolean hasVelocity() {
        return (getDX() != VELOCITY_NONE || getDY() != VELOCITY_NONE);
    }

    public boolean isMoving() {
        return (isMoveLeft() || isMoveRight() || isMoveUp() || isMoveDown());
    }

    public boolean isMoveRight() {
        return this.moveRight;
    }

    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }

    public boolean isMoveLeft() {
        return this.moveLeft;
    }

    public void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;
    }

    public boolean isMoveUp() {
        return this.moveUp;
    }

    public void setMoveUp(boolean moveUp) {
        this.moveUp = moveUp;
    }

    public boolean isMoveDown() {
        return this.moveDown;
    }

    public void setMoveDown(boolean moveDown) {
        this.moveDown = moveDown;
    }

    public void setTeleporterId(String teleporterId) {
        this.teleporterId = teleporterId;
    }

    public String getTeleporterId() {
        return this.teleporterId;
    }

    public void checkBombs(Level level) {

        //if ready to move but haven't started
        if (isMoving() && !hasVelocity()) {

            LevelObject obj = null;
            int col = (int)getCol();
            int row = (int)getRow();

            if (isMoveUp()) {
                obj = level.getLevelObject(col, row + 1);
            } else if (isMoveDown()) {
                obj = level.getLevelObject(col, row - 1);
            } else if (isMoveLeft()) {
                obj = level.getLevelObject(col - 1, row);
            } else if (isMoveRight()) {
                obj = level.getLevelObject(col + 1, row);
            }

            //if we are next to a bomb we can detonate now
            if (obj != null && obj.getType().equals(Type.Bomb))
                ((Bomb)obj).setTime(Bomb.TIME_EXPIRED);
        }
    }

    @Override
    public void update(Level level) {

        //call parent
        super.update(level);

        if (isMoveRight())
            setDX(DEFAULT_VELOCITY_X);
        if (isMoveLeft())
            setDX(-DEFAULT_VELOCITY_X);
        if (isMoveDown())
            setDY(-DEFAULT_VELOCITY_Y);
        if (isMoveUp())
            setDY(DEFAULT_VELOCITY_Y);

        //update location
        setCol(getCol() + getDX());
        setRow(getRow() + getDY());
    }

    @Override
    public void updateCollision(Level level) {
        //don't update collision with self!!!!!
    }

    @Override
    public void reset(Level level) {

        //make sure the player is stopped
        stop();

        //we also want the player back at the start
        setCol(getStartCol());
        setRow(getStartRow());

        //we did not come from a teleporter when resetting
        setTeleporterId(null);
    }

    @Override
    public void stop() {

        //reset the teleporter when we stop
        setTeleporterId(null);

        //stop moving
        setMoveDown(false);
        setMoveLeft(false);
        setMoveRight(false);
        setMoveUp(false);

        //call parent
        super.stop();
    }

    public boolean hasBounds() {

        if (getX() + getW() < 0)
            return false;
        if (getX() > SCREEN_WIDTH)
            return false;
        if (getY() + getH() < 0)
            return false;
        if (getY() > SCREEN_HEIGHT)
            return false;

        return true;
    }

    public void setStartCol(int startCol) {
        this.startCol = startCol;
    }

    public int getStartCol() {
        return this.startCol;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getStartRow() {
        return this.startRow;
    }

    @Override
    public void render(SpriteBatch batch) {

        //render particles if we have them
        if (getParticleEffect() != null) {

            //only draw particles if we are moving
            if (getDX() != VELOCITY_NONE || getDY() != VELOCITY_NONE) {

                //make sure the particles are in the correct position etc...
                getParticleEffect().setPosition(
                        getX() + (getW() / 2),
                        getY() + (getH() / 2)
                );

                //update particle effect
                getParticleEffect().update(Gdx.graphics.getDeltaTime());

                ParticleEmitter.ScaledNumericValue angle = getParticleEmitter().getAngle();

                //change the angle depending on how we are moving
                if (getDX() != VELOCITY_NONE) {

                    if (getDX() < 0) {
                        angle.setHigh(0);
                        angle.setLow(0);
                    } else {
                        angle.setHigh(180);
                        angle.setLow(180);
                    }
                } else if (getDY() != VELOCITY_NONE) {
                    if (getDY() < 0) {
                        angle.setHigh(90);
                        angle.setLow(90);
                    } else {
                        angle.setHigh(270);
                        angle.setLow(270);
                    }
                }

                //if particle animation is done, start it again
                if (getParticleEffect().isComplete())
                    getParticleEffect().start();

                //render our particle(s)
                getParticleEffect().draw(batch);

            } else {

                //update particle effect
                getParticleEffect().update(Gdx.graphics.getDeltaTime());
            }
        }

        //render the player as well :)
        super.render(batch);
    }
}