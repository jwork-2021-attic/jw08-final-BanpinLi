package com.umi.entity.bean;

import UIFrame.ItemImage;
import com.umi.entity.NonLiving;
import com.umi.frame.Tile;
import com.umi.frame.World;

public class Tree extends NonLiving {

    public Tree(ItemImage itemImage, World world) {
        super(itemImage, world, Tile.MID);
    }

    public Tree(World world) {
        this(ItemImage.TREE, world);
    }

}
