package com.umi.frame;

import UIFrame.ItemImage;
import com.umi.entity.Creature;
import com.umi.entity.bean.*;
import com.umi.saveLoad.SaveAndLoad;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class World {
    public static final int WIDTH = 25;
    public static final int HEIGHT = 25;
    public static final int FIRST_GOURD = 1;
    public static final int SECOND_GOURD = 2;
    // 新增的为多人联机对战的角色
    public static final int THIRD_GOURD = 3;
    public static final int FOURTH_GOURD = 4;

    private Tile<Thing>[][] tiles;
    private Gourd firstGourd;
    private Gourd secondGourd;
    private Gourd thirdGourd;
    private Gourd fourthGourd;

    private Set<Creature> creatures;

    public World() {
        this.tiles = new Tile[WIDTH][HEIGHT];
        creatures = new HashSet<>();

        // 最好是通过加载一张地图的形式来加载出来地形 loadTheMap();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tiles[i][j] = new Tile<>(i, j);
            }
        }
    }

    // 加载新游戏的地图
    public void loadNewMap() {
        LoadMap loadMap = new LoadMap();
        try {
            loadMap.load();
            loadMap.putInWorld(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gourd gourd1 = new Gourd(ItemImage.GOURD_1,this);
        this.firstGourd = gourd1;
        Gourd gourd2 = new Gourd(ItemImage.GOURD_2, this);
        this.secondGourd = gourd2;
        Monster monster1 = new Monster(this);
        Monster monster2 = new Monster(this);
        Monster monster3 = new Monster(this);
        Monster monster4 = new Monster(this);
        Monster monster5 = new Monster(this);
        Monster monster6 = new Monster(this);
        tiles[1][23].setMidThing(gourd1);
        tiles[23][1].setMidThing(gourd2);
        tiles[4][10].setMidThing(monster1);
        tiles[20][14].setMidThing(monster2);
        tiles[9][16].setMidThing(monster3);
        tiles[15][8].setMidThing(monster4);
        tiles[10][5].setMidThing(monster5);
        tiles[14][17].setMidThing(monster6);
        new Thread(gourd1, "gourd1").start();
        new Thread(gourd2, "gourd2").start();
        new Thread(monster1, "monster1").start();
        new Thread(monster2, "monster2").start();
        new Thread(monster3, "monster3").start();
        new Thread(monster4, "monster4").start();
        new Thread(monster5, "monster5").start();
        new Thread(monster6, "monster6").start();
        creatures.add(gourd1);
        creatures.add(gourd2);
        creatures.add(monster1);
        creatures.add(monster2);
        creatures.add(monster3);
        creatures.add(monster4);
        creatures.add(monster5);
        creatures.add(monster6);
    }

    // 在创建两个角色，用于联机模式使用
    public void createCharacter() {
        Gourd gourd3 = new Gourd(ItemImage.GOURD_1, this);
        this.thirdGourd = gourd3;
        Gourd gourd4 = new Gourd(ItemImage.GOURD_2, this);
        this.fourthGourd = gourd4;
        tiles[1][1].setMidThing(gourd3);
        tiles[23][23].setMidThing(gourd4);
        new Thread(gourd3, "gourd3").start();
        new Thread(gourd4, "gourd4").start();
        creatures.add(gourd3);
        creatures.add(gourd4);
    }


    // 加载存档地图
    public void loadTheLoad() {
        JFrame jFrame = new JFrame();
        try {
            SaveAndLoad saveAndLoad = new SaveAndLoad(this);
            JLabel label = new JLabel(" ".repeat(14) + "正在加载上次的存档...");
            label.setFont(new Font("微软雅黑", Font.BOLD, 20));
            jFrame.add(label);
            jFrame.setVisible(true);
            jFrame.setLocation(250, 250);
            jFrame.setSize(350, 200);
            saveAndLoad.load();
        } catch (Exception e) {
            e.printStackTrace();
            loadNewMap();
        }
        jFrame.setVisible(false);
    }

    public Set<Creature> getCreatures() {
        return this.creatures;
    }

    public void addCreature(Creature creature) {
        creatures.add(creature);
    }

    public void removeCreature(Creature creature) {
        this.creatures.remove(creature);
    }

    public Thing getMidThing(int x, int y) {
        return this.tiles[x][y].getMidThing();
    }

    public Thing getGroundThing(int x, int y) {
        return this.tiles[x][y].getGroundThing();
    }

    public Thing getSkyThing(int x, int y) {
        return this.tiles[x][y].getSkyThing();
    }

    public Tile<? extends Thing> getTile(int x, int y) {
        return tiles[x][y];
    }

    public Gourd getTheFirstGourd() {
        return this.firstGourd;
    }

    public void setFirstGourd(Gourd gourd) {
        this.firstGourd = gourd;
    }

    public Gourd getTheSecondGourd() {
        return this.secondGourd;
    }

    public void setSecondGourd(Gourd gourd) {
        this.secondGourd = gourd;
    }

    public Gourd getThirdGourd() {
        return this.thirdGourd;
    }

    public void setThirdGourd(Gourd gourd) {
        this.thirdGourd = gourd;
    }

    public Gourd getFourthGourd() {
        return this.fourthGourd;
    }

    public void setFourthGourd(Gourd gourd) {
        this.fourthGourd = gourd;
    }

    public void putInMid(Thing t, int x, int y) throws Exception {
        this.tiles[x][y].setMidThing(t);
    }

    public void putInGround(Thing t, int x, int y) throws Exception {
        this.tiles[x][y].setGroundThing(t);
    }

    public void putInSky(Thing t, int x, int y) throws Exception {
        this.tiles[x][y].setSkyThing(t);
    }

}
