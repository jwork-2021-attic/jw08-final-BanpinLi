package com.umi.saveLoad;

import UIFrame.ItemImage;
import com.umi.entity.Bullet;
import com.umi.entity.Creature;
import com.umi.frame.Thing;
import com.umi.frame.World;

import java.io.*;

public class SaveAndLoad {
    private World world;
    private String savePath;
    private String projectName = "Server/";
    private String dir = "src/main/resources/";

    public SaveAndLoad(World world) {
        this(world, "save/game.save");
    }

    public SaveAndLoad(World world, String savePath) {
        this.world = world;
        this.savePath = savePath;
    }

    public void save() {
        try (FileOutputStream fos = new FileOutputStream(savePath)) {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            Data data = new Data(world);
            oos.writeObject(data);
        } catch (Exception e) {
            try (FileOutputStream fos = new FileOutputStream(dir + savePath)) {
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                Data data = new Data(world);
                oos.writeObject(data);
            } catch (Exception ee) {
                try (FileOutputStream fos = new FileOutputStream(projectName + dir + savePath)) {
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    Data data = new Data(world);
                    oos.writeObject(data);
                } catch (Exception eee) {
                    eee.printStackTrace();
                    ee.printStackTrace();
                    e.printStackTrace();
                }
            }
        }
    }

    public void load() throws Exception {
        Data data;
        try (FileInputStream fis = new FileInputStream(savePath)) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            data = (Data) ois.readObject();
        } catch (Exception e) {
            try (FileInputStream fis = new FileInputStream((dir + savePath))) {
                ObjectInputStream ois = new ObjectInputStream(fis);
                data = (Data) ois.readObject();
            } catch (Exception ee) {
                try (FileInputStream fis = new FileInputStream((projectName + dir + savePath))) {
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    data = (Data) ois.readObject();
                } catch (Exception eee) {
                    e.printStackTrace();
                    ee.printStackTrace();
                    throw eee;
                }

            }
        }

        for(int i = 0;i < World.WIDTH;i++) {
            for(int j = 0;j < World.HEIGHT;j++) {
                Thing groundThing = data.groundThings[i][j];
                Thing midThing = data.midThings[i][j];
                Thing skyThing = data.skyThings[i][j];
                if(groundThing != null) {
                    groundThing.setItemImage(new ItemImage(groundThing.getImageFilename()));
                    groundThing.setWorld(world);
                    world.putInGround(groundThing, i, j);
                }
                if(midThing != null) {
                    midThing.setItemImage(new ItemImage(midThing.getImageFilename()));
                    midThing.setWorld(world);
                    world.putInMid(midThing, i, j);
                }
                if(skyThing != null) {
                    skyThing.setItemImage(new ItemImage(skyThing.getImageFilename()));
                    skyThing.setWorld(world);
                    world.putInSky(skyThing, i, j);
                }
            }
        }
        world.setSecondGourd(data.theSecondGourd);
        world.setFirstGourd(data.theFirstGourd);

        for(Creature creature : data.creatures) {
            new Thread(creature).start();
            world.addCreature(creature);
            if(creature.getBullets().size() != 0) {
                for(Bullet bullet : creature.getBullets()) {
                    new Thread(bullet).start();
                }
            }
        }
    }
}
