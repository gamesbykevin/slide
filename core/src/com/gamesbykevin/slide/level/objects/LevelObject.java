package com.gamesbykevin.slide.level.objects;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.*;
import com.gamesbykevin.slide.entity.Entity;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.textures.Textures;

import static com.gamesbykevin.slide.MyGdxGame.getTextures;
import static com.gamesbykevin.slide.level.Level.updateCoordinates;
import static com.gamesbykevin.slide.screen.ParentScreen.SCREEN_HEIGHT;
import static com.gamesbykevin.slide.screen.ParentScreen.SCREEN_WIDTH;

public abstract class LevelObject extends Entity {

    //default size of a level object
    public static float DEFAULT_DIMENSION = 32;

    //default speed
    public static final float DEFAULT_VELOCITY_X = .33f;
    public static final float DEFAULT_VELOCITY_Y = .33f;

    //speed when not moving
    public static final float VELOCITY_NONE = 0.00f;

    //distance(s) used to detect collision
    public static final float DISTANCE_COLLISION = .5f;

    //what texture do we render for this object
    private Textures.Key textureKey;

    //do we rotate?
    private boolean rotate = false;

    //any level object can have particles
    private ParticleEffect particleEffect;

    //used to change the angle of the particles, etc...
    private ParticleEmitter particleEmitter;

    //where is our particle meta particles
    public static final String PARTICLE_PATH = "particles/";

    public enum Type {
        Bomb, Danger, Dot, Goal, Indicator, Key, LockedGoal, PartialWall,
        Player, Redirect, Teleporter, Wall, WallConnector
    }

    //what type of level object is this?
    private Type type;

    public LevelObject(Type type) {

        //call parent
        super();

        //assign the level object type
        setType(type);

        //default values
        setW(DEFAULT_DIMENSION);
        setH(DEFAULT_DIMENSION);
        setRotate(false);
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ParticleEffect getParticleEffect() {
        return this.particleEffect;
    }

    public void createParticleEffect(FileHandle fileHandle1, FileHandle fileHandle2) {

        //particles currently break in html
        if (Gdx.app.getType() != Application.ApplicationType.WebGL) {
            this.particleEffect = new ParticleEffect();
            this.particleEffect.load(fileHandle1, fileHandle2);

            this.particleEmitter = getParticleEffect().getEmitters().first();
            this.particleEmitter.setPosition(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);

            //tell it to start at the beginning
            getParticleEffect().start();
        }
    }

    public ParticleEmitter getParticleEmitter() {
        return this.particleEmitter;
    }

    /**
     * Logic to update
     */
    public void update(Level level) {

        if (getDX() != VELOCITY_NONE || getDY() != VELOCITY_NONE)
            updateCoordinates(this);
    }

    /**
     * Update the player based on the object
     * @param level
     */
    public abstract void updateCollision(Level level);

    /**
     * Reset the object
     */
    public abstract void reset(Level level);

    public void render(SpriteBatch batch) {
        render(batch, getTextures().getSprite(getTextureKey()));
    }

    private void render(SpriteBatch batch, Sprite sprite) {

        if (isRotate()) {
            sprite.setPosition(getX(), getY());
            sprite.setSize(getW(), getH());
            sprite.setOriginCenter();

            //now we can add it to the batch to be rendered
            sprite.draw(batch);

        } else {

            //add it to be rendered
            batch.draw(
                sprite.getTexture(),
                getX(),
                getY(),
                getW(),
                getH()
            );
        }
    }

    public void stop() {
        this.setDX(VELOCITY_NONE);
        this.setDY(VELOCITY_NONE);
        updateCoordinates(this);
    }

    public boolean isRotate() {
        return this.rotate;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }

    public Textures.Key getTextureKey() {
        return this.textureKey;
    }

    public void setTextureKey(Textures.Key textureKey) {
        this.textureKey = textureKey;
    }

    public boolean hasCollision(LevelObject object) {
        return hasCollision(object, DISTANCE_COLLISION);
    }

    public boolean hasCollision(LevelObject object, float collisionDistance) {

        //calculate distance
        double x = Math.pow(getCol() - object.getCol(), 2);
        double y = Math.pow(getRow() - object.getRow(), 2);
        double distance = Math.sqrt(x + y);

        //if close enough we have collision
        return (distance <= collisionDistance);
    }
}