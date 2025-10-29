package ui;

import java.util.Scanner;

import static ui.OutputManager.printMessage;

public class InputManager {


    public static String getNextCommand(Scanner scanner) {
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
