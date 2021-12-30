package UIFrame;

import com.umi.screen.MonitorGameCourse;
import server.Server;

import javax.swing.*;

public class UIFlash {
    MonitorGameCourse gameMonitor;
    JFrame app;

    public UIFlash(JFrame app, MonitorGameCourse gameMonitor) {
        this.gameMonitor = gameMonitor;
        this.app = app;
        createThread();
    }

    private void createThread() {
        // 创建一个用来刷新UI的线程，每隔固定时间就将UI刷新一遍
        new Thread(() -> {
            int flashTime = 60;
            long startTime = System.currentTimeMillis();
            while (true) {
                long curTime = System.currentTimeMillis();
                if(curTime - startTime < flashTime) {
                    Thread.yield();
                } else {
                    if(!gameMonitor.getTheGameState()) {
                        app.repaint();
                    }
                    startTime = curTime;
                }
            }
        }).start();
    }

}
