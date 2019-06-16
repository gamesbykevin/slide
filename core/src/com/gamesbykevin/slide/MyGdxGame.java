package com.gamesbykevin.slide;

import com.badlogic.gdx.Game;
import com.gamesbykevin.slide.controller.Controller;
import com.gamesbykevin.slide.preferences.AppPreferences;
import com.gamesbykevin.slide.screen.ScreenHelper;

public class MyGdxGame extends Game {

	//our game input controller
	private Controller controller;

	//our application preferences
	private AppPreferences preferences;

	//manage our screens
	private ScreenHelper screenHelper;

	//is the game paused?
	private boolean paused = false;

	/**
	 * Frames per second speed for our game play
	 */
	public static final float FPS = 60f;

	/**
	 * How long is 1 frame in milliseconds
	 */
	public static final float FRAME_MS = (1000f / FPS);

	@Override
	public void create() {

		//create our game controller
		this.controller = new Controller(this);

		//create our screen container
		this.screenHelper = new ScreenHelper(this);
	}

	public ScreenHelper getScreenHelper() {
	    return this.screenHelper;
    }

	public Controller getController() {
		return this.controller;
	}

	public AppPreferences getPreferences() {

		if (this.preferences == null)
			this.preferences = new AppPreferences();

		return this.preferences;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(final boolean paused) {
		this.paused = paused;
	}

	@Override
	public void pause() {
		super.pause();
	}
}