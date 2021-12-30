package actionEvent;

import org.junit.Test;

public class KeyActionTest {
    @Test
    public void serializeAndDeserializeTest() {
        KeyAction keyAction = new KeyAction(1, 1);

        byte[] bytes = KeyAction.serialize(keyAction);
        KeyAction deserializeKeyAction = KeyAction.deserialize(bytes);

        assert keyAction.getKey() == deserializeKeyAction.getKey();
        assert keyAction.getCharacter() == deserializeKeyAction.getCharacter();
    }
}
