package ui;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputManagerTest {
    @Test
    public void getNextCommand_validInput_returnsTrimmedInput() {
        String simulatedInput = "   addbank 1000 SGD\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));

        String result = InputManager.getNextCommand(scanner);

        assertEquals("addbank 1000 SGD", result);
    }

    @Test
    public void getNextCommand_emptyThenValidInput_promptsUser() {
        // simulate empty line first, then valid input
        String simulatedInput = "\naddbank 5000 MYR\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));  // capture console output

        String result = InputManager.getNextCommand(scanner);

        String consoleOutput = outputStream.toString();
        assertEquals("addbank 5000 MYR", result);
        // check that prompt message was printed at least once
        assert(consoleOutput.contains("Please enter a command"));
    }
}
