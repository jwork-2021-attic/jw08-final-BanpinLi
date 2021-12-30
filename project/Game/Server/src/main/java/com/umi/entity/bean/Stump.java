package com.umi.entity.bean;

import UIFrame.ItemImage;
import com.umi.entity.NonLiving;
import com.umi.frame.Tile;
import com.umi.frame.World;

public class Stump extends NonLiving {

    public Stump(ItemImage itemImage, World world) {
        super(itemImage, world, Tile.MID);
    }

    public Stump(World world) {
        this(ItemImage.STUMP, world);
    }

}
