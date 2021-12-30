package com.umi.entity;

import UIFrame.ItemImage;
import actionEvent.KeyAction;
import com.umi.entity.bean.Air;
import com.umi.entity.bean.Gourd;
import com.umi.entity.bean.Monster;
import com.umi.entity.bean.Water;
import com.umi.frame.Thing;
import com.umi.frame.Tile;
import com.umi.frame.World;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

public class Creature extends Thing implements Runnable {
    // 生命值
    protected int hp;
    // 表示这个生物拥有的子弹
    protected Set<Bullet> bullets;
    // 一个生物当前朝向的是哪边
    protected int direction;
    protected boolean threadState;

    public Creature(ItemImage itemImage, World world) {
        this(itemImage, world, Tile.MID);
        init();
    }

    public Creature(ItemImage itemImage, World world, int position) {
        super(itemImage, world, position);
        init();
    }

    private void init() {
        hp = 1;
        threadState = true;
        direction = KeyAction.RIGHT;
        bullets = new HashSet<>();
    }

    public int getDirection() {
        return this.direction;
    }

    public void setThreadState(boolean state) {
        this.threadState = state;
    }

    public boolean getThreadState() {
        return this.threadState;
    }

    /**
     * 朝着固定xy坐标进行移动，是线程安全的
     * @param xPos x坐标
     * @param yPos y坐标
     */
    public void moveTo(int xPos, int yPos) {
        // 当一个生物体决定移动的时候，应该避免另一个生物也进行移动，使用Tile作为锁进行上锁

        // 如果需要移动的地方越界了，禁止移动对象
        if(xPos < 0 || xPos >= World.WIDTH || yPos < 0 || yPos >= World.HEIGHT) {
            return;
        }

        int x = getTile().getxPos();
        int y = getTile().getyPos();
        // 获取xy位置的tile
        Tile<? extends Thing> nextTile = world.getTile(xPos, yPos);
        synchronized (nextTile) {
            // 移动到下一个tile上面，判断陆地上是否有生物
            if (nextTile.getMidThing().getClass() == Air.class) {
                // 判断是否站在了某些不能站立的位置
                if(nextTile.getGroundThing().getClass() == Water.class) {
                    die();
                } else {
                    try {
                        world.putInMid(this, xPos, yPos);
                        world.putInMid(new Air(world), x, y);
//                        System.out.println(Thread.currentThread().getName() + " move.");
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("ArrayIndexOutOfBoundsException");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                collideAnotherThing(nextTile.getMidThing());
            }
        }
    }

    public Set<Bullet> getBullets() {
        return this.bullets;
    }

    public void addBullets(Bullet bullet) {
        this.bullets.add(bullet);
    }

    /**
     * 在某一个位置，向某一个方向，以默认速度发射可以走默认距离的子弹
     * @param x x坐标
     * @param y y坐标
     * @param direction 表示向什么方向射击
     */
    protected void shotABullet(int x, int y, int direction) {
        shotABullet(x, y, direction, 20, 8);
    }

    /**
     * 在某一个位置，向某一个方向，以speed速度发射可以走distance距离的子弹
     * @param x x坐标
     * @param y y坐标
     * @param direction 表示向什么方向射击
     */
    protected void shotABullet(int x, int y, int direction, int speed, int distance) {
        Bullet newBullet = null;
        if(this.getClass() == Gourd.class) {
            newBullet = new Bullet(ItemImage.BULLET_GOURD, world, this, direction, speed, distance);
        } else if(this.getClass() == Monster.class) {
            newBullet = new Bullet(ItemImage.BULLET_MONSTER, world, this, direction, speed, distance);
        }
        int[] pair = getNextPosition(x, y, direction);
        Tile<? extends Thing> nextTile = null;
        try {
            nextTile = world.getTile(pair[0], pair[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ArrayIndexOutOfBoundsException");
            removeBullet(newBullet);
            newBullet = null;
            return;
        }
        synchronized (nextTile) {
            if(nextTile.getMidThing().getClass() == Air.class) {
                try {
                    world.putInMid(newBullet, pair[0], pair[1]);
                    new Thread(newBullet).start();
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("ArrayIndexOutOfBoundsException");
                    removeBullet(newBullet);
                    newBullet = null;
                } catch (Exception e) {
                    removeBullet(newBullet);
                    newBullet = null;
                    e.printStackTrace();
                }
            } else {
                nextTile.getMidThing().beCollided(newBullet);
                removeBullet(newBullet);
            }
        }

    }

    private int[] getNextPosition(int x, int y, int direction) {
        int[] ret = new int[2];
        switch (direction) {
            case KeyAction.LEFT:
                ret[0] = x - 1;
                ret[1] = y;
                break;
            case KeyAction.RIGHT:
                ret[0] = x + 1;
                ret[1] = y;
                break;
            case KeyAction.UP:
                ret[0] = x;
                ret[1] = y - 1;
                break;
            case KeyAction.DOWN:
                ret[0] = x;
                ret[1] = y + 1;
                break;
        }
        return ret;
    }

    /**
     * 移出当前Creature所持有的bullet，该移出过程是线程安全的，不会受到其他移出过程影响
     * @param bullet 需要进行移出的Bullet
     * @return 返回是否是移出成功了
     */
    public synchronized boolean removeBullet(Bullet bullet) {
//        System.out.println(bullets.size());
        return this.bullets.remove(bullet);
    }

    /**
     * 处理当前Creature和其他Thing之间的碰撞
     * @param ant 撞到的Thing
     */
    protected void collideAnotherThing(Thing ant) {}

    /**
     * 处理当前Creature和其他的Thing之间的碰撞
     * @param ant 当前Creature被ant撞到了
     */
    @Override
    public void beCollided(Thing ant) {}

    /**
     * 释放该生物占用的Tile，让Air占用该Tile，并同时将生物所出线程的标志位标记为停止
     */
    protected void die() {
        Tile<? extends Thing> curTile = getTile();
        int x = curTile.getxPos();
        int y = curTile.getyPos();
        try {
            world.putInMid(new Air(world), x, y);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ArrayIndexOutOfBoundsException");
        } catch (Exception e) {
            e.printStackTrace();
        }

        world.removeCreature(this);
        setTile(null);
        setThreadState(false);
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

    @Override
    public void run() {}
}
