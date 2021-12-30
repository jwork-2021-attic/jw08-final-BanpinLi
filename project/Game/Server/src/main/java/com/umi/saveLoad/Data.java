package com.umi.saveLoad;

import com.umi.entity.Creature;
import com.umi.entity.bean.Gourd;
import com.umi.frame.Thing;
import com.umi.frame.World;

import java.io.Serializable;
import java.util.Set;

public class Data implements Serializable {
    private transient World world;
    public Thing[][] groundThings;
    public Thing[][] midThings;
    public Thing[][] skyThings;
    public Set<Creature> creatures;
    public Gourd theFirstGourd;
    public Gourd theSecondGourd;

    public Data(World world) {
        this.world = world;
        loadTheData();
    }

    private void loadTheData() {
        creatures = world.getCreatures();
        theFirstGourd = world.getTheFirstGourd();
        theSecondGourd = world.getTheSecondGourd();
        groundThings = new Thing[World.WIDTH][World.HEIGHT];
        midThings = new Thing[World.WIDTH][World.HEIGHT];
        skyThings = new Thing[World.WIDTH][World.HEIGHT];
        for(int i = 0;i < World.WIDTH;i++) {
            for(int j = 0;j < World.HEIGHT;j++) {
                groundThings[i][j] = world.getGroundThing(i, j);
                midThings[i][j] = world.getMidThing(i, j);
                skyThings[i][j] = world.getSkyThing(i, j);
            }
        }
    }
}
