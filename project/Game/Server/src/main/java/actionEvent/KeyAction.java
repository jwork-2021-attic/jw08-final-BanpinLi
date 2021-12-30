package actionEvent;

public class KeyAction {
    // 单方向控制
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;

    // 特殊行为控制
    // 发射远程子弹
    public static final int SHOT = 8;
    // 发射近程子弹
    public static final int SHOTS = 13;
    // 和子弹交换位置
    public static final int SWAP = 14;


    // 键盘事件的含义
    private final int value;
    // 键盘事件应该对谁进行响应，对谁进行响应的标准定义在World类中
    private final int character;

    public KeyAction(int value, int character) {
        this.value = value;
        this.character = character;
    }

    public int getKey() {
        return value;
    }

    public int getCharacter() {
        return this.character;
    }

    /**
     * 该方法提供将KeyAction对象以某种方式序列化成byte[]数组
     * @param keyAction 需要进行序列化的对象
     * @return 返回序列化之后的byte数组
     */
    public static byte[] serialize(KeyAction keyAction) {
        int key = keyAction.getKey();
        int cha = keyAction.getCharacter();
        byte[] bytes = new byte[8];
        for(int i = 0;i < 4;i++) {
            bytes[3 - i] = (byte)(key % 256);
            key = key / 256;
            bytes[7 - i] = (byte)(cha % 256);
            cha = cha / 256;
        }
        return bytes;
    }

    /**
     * 将byte[]数组反序列化成一个KeyAction对象
     * @param bytes 反序列化提供的byte数组
     * @return 返回一个KeyAction对象
     */
    public static KeyAction deserialize(byte[] bytes) {
        int key = 0;
        int cha = 0;
        for(int i = 0;i < 4;i++) {
            key = key * 256 + (int)bytes[i];
            cha = cha * 256 + (int)bytes[i + 4];
        }
        return new KeyAction(key, cha);
    }
}
