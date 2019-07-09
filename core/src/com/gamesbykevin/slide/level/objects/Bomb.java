package com.gamesbykevin.slide.level.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.textures.Textures;

import static com.gamesbykevin.slide.MyGdxGame.*;

public class Bomb extends LevelObject {

    //countdown timer
    private float time = TIME_START;

    //how long is our timer
    public static final float TIME_START = 9f;

    //when has time expired?
    public static final float TIME_EXPIRED = 0f;

    //do we start the countdown?
    private boolean countdown = false;

    //where is our particle meta particles at?
    private static final String PARTICLE_FILE = "explosion.p";

    /**
     * Default constructor
     */
    public Bomb() {
        super(Type.Bomb);
        super.setTextureKey(Textures.Key.Bomb);
        super.createParticleEffect(Gdx.files.internal(PARTICLE_PATH + PARTICLE_FILE), Gdx.files.internal(PARTICLE_PATH));
    }

    @Override
    public void update(Level level) {

        //call parent
        super.update(level);

        //if we haven't started the countdown
        if (!hasCountdown())
            return;

        //did the time expire
        boolean previousExpired = hasTimeExpired();

        //update timer
        setTime(getTime() - (TIME_DURATION * 2));

        //if time expired
        if (hasTimeExpired()) {

            //if this bomb just expired add to the count
            if (!previousExpired)
                level.setCountBomb(level.getCountBomb() + 1);

            //time has expired
            setTime(TIME_EXPIRED);

            //make sure the particles are in the correct position etc...
            if (getParticleEffect() != null)
                getParticleEffect().setPosition(getX() + (getW() / 2), getY() + (getH() / 2));
        }
    }

    @Override
    public void updateCollision(Level level) {

        //lets position the player the right way if the bomb has not yet exploded
        if (!hasTimeExpired()) {
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

            //stop moving the player
            level.getPlayer().stop();

            //start the countdown
            setCountdown(true);
        }
    }

    @Override
    public void reset(Level level) {
        setTime(TIME_START);
        setCountdown(false);

        if (getParticleEffect() != null)
            getParticleEffect().start();
    }

    @Override
    public void render(SpriteBatch batch) {

        if (!hasTimeExpired()) {

            //render bomb if time has not expired
            super.render(batch);

            //render the timer text...
            getFont().draw(batch, "" + (int) (getTime()), getX() + ((getW() - TEXT_WIDTH) / 2), getY() + (getH() * .66f));

        } else {

            //else we will draw a particle effect
            if (getParticleEffect() != null && !getParticleEffect().isComplete()) {

                //update particle animation
                getParticleEffect().update(Gdx.graphics.getDeltaTime());

                //render the particle effect
                getParticleEffect().draw(batch);
            }
        }
    }

    public void setTime(float time) {
        this.time = time;
    }

    public float getTime() {
        return time;
    }

    public boolean hasTimeExpired() {
        return (getTime() < TIME_EXPIRED + 1);
    }

    public void setCountdown(boolean countdown) {
        this.countdown = countdown;
    }

    public boolean hasCountdown() {
        return this.countdown;
    }
}