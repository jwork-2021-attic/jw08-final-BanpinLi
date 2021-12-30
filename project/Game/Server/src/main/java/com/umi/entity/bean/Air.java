package com.umi.entity.bean;

import UIFrame.ItemImage;
import com.umi.entity.NonLiving;
import com.umi.frame.Thing;
import com.umi.frame.Tile;
import com.umi.frame.World;

import java.awt.Color;

public class Air extends NonLiving {

    public Air(ItemImage itemImage, World world) {
        super(itemImage, world, Tile.MID);
    }

    public Air(World world) {
        this(ItemImage.AIR, world);
    }

}
