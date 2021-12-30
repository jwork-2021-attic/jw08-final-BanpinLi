package com.umi.entity.bean;

import UIFrame.ItemImage;
import com.umi.entity.NonLiving;
import com.umi.frame.Tile;
import com.umi.frame.World;

public class Water extends NonLiving {

    public Water(ItemImage itemImage, World world) {
        super(itemImage, world, Tile.GROUND);
    }

    public Water(World world) {
        this(ItemImage.WATER, world);
    }
}
