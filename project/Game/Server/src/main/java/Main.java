import UIFrame.GameModeChoose;
import UIFrame.ImagePanel;
import UIFrame.UIFlash;
import actionEvent.KeyListenerImpl;
import com.umi.frame.World;
import com.umi.saveLoad.SaveAndLoad;
import com.umi.screen.MonitorGameCourse;
import com.umi.screen.WorldScreen;
import server.Server;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main extends JFrame {
    private WorldScreen screen;
    private ImagePanel terminal;
    private int gameMode = 1;
    private Server server;

    public Main() {
        super();
        terminal = new ImagePanel(World.WIDTH, World.HEIGHT, 30);
        GameModeChoose gms = new GameModeChoose();
        while(true) {
            gameMode = gms.getChoice();
            // 这里要执行两条语句，否则可能会导致不能响应点击事件
            try {
                Thread.sleep(1);
            } catch (Exception e) {}
            if(gameMode != -1) {
                gms.hidden();
                break;
            }
        }
        screen = new WorldScreen();
        add(terminal);
        pack();
        startGame();
        addKeyListener(new KeyListenerImpl(screen));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("正在进行存档中...");
                World world = screen.getWorld();
                new SaveAndLoad(world).save();
                System.out.println("退出游戏");

                System.exit(0);
            }
        });
        MonitorGameCourse gameMonitor;
        if(gameMode == 3) {
            gameMonitor = new MonitorGameCourse(screen.getWorld(), this, server);
        } else {
            gameMonitor = new MonitorGameCourse(screen.getWorld(), this);
        }
        new UIFlash(this, gameMonitor);
    }

    private void startGame() {
        if(gameMode == 1) {
            screen.getWorld().loadNewMap();
        } else if(gameMode == 2) {
            screen.getWorld().loadTheLoad();
        } else if(gameMode == 3) {
            try {
                server = new Server(screen);
                server.createThreadToTransferData();
                server.createThreadToHandleClientControl();
            } catch (Exception e) {
                System.out.println("启动游戏失败.");
                System.exit(-1);
            }
            screen.getWorld().loadNewMap();
            screen.getWorld().createCharacter();
        }
    }

    @Override
    public void repaint() {
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
        if(gameMode == 3) {
            server.pushData(screen.getWorld());
        }
    }

    public static void main(String[] args) {
        // 关于地图坐标的一个说明
        // 从左向右增加是y坐标
        // 从上到下增加是x坐标
        // 新版框架，使用的是横向x，纵向y
        // 使用getResourceAsStream不能加载透明图片，需要使用FileInputStream，并且查找目录是工程根目录
        Main app = new Main();

        app.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        app.getGraphicsConfiguration().getDevice().setFullScreenWindow(app);
        app.setSize(World.WIDTH * 30 + 15, World.HEIGHT * 30 + 40);
        app.setVisible(true);
    }
}
