package com.umi.entity;

import com.umi.entity.bean.Air;
import com.umi.entity.bean.Gourd;
import com.umi.entity.bean.Turf;
import com.umi.frame.World;
import org.junit.Test;

public class CreatureTest {
    @Test
    public void getDirectionTest() {
        Creature creature = new Gourd(new World());
        assert creature.getDirection() == 3;
    }

    @Test
    public void getSetThreadStateTest() {
        Creature creature = new Gourd(new World());
        creature.setThreadState(false);
        assert !creature.getThreadState();
        creature.setThreadState(true);
        assert creature.getThreadState();
    }

    @Test
    public void moveToTest() throws Exception {
        World world = new World();
        Creature creature = new Gourd(world);
        world.putInMid(creature, 0, 0);
        world.putInMid(new Air(world), 1, 1);
        world.putInGround(new Turf(world), 1, 1);
        creature.moveTo(1, 1);
        assert world.getMidThing(1, 1) == creature;
    }

    @Test
    public void getAddRemoveBulletsTest() {
        World world = new World();
        Creature creature = new Gourd(world);
        Bullet bullet = new Bullet(world, creature, 1, 1, 1);
        creature.addBullets(bullet);
        for(Bullet b : creature.getBullets()) {
            assert b == bullet;
        }
        creature.removeBullet(bullet);
        assert creature.getBullets().size() == 0;
    }
}
