package UIFrame;

import org.junit.Test;

import javax.swing.*;

public class ImagePanelTest {
    // 测试display方法和paint方法，看是否可以画出图像来
    @Test
    public void displayTest() {
        DisplayableImpl dt = new DisplayableImpl("src/test/resources/test.png");
        ImagePanel panel = new ImagePanel(3, 1, 30);

        panel.display(dt, 0, 0);
        panel.display(null, 0, 0);
        dt = new DisplayableImpl("src/test/resources/test1.png");
        panel.display(dt, 1, 0);

        JFrame jFrame = new JFrame();
        jFrame.add(panel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setSize(200, 200);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
