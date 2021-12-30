package com.umi.entity.bean;

import UIFrame.ItemImage;
import com.umi.entity.NonLiving;
import com.umi.frame.Tile;
import com.umi.frame.World;

public class Turf extends NonLiving {

    public Turf(ItemImage itemImage, World world) {
        super(itemImage, world, Tile.GROUND);
    }

    public Turf(World world) {
        this(ItemImage.TURF, world);
    }

}
