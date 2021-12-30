package UIFrame;

import org.junit.Test;

public class GameModeChooseTest {
    // 测试getChoice方法，测试是否能够得到想要的choice
    @Test
    public void getChoiceTest() {
        GameModeChoose gameModeChoose1 = new GameModeChoose();
        try {
            while(true) {
                Thread.sleep(1);
                if(gameModeChoose1.getChoice() != -1) {
                    assert gameModeChoose1.getChoice() == 1;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        GameModeChoose gameModeChoose2 = new GameModeChoose();
        try {
            while(true) {
                Thread.sleep(1);
                if(gameModeChoose2.getChoice() != -1) {
                    assert gameModeChoose2.getChoice() == 2;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        GameModeChoose gameModeChoose3 = new GameModeChoose();
        try {
            while(true) {
                Thread.sleep(1);
                if(gameModeChoose3.getChoice() != -1) {
                    assert gameModeChoose3.getChoice() == 3;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
