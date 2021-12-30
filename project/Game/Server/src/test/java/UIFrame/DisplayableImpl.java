package UIFrame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

public class DisplayableImpl implements Displayable {
    BufferedImage image;

    public DisplayableImpl(String name) {
        try {
            image = ImageIO.read(new FileInputStream(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public BufferedImage getBufferedImage() {
        return image;
    }

    @Override
    public void setBufferedImage(BufferedImage image) {
        this.image = image;
    }
}
