package com.umi.entity.bean;

import UIFrame.ItemImage;
import com.umi.entity.NonLiving;
import com.umi.frame.Tile;
import com.umi.frame.World;

public class Sand extends NonLiving {

    public Sand(ItemImage itemImage, World world) {
        super(itemImage, world, Tile.GROUND);
    }

    public Sand(World world) {
        this(ItemImage.SAND, world);
    }

}
