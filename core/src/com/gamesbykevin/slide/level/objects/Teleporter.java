package com.gamesbykevin.slide.level.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.slide.level.Level;

import static com.gamesbykevin.slide.level.Level.updateCoordinates;

public class Teleporter extends LevelObject {

    //how fast to rotate
    public static final float DEFAULT_ROTATION = -1f;

    //the teleporter this one is linked to
    private String linkId;

    /**
     * Default constructor
     */
    public Teleporter() {
        setRotate(true);
        setRotationSpeed(DEFAULT_ROTATION);
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getLinkId() {
        return this.linkId;
    }

    @Override
    public void update() {

        //call parent
        super.update();
    }

    @Override
    public void updateCollision(Level level) {

        Player player = level.getPlayer();

        //get the teleporter that we are linked to
        Teleporter teleporter = (Teleporter)level.getLevelObject(getLinkId());

        //we just came from this teleporter so we ignore
        if (player.getTeleporterId() != null && player.getTeleporterId().equals(getId()))
            return;

        //teleport to the other location
        player.setCol(teleporter.getCol());
        player.setRow(teleporter.getRow());
        player.setTeleporterId(teleporter.getId());
        updateCoordinates(player);
    }

    @Override
    public void reset(Level level) {
        //do we need to reset anything here
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }
}