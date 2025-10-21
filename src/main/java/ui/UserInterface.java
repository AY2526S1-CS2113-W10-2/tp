package ui;

import user.User;

import java.util.Scanner;
import static ui.Parser.parseCommand;

public class UserInterface {
    public static void main(String[] args) {
        User.initialise();
        Scanner scanner = new Scanner(System.in);
        boolean isExit = false;

        while (!isExit) {
            String input = InputManager.getNextCommand(scanner);
            try {
                isExit = parseCommand(input);
            } catch (FinanceException e) {
                OutputManager.printMessage(e.getMessage());
            }
        }
    }
}
