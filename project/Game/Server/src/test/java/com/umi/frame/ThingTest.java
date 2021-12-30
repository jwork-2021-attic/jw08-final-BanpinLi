package com.umi.frame;

import UIFrame.ItemImage;
import com.umi.entity.bean.Gourd;
import com.umi.entity.bean.Stone;
import org.junit.Test;

import java.util.Objects;

public class ThingTest {
    @Test
    public void getSetItemImageTest() {
        Thing thing = new Gourd(new World());
        thing.setItemImage(ItemImage.TREE);
        assert thing.getItemImage() == ItemImage.TREE;
    }

    @Test
    public void getItemImageTest() {
        Thing thing = new Gourd(new World());
        assert thing.getItemImage() == ItemImage.GOURD_1;
    }

    @Test
    public void getBufferedImage() {
        Thing thing = new Stone(new World());
        assert thing.getBufferedImage() == ItemImage.STONE.getImage();
    }

    @Test
    public void getSetTileTest() {
        Thing thing = new Stone(new World());
        Tile<Thing> tile = new Tile<>();
        thing.setTile(tile);
        assert tile == thing.getTile();
    }

    @Test
    public void getImageFilenameTest() {
        Thing thing = new Stone(new World());
        assert Objects.equals(thing.getImageFilename(), ItemImage.STONE.getImageFilename());
    }
}
