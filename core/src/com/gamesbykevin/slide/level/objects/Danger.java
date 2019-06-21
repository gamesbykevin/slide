package com.gamesbykevin.slide.level.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.preferences.AppPreferences;
import com.gamesbykevin.slide.rumble.Rumble;

import static com.gamesbykevin.slide.MyGdxGame.DURATION_VIBRATE;
import static com.gamesbykevin.slide.preferences.AppPreferences.PREF_VIBRATE_ENABLED;

public class Danger extends LevelObject {

    //how fast to rotate
    public static final float DEFAULT_ROTATION = 10f;

    //where is our particle meta particles at?
    private static final String PARTICLE_FILE = "cut.p";

    //do we show our particle
    private boolean display = false;

    /**
     * Default constructor
     */
    public Danger() {
        setRotate(true);
        setRotationSpeed(DEFAULT_ROTATION);

        //create our particle
        super.createParticleEffect(Gdx.files.internal(PARTICLE_PATH + PARTICLE_FILE), Gdx.files.internal(PARTICLE_PATH));
    }

    @Override
    public void update() {
        //do anything here?
    }

    @Override
    public void updateCollision(Level level) {

        Player player = level.getPlayer();

        if (player.hasCollisionClose(this)) {

            //vibrate if the option is enabled
            if (AppPreferences.isEnabled(PREF_VIBRATE_ENABLED))
                Gdx.input.vibrate(DURATION_VIBRATE);

            //play sound effect?

            //shake the screen
            Rumble.reset();

            //reset level
            level.reset();

            //display our particle effect
            setDisplay(true);
        }
    }

    public boolean isDisplay() {
        return this.display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    @Override
    public void reset(Level level) {

        //do we need to reset anything here
        setDisplay(false);

        //make sure it starts from the beginning
        getParticleEffect().reset();

        //make sure the particles are in the correct position etc...
        getParticleEffect().setPosition(getX() + (getW() / 2), getY() + (getH() / 2));
    }

    @Override
    public void render(SpriteBatch batch, Sprite sprite, BitmapFont font) {

        super.render(batch, sprite, font);

        if (isDisplay()) {

            if (!getParticleEffect().isComplete()) {

                //update particle animation
                getParticleEffect().update(Gdx.graphics.getDeltaTime());

                //render the particle effect
                getParticleEffect().draw(batch);

            } else {

                //don't need to show anymore
                setDisplay(false);
            }
        }
    }
}