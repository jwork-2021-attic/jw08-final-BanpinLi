package imagePanel;

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

    public ImagePanel(int width, int height, int imageSize) {
        super();

        bufferedImages = new BufferedImageArrayList[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
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
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (BufferedImage bufferedImage : bufferedImages[x][y]) {
                    bufferedCanvas.getGraphics().drawImage(bufferedImage, x * imageSize, y * imageSize, null);
                }
            }
        }
        g.drawImage(bufferedCanvas, 0, 0, this);
    }

    public void addBufferedImage(BufferedImage image, int x, int y) {
        bufferedImages[x][y].add(image);
    }

    /**
     * 将屏幕上的东西全部清除
     */
    public synchronized void clear() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                bufferedImages[i][j].clear();
            }
        }
    }

    private class BufferedImageArrayList extends ArrayList<BufferedImage> {
    }

}
