package com.umi.frame;

import com.umi.entity.bean.Gourd;
import org.junit.Test;

public class TileTest {
    @Test
    public void getAndSetThingTest() {
        Tile<Thing> tile = new Tile<>();
        Thing thing = new Gourd(new World());

        // ----------getThing方法-----------
        tile.setGroundThing(thing);
        tile.setMidThing(thing);
        tile.setSkyThing(thing);
        assert thing == tile.getGroundThing();
        assert thing == tile.getMidThing();
        assert thing == tile.getSkyThing();
    }

    @Test
    public void getAndSetPosTest() {
        Tile<Thing> tile = new Tile<>();
        Thing thing = new Gourd(new World());

        // ----------getPos和setPos方法----------
        tile.setxPos(0);
        tile.setyPos(0);
        assert tile.getxPos() == 0;
        assert tile.getyPos() == 0;
    }
}
