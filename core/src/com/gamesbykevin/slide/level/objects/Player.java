package com.gamesbykevin.slide.level.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.gamesbykevin.slide.level.Level;

import static com.gamesbykevin.slide.screen.ParentScreen.SCREEN_HEIGHT;
import static com.gamesbykevin.slide.screen.ParentScreen.SCREEN_WIDTH;

public class Player extends LevelObject {

    //start location needed to reset
    private int startCol, startRow;

    //used to change the angle of the particles
    private ParticleEmitter particleEmitter;

    //which teleporter did we come from
    private String teleporterId;

    //where is our particle meta particles at?
    private static final String PARTICLE_FILE = "player_particle.p";

    /**
     * Default constructor
     */
    public Player() {
        super.createParticleEffect(Gdx.files.internal(PARTICLE_PATH + PARTICLE_FILE), Gdx.files.internal(PARTICLE_PATH));
        this.particleEmitter = getParticleEffect().getEmitters().first();
        this.particleEmitter.setPosition(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        getParticleEffect().start();
    }

    public void setTeleporterId(String teleporterId) {
        this.teleporterId = teleporterId;
    }

    public String getTeleporterId() {
        return this.teleporterId;
    }

    @Override
    public void update() {

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
    public void render(SpriteBatch batch, Sprite sprite, BitmapFont font) {

        //only draw particles if we are moving
        if (getDX() != VELOCITY_NONE || getDY() != VELOCITY_NONE) {

            //make sure the particles are in the correct position etc...
            getParticleEffect().setPosition(
                    getX() + (getW() / 2),
                    getY() + (getH() / 2)
            );

            //update particle effect
            getParticleEffect().update(Gdx.graphics.getDeltaTime());

            //change the angle depending on how we are moving
            if (getDX() != VELOCITY_NONE) {
                ParticleEmitter.ScaledNumericValue angle = this.particleEmitter.getAngle();
                angle.setHigh(0);
                angle.setLow(0);
            } else if (getDY() != VELOCITY_NONE) {
                ParticleEmitter.ScaledNumericValue angle = this.particleEmitter.getAngle();
                angle.setHigh(90);
                angle.setLow(90);
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

        //render the player as well :)
        super.render(batch, sprite, font);
    }
}