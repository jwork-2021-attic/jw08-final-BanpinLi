package actionEvent;

import com.umi.frame.World;
import com.umi.screen.Screen;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyListenerImpl implements KeyListener {
    private Screen screen;

    public KeyListenerImpl(Screen screen) {
       this.screen = screen;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        // 将keyEvent包装成keyAction对象然后交由screen进行进一步处理
        KeyAction keyAction = null;
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_W:
                keyAction = new KeyAction(KeyAction.UP, World.FIRST_GOURD);
                break;
            case KeyEvent.VK_A:
                keyAction = new KeyAction(KeyAction.LEFT, World.FIRST_GOURD);
                break;
            case KeyEvent.VK_S:
                keyAction = new KeyAction(KeyAction.DOWN, World.FIRST_GOURD);
                break;
            case KeyEvent.VK_D:
                keyAction = new KeyAction(KeyAction.RIGHT, World.FIRST_GOURD);
                break;
            case KeyEvent.VK_G:
                keyAction = new KeyAction(KeyAction.SHOT, World.FIRST_GOURD);
                break;
            case KeyEvent.VK_H:
                keyAction = new KeyAction(KeyAction.SHOTS, World.FIRST_GOURD);
                break;
            case KeyEvent.VK_J:
                keyAction = new KeyAction(KeyAction.SWAP, World.FIRST_GOURD);
                break;
            // 从下面开始，表示的是另一个实体被操控
            case KeyEvent.VK_UP:
                keyAction = new KeyAction(KeyAction.UP, World.SECOND_GOURD);
                break;
            case KeyEvent.VK_DOWN:
                keyAction = new KeyAction(KeyAction.DOWN, World.SECOND_GOURD);
                break;
            case KeyEvent.VK_LEFT:
                keyAction = new KeyAction(KeyAction.LEFT, World.SECOND_GOURD);
                break;
            case KeyEvent.VK_RIGHT:
                keyAction = new KeyAction(KeyAction.RIGHT, World.SECOND_GOURD);
                break;
            case KeyEvent.VK_NUMPAD1:
                keyAction = new KeyAction(KeyAction.SHOT, World.SECOND_GOURD);
                break;
            case KeyEvent.VK_NUMPAD2:
                keyAction = new KeyAction(KeyAction.SHOTS, World.SECOND_GOURD);
                break;
            case KeyEvent.VK_NUMPAD3:
                keyAction = new KeyAction(KeyAction.SWAP, World.SECOND_GOURD);
                break;
        }
        if(keyAction != null) {
            screen.respondToUserInput(keyAction);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
