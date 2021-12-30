package com.umi.entity.bean;

import com.umi.entity.Bullet;
import com.umi.frame.World;
import org.junit.Test;

public class GourdTest {
    @Test
    public void beCollidedTest() throws Exception {
        World world = new World();

        Gourd gourd = new Gourd(world);
        world.putInMid(gourd, 0, 0);
        gourd.beCollided(new Gourd(world));
        assert world.getMidThing(0, 0) == gourd;

        Bullet bullet = new Bullet(world, new Gourd(world), 1, 1, 1);
        gourd.beCollided(bullet);
        assert world.getMidThing(0, 0) != gourd;

        world.putInMid(gourd, 0, 0);
        gourd.beCollided(new Monster(world));
        assert world.getMidThing(0, 0) != gourd;
    }
}
