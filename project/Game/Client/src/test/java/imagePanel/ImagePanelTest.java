package imagePanel;

import client.imageProcess.ItemImage;
import org.junit.Test;

import javax.swing.*;

public class ImagePanelTest {
    @Test
    public void addBufferedImageTest() {
        ImagePanel panel = new ImagePanel(1, 1, 30);
        panel.addBufferedImage(ItemImage.STONE.getImage(), 0, 0);

        JFrame jFrame = new JFrame();
        jFrame.add(panel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setSize(200, 200);
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
