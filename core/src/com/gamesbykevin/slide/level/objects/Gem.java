package com.gamesbykevin.slide.level.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.textures.Textures;

public class Gem extends LevelObject {

    private boolean display = true;

    public Gem() {
        super(Type.Gem);
        super.setTextureKey(Textures.Key.Gem);
    }

    public boolean isDisplay() {
        return this.display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    @Override
    public void updateCollision(Level level) {

        //if we are displaying, count that we collected it
        if (isDisplay())
            level.setCountGem(level.getCountGem() + 1);

        setDisplay(false);
    }

    @Override
    public void reset(Level level) {
        setDisplay(true);
    }

    @Override
    public void render(SpriteBatch batch) {

        if (isDisplay())
            super.render(batch);
    }
}