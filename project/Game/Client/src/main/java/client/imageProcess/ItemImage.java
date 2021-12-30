package client.imageProcess;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

public class ItemImage {
    public static final ItemImage GOURD_1 = new ItemImage("images/gourd_1.png");
    public static final ItemImage MONSTER_BROWN = new ItemImage("images/monster_brown.png");
    public static final ItemImage STUMP = new ItemImage("images/stump.png");
    public static final ItemImage WATER = new ItemImage("images/water.png");
    public static final ItemImage TURF = new ItemImage("images/turf.png");
    public static final ItemImage TREE = new ItemImage("images/tree.png");
    public static final ItemImage BULLET_GOURD = new ItemImage("images/bullet_gourd.png");
    public static final ItemImage BULLET_MONSTER = new ItemImage("images/bullet_monster.png");
    public static final ItemImage AIR = new ItemImage("images/air.png");
    public static final ItemImage SAND = new ItemImage("images/sand.png");
    public static final ItemImage STONE = new ItemImage("images/stone.png");
    public static final ItemImage GOURD_2 = new ItemImage("images/gourd_2.png");

    private BufferedImage image;
    private String filename;
    private String projectName = "Server/";
    private String dir = "src/main/resources/";

    public ItemImage(String filename) {
        this.filename = filename;
        FileInputStream is = null;

        try {
            is = new FileInputStream(filename);
        } catch (Exception e) {
            try {
                is = new FileInputStream(dir + filename);
            } catch (Exception ee) {
                try {
                    is = new FileInputStream(projectName + dir + filename);
                } catch (Exception eee) {
                    eee.printStackTrace();
                    ee.printStackTrace();
                    e.printStackTrace();
                }
            }
        }

        try {
            image = ImageIO.read(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getWidth() {
        return this.image.getWidth();
    }

    public int getHeight() {
        return this.image.getHeight();
    }

    public String getImageFilename() {
        return this.filename;
    }

    @Override
    public boolean equals(Object obj) {
        ItemImage ant;
        try {
            ant = (ItemImage)obj;
        } catch (Exception e) {
            return false;
        }
        return ant.getImageFilename().equals(filename);
    }

}
