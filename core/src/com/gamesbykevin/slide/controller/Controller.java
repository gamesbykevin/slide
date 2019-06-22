package com.gamesbykevin.slide.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.exception.ScreenException;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.level.objects.LevelObject;
import com.gamesbykevin.slide.level.objects.Player;

import static com.gamesbykevin.slide.level.objects.LevelObject.DEFAULT_VELOCITY_X;
import static com.gamesbykevin.slide.level.objects.LevelObject.DEFAULT_VELOCITY_Y;
import static com.gamesbykevin.slide.screen.ScreenHelper.*;

public class Controller implements InputProcessor {

    //keep reference to our game
    private final MyGdxGame game;

    //calculate the projected touch position
    private Vector3 touchPos;

    //how many fingers are on the screen
    private int count = 0;

    public Controller(MyGdxGame game) {
        this.game = game;

        //our touch coordinates
        this.touchPos = new Vector3();

        //make sure the controller has the input
        setInput();
    }

    public void setInput() {

        //we will use the game controller to capture input
        Gdx.input.setInputProcessor(this);

        //catch the back key
        Gdx.input.setCatchBackKey(true);
    }

    private MyGdxGame getGame() {
        return this.game;
    }

    @Override
    public boolean keyDown(int keycode) {

        //game can't be paused when checking for input
        if (getGame().isPaused())
            return false;

        if (keycode == Input.Keys.BACK) {

            try {
                switch (getGame().getScreenHelper().getScreenIndex()) {
                    case SCREEN_OPTIONS:
                    case SCREEN_GAME:
                    case SCREEN_CREATE:
                        getGame().getScreenHelper().changeScreen(SCREEN_MENU);
                        break;
                }

                // Do your optional back button handling (show pause menu?)
                System.out.println("BACK PRESSED!!!");

            } catch (ScreenException ex) {
                ex.printStackTrace();
            }
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
                    case SCREEN_CREATE:
                        getGame().getScreenHelper().changeScreen(SCREEN_MENU);
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
            this.touchPos.x = screenX;
            this.touchPos.y = screenY;

            //check if we selected something
            switch(getGame().getScreenHelper().getScreenIndex()) {

                case SCREEN_CREATE:
                    getGame().getScreenHelper().getCreateScreen().setPressed(true);
                    calculateTouchPosition();
                    break;
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        //after touching the screen the game is not paused
        getGame().setPaused(false);

        //keep track of fingers on screen
        count--;

        if (count == 0) {

            switch(getGame().getScreenHelper().getScreenIndex()) {

                case SCREEN_CREATE:
                    getGame().getScreenHelper().getCreateScreen().setPressed(false);
                    break;

                case SCREEN_GAME:

                    float xDiff = Math.abs(screenX - this.touchPos.x);
                    float yDiff = Math.abs(screenY - this.touchPos.y);

                    if (xDiff > yDiff) {

                        if (screenX > this.touchPos.x) {
                            moveRight();
                        } else if (screenX < this.touchPos.x) {
                            moveLeft();
                        }

                    } else if (xDiff < yDiff) {

                        if (screenY < this.touchPos.y) {
                            moveUp();
                        } else if (screenY > this.touchPos.y) {
                            moveDown();
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
                getTouchPos().x = screenX;
                getTouchPos().y = screenY;
                calculateTouchPosition();
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

    public Vector3 getTouchPos() {
        return touchPos;
    }

    public void calculateTouchPosition() {
        calculateTouchPosition(getTouchPos().x, getTouchPos().y, getTouchPos().z);
    }

    //do we need this?
    public void calculateTouchPosition(float x, float y, float z) {

        //System.out.println("touchPos before: x=" + x + ", y=" + y + ", z=" + z);

        //set our touch position accordingly
        getTouchPos().set(x, y, z);

        try {
            //now adjust the coordinates based on our camera projection
            getGame().getScreenHelper().getCurrentScreen().getCamera().unproject(getTouchPos());
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
        LevelObject player = getPlayer();

        //only move if not moving
        if (player.getDY() == 0 && player.getDX() == 0)
            player.setDX(DEFAULT_VELOCITY_X);
    }

    private void moveLeft() {

        //don't accept input if we solved the level
        if (getLevel().isSolved())
            return;

        //get the player object
        LevelObject player = getPlayer();

        //only move if not moving
        if (player.getDY() == 0 && player.getDX() == 0)
            player.setDX(-DEFAULT_VELOCITY_X);
    }

    private void moveUp() {

        //don't accept input if we solved the level
        if (getLevel().isSolved())
            return;

        //get the player object
        LevelObject player = getPlayer();

        //only move if not moving
        if (player.getDY() == 0 && player.getDX() == 0)
            player.setDY(DEFAULT_VELOCITY_Y);
    }

    private void moveDown() {

        //don't accept input if we solved the level
        if (getLevel().isSolved())
            return;

        //get the player object
        LevelObject player = getPlayer();

        //only move if not moving
        if (player.getDY() == 0 && player.getDX() == 0)
            player.setDY(-DEFAULT_VELOCITY_Y);
    }

    private LevelObject getPlayer() {
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