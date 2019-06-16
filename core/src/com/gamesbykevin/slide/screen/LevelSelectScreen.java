package com.gamesbykevin.slide.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gamesbykevin.slide.MyGdxGame;
import com.gamesbykevin.slide.exception.ScreenException;
import com.gamesbykevin.slide.model.GameModel;

import static com.gamesbykevin.slide.screen.ScreenHelper.SCREEN_GAME;

public class LevelSelectScreen extends TemplateScreen {

    //container with all our selections
    private Table container;

    //how big is each level selection
    private static final float BUTTON_SIZE = 80;

    //how many columns on our table
    private static final int COLUMNS = 5;

    //how much pixel padding between cells
    private static final int PADDING = 10;

    public LevelSelectScreen(MyGdxGame game) {
        super(game);
        create();
    }

    public void create() {

        //this container will have our buttons and the scroll pane
        container = new Table();

        //make entire size of screen
        getContainer().setFillParent(true);

        //add the container to our stage
        getStage().addActor(getContainer());

        //create our table with level select buttons
        Table table = new Table();

        //we need to allow the user to scroll
        final ScrollPane scroll = new ScrollPane(table, getSkin());
        scroll.setFlickScroll(true);

        table.pad(PADDING).defaults().expandX().space(PADDING);
        table.row();
        table.add(new Label("Level Select", getSkin())).expandX().fillX().colspan(COLUMNS).top();

        //how many levels are there
        int levels = Gdx.files.internal("levels").list().length;

        //keep track of level #
        int count = 0;

        //continue until we add all our level selections to the list
        while (true) {

            //create a new row
            table.row();

            for (int col = 0; col < COLUMNS; col++) {

                //need final value to apply to listener
                final int levelIndex = count;

                //each button will have the level # as the label
                TextButton button = new TextButton((count + 1) + "", getSkin());

                //each button will be the same size
                table.add(button).size(BUTTON_SIZE, BUTTON_SIZE).top();

                //add on click event
                button.addListener(new ClickListener() {
                    public void clicked (InputEvent event, float x, float y) {

                        //change to the game screen
                        try {

                            //assign the selected level
                            GameModel.LEVEL_INDEX = levelIndex;

                            //switch to the game screen
                            getGame().getScreenHelper().changeScreen(SCREEN_GAME);

                        } catch (ScreenException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                //keep track of the levels added
                count++;

                //if we reached our limit, let's stop
                if (count >= levels)
                    break;
            }

            //if we reached our limit, let's stop
            if (count >= levels)
                break;
        }

        getContainer().add(scroll).expand().fill();
        getContainer().row().space(PADDING).padBottom(PADDING);

        /*
        container.add(scroll).expand().fill().colspan(4);
        container.row().space(10).padBottom(10);
        container.add(flickButton).right().expandX();
        container.add(onTopButton);
        container.add(smoothButton);
        container.add(fadeButton).left().expandX();
        */
    }

    private Table getContainer() {
        return this.container;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {

        //clear the screen
        super.clearScreen();

        //draw the background first
        getStage().getBatch().begin();
        drawBackground(getStage().getBatch());
        getStage().getBatch().end();

        //now draw the stage
        getStage().act(Gdx.graphics.getDeltaTime());
        getStage().draw();
    }
}