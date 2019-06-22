package com.gamesbykevin.slide;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.gamesbykevin.slide.controller.Controller;
import com.gamesbykevin.slide.preferences.AppPreferences;
import com.gamesbykevin.slide.screen.ScreenHelper;
import com.gamesbykevin.slide.textures.Textures;

public class MyGdxGame extends Game {

	//our game input controller
	private Controller controller;

	//our application preferences
	private AppPreferences preferences;

	//manage our screens
	private ScreenHelper screenHelper;

	//our game textures
	private static Textures TEXTURES;

	//our bitmap font to render text in our game
	private static BitmapFont FONT;

	//what is the width of the text
	public static float TEXT_WIDTH;

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

	/**
	 * How long do we vibrate the phone
	 */
	public static final int DURATION_VIBRATE = 1000;

	@Override
	public void create() {

		//create our game controller
		this.controller = new Controller(this);

		//create our screen container
		this.screenHelper = new ScreenHelper(this);
	}

	public static BitmapFont getFont() {

		//create new font
		if (FONT == null) {
			FONT = new BitmapFont();

			//create our font metrics for reference
			GlyphLayout layout = new GlyphLayout(FONT, "9");

			//update the text width
			TEXT_WIDTH = layout.width;

			layout = null;
		}

		return FONT;
	}

	public static Textures getTextures() {

		//create our game textures if null
		if (TEXTURES == null)
			TEXTURES = new Textures();

		return TEXTURES;
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