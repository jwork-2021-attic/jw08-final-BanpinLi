package com.umi.screen;

import com.umi.frame.World;
import server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

// 监控游戏进程，也就是如果有玩家死亡的话，那么游戏就结束判断胜利者
public class MonitorGameCourse {
    private boolean gameOver;
    private World world;
    private JFrame app;
    private Server server;
    private int[] jdg;

    public MonitorGameCourse(World world, JFrame app, Server server) {
        this.world = world;
        this.app = app;
        this.server = server;
        this.jdg = new int[4];
        Arrays.fill(jdg, 1);
        createThread();
    }

    public MonitorGameCourse(World world, JFrame app) {
        this(world, app, null);
    }

    private void createThread() {
        new Thread(() -> {
            gameOver = false;
            while(true) {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {}
                if(isGameOver() && (!gameOver)) {
                    gameOver = true;
                    JFrame jFrame = new JFrame();
                    String winner = getTheWinner();
                    String msg = "               " + winner + "获得了胜利！";
                    if(server != null) {
                        try {
                            server.sendGameOverSignal(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    jFrame.add(new JLabel(msg));
                    jFrame.add(new JLabel("             关闭此窗口来结束游戏."));
                    jFrame.setLayout(new GridLayout(2, 1));
                    jFrame.setTitle("Tip!");
                    jFrame.setVisible(true);
                    jFrame.setSize(220, 200);
                    jFrame.setLocation(200, 200);
                    jFrame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            System.exit(0);
                        }
                    });
                }
            }
        }).start();
    }

    // 当world里面只剩一个gourd的时候，游戏结束
    private boolean isGameOver() {
        jdg[0] = world.getTheFirstGourd() == null ? 0 : 1;
        jdg[1] = world.getTheSecondGourd() == null ? 0 : 1;
        jdg[2] = world.getThirdGourd() == null ? 0 : 1;
        jdg[3] = world.getFourthGourd() == null ? 0 : 1;
        return jdg[0] + jdg[1] + jdg[2] + jdg[3] == 1;
    }

    private String getTheWinner() {
        for(int i = 0;i < jdg.length;i++) {
            if(jdg[i] == 1) {
                return "玩家" + (i + 1);
            }
        }
        return null;
    }

    public boolean getTheGameState() {
        return gameOver;
    }

}
