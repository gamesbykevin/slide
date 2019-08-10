package com.gamesbykevin.slide;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.I18NBundle;
import com.gamesbykevin.slide.controller.Controller;
import com.gamesbykevin.slide.preferences.AppPreferences;
import com.gamesbykevin.slide.screen.ScreenHelper;
import com.gamesbykevin.slide.textures.Textures;
import com.gamesbykevin.slide.util.Language;

import java.util.Locale;

import static com.gamesbykevin.slide.audio.GameAudio.recycle;
import static com.gamesbykevin.slide.preferences.AppPreferences.getPreferenceValue;

public class MyGdxGame extends Game {

	//our game input controller
	private Controller controller;

	//manage our screens
	private ScreenHelper screenHelper;

	//our game textures
	private static Textures TEXTURES;

	//our bitmap font to render text in our game
	private static BitmapFont FONT;

	//what is the size of the text
	public static float TEXT_WIDTH;
	public static float TEXT_HEIGHT;

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

	//how much time to take away from our timer per frame
	public static final float TIME_DURATION = (1f / FPS);

	/**
	 * How long do we vibrate the phone
	 */
	public static final int DURATION_VIBRATE = 333;

	//object used for localization
	private static I18NBundle MY_BUNDLE;

	public static void exit() {

		//recycle audio
		recycle();

		//recycle textures
		resetTextures();

		//exit app
		Gdx.app.exit();
	}

	public static I18NBundle getMyBundle() {

		if (MY_BUNDLE == null) {

			//do we have a language setting
			final int index = getPreferenceValue(AppPreferences.PREF_LANGUAGE);

			//if we selected a language set it
			if (index >= 0) {
				changeMyBundle(index);
			} else {
				MY_BUNDLE = I18NBundle.createBundle(Gdx.files.internal("i18n/MyBundle"));
			}
		}

		return MY_BUNDLE;
	}

	public static void changeMyBundle(int index) {

		//list of languages
		Language.Languages[] languages = Language.Languages.values();

		//language settings
		final String countryCode = languages[index].getCountryCode();
		final String languageCode = languages[index].getLanguageCode();

		Locale locale;

		//create the locale
		if (countryCode != null && countryCode.length() > 0) {
			locale = new Locale(languageCode, countryCode);
		} else {
			locale = new Locale(languageCode);
		}

		//create new bundle with the specified language and country code
		MY_BUNDLE = I18NBundle.createBundle(Gdx.files.internal("i18n/MyBundle"), locale);
	}

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
			TEXT_HEIGHT = layout.height;
			layout = null;
		}

		return FONT;
	}

	public static void resetTextures() {

		if (TEXTURES != null) {
			TEXTURES.dispose();
			TEXTURES = null;
		}
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