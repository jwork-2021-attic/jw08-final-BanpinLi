package com.umi.frame;

public class Tile<T extends Thing> {
    public static final int GROUND = 0;
    public static final int MID = 1;
    public static final int SKY = 2;

    private T midThing;
    // 根据空间位置的不同，还可以拥有地面-空中两个位置的thing
    private T groundThing;
    private T skyThing;
    private int xPos;
    private int yPos;

    public Tile() {
        this.xPos = -1;
        this.yPos = -1;
    }

    public Tile(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void setMidThing(T midThing) {
        this.midThing = midThing;
        this.midThing.setTile(this);
    }

    public void setGroundThing(T groundThing) {
        this.groundThing = groundThing;
        this.groundThing.setTile(this);
    }

    public void setSkyThing(T skyThing) {
        this.skyThing = skyThing;
        this.skyThing.setTile(this);
    }

    public T getMidThing() {
        return this.midThing;
    }

    public T getGroundThing() {
        return this.groundThing;
    }

    public T getSkyThing() {
        return this.skyThing;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getxPos() {
        return this.xPos;
    }

    public int getyPos() {
        return this.yPos;
    }
}
