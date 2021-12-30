package client.imageProcess;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ItemImageMapping {

    private static Map<String, Integer> imgToInt;
    private static Map<Integer, BufferedImage> intToImg;

    static {
        imgToInt = new HashMap<>();
        intToImg = new HashMap<>();
        imgToInt.put(ItemImage.GOURD_1.getImageFilename(), 1);
        intToImg.put(1, ItemImage.GOURD_1.getImage());
        imgToInt.put(ItemImage.MONSTER_BROWN.getImageFilename(), 2);
        intToImg.put(2, ItemImage.MONSTER_BROWN.getImage());
        imgToInt.put(ItemImage.STUMP.getImageFilename(), 3);
        intToImg.put(3, ItemImage.STUMP.getImage());
        imgToInt.put(ItemImage.WATER.getImageFilename(), 4);
        intToImg.put(4, ItemImage.WATER.getImage());
        imgToInt.put(ItemImage.TURF.getImageFilename(), 5);
        intToImg.put(5, ItemImage.TURF.getImage());
        imgToInt.put(ItemImage.TREE.getImageFilename(), 6);
        intToImg.put(6, ItemImage.TREE.getImage());
        imgToInt.put(ItemImage.BULLET_GOURD.getImageFilename(), 7);
        intToImg.put(7, ItemImage.BULLET_GOURD.getImage());
        imgToInt.put(ItemImage.BULLET_MONSTER.getImageFilename(), 8);
        intToImg.put(8, ItemImage.BULLET_MONSTER.getImage());
        imgToInt.put(ItemImage.AIR.getImageFilename(), 9);
        intToImg.put(9, ItemImage.AIR.getImage());
        imgToInt.put(ItemImage.SAND.getImageFilename(), 10);
        intToImg.put(10, ItemImage.SAND.getImage());
        imgToInt.put(ItemImage.STONE.getImageFilename(), 11);
        intToImg.put(11, ItemImage.STONE.getImage());
        imgToInt.put(ItemImage.GOURD_2.getImageFilename(), 12);
        intToImg.put(12, ItemImage.GOURD_2.getImage());
    }

    private ItemImageMapping() {}

    public static byte getByteByItemImage(ItemImage itemImage) {
        int ret = imgToInt.get(itemImage.getImageFilename());
        return (byte)ret;
    }

    public static BufferedImage getBufferedImageByByte(byte b) {
        // 可用的只有后6位，前两位用来表示image的subImage位置
        int n = b;
        n += b < 0 ? 128 : 0;
        int serialNumber = n / 64 + (b < 0 ? 2 : 0);
        int imgNumber = n % 64;
        BufferedImage totalImg = intToImg.get(imgNumber);
        int width = totalImg.getWidth();
        if(width != totalImg.getHeight()) {
            totalImg = totalImg.getSubimage(0, serialNumber * width, width, width);
        }
        return totalImg;
    }
}
