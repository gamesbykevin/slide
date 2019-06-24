package com.gamesbykevin.slide.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.exception.ScreenException;

public class TemplateScreen extends ParentScreen {

    //links to social media
    public static final String URL_YOUTUBE = "https://youtube.com/gamesbykevin";
    public static final String URL_FACEBOOK = "https://facebook.com/gamesbykevin";
    public static final String URL_TWITTER = "https://twitter.com/gamesbykevin";
    public static final String URL_INSTAGRAM = "https://instagram.com/gamesbykevin";

    //items to show our menu
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;

    //size of our buttons
    protected static final int BUTTON_WIDTH = (int)(SCREEN_WIDTH * .5f);
    protected static final int BUTTON_HEIGHT = 50;

    //padding for each button
    protected static final int BUTTON_PADDING = 10;

    //default padding for controls
    protected static final int DEFAULT_PADDING = 20;

    //how big are the check boxes
    protected static final float CHECKBOX_SCALE = 2.05f;

    //how big are our social media icons
    protected static final int SOCIAL_ICON_SIZE = 64;

    //how much space between icons
    protected static final int SOCIAL_ICON_PADDING = 15;

    //font ratio size
    protected static final float FONT_SCALE = 1.25f;

    //did we prompt user when they hit the back button
    private boolean prompt = false;

    //needed to capture input from keyboard/mobile as well as the stage
    private InputMultiplexer inputMultiplexer;

    public TemplateScreen(MyGdxGame game) {
        super(game);

        //create our input multiplexer
        this.inputMultiplexer = new InputMultiplexer();

        //load our atlas and skin
        atlas = new TextureAtlas("skin/skin.atlas");
        skin = new Skin(Gdx.files.internal("skin/skin.json"), atlas);

        Viewport viewport = new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT, getCamera());
        viewport.apply();

        //create the stage
        stage = new Stage(viewport) {

            //handle back button
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK || keyCode == Input.Keys.ESCAPE) {
                    try {
                        if (getGame().getScreenHelper().getScreenIndex() == ScreenHelper.SCREEN_OPTIONS)
                            getGame().getScreenHelper().changeScreen(ScreenHelper.SCREEN_MENU);
                        if (getGame().getScreenHelper().getScreenIndex() == ScreenHelper.SCREEN_MENU) {

                            if (prompt) {

                                //if user was already prompted, close app
                                Gdx.app.exit();

                            } else {
                                //TemplateScreen.super.createToastShort("Hit back again to exit!");
                                prompt = true;
                            }
                        }
                    } catch (ScreenException ex) {
                        ex.printStackTrace();
                    }
                }
                return super.keyDown(keyCode);
            }
        };
    }

    @Override
    public void pause() {
        super.pause();

        //flag false
        this.prompt = false;
    }

    @Override
    public void resume() {
        super.resume();

        //flag false
        this.prompt = false;
    }

    @Override
    public void show() {
        super.show();

        //menu will capture input
        captureMenuInput();
    }

    public void captureMenuInput() {

        //clear anything we have added
        getInputMultiplexer().clear();

        //add the stage and controller so we can check input for both
        getInputMultiplexer().addProcessor(getStage());
        getInputMultiplexer().addProcessor(getGame().getController());

        //then set the multiplexer to capture both input
        Gdx.input.setInputProcessor(getInputMultiplexer());

        //catch the back key as well
        Gdx.input.setCatchBackKey(true);
    }

    private InputMultiplexer getInputMultiplexer() {
        return this.inputMultiplexer;
    }

    protected Skin getSkin() {
        return this.skin;
    }

    protected Stage getStage() {
        return this.stage;
    }

    protected TextureAtlas getAtlas() {
        return this.atlas;
    }

    @Override
    public void dispose() {

        if (atlas != null)
            atlas.dispose();
        if (skin != null)
            skin.dispose();
        if (stage != null)
            stage.dispose();

        atlas = null;
        skin = null;
        stage = null;
    }

    public void addSocialIcons() {


        //Create Table
        Table table = new Table();

        //Set table to fill stage
        table.setFillParent(true);

        //Set alignment of contents in the table.
        table.bottom();

        //Create buttons
        ImageButton buttonTwitter = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("social_icons/twitter.png")))));
        ImageButton buttonYoutube = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("social_icons/youtube.png")))));
        ImageButton buttonFacebook = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("social_icons/facebook.png")))));
        ImageButton buttonInstagram = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("social_icons/instagram.png")))));

        //add on down effect
        buttonTwitter.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("social_icons/twitter_down.png"))));
        buttonYoutube.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("social_icons/youtube_down.png"))));
        buttonFacebook.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("social_icons/facebook_down.png"))));
        buttonInstagram.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("social_icons/instagram_down.png"))));

        //Add listeners to buttons
        buttonTwitter.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { Gdx.net.openURI(URL_TWITTER); }
        });

        buttonYoutube.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { Gdx.net.openURI(URL_YOUTUBE); }
        });

        buttonFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { Gdx.net.openURI(URL_FACEBOOK); }
        });

        buttonInstagram.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { Gdx.net.openURI(URL_INSTAGRAM); }
        });

        //Add buttons to table
        table.add(buttonYoutube).width(SOCIAL_ICON_SIZE).height(SOCIAL_ICON_SIZE).pad(SOCIAL_ICON_PADDING);
        table.add(buttonInstagram).width(SOCIAL_ICON_SIZE).height(SOCIAL_ICON_SIZE).pad(SOCIAL_ICON_PADDING);
        table.add(buttonFacebook).width(SOCIAL_ICON_SIZE).height(SOCIAL_ICON_SIZE).pad(SOCIAL_ICON_PADDING);
        table.add(buttonTwitter).width(SOCIAL_ICON_SIZE).height(SOCIAL_ICON_SIZE).pad(SOCIAL_ICON_PADDING);

        //Add table to stage
        getStage().addActor(table);
    }
}