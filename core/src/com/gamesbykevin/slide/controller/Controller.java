package com.gamesbykevin.slide.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.exception.ScreenException;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.level.objects.Player;
import com.gamesbykevin.slide.screen.CreateScreen;
import com.gamesbykevin.slide.screen.GameScreen;
import com.gamesbykevin.slide.screen.LevelScreen;
import com.gamesbykevin.slide.textures.Textures;

import static com.gamesbykevin.slide.level.Level.LEVEL_COMPLETE_DELAY;
import static com.gamesbykevin.slide.screen.ScreenHelper.*;

public class Controller implements InputProcessor {

    //keep reference to our game
    private final MyGdxGame game;

    //calculate the projected touch position
    private Vector3 touchPos, touchPosRelease;

    //how many fingers are on the screen
    private int count = 0;

    public Controller(MyGdxGame game) {
        this.game = game;

        //our touch coordinates
        this.touchPos = new Vector3();
        this.touchPosRelease = new Vector3();
    }

    private MyGdxGame getGame() {
        return this.game;
    }

    @Override
    public boolean keyDown(int keycode) {

        //game can't be paused when checking for input
        if (getGame().isPaused())
            return false;

        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            //do anything here
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        //game can't be paused when checking for input
        if (getGame().isPaused())
            return false;

        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {

            try {
                switch (getGame().getScreenHelper().getScreenIndex()) {

                    case SCREEN_GAME:

                        if (GameScreen.CUSTOM_LEVEL) {
                            getGame().getScreenHelper().changeScreen(SCREEN_SELECT_LEVEL_CUSTOM);
                        } else {
                            getGame().getScreenHelper().changeScreen(SCREEN_SELECT_LEVEL);
                        }
                        break;

                    case SCREEN_SELECT_LEVEL:
                    case SCREEN_SELECT_CREATE:
                    case SCREEN_OPTIONS:
                    case SCREEN_SELECT_LEVEL_CUSTOM:
                        //go back to the menu
                        getGame().getScreenHelper().changeScreen(SCREEN_MENU);
                        break;

                    case SCREEN_CREATE:

                        //flag that we want to exit
                        getGame().getScreenHelper().getCreateScreen().setExit(true);
                        break;
                }
            } catch (ScreenException ex) {
                ex.printStackTrace();
            }

        } else {

            switch (getGame().getScreenHelper().getScreenIndex()) {
                case SCREEN_GAME:
                case SCREEN_CREATE:
                    if (keycode == Input.Keys.RIGHT) {
                        moveRight();
                    } else if (keycode == Input.Keys.LEFT) {
                        moveLeft();
                    } else if (keycode == Input.Keys.DOWN) {
                        moveDown();
                    } else if (keycode == Input.Keys.UP) {
                        moveUp();
                    }
                    break;
            }
        }

        return false;
    }

