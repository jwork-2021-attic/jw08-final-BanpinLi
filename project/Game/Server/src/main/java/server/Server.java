package server;

import UIFrame.ItemImageMapping;
import actionEvent.KeyAction;
import com.umi.entity.Bullet;
import com.umi.entity.Creature;
import com.umi.entity.bean.Gourd;
import com.umi.entity.bean.Monster;
import com.umi.frame.Thing;
import com.umi.frame.World;
import com.umi.screen.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

/**
 * 创建类的对象，用于完成客户端和服务器端的通信
 * 对传输数据方式进行改变，改为传输byte数组，每一个byte表示一张Image，然后在客户端读取到这些byte，
 * 翻译成Image，这里使用的是本地资源，所以速度应该会快很多，而且会少担心一点传输数据丢失的情况
 * 接受客户端发送过来的键盘请求，并进行反序列化得到KeyAction对象
 */
public class Server {
    private Screen screen;
    private Deque<byte[][][]> dataBuffer;

    private int playerNumber;
    private SocketChannel[] dataClientChannel;
    private SocketChannel[] gameMonClientChannel;
    private Selector controlSelector;

    public Server(Screen screen, int playerNumber) throws Exception {
        this.playerNumber = playerNumber;
        this.screen = screen;
        dataBuffer = new LinkedList<>();
        createServerAction();
    }

    public Server(Screen screen) throws Exception {
        this(screen, 4);
    }

    // 开两个线程，一个用于数据传输，一个用于键盘控制传输
    private void createServerAction() throws Exception {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(11451));

        JFrame jFrame = new JFrame();
        jFrame.setSize(500, 250);
        jFrame.setLocation(200, 300);
        jFrame.setVisible(true);
        jFrame.setAlwaysOnTop(true);

        JLabel tipLabel = new JLabel();
        tipLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        jFrame.add(tipLabel);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        dataClientChannel = new SocketChannel[playerNumber - 1];
        gameMonClientChannel = new SocketChannel[playerNumber - 1];
        controlSelector = Selector.open();
        for(int i = 0;i < playerNumber - 1;i++) {
            String tips = " ".repeat(25) + "正在等待玩家" + (i + 2) + "连接...";
            tipLabel.setText(tips);
            dataClientChannel[i] = ssc.accept();

            SocketChannel controlChannel = ssc.accept();
            controlChannel.configureBlocking(false);
            // 监控控制传输，并对读数据感兴趣
            controlChannel.register(controlSelector, SelectionKey.OP_READ);

            gameMonClientChannel[i] = ssc.accept();
        }

        jFrame.setVisible(false);

    }

    // 创建线程来处理数据传输
    public void createThreadToTransferData() {
        new Thread(() -> {
            while(true) {
                try {
                    transferData();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("退出游戏");
                    System.exit(-1);
                }
            }
        }).start();
    }

    // 将游戏结束的信号发送给各个客户端
    public void sendGameOverSignal(String msg) throws Exception {
        byte[] bytes = msg.getBytes();
        for(int i = 0;i < playerNumber - 1;i++) {
            ByteBuffer buffer = ByteBuffer.allocate(128);
            buffer.clear();
            buffer.put(bytes);
            buffer.flip();
            gameMonClientChannel[i].write(buffer);
        }
    }

    // 将缓冲区里面的数据发送给客户端
    private void transferData() throws IOException {
        byte[][][] data = null;
        synchronized (this) {
            if(dataBuffer.size() != 0) {
                data = dataBuffer.poll();
            }
        }
        if(data == null) {
            return;
        }

        byte[] transData = new byte[data.length * data[0].length * data[0][0].length];
        int off = 0;
        for(byte[][] bytes : data) {
            for(byte[] bytes1 : bytes) {
                for(byte b : bytes1) {
                    transData[off++] = b;
                }
            }
        }

        for(int i = 0;i < playerNumber - 1;i++) {
            ByteBuffer buffer = ByteBuffer.allocate(10240);
            buffer.clear();
            buffer.put(transData);
            buffer.flip();
            dataClientChannel[i].write(buffer);
        }
    }

    // 将数据放到临界区里面
    public void pushData(World world) {
        byte[][][] bytes = serializeWorld(world);
        synchronized (this) {
            dataBuffer.offer(bytes);
        }
    }

    // 将world里面所有东西的img对应的编号拿出来形成byte数组
    private byte[][][] serializeWorld(World world) {
        int width = World.WIDTH;
        int height = World.HEIGHT;
        byte[][][] ret = new byte[width][height][2];
        for(int i = 0;i < width;i++) {
            for(int j = 0;j < height;j++) {
                Thing groundThing = world.getGroundThing(i, j);
                Thing midThing = world.getMidThing(i, j);
                if(groundThing != null) {
                    ret[i][j][0] = ItemImageMapping.getByteByItemImage(groundThing.getItemImage());
                }
                if(midThing != null) {
                    ret[i][j][1] = ItemImageMapping.getByteByItemImage(midThing.getItemImage());
                    if(midThing.getClass() == Bullet.class
                            || midThing.getClass() == Monster.class
                            || midThing.getClass() == Gourd.class) {
                        int direction = 0;
                        try {
                            direction = ((Bullet) midThing).getDirection();
                        } catch (Exception e) {
                            direction = ((Creature)midThing).getDirection();
                        }
                        if(direction == KeyAction.UP) {
                            ret[i][j][1] += 192;
                        } else if(direction == KeyAction.LEFT) {
                            ret[i][j][1] += 64;
                        } else if(direction == KeyAction.RIGHT) {
                            ret[i][j][1] += 128;
                        }
                    }
                }
            }
        }
        return ret;
    }

    // 创建线程来响应客户端发送过来的键盘控制请求，每8个字节读一次
    public void createThreadToHandleClientControl() {
        new Thread(() -> {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(8);
                while(true) {
                    controlSelector.select();

                    Set<SelectionKey> selectionKeys = controlSelector.selectedKeys();
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    while(it.hasNext()) {
                        System.out.println("it");
                        SelectionKey key = it.next();
                        if(key.isReadable()) {
                            SocketChannel channel = (SocketChannel)key.channel();
                            buffer.clear();
                            channel.read(buffer);
                            buffer.flip();
                            byte[] bs = new byte[8];
                            if(buffer.limit() == 8) {
                                buffer.get(bs);
                                KeyAction keyAction = KeyAction.deserialize(bs);
                                screen.respondToUserInput(keyAction);
                                System.out.println("read.");
                            }
                        }
                        it.remove();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
