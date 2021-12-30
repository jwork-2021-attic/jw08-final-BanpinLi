package com.umi.entity;

import UIFrame.ItemImage;
import com.umi.frame.Thing;
import com.umi.frame.Tile;
import com.umi.frame.World;

public class NonLiving extends Thing {

    public NonLiving(ItemImage itemImage, World world) {
        this(itemImage, world, Tile.GROUND);
    }

    public NonLiving(ItemImage itemImage, World world, int position) {
        super(itemImage, world, position);
    }

    @Override
    public void beCollided(Thing ant) {}
}
