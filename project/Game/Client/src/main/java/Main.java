import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import actionEvent.KeyAction;
import client.Client;
import imagePanel.ImagePanel;

public class Main extends JFrame implements KeyListener {
    List<Byte> imageData;
    ImagePanel terminal;
    Client client;

    // 初始化三个连接，分别用于不同的功能
    public Main() {
        int width = Client.WIDTH;
        int height = Client.HEIGHT;
        terminal = new ImagePanel(width, height, 30);
        imageData = new ArrayList<>();
        pack();
        add(terminal);
        addKeyListener(this);

        try {
            client = new Client(terminal, this);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int value= -1;

        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_W:
                value = KeyAction.UP;
                break;
            case KeyEvent.VK_A:
                value = KeyAction.LEFT;
                break;
            case KeyEvent.VK_S:
                value = KeyAction.DOWN;
                break;
            case KeyEvent.VK_D:
                value = KeyAction.RIGHT;
                break;
            case KeyEvent.VK_G:
                value = KeyAction.SHOT;
                break;
            case KeyEvent.VK_H:
                value = KeyAction.SHOTS;
                break;
            case KeyEvent.VK_J:
                value = KeyAction.SWAP;
                break;
        }
        if(value == -1) {
            return;
        }

        try {
            KeyAction keyAction = new KeyAction(value, client.getPlayerId());
            byte[] bytes = KeyAction.serialize(keyAction);
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.clear();
            buffer.put(bytes);
            buffer.flip();
            client.getControlSocketChannel().write(buffer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    public static void main(String[] args) {
        Main app = new Main();
        app.setVisible(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(Client.WIDTH * 30 + 20, Client.HEIGHT * 30 + 40);
        app.setLocation(Client.WIDTH * 30, 0);
    }
}
