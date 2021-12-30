package com.umi.entity;

import com.umi.entity.bean.Gourd;
import com.umi.entity.bean.Stone;
import com.umi.frame.World;
import org.junit.Test;

public class BulletTest {
    @Test
    public void getOwnerTest() {
        World world = new World();
        Gourd gourd = new Gourd(world);
        Bullet bullet = new Bullet(world, gourd, 1, 1, 1);
        assert bullet.getOwner() == gourd;
    }

    @Test
    public void getDirectionTest() {
        World world = new World();
        Gourd gourd = new Gourd(world);
        Bullet bullet = new Bullet(world, gourd, 1, 1, 1);
        assert bullet.getDirection() == 1;
    }

    @Test
    public void destroyTest() throws Exception {
        World world = new World();
        Gourd gourd = new Gourd(world);
        Bullet bullet = new Bullet(world, gourd, 1, 1, 1);
        world.putInMid(bullet, 0, 0);
        bullet.destroy(gourd);
        assert world.getMidThing(0, 0) != bullet;
    }

    @Test
    public void beCollidedTest() throws Exception {
        World world = new World();
        Gourd gourd = new Gourd(world);
        Bullet bullet = new Bullet(world, gourd, 1, 1, 1);
        world.putInMid(bullet, 0, 0);
        bullet.beCollided(new Stone(world));
        assert world.getMidThing(0, 0) != bullet;
    }
}
