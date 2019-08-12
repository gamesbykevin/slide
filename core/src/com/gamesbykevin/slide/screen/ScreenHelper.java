package com.gamesbykevin.slide.screen;

import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.exception.ScreenException;

import static com.gamesbykevin.slide.MyGdxGame.exit;
import static com.gamesbykevin.slide.MyGdxGame.resetTextures;

public class ScreenHelper {

    /**
     * Our different screens
     */
    public static final int SCREEN_SPLASH = 0;
    public static final int SCREEN_MENU = 1;
    public static final int SCREEN_OPTIONS = 2;
    public static final int SCREEN_GAME = 3;
    public static final int SCREEN_SELECT_LEVEL = 4;
    public static final int SCREEN_CREATE = 5;
    public static final int SCREEN_SELECT_CREATE = 6;
    public static final int SCREEN_SELECT_LEVEL_CUSTOM = 7;

    //what screen are we on?
    private int screenIndex = SCREEN_SPLASH;

    //our different screens
    private SplashScreen splashScreen;
    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private OptionsScreen optionsScreen;
    private LevelSelectScreen levelSelectScreen;
    private CreateScreen createScreen;
    private CreateSelectScreen createSelectScreen;
    private LevelSelectCustomScreen levelSelectCustomScreen;

    private final MyGdxGame game;

    public ScreenHelper(MyGdxGame game) {

        //store game reference
        this.game = game;

        try {
            //set splash screen as start
            this.changeScreen(SCREEN_SPLASH);
        } catch (ScreenException exception) {
            exception.printStackTrace();
            exit();
        }
    }

    private MyGdxGame getGame() {
        return this.game;
    }

    public LevelSelectCustomScreen getLevelSelectCustomScreen() {

        if (this.levelSelectCustomScreen == null)
            this.levelSelectCustomScreen = new LevelSelectCustomScreen(getGame());

        return this.levelSelectCustomScreen;
    }

    public CreateSelectScreen getCreateSelectScreen() {

        if (this.createSelectScreen == null)
            this.createSelectScreen = new CreateSelectScreen(getGame());

        return this.createSelectScreen;
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

    public CreateScreen getCreateScreen() {

        if (this.createScreen == null)
            this.createScreen = new CreateScreen(getGame());

        return this.createScreen;
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

    public TemplateScreen getScreen(int screenIndex) throws ScreenException {

        switch (screenIndex) {

            case SCREEN_SPLASH:
                return getSplashScreen();

            case SCREEN_MENU:
                return getMenuScreen();

            case SCREEN_GAME:
                return getGameScreen();

            case SCREEN_OPTIONS:
                return getOptionsScreen();

            case SCREEN_SELECT_LEVEL:
                return getLevelSelectScreen();

            case SCREEN_CREATE:
                return getCreateScreen();

            case SCREEN_SELECT_CREATE:
                return getCreateSelectScreen();

            case SCREEN_SELECT_LEVEL_CUSTOM:
                return getLevelSelectCustomScreen();

            default:
                throw new ScreenException("Screen not found: " + getScreenIndex());
        }
    }

    public void changeScreen(int screenIndex) throws ScreenException {


        //get the new screen
        TemplateScreen templateScreen = getScreen(screenIndex);

        //set prompt to false every time we change the screen
        templateScreen.setPrompt(false);

        //set the screen accordingly
        getGame().setScreen(templateScreen);

        //if we are leaving the game screen recycle the level etc...
        switch (getScreenIndex()) {
            case SCREEN_GAME:
                if (getGameScreen().getLevel() != null) {
                    getGameScreen().getLevel().dispose();
                    getGameScreen().setLevel(null);
                    resetTextures();
                }
                break;
        }

        //if no exceptions are thrown let's officially change the screen index
        setScreenIndex(screenIndex);
    }
}