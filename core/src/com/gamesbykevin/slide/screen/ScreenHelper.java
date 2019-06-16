package com.gamesbykevin.slide.screen;

import com.badlogic.gdx.Gdx;
import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.exception.ScreenException;

public class ScreenHelper {

    /**
     * Our different screens
     */
    public static final int SCREEN_SPLASH = 0;
    public static final int SCREEN_MENU = 1;
    public static final int SCREEN_OPTIONS = 2;
    public static final int SCREEN_GAME = 3;
    public static final int SCREEN_SELECT = 4;

    //what screen are we on?
    private int screenIndex = SCREEN_SPLASH;

    //our different screens
    private SplashScreen splashScreen;
    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private OptionsScreen optionsScreen;
    private LevelSelectScreen levelSelectScreen;

    private final MyGdxGame game;

    public ScreenHelper(MyGdxGame game) {

        //store game reference
        this.game = game;

        try {
            //set splash screen as start
            this.changeScreen(SCREEN_SPLASH);
        } catch (ScreenException exception) {
            exception.printStackTrace();
            Gdx.app.exit();
        }
    }

    private MyGdxGame getGame() {
        return this.game;
    }

    public LevelSelectScreen getLevelSelectScreen() {

        //create our screen if null
        if (this.levelSelectScreen == null)
            this.levelSelectScreen = new LevelSelectScreen(getGame());

        return this.levelSelectScreen;
    }

    public OptionsScreen getOptionsScreen() {

        //create our screen if null
        if (this.optionsScreen == null)
            this.optionsScreen = new OptionsScreen(getGame());

        return this.optionsScreen;
    }

    public SplashScreen getSplashScreen() {

        //create our screen if null
        if (this.splashScreen == null)
            this.splashScreen = new SplashScreen(getGame());

        return splashScreen;
    }

    public GameScreen getGameScreen() {

        //create our screen if null
        if (this.gameScreen == null)
            this.gameScreen = new GameScreen(getGame());

        return this.gameScreen;
    }

    public MenuScreen getMenuScreen() {

        if (this.menuScreen == null)
            this.menuScreen = new MenuScreen(getGame());

        return this.menuScreen;
    }

    public void setScreenIndex(final int screenIndex) {
        this.screenIndex = screenIndex;
    }

    public int getScreenIndex() {
        return this.screenIndex;
    }

    public ParentScreen getCurrentScreen() throws ScreenException {
        return getScreen(getScreenIndex());
    }

    public ParentScreen getScreen(int screenIndex) throws ScreenException {

        switch (screenIndex) {

            case SCREEN_SPLASH:
                return getSplashScreen();

            case SCREEN_MENU:
                return getMenuScreen();

            case SCREEN_GAME:
                return getGameScreen();

            case SCREEN_OPTIONS:
                return getOptionsScreen();

            case SCREEN_SELECT:
                return getLevelSelectScreen();

            default:
                throw new ScreenException("Screen not found: " + getScreenIndex());
        }
    }

    public void changeScreen(int screenIndex) throws ScreenException {

        //set the screen accordingly
        getGame().setScreen(getScreen(screenIndex));

        //if no exceptions are thrown let's officially change the screen index
        setScreenIndex(screenIndex);
    }
}