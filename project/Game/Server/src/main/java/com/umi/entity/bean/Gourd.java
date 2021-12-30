package com.umi.entity.bean;

import UIFrame.ItemImage;
import actionEvent.KeyAction;
import com.umi.entity.*;
import com.umi.frame.Thing;
import com.umi.frame.World;

import java.util.Deque;
import java.util.LinkedList;

public class Gourd extends Creature {
    private transient Deque<KeyAction> controls;

    public Gourd(World world) {
        this(ItemImage.GOURD_1, world);
    }

    public Gourd(ItemImage itemImage, World world) {
        super(itemImage, world);
        controls = new LinkedList<>();
    }

    /**
     * 接受键盘事件，线程安全的
     * @param keyAction 接收到的键盘事件
     */
    public synchronized void receiveControl(KeyAction keyAction) {
        if(controls == null) {
            controls = new LinkedList<>();
        }
        controls.offer(keyAction);
    }

    /**
     * 对键盘交互进行响应，同时加锁保证同一时间内只能够有一个线程操作controls
     */
    private long shotStartTime = 0;
    private long intervalTime = 500;
    private synchronized void responseControls() {
        if(controls == null) {
            controls = new LinkedList<>();
        }
        while(!controls.isEmpty()) {
            int x = getTile().getxPos();
            int y = getTile().getyPos();
            switch (controls.poll().getKey()) {
                case KeyAction.LEFT:
                    direction = KeyAction.LEFT;
                    moveTo(x - 1, y);
                    break;
                case KeyAction.UP:
                    direction = KeyAction.UP;
                    moveTo(x, y - 1);
                    break;
                case KeyAction.RIGHT:
                    direction = KeyAction.RIGHT;
                    moveTo(x + 1, y);
                    break;
                case KeyAction.DOWN:
                    direction = KeyAction.DOWN;
                    moveTo(x, y + 1);
                    break;
                case KeyAction.SHOT:
                    if(getBullets().size() == 0) {
                        shotABullet(x, y, direction);
                    }
                    break;
                case KeyAction.SHOTS:
                    long curTime = System.currentTimeMillis();
                    if(getBullets().size() == 0 && curTime - shotStartTime > intervalTime) {
                        shotStartTime = curTime;
                        shotABullet(x, y, KeyAction.UP, 30, 1);
                        shotABullet(x, y, KeyAction.DOWN, 30, 1);
                        shotABullet(x, y, KeyAction.LEFT, 30, 1);
                        shotABullet(x, y, KeyAction.RIGHT, 30, 1);
                    }
                    break;
                case KeyAction.SWAP:
                    swapWithBullet();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected synchronized void collideAnotherThing(Thing ant) {
        // 在葫芦中重写这个方法，判断对于当前碰撞到的ant应该作何反应
        if(ant.getClass() == Gourd.class) {
            // TODO: 应该处理一下两个葫芦碰到的情况
            System.out.println("两个葫芦碰到一起了");
        } else if(ant.getClass() == Monster.class) {
            ant.beCollided(this);
            die();
        } else if(ant.getClass() == Bullet.class) {
            if(getBullets() != null && getBullets().contains(ant)) {
                return;
            }
            ant.beCollided(this);
            die();
        } else if(ant.getClass() == Stump.class) {
            System.out.println("撞墙");
        } else if(ant.getClass() == Tree.class) {
            System.out.println("撞树");
        }
    }

    @Override
    public synchronized void beCollided(Thing ant) {
        if(ant.getClass() == Bullet.class || ant.getClass() == Monster.class) {
            die();
        }
    }

    /**
     * 调用此方法，让当前的葫芦娃和子弹交换位置，交换过程应该是线程安全的，使用moveTo方法
     */
    private void swapWithBullet() {
        if(bullets.size() == 1) {
            Bullet bullet = (Bullet) bullets.toArray()[0];
            int bltX = bullet.getTile().getxPos();
            int bltY = bullet.getTile().getyPos();

            bullet.destroy(this);
            moveTo(bltX, bltY);
        }
    }

    @Override
    public void run() {
        while(threadState) {
            responseControls();
        }
        removeSelfFromWorld();
        System.out.println(Thread.currentThread().getName() + " die.");
    }

    private void removeSelfFromWorld() {
        if(this == world.getTheFirstGourd()) {
            world.setFirstGourd(null);
        } else if(this == world.getTheSecondGourd()) {
            world.setSecondGourd(null);
        } else if(this == world.getThirdGourd()) {
            world.setThirdGourd(null);
        } else if(this == world.getFourthGourd()) {
            world.setFourthGourd(null);
        }
    }
}
