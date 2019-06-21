package com.gamesbykevin.slide.level.objects;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.*;
import com.gamesbykevin.slide.entity.Entity;
import com.gamesbykevin.slide.level.Level;
import com.gamesbykevin.slide.textures.Textures;

public abstract class LevelObject extends Entity {

    //default size of a level object
    public static final float DEFAULT_WIDTH = 32;
    public static final float DEFAULT_HEIGHT = 32;

    //default speed
    public static final float DEFAULT_VELOCITY_X = .33f;
    public static final float DEFAULT_VELOCITY_Y = .33f;

    //speed when not moving
    public static final float VELOCITY_NONE = 0.00f;

    //distance(s) used to detect collision
    public static final float DISTANCE_COLLISION_NORMAL = .9f;
    public static final float DISTANCE_COLLISION_CLOSE = .1f;

    //what texture do we render for this object
    private Textures.Key key;

    //do we rotate?
    private boolean rotate = false;

    //the rotation speed in degrees
    private float rotationSpeed = 0;

    //any level object can have particles
    private ParticleEffect particleEffect;

    //where is our particle meta particles
    public static final String PARTICLE_PATH = "particles/";

    public LevelObject() {

        //call parent
        super();

        //default values
        setW(DEFAULT_WIDTH);
        setH(DEFAULT_HEIGHT);
        setRotate(false);
        setRotationSpeed(0);
    }

    public ParticleEffect getParticleEffect() {
        return this.particleEffect;
    }

    public void createParticleEffect(FileHandle fileHandle1, FileHandle fileHandle2) {
        this.particleEffect = new ParticleEffect();
        this.particleEffect.load(fileHandle1, fileHandle2);

        //TextureAtlas atlas = new TextureAtlas();
        //atlas.addRegion("explosion",new TextureRegion(new Texture("particles/explosion.png")));
        //this.particleEffect.load(Gdx.files.internal("particles/explosion.p"), atlas);
    }

    /**
     * Logic to update
     */
    public abstract void update();

    /**
     * Update the player based on the object
     * @param level
     */
    public abstract void updateCollision(Level level);

    /**
     * Reset the object
     */
    public abstract void reset(Level level);

    public void render(SpriteBatch batch, Sprite sprite, BitmapFont font) {

        if (isRotate()) {
            sprite.setPosition(getX(), getY());
            sprite.setSize(getW(), getH());
            sprite.setOriginCenter();
            sprite.rotate(getRotationSpeed());

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
    }

    public boolean isRotate() {
        return this.rotate;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }

    public float getRotationSpeed() {
        return this.rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public Textures.Key getKey() {
        return this.key;
    }

    public void setKey(Textures.Key key) {
        this.key = key;
    }

    public boolean hasCollisionNormal(LevelObject object) {
        return hasCollision(object, DISTANCE_COLLISION_NORMAL);
    }

    public boolean hasCollisionClose(LevelObject object) {
        return hasCollision(object, DISTANCE_COLLISION_CLOSE);
    }

    private boolean hasCollision(LevelObject object, float collisionDistance) {

        //calculate distance
        double x = Math.pow(getCol() - object.getCol(), 2);
        double y = Math.pow(getRow() - object.getRow(), 2);
        double distance = Math.sqrt(x + y);

        //if close enough we have collision
        return (distance <= collisionDistance);
    }
}