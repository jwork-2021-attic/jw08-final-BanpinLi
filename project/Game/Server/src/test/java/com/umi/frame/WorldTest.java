package com.umi.frame;

import com.umi.entity.Creature;
import com.umi.entity.bean.Gourd;
import com.umi.entity.bean.Stone;
import org.junit.Test;

public class WorldTest {
    World world;

    @Test
    public void loadNewMapTest() {
        world = new World();
        // 测试一下loadNewMap方法，是否将生物都进行了加载，判断数量即可
        world.loadNewMap();
        assert world.getCreatures().size() == 8;
    }

    @Test
    public void createCharacterTest() {
        // 判断加载生物的数量
        world = new World();
        world.createCharacter();
        assert world.getCreatures().size() == 2;
        assert world.getThirdGourd() != null;
        assert world.getFourthGourd() != null;
    }

    @Test
    public void getAddRemoveCreatureTest() {
        world = new World();
        Gourd gourd = new Gourd(world);
        world.addCreature(gourd);
        for(Creature c : world.getCreatures()) {
            assert c == gourd;
        }
        world.removeCreature(gourd);
        assert world.getCreatures().size() == 0;
    }

    @Test
    public void putGetThingTest() {
        world = new World();
        Stone stone = new Stone(world);
        try {
            world.putInGround(stone, 0, 0);
            world.putInMid(stone, 0, 0);
            world.putInSky(stone, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert stone == world.getGroundThing(0, 0);
        assert stone == world.getMidThing(0, 0);
        assert stone == world.getSkyThing(0, 0);
    }

    @Test
    public void getSetGourdTest() {
        world = new World();
        Gourd gourd = new Gourd(world);
        world.setFirstGourd(gourd);
        world.setSecondGourd(gourd);
        world.setThirdGourd(gourd);
        world.setFourthGourd(gourd);
        assert world.getTheFirstGourd() == gourd;
        assert world.getTheSecondGourd() == gourd;
        assert world.getThirdGourd() == gourd;
        assert world.getFourthGourd() == gourd;
    }
}
