package com.gamesbykevin.slide.entity;

import java.util.UUID;

public abstract class Entity {

    //size of object
    private float w, h;

    //velocity of x and y
    private float dx, dy;

    //the (x,y) render coordinates
    private float x, y;

    //create a unique id for each object
    private final UUID id;

    //location of entity
    private float col, row;

    /**
     * Default constructor
     */
    public Entity() {

        //assign a unique id
        this.id = UUID.randomUUID();

        setX(0);
        setY(0);
        setW(0);
        setH(0);
        setDX(0);
        setDY(0);
    }

    public UUID getId() {
        return this.id;
    }

    public float getY() {
        return this.y;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

    public float getDX() {
        return dx;
    }

    public void setDX(float dx) {
        this.dx = dx;
    }

    public float getDY() {
        return dy;
    }

    public void setDY(float dy) {
        this.dy = dy;
    }

    public float getCol() {
        return this.col;
    }

    public void setCol(float col) {
        this.col = col;
    }

    public float getRow() {
        return row;
    }

    public void setRow(float row) {
        this.row = row;
    }
}