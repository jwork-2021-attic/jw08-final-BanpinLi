package com.umi.screen;

import UIFrame.ImagePanel;
import actionEvent.KeyAction;

public interface Screen {

    public void displayOutput(ImagePanel terminal);

    public Screen respondToUserInput(KeyAction keyAction);
}
