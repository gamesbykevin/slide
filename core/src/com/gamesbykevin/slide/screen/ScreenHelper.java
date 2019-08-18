package com.gamesbykevin.slide.screen;

import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.exception.ScreenException;

import java.util.HashMap;

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

    //hash map containing our screens
    private HashMap<Integer, TemplateScreen> screens;

    //our game reference
    private final MyGdxGame game;

    public ScreenHelper(MyGdxGame game) {

        //store game reference
        this.game = game;

        try {
            //set splash screen as start
            this.changeScreen(SCREEN_SPLASH);
        } catch (ScreenException exception) {
            exception.printStackTrace();
            exit(getGame());
        }
    }

    private MyGdxGame getGame() {
        return this.game;
    }

    private HashMap<Integer, TemplateScreen> getScreens() {

        if (this.screens == null)
            this.screens = new HashMap<>();

        return this.screens;
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

    public TemplateScreen getScreen(int screenIndex) {

        //get the screen from our hash map
        TemplateScreen screen = getScreens().get(screenIndex);

        //if the screen does not exist, we will create it
        if (screen == null) {

            switch (screenIndex) {

                case SCREEN_SPLASH:
                    screen = new SplashScreen(getGame());
                    break;

                case SCREEN_MENU:
                    screen = new MenuScreen(getGame());
                    break;

                case SCREEN_GAME:
                    screen = new GameScreen(getGame());
                    break;

                case SCREEN_OPTIONS:
                    screen = new OptionsScreen(getGame());
                    break;

                case SCREEN_SELECT_LEVEL:
                    screen = new LevelSelectScreen(getGame());
                    break;

                case SCREEN_CREATE:
                    screen = new CreateScreen(getGame());
                    break;

                case SCREEN_SELECT_CREATE:
                    screen = new CreateSelectScreen(getGame());
                    break;

                case SCREEN_SELECT_LEVEL_CUSTOM:
                    screen = new LevelSelectCustomScreen(getGame());
                    break;
            }

            //add the screen to our hash map
            getScreens().put(screenIndex, screen);
        }

        //return our screen
        return screen;
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
                GameScreen screen = (GameScreen)getScreen(SCREEN_GAME);
                if (screen != null && screen.getLevel() != null) {
                    screen.getLevel().dispose();
                    screen.setLevel(null);
                    resetTextures();
                }
                break;
        }

        //if no exceptions are thrown let's officially change the screen index
        setScreenIndex(screenIndex);
    }

    public void dispose() {

        if (this.screens != null) {
            for (Integer integer : this.screens.keySet()) {
                this.screens.get(integer).dispose();
                this.screens.put(integer, null);
            }

            this.screens.clear();
        }

        this.screens = null;
    }
}