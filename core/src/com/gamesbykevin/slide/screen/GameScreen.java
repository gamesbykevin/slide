package com.gamesbykevin.slide.screen;

import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.audio.GameAudio;
import com.gamesbykevin.slide.level.LevelHelper;
import com.gamesbykevin.slide.level.objects.Player;

import java.io.IOException;

import static com.gamesbykevin.slide.level.Level.LEVEL_COMPLETE_DELAY;
import static com.gamesbykevin.slide.level.Level.LEVEL_INDEX;
import static com.gamesbykevin.slide.screen.GameScreenHelper.createGameover;
import static com.gamesbykevin.slide.screen.GameScreenHelper.maintainFps;

public class GameScreen extends LevelScreen {

    //are we playing levels we created
    public static boolean CUSTOM_LEVEL = false;

    public GameScreen(MyGdxGame game) {
        super(game);
    }

    public void reset() {

        //flag if we are editing a level
        CreateScreen.EDITING = false;

        //default zoom
        this.setZoomRate(ZOOM_DEFAULT);

        //make sure camera is in the correct location
        resetCameraOrigin();

        try {

            //create our level
            if (CUSTOM_LEVEL) {
                setLevel(LevelHelper.create(LevelHelper.getCreatedLevelLines(LEVEL_INDEX)));
            } else {

                String levelName = (LEVEL_INDEX + 1) + "";

                while (levelName.length() < 3) {
                    levelName = "0" + levelName;
                }

                levelName += ".txt";

                setLevel(LevelHelper.create(levelName));
            }

            getLevel().reset();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void show() {
        super.show();
        
        //set zoom to normal
        getCamera().zoom = ZOOM_DEFAULT;

        //create the game over menu
        createGameover(this);

        //if the game isn't paused reset the level
        if (!getGame().isPaused())
            reset();

        //capture the game input
        captureInputGame();
    }

    @Override
    public void pause() {
        super.pause();
        getGame().setPaused(true);
    }

    @Override
    public void resume() {
        super.resume();


        if (getLevel().isSolved() && getLevel().getLapsedComplete() >= LEVEL_COMPLETE_DELAY) {

            //play music
            GameAudio.playMusic(GameAudio.SoundMusic.Victory, true);
        } else {

            //play music
            GameAudio.playMusic(GameAudio.SoundMusic.Theme, false);
        }
    }

    @Override
    public void render(float delta) {

        //call parent
        super.render(delta);

        if (getLevel().isSolved()) {

            if (getLevel().getLapsedComplete() >= LEVEL_COMPLETE_DELAY) {

                //draw the menu screen
                drawGameover();

            } else {

                //continue to zoom in/out the meantime
                setZoomRate(isZoom() ? getZoomRate() - ZOOM_RATE : getZoomRate() + ZOOM_RATE);

                //watch how far we zoom
                if (getZoomRate() < ZOOM_MIN) {
                    setZoomRate(ZOOM_MIN);
                    setZoom(!isZoom());
                } else if (getZoomRate() > ZOOM_MAX) {
                    setZoomRate(ZOOM_MAX);
                    setZoom(!isZoom());
                }

                //zoom in on camera
                getCamera().zoom = getZoomRate();

                //we will move the camera towards the player
                Player player = getLevel().getPlayer();

                if (getCamera().position.x < player.getX() + (player.getW() / 2)) {
                    getCamera().position.x++;
                } else if (getCamera().position.x > player.getX() + (player.getW() / 2)) {
                    getCamera().position.x--;
                }

                if (getCamera().position.y < player.getY() + (player.getH() / 2)) {
                    getCamera().position.y++;
                } else if (getCamera().position.y > player.getY() + (player.getH() / 2)) {
                    getCamera().position.y--;
                }
            }

        } else if (getGame().isPaused()) {

            //if paused show the overlay
            getOverlay().draw(getBatch());

        }

        //finished rendering
        getBatch().end();

        //keep the speed of the game steady
        maintainFps(getStart());
    }
}