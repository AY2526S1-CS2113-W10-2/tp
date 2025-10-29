package ui;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UserInterfaceTest {

    @Test
    public void main_withExitInput_exitsCleanly() {
        String simulatedInput = "exit\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);

        assertDoesNotThrow(() -> UserInterface.main(new String[0]));
    }
}
