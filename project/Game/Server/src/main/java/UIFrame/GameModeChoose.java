package UIFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameModeChoose {
    private int choice;

    private JFrame app;

    public GameModeChoose() {
        app = new JFrame();
        choice = -1;
        JButton newGame = new JButton("开始游戏");
        JButton loadGame = new JButton("继续游戏");
        JButton onlineGame = new JButton("多人联机");
        JButton exitGame = new JButton("退出游戏");

        newGame.addActionListener(e -> choice = 1);
        loadGame.addActionListener(e -> choice = 2);
        onlineGame.addActionListener(e -> choice = 3);
        exitGame.addActionListener(e -> System.exit(0));

        JPanel panel2 = new JPanel();
        panel2.add(newGame);
        JPanel panel3 = new JPanel();
        panel3.add(loadGame);
        JPanel panel4 = new JPanel();
        panel4.add(onlineGame);
        JPanel panel5 = new JPanel();
        panel5.add(exitGame);

        app.setLayout(new GridLayout(6, 1));
        app.add(new JPanel());
        app.add(panel2);
        app.add(panel3);
        app.add(panel4);
        app.add(panel5);

        app.setSize(350, 300);
        app.setVisible(true);
        app.setLocation(200, 300);
        app.setAlwaysOnTop(true);
        app.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        app.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public int getChoice() {
        return this.choice;
    }

    public void hidden() {
        app.setVisible(false);
    }

}
