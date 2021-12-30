package com.umi.entity;

import UIFrame.ItemImage;
import actionEvent.KeyAction;
import com.umi.entity.bean.Air;
import com.umi.frame.Thing;
import com.umi.frame.Tile;
import com.umi.frame.World;

import java.awt.image.BufferedImage;

public class Bullet extends Thing implements Runnable {
    // 表示方向
    private int direction;
    // 表示这颗子弹所属的生物
    private Creature owner;
    // 子弹的速度，表示1s可以移动的距离
    private int speed;
    // 可以在地图中移动的距离
    private int distance;
    // 用来控制线程状态
    private boolean threadState;
    // 表示这颗子弹是否已经湮灭了
    private boolean isDestroyed;

    private long startTime;
    private int waitTime;

    public Bullet(World world, Creature creature, int direction, int speed, int distance) {
//        super(AsciiPanel.brightRed, (char)42, world);
        this(ItemImage.BULLET_GOURD, world, creature, direction, speed, distance);
    }

    public Bullet(ItemImage itemImage, World world, Creature creature, int direction, int speed, int distance) {
        super(itemImage, world, Tile.MID);
        this.owner = creature;
        creature.addBullets(this);
        this.direction = direction;
        this.speed = speed;
        this.distance = distance;
        threadState = true;
        isDestroyed = false;
        startTime = System.currentTimeMillis();
        waitTime = 1000 / speed;
    }

    public Creature getOwner() {
        return this.owner;
    }

    public int getDirection() {
        return this.direction;
    }

    @Override
    public void run() {
        // 作为单独的线程，往某一个方向进行不断运动
        while(threadState) {
            long curTime = System.currentTimeMillis();
            if(curTime - startTime < waitTime) {
                Thread.yield();
            } else {
                try {
                    startTime += waitTime;
                    shot();
                } catch (Exception e) {
                    break;
                }
            }
        }
        destroy();
    }

    // 子弹朝着某个方向进行运动一个位置，同时考虑竞争问题
    private void shot() {
        if(!threadState) {
            return;
        }

        Tile<? extends Thing> curTile = getTile();
        int x = curTile.getxPos();
        int y = curTile.getyPos();

        switch (direction) {
            case KeyAction.LEFT:
                flyTo(x - 1, y);
                break;
            case KeyAction.UP:
                flyTo(x, y - 1);
                break;
            case KeyAction.RIGHT:
                flyTo(x + 1, y);
                break;
            case KeyAction.DOWN:
                flyTo(x, y + 1);
                break;
        }

    }

    // 子弹从一个地方飞行到另一个地方，和creature不同的是，子弹会消耗距离
    private void flyTo(int xPos, int yPos) {
        if(xPos < 0 || xPos >= World.WIDTH || yPos < 0 || yPos >= World.HEIGHT || distance == 0 || !threadState) {
            threadState = false;
            return;
        }

        int x = getTile().getxPos();
        int y = getTile().getyPos();
        // 获取xy位置的tile
        Tile<? extends Thing> nextTile = world.getTile(xPos, yPos);
        synchronized (nextTile) {
            // 当下一个tile上面没有东西就进行移动，否则就是发生碰撞了
            if (nextTile.getMidThing().getClass() == Air.class) {
                distance--;
                try {
                    world.putInMid(this, xPos, yPos);
                    world.putInMid(new Air(world), x, y);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("ArrayIndexOutOfBoundsException");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                collideAnotherThing(nextTile.getMidThing());
            }
        }
    }

    /**
     * 调用此方法来实现子弹的消失
     * @param ant 在什么类中进行的调用
     */
    public void destroy(Thing ant) {
        if(isDestroyed) {
            return;
        }
        if(ant == owner) {
            destroy();
        }
    }

    // 当子弹击中物体或者移动完指定距离之后，就会消亡
    private void destroy() {
        if(isDestroyed) {
            return;
        } else {
            isDestroyed = true;
        }

        owner.removeBullet(this);
        owner = null;
        try {
            world.putInMid(new Air(world), getTile().getxPos(), getTile().getyPos());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ArrayIndexOutOfBoundsException");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTile(null);
        threadState = false;
    }

    @Override
    public void beCollided(Thing ant) {
        destroy();
    }

    protected void collideAnotherThing(Thing ant) {
        ant.beCollided(this);
        destroy();
    }

    @Override
    public BufferedImage getBufferedImage() {
        // 应该根据当前目标朝向来返回一张图片的不同的部分
        BufferedImage totalImg = itemImage.getImage();
        int width = itemImage.getWidth();
        int number = 0;
        switch (direction) {
            case KeyAction.UP:
                number = 3;
                break;
            case KeyAction.DOWN:
                number = 0;
                break;
            case KeyAction.LEFT:
                number = 1;
                break;
            case KeyAction.RIGHT:
                number = 2;
                break;
        }
        return totalImg.getSubimage(0, number * width, width, width);
    }
}
