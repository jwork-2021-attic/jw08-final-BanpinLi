package com.umi.entity.bean;

import UIFrame.ItemImage;
import com.umi.entity.NonLiving;
import com.umi.frame.Tile;
import com.umi.frame.World;

public class Stone extends NonLiving {

    public Stone(ItemImage itemImage, World world) {
        super(itemImage, world, Tile.MID);
    }

    public Stone(World world) {
        this(ItemImage.STONE, world);
    }
}
