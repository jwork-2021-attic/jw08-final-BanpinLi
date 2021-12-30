package UIFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImagePanel extends JPanel {
    private BufferedImageArrayList[][] bufferedImages;
    private int width;
    private int height;
    // 规定每一张图的宽度
    private int imageSize;

    public ImagePanel() {
        this(50, 50, 15);
    }

    public ImagePanel(int width, int height, int imageSize) {
        super();

        bufferedImages = new BufferedImageArrayList[width][height];
        for(int i = 0;i < width;i++) {
            for(int j = 0;j < height;j++) {
                bufferedImages[i][j] = new BufferedImageArrayList();
            }
        }
        this.width = width;
        this.height = height;
        this.imageSize = imageSize;
    }

    @Override
    public synchronized void paint(Graphics g) {
        BufferedImage bufferedCanvas = new BufferedImage(imageSize * width, imageSize * height, BufferedImage.TYPE_4BYTE_ABGR);
        for(int x = 0;x < width;x++) {
            for(int y = 0;y < height;y++) {
                for(BufferedImage bufferedImage : bufferedImages[x][y]) {
                    bufferedCanvas.getGraphics().drawImage(bufferedImage, x * imageSize, y * imageSize, null);
                }
            }
        }
        g.drawImage(bufferedCanvas, 0, 0, this);
    }

    /**
     * 将Displayable对象展示在屏幕的x y坐标点上，规定横向为x，纵向为y
     * @param displayable 需要进行展示的对象
     * @param x x坐标
     * @param y y坐标
     */
    public synchronized void display(Displayable displayable, int x, int y) {
        // 将对象对应的图片放在缓冲区，最后统一使用paint画出来
        if(displayable == null) {
            return;
        }

        BufferedImage inputImage = displayable.getBufferedImage();
        if(inputImage == null) {
            return;
        }

        if(inputImage.getWidth() != imageSize || inputImage.getHeight() != imageSize) {
            // 如果输入进来的图片和需要的尺寸不匹配，需要进行转换
            Image scaledImage = inputImage.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
            BufferedImage scaledBuffedImage = new BufferedImage(imageSize, imageSize, inputImage.getType());
            scaledBuffedImage.getGraphics().drawImage(scaledImage, 0, 0, null);
            inputImage = scaledBuffedImage;
        }
        bufferedImages[x][y].add(inputImage);
    }

    /**
     * 将屏幕上的东西全部清除
     */
    public synchronized void clear() {
        for(int i = 0;i < width;i++) {
            for(int j = 0;j < height;j++) {
                bufferedImages[i][j].clear();
            }
        }
    }

    @Override
    public int getWidth() {
        return this.width * this.imageSize;
    }

    @Override
    public int getHeight() {
        return this.height * this.imageSize;
    }

    /**
     * 为了方便操作定义一个内部类
     */
    private class BufferedImageArrayList extends ArrayList<BufferedImage> {}
}