    @Override
    public boolean keyTyped(char keycode) {

        //game can't be paused when checking for input
        if (getGame().isPaused())
            return false;

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        //game can't be paused when checking for input
        if (getGame().isPaused())
            return false;

        //keep track of fingers on screen
        count++;

        //if 1 finger touched track the coordinates
        if (count == 1) {

            //touch the coordinates
            getTouchPos().x = screenX;
            getTouchPos().y = screenY;

            //check if we selected something
            switch(getGame().getScreenHelper().getScreenIndex()) {

                case SCREEN_CREATE:
                    calculateTouchPosition(getTouchPos());
                    getGame().getScreenHelper().getCreateScreen().setPressed(true);
                    getGame().getScreenHelper().getCreateScreen().setDragged(false);
                    getGame().getScreenHelper().getCreateScreen().setReleased(false);
                    break;
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        //after touching the screen the game is not paused
        if (getGame().isPaused()) {
            getGame().setPaused(false);
            return false;
        }

        //keep track of fingers on screen
        count--;

        //we can't have less than 0 fingers on the screen
        if (count < 0)
            count = 0;

        if (count == 0) {

            switch(getGame().getScreenHelper().getScreenIndex()) {

                case SCREEN_CREATE:

                    //if we don't have anything selected, let's play the level
                    if (getGame().getScreenHelper().getCreateScreen().getSelectedId() == null) {

                        //figure out where the new position is
                        getTouchPosRelease().x = screenX;
                        getTouchPosRelease().y = screenY;
                        calculateTouchPosition(getTouchPosRelease());

                        //if we touched "go back"
                        if (getTouchPosRelease().x >= LevelScreen.GO_BACK_X && getTouchPosRelease().x <= LevelScreen.GO_BACK_X + LevelScreen.GO_BACK_SIZE) {
                            if (getTouchPosRelease().y >= LevelScreen.GO_BACK_Y && getTouchPosRelease().y <= LevelScreen.GO_BACK_Y + LevelScreen.GO_BACK_SIZE) {

                                //also make sure button exists
                                if (getGame().getTextures().getSprite(Textures.Key.GoBackMenu) != null) {

                                    //flag that we want to exit, which will also save the level
                                    getGame().getScreenHelper().getCreateScreen().setExit(true);
                                }
                            }
                        }

                    } else {

                        //update released coordinates
                        getTouchPos().x = screenX;
                        getTouchPos().y = screenY;
                        calculateTouchPosition(getTouchPos());

                        //we have released
                        getGame().getScreenHelper().getCreateScreen().setPressed(false);
                        getGame().getScreenHelper().getCreateScreen().setDragged(false);
                        getGame().getScreenHelper().getCreateScreen().setReleased(true);
                    }
                    break;

                case SCREEN_GAME:

                    managePlayerMovement(screenX, screenY, getTouchPos().x, getTouchPos().y);

                    //if we touched "go back"
                    calculateTouchPosition(getTouchPos());
                    if (getTouchPos().x >= LevelScreen.GO_BACK_X && getTouchPos().x <= LevelScreen.GO_BACK_X + LevelScreen.GO_BACK_SIZE) {
                        if (getTouchPos().y >= LevelScreen.GO_BACK_Y && getTouchPos().y <= LevelScreen.GO_BACK_Y + LevelScreen.GO_BACK_SIZE) {

                            //also make sure "go back" exists
                            if (getGame().getTextures().getSprite(Textures.Key.GoBackMenu) != null) {

                                //go back to menu screen
                                try {
                                    getGame().getScreenHelper().changeScreen(SCREEN_MENU);
                                } catch (ScreenException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    break;
            }
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        //game can't be paused when checking for input
        if (getGame().isPaused())
            return false;

        //check if we selected something
        switch(getGame().getScreenHelper().getScreenIndex()) {

            case SCREEN_CREATE:

                //only update location if we are actually dragging an item
                if (getGame().getScreenHelper().getCreateScreen().getSelectedId() != null) {
                    getTouchPos().x = screenX;
                    getTouchPos().y = screenY;
                    calculateTouchPosition(getTouchPos());
                    getGame().getScreenHelper().getCreateScreen().setPressed(false);
                    getGame().getScreenHelper().getCreateScreen().setDragged(true);
                    getGame().getScreenHelper().getCreateScreen().setReleased(false);
                }
                break;
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y) {

        //game can't be paused when checking for input
        if (getGame().isPaused())
            return false;

        return false;
    }

    @Override
    public boolean scrolled(int amount) {

        //game can't be paused when checking for input
        if (getGame().isPaused())
            return false;

        return false;
    }

    private void managePlayerMovement(float screenX, float screenY, float previousScreenX, float previousScreenY) {

        float xDiff = Math.abs(screenX - previousScreenX);
        float yDiff = Math.abs(screenY - previousScreenY);

        if (xDiff > yDiff) {

            if (screenX > previousScreenX) {
                moveRight();
            } else if (screenX < previousScreenX) {
                moveLeft();
            }

        } else if (xDiff < yDiff) {

            if (screenY < previousScreenY) {
                moveUp();
            } else if (screenY > previousScreenY) {
                moveDown();
            }
        }
    }

    public Vector3 getTouchPos() {
        return this.touchPos;
    }

    public Vector3 getTouchPosRelease() {
        return this.touchPosRelease;
    }

    public void calculateTouchPosition(Vector3 position) {

        //System.out.println("touchPos before: x=" + getTouchPos().x + ", y=" + getTouchPos().y + ", z=" + getTouchPos().z);

        try {
            //now adjust the coordinates based on our camera projection
            getGame().getScreenHelper().getCurrentScreen().getCamera().unproject(position);
        } catch (ScreenException e) {
            e.printStackTrace();
        }

        //System.out.println("touchPos after: x=" + getTouchPos().x + ", y=" + getTouchPos().y + ", z=" + getTouchPos().z);
    }

    private void moveRight() {

        //don't accept input if we solved the level
        if (getLevel().isSolved())
            return;

        //get the player object
        Player player = getPlayer();

        //only move if not moving
        if (!player.isMoving())
            player.setMoveRight(true);
    }

    private void moveLeft() {

        //don't accept input if we solved the level
        if (getLevel().isSolved())
            return;

        //get the player object
        Player player = getPlayer();

        //only move if not moving
        if (!player.isMoving())
            player.setMoveLeft(true);
    }

    private void moveUp() {

        //don't accept input if we solved the level
        if (getLevel().isSolved())
            return;

        //get the player object
        Player player = getPlayer();

        //only move if not moving
        if (!player.isMoving())
            player.setMoveUp(true);
    }

    private void moveDown() {

        //don't accept input if we solved the level
        if (getLevel().isSolved())
            return;

        //get the player object
        Player player = getPlayer();

        //only move if not moving
        if (!player.isMoving())
            player.setMoveDown(true);
    }

    private Player getPlayer() {
        return getLevel().getPlayer();
    }

    private Level getLevel() {

        switch(getGame().getScreenHelper().getScreenIndex()) {
            case SCREEN_GAME:
                return getGame().getScreenHelper().getGameScreen().getLevel();

            case SCREEN_CREATE:
                return getGame().getScreenHelper().getCreateScreen().getLevel();
        }

        //couldn't find the level
        return null;
    }
}