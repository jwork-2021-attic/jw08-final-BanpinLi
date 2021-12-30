package actionEvent;

import UIFrame.ImagePanel;
import com.umi.frame.World;
import com.umi.screen.Screen;
import org.junit.Test;

import javax.swing.*;

public class KeyListenerImplTest {
    @Test
    public void keyPressedTest() {
        final boolean[] end = {false};
        Screen screen = new Screen() {
            @Override
            public void displayOutput(ImagePanel terminal) {

            }

            @Override
            public Screen respondToUserInput(KeyAction keyAction) {
                assert keyAction.getKey() == KeyAction.UP;
                assert keyAction.getCharacter() == World.FIRST_GOURD;
                end[0] = true;
                return null;
            }
        };

        KeyListenerImpl keyListener = new KeyListenerImpl(screen);
        JFrame jFrame = new JFrame();
        jFrame.addKeyListener(keyListener);
        jFrame.setVisible(true);
        jFrame.setSize(300, 300);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while(!end[0]) {
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
