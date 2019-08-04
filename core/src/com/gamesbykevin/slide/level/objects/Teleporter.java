package com.gamesbykevin.slide.level.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.audio.GameAudio;

import static com.gamesbykevin.slide.level.Level.updateCoordinates;

public class Teleporter extends LevelObject {

    //how fast to rotate
    public static final float DEFAULT_ROTATION = -2f;

    //the teleporter this one is linked to
    private String linkId;

    //the character from our level data used to create this teleporter
    private String fileCharKey;

    //the teleporter we are linked with
    private Teleporter link;

    //is the player colliding with the teleporter already?
    private boolean collision = false;

    //distance to check for collision
    private static final float COLLISION_DISTANCE = .99f;

    /**
     * Default constructor
     */
    public Teleporter() {
        super(Type.Teleporter);
        setRotate(true);
    }

    public void setFileCharKey(String fileCharKey) {
        this.fileCharKey = fileCharKey;
    }

    public String getFileCharKey() {
        return this.fileCharKey;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getLinkId() {
        return this.linkId;
    }

    @Override
    public void update(Level level) {

        //call parent
        super.update(level);

        //if we have collision check if that changed
        if (isCollision()) {

            //if we no longer have collision the player has exited
            if (!hasCollision(level.getPlayer(), COLLISION_DISTANCE)) {
                setCollision(false);
            }
        }
    }

    @Override
    public void updateCollision(Level level) {

        //don't check if we already have collision
        if (isCollision())
            return;

        Player player = level.getPlayer();

        //wait till the player is close enough to the teleporter
        if (player.getDX() > 0 && player.getCol() < getCol())
            return;
        if (player.getDX() < 0 && player.getCol() > getCol())
            return;
        if (player.getDY() > 0 && player.getRow() < getRow())
            return;
        if (player.getDY() < 0 && player.getRow() > getRow())
            return;

        //flag that we have collision
        setCollision(true);

        //get the teleporter that we are linked to
        if (getLink() == null) {
            this.link = (Teleporter)level.getLevelObject(getLinkId());
        }

        //teleport to the other location
        player.setCol(getLink().getCol());
        player.setRow(getLink().getRow());
        getLink().setCollision(true);
        updateCoordinates(player);

        //play sound effect
        GameAudio.playSfx(GameAudio.SoundEffect.Teleport);
    }

    public boolean isCollision() {
        return this.collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public Teleporter getLink() {
        return this.link;
    }

    @Override
    public void reset(Level level) {
        //do we need to reset anything here
        setCollision(false);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }
}