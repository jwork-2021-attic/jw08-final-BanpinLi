package client;

import client.imageProcess.ItemImageMapping;
import imagePanel.ImagePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.List;

public class Client {
    private String ip;
    private int port;
    private ImagePanel terminal;
    private JFrame app;

    private int playerId;

    List<Byte> imageData;
    Deque<byte[][][]> dataBuffer;
    public static int WIDTH = 25;
    public static int HEIGHT = 25;

    private SocketChannel dataSocketChannel;
    private SocketChannel controlSocketChannel;
    private SocketChannel gameMonSocketChannel;

    public Client(ImagePanel terminal, JFrame app) throws Exception {
        this.terminal = terminal;
        this.app = app;
        dataBuffer = new LinkedList<>();
        imageData = new ArrayList<>();

        // 从键盘输入信息连接服务器
        inputMessageFromKeyBoard();

//        ip = "127.0.0.1";
//        port = 11451;
        System.out.println("ip = " + ip + ", port = " + port);

        // 创建客户端连接
        createSocket();

        // 创建线程来刷新UI
        createThreadToFlashUI();
    }

    private void inputMessageFromKeyBoard() {
        JFrame jFrame = new JFrame();
        jFrame.setLayout(new GridLayout(6, 1));
        jFrame.add(new Panel());

        JTextField ipText = new JTextField(8);
        JTextField portText = new JTextField(8);
        JTextField idText = new JTextField(8);
        JButton submit = new JButton("确定");

        JPanel panel2 = new JPanel();
        panel2.add(new JLabel("输入ip地址："));
        panel2.add(ipText);
        jFrame.add(panel2);

        JPanel panel3= new JPanel();
        panel3.add(new JLabel("输入端口号："));
        panel3.add(portText);
        jFrame.add(panel3);

        JPanel panel4 = new JPanel();
        panel4.add(new JLabel("输入你的编号："));
        panel4.add(idText);
        jFrame.add(panel4);

        JPanel panel5 = new JPanel();
        panel5.add(submit);
        jFrame.add(panel5);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setSize(450, 300);
        jFrame.setLocation(600, 300);
        jFrame.setAlwaysOnTop(true);

        submit.addActionListener(e -> {
            try {
                String ipInput = ipText.getText();
                int portInput = Integer.parseInt(portText.getText());
                int idInput = Integer.parseInt(idText.getText());

                if(ipInput != null) {
                    this.ip = ipInput;
                    this.port = portInput;
                    this.playerId = idInput;

                    jFrame.setVisible(false);
                }
            } catch (Exception ex) {}

        });

        while (ip == null) {
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private final int flashTime = 60;
    private void createThreadToFlashUI() {

        new Thread(() -> {
            while(true) {
                byte[][][] data = getByteData();
                if(data == null) {
                    continue;
                }
                for(int i = 0;i < data.length;i++) {
                    for(int j = 0;j < data[0].length;j++) {
                        for(int k = 0;k < data[0][0].length;k++) {
                            try {
                                BufferedImage img = ItemImageMapping.getBufferedImageByByte(data[i][j][k]);
                                terminal.addBufferedImage(img, i, j);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                app.repaint();
                try {
                    Thread.sleep(flashTime);
                } catch (Exception e) {}
                terminal.clear();
            }
        }).start();
    }

    private void createSocket() throws Exception {
        // 建立连接用来传输数据
        dataSocketChannel = SocketChannel.open();
        dataSocketChannel.connect(new InetSocketAddress(ip, port));
        System.out.println("建立数据传输连接");
        creatThreadToReceiveData();

        // 建立响应键盘连接
//        controlSocket = new Socket(ip, port);
        controlSocketChannel = SocketChannel.open();
        controlSocketChannel.connect(new InetSocketAddress(ip, port));
        System.out.println("建立键盘事件传输连接");

        gameMonSocketChannel = SocketChannel.open();
        gameMonSocketChannel.connect(new InetSocketAddress(ip, port));
        System.out.println("建立传输游戏结束信号连接");
        createThreadToHandleGameOver();

    }

    private void createThreadToHandleGameOver() {
        new Thread(() -> {
            try {
                // 接受游戏结束信号可以使用nio来进行操作，一旦没有读取到数据，就进行调度，节省资源
                gameMonSocketChannel.configureBlocking(false);
                String msg;
                while(true) {
                    ByteBuffer buffer = ByteBuffer.allocate(128);
                    buffer.clear();
                    gameMonSocketChannel.read(buffer);
                    buffer.flip();
                    if(!buffer.hasRemaining()) {
                        Thread.yield();
                        continue;
                    }
                    byte[] buff = new byte[buffer.limit()];
                    buffer.get(buff);
                    msg = new String(buff);
                    break;
                }

                JFrame jFrame = new JFrame();
                jFrame.add(new JLabel(msg));
                jFrame.add(new JLabel("             关闭此窗口来结束游戏."));
                jFrame.setLayout(new GridLayout(2, 1));
                jFrame.setTitle("Tip!");
                jFrame.setVisible(true);
                jFrame.setSize(220, 200);
                jFrame.setLocation(1000, 200);
                jFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // 创建线程用来处理数据接收事件
    private void creatThreadToReceiveData() {
        new Thread(() -> {
            while(true) {
                try {
                    receiveData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 接受数据放在缓冲区
    private void receiveData() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(10240);
        buffer.clear();
        dataSocketChannel.read(buffer);
        buffer.flip();
        byte[] data = new byte[WIDTH * HEIGHT * 2];
        buffer.get(data);

        byte[][][] transData =  new byte[WIDTH][HEIGHT][2];
        int off = 0;
        for(int i = 0;i < transData.length;i++) {
            for(int j = 0;j < transData[0].length;j++) {
                for(int k = 0;k < transData[0][0].length;k++) {
                    transData[i][j][k] = data[off++];
                }
            }
        }

        synchronized (this) {
            while (dataBuffer.size() != 0) {
                dataBuffer.pop();
            }
            dataBuffer.push(transData);
        }
    }

    // 得到用来刷新的一帧数据的byte数组
    public byte[][][] getByteData() {
        byte[][][] ret = null;
        synchronized (this) {
            if(dataBuffer.size() != 0) {
                ret = dataBuffer.poll();
            }
            if(dataBuffer.size() != 0) {
                ret = dataBuffer.pop();
            }
        }
        return ret;
    }

    /**
     * 返回键盘控制连接
     * @return socket
     */
    public SocketChannel getControlSocketChannel() {
        return this.controlSocketChannel;
    }

    public int getPlayerId() {
        return this.playerId;
    }

//    // ------------- 下面方法全部废弃 ----------------- //
//    // 使用连接来传输数据，传输的数据是一个序列化的对象：list<list<list<byte[]>>>
//    private void createTransferSocketAction(Socket socket) {
//        new Thread(() -> {
//            try {
//                InputStream is = socket.getInputStream();
//                byte[] buff = new byte[WIDTH * HEIGHT * 2];
//                while (true) {
//                    // 读入数据并将数据写入到imageData数据区
//                    int len = is.read(buff);
//                    // 加锁是为了防止错误的长度被读取到了
//                    synchronized (this) {
//                        for(int i = 0;i < len;i++) {
//                            imageData.add(buff[i]);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
//
//    // 将imageData反序列化成img集合
//    private List<List<List<BufferedImage>>> deserialize() {
//        byte[] data = new byte[imageData.size()];
//        for(int i = 0;i < data.length;i++) {
//            data[i] = imageData.get(i);
//        }
//
//        List<List<List<byte[]>>> imageBytes = null;
//        try {
//            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
//            try {
//                imageBytes = (List<List<List<byte[]>>>)ois.readObject();
//            } catch (EOFException e) {
//                System.out.println("EOFException.");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        List<List<List<BufferedImage>>> ret = new ArrayList<>();
//        for(List<List<byte[]>> byteListList : imageBytes) {
//            List<List<BufferedImage>> imageListList = new ArrayList<>();
//            for(List<byte[]> byteList : byteListList) {
//                List<BufferedImage> imageList = new ArrayList<>();
//                for(byte[] bytes : byteList) {
//                    BufferedImage bufferedImage = bytesToBufferedImage(bytes);
//                    imageList.add(bufferedImage);
//                }
//                imageListList.add(imageList);
//            }
//            ret.add(imageListList);
//        }
//
//        return ret;
//    }
//
//    // 将一个byte数组转化成一个BufferedImage对象
//    private BufferedImage bytesToBufferedImage(byte[] bytes) {
//        try {
//            return ImageIO.read(new ByteArrayInputStream(bytes));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    // 使用连接来控制数据的传输
//    private void createTransferControlSocketAction(Socket socket) {
//        new Thread(() -> {
//            try {
//                InputStream is = socket.getInputStream();
//                OutputStream os = socket.getOutputStream();
//
//                while(true) {
//                    // 从服务器读入一个数据，表示的是传输过来的数据的长度应该为多少个byte
//                    byte[] expLenBytes = new byte[4];
//                    int len = is.read(expLenBytes);
//                    System.out.println(len);
//                    int expectedLength = bytesToInt(expLenBytes);
//                    System.out.println(expectedLength);
//                    // 如果此时读到的数据长度和预期长度不一致，就一直阻塞
////                    int realLength = -1;
////                    while(expectedLength != realLength) {
////                        synchronized (this) {
////                            realLength = imageData.size();
////                        }
////                    }
//
//                    // 得到反序列化后的img集合，并将其在屏幕上进行显示
//                    List<List<List<BufferedImage>>> buffImageList = deserialize();
//                    for(int i = 0;i < buffImageList.size();i++) {
//                        for(int j =0;j < buffImageList.get(0).size();i++) {
//                            for(BufferedImage img : buffImageList.get(i).get(j)) {
//                                terminal.addBufferedImage(img, i, j);
//                            }
//                        }
//                    }
//                    app.repaint();
//                    terminal.clear();
//
////                    // 将imageData锁起来，不要同时进行操作，防止数据错误
////                    synchronized (this) {
////                        if(signal == CtrlSignal.SEND_IMG) {
////                            // 将byteList转换成图像
////                            BufferedImage image = transformBytesToBufferedImage();
////                            imageData.clear();
////                            terminal.addBufferedImage(image, x, y);
////                        } else if (signal == CtrlSignal.SEND_GROUP) {
////                            // 换坐标
////                            y++;
////                            if(y == height) {
////                                y = 0;
////                                x++;
////                            }
////                        } else if (signal == CtrlSignal.SEND_OVER) {
////                            // 地图已经全部传输完毕了，可以开始刷新
////                            x = y = 0;
////                            // 不用控制刷新的时间间隔，因为在服务器端已经进行了控制
////                            app.repaint();
////                            terminal.clear();
////                        }
////                    }
//
//                    // 此时将完成信号'1'发给服务器，表示接收完数据
//                    os.write(1);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
//
//    private int bytesToInt(byte[] bytes) {
//        int n = 0;
//        for(int i = 0;i < 4;i++) {
//            n = n * 256 + bytes[i];
//        }
//        return n;
//    }
//
//    // 用于将byte数组转换成BufferedImage
//    private BufferedImage transformBytesToBufferedImage() {
//        byte[] data = new byte[imageData.size()];
//        for(int i = 0;i < data.length;i++) {
//            data[i] = imageData.get(i);
//        }
//        try {
//            return ImageIO.read(new ByteArrayInputStream(data));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
