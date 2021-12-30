package com.umi.entity.bean;

import com.umi.entity.Bullet;
import com.umi.frame.World;
import org.junit.Test;

public class MonsterTest {
    @Test
    public void beCollidedTest() throws Exception {
        World world = new World();
        Monster monster = new Monster(world);
        world.putInMid(monster, 0, 0);
        monster.beCollided(new Gourd(world));
        assert world.getMidThing(0, 0) == monster;

        Bullet bullet = new Bullet(world, new Gourd(world), 1, 1, 1);
        monster.beCollided(bullet);
        assert world.getMidThing(0, 0) != monster;
    }
}
