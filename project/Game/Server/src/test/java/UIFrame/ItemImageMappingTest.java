package UIFrame;

import org.junit.Test;

import java.awt.image.BufferedImage;

public class ItemImageMappingTest {
    // 测试ItemImageMapping中的两个静态方法
    @Test
    public void getByteByItemImageTest() {
        ItemImage air = ItemImage.AIR;
        byte b = ItemImageMapping.getByteByItemImage(air);
        assert b == 9;
    }

    @Test
    public void getBufferedImageByByteTest() {
        ItemImage air = ItemImage.AIR;
        BufferedImage image = ItemImageMapping.getBufferedImageByByte((byte)9);
        assert air.getImage() == image;
    }
}
