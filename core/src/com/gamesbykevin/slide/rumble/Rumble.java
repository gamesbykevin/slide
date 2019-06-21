package com.gamesbykevin.slide.rumble;

import com.badlogic.gdx.graphics.Camera;
import com.gamesbykevin.slide.preferences.AppPreferences;

import java.util.Random;

import static com.gamesbykevin.slide.preferences.AppPreferences.PREF_SCREEN_SHAKE_ENABLED;

public class Rumble {

    //default shake values
    public static float DEFAULT_DURATION = .33f;

    //default strength of shaking
    private static float DEFAULT_INTENSITY = 20;

    //how long do we shake?
    private static float DURATION = DEFAULT_DURATION;

    //how much does the screen shake?
    private static float INTENSITY = DEFAULT_INTENSITY;

    //how long have we been shaking already
    private static float LAPSED = DURATION;

    //track the shaking so we can reset the camera position
    private static float OFFSET_X = 0;
    private static float OFFSET_Y = 0;

    //used to move the screen in random fashion
    private static Random RANDOM = new Random();

    public static void reset() {
        LAPSED = 0;
        OFFSET_X = 0;
        OFFSET_Y = 0;
    }

    public static void tick(Camera camera, float delta) {

        //only shake the screen if setting enabled
        if (!AppPreferences.isEnabled(PREF_SCREEN_SHAKE_ENABLED)) {
            LAPSED = DURATION;
            return;
        }

        //only shake if time if left
        if (LAPSED < DURATION) {

            //the power will change as time passes
            float power = INTENSITY * ((DURATION - LAPSED) / DURATION);

            //calculate offset
            float x = (RANDOM.nextFloat() - 0.5f) * power;
            float y = (RANDOM.nextFloat() - 0.5f) * power;

            //move the camera
            camera.translate(x, y, 0);

            //keep track of changes, so we can reset position later
            OFFSET_X += x;
            OFFSET_Y += y;

            // Increase the elapsed time by the delta provided.
            LAPSED += delta;

            //if done, reset camera position
            if (LAPSED >= DURATION)
                camera.translate(-OFFSET_X, -OFFSET_Y, 0);
        }
    }
}