package com.umi.frame;

import UIFrame.Displayable;
import UIFrame.ItemImage;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public abstract class Thing implements Displayable, Serializable {

    protected transient World world;
    private transient Tile<? extends Thing> tile;
    protected transient ItemImage itemImage;
    private String imageFilename;
    private int position;

    public Thing(ItemImage itemImage, World world, int position) {
        this.itemImage = itemImage;
        this.imageFilename = itemImage.getImageFilename();
        this.world = world;
        this.position = position;
    }

    public ItemImage getItemImage() {
        return this.itemImage;
    }

    @Override
    public BufferedImage getBufferedImage() {
        return itemImage.getImage();
    }

    public void setItemImage(ItemImage itemImage) {
        this.itemImage = itemImage;
    }

    @Override
    public void setBufferedImage(BufferedImage image) {

    }

    public void setTile(Tile<? extends Thing> tile) {
        this.tile = tile;
    }

    public Tile<? extends Thing> getTile() {
        return this.tile;
    }

    public int getPosition() {
        return position;
    }

    public String getImageFilename() {
        return this.imageFilename;
    }

    // 被另外的Thing碰撞到了
    public abstract void beCollided(Thing ant);

    public void setWorld(World world) {
        this.world = world;
    }
}
