package com.umi.entity.bean;

import UIFrame.ItemImage;
import actionEvent.KeyAction;
import com.umi.entity.Bullet;
import com.umi.entity.Creature;
import com.umi.frame.Thing;
import com.umi.frame.Tile;
import com.umi.frame.World;

import java.util.Random;

public class Monster extends Creature {
    public Monster(World world) {
        this(ItemImage.MONSTER_BROWN, world);
    }

    public Monster(ItemImage itemImage, World world) {
        super(itemImage, world);
    }

    @Override
    protected synchronized void collideAnotherThing(Thing ant) {
        // 在Monster中重写这个方法，处理当前碰撞到的ant
        if(ant.getClass() == Bullet.class) {
            ant.beCollided(this);
            die();
        } else if(ant.getClass() == Gourd.class) {
            Tile<? extends Thing> antTile = ant.getTile();
            ant.beCollided(this);
            int x = antTile.getxPos();
            int y = antTile.getyPos();
            moveTo(x, y);
        } else if(ant.getClass() == Stump.class) {
            System.out.println("撞墙");
        } else if(ant.getClass() == Tree.class) {
            System.out.println("撞树");
        }
    }

    @Override
    public synchronized void beCollided(Thing ant) {
        // 判断ant是什么，如果是bullet，直接死
        if(ant.getClass() == Bullet.class) {
            die();
        }
    }

    @Override
    public void run() {
        int waitTime = 300;
        long startTime = System.currentTimeMillis();
        while(threadState) {
            long curTime = System.currentTimeMillis();
            if(curTime - startTime < waitTime) {
                Thread.yield();
            } else {
                int dire = getTheShotDirection(7);
                if(dire != getDirection()) {
                    move(dire);
                } else {
                    attack();
                }
                startTime += waitTime;
            }
        }
    }

    private void move(int dire) {
        direction = dire;
        if(!threadState) {
            return;
        }

        Tile<? extends Thing> tile = getTile();
        int x = tile.getxPos();
        int y = tile.getyPos();
        if(dire == KeyAction.UP) {
            y -= 1;
            if(world.getGroundThing(x, y).getClass() == Water.class) {
                y += 2;
                direction = KeyAction.DOWN;
            }
        } else if(dire == KeyAction.DOWN) {
            y += 1;
            if(world.getGroundThing(x, y).getClass() == Water.class) {
                y -= 2;
                direction = KeyAction.UP;
            }
        } else if(dire == KeyAction.LEFT) {
            x -= 1;
            if(world.getGroundThing(x, y).getClass() == Water.class) {
                x += 2;
                direction = KeyAction.RIGHT;
            }
        } else if(dire == KeyAction.RIGHT) {
            x += 1;
            if(world.getGroundThing(x, y).getClass() == Water.class) {
                x -= 2;
                direction = KeyAction.LEFT;
            }
        }


        moveTo(x, y);

    }

    private void attack() {
        if(!threadState) {
            return;
        }

        int x = getTile().getxPos();
        int y = getTile().getyPos();
        if(getBullets().size() == 0) {
            shotABullet(x, y, direction);
        }
    }

    // 获取射击方向
    private int getTheShotDirection(int distance) {
        int curX = getTile().getxPos();
        int curY = getTile().getyPos();
        for(int i = curX - distance;i < curX + distance;i++) {
            try {
                if(world.getMidThing(i, curY).getClass() == Gourd.class) {
                    return i < curX ? KeyAction.LEFT : KeyAction.RIGHT;
                }
            } catch (Exception e) {}
        }
        for(int i = curY - distance;i < curY + distance;i++) {
            try {
                if(world.getMidThing(curX, i).getClass() == Gourd.class) {
                    return i < curY ? KeyAction.UP : KeyAction.DOWN;
                }
            } catch (Exception e) {}
        }
        return new Random().nextInt(4);
    }
}
