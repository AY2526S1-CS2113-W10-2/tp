package ui;

import java.util.Scanner;

import static ui.OutputManager.printMessage;

public class InputManager {


    public static String getNextCommand(Scanner scanner) {
        if (!scanner.hasNextLine()) {
            return null;    // If no input line, no return (CI seems to break without this)
        }
        String input;
        while (true) {
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                break;
            }
            printMessage("Please enter a command");
        }
        return input;
    }
}
