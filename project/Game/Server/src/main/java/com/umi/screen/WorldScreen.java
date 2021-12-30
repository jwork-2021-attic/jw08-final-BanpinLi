package com.umi.screen;

import UIFrame.ImagePanel;
import actionEvent.KeyAction;
import com.umi.entity.bean.Gourd;
import com.umi.frame.World;

public class WorldScreen implements Screen {

    private World world;

    public WorldScreen() {
        world = new World();
    }

    public World getWorld() {
        return this.world;
    }

    @Override
    public void displayOutput(ImagePanel terminal) {
        for (int i = 0; i < World.WIDTH; i++) {
            for (int j = 0; j < World.HEIGHT; j++) {
                terminal.display(world.getGroundThing(i, j), i, j);
                terminal.display(world.getMidThing(i, j), i, j);
                terminal.display(world.getSkyThing(i, j), i, j);
            }
        }
    }

    @Override
    public Screen respondToUserInput(KeyAction keyAction) {
        // 响应键盘的控制信号，并将控制信号传递给world中能够接受相应信号的gourd
        switch(keyAction.getCharacter()) {
            case World.FIRST_GOURD:
                Gourd theFirstGourd = world.getTheFirstGourd();
                if(theFirstGourd != null) {
                    theFirstGourd.receiveControl(keyAction);
                }
                break;
            case World.SECOND_GOURD:
                Gourd theSecondGourd = world.getTheSecondGourd();
                if(theSecondGourd != null) {
                    theSecondGourd.receiveControl(keyAction);
                }
                break;
            case World.THIRD_GOURD:
                Gourd theThirdGourd = world.getThirdGourd();
                if(theThirdGourd != null) {
                    theThirdGourd.receiveControl(keyAction);
                }
                break;
            case World.FOURTH_GOURD:
                Gourd theFourthGourd = world.getFourthGourd();
                if(theFourthGourd != null) {
                    theFourthGourd.receiveControl(keyAction);
                }
                break;
            default:
                break;
        }
        return this;
    }
}
