package UIFrame;

import java.awt.image.BufferedImage;

/**
 * 能够在在屏幕中进行展示的东西，必须实现这个接口
 */
public interface Displayable {

    public BufferedImage getBufferedImage();

    public void setBufferedImage(BufferedImage image);

}
