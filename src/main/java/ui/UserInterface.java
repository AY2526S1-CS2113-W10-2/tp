package ui;

import user.User;

import java.util.Scanner;
import static ui.Parser.parseCommand;

public class UserInterface {
    public static void main(String[] args) {
        System.out.println("Warning: If '⚠️'(hazard symbol) looks like '??'(two question marks) " +
                "then your console encoding is not UTF-8, some symbols (e.g. '฿') may not display correctly. " +
                "Consider running 'chcp 65001' in cmd before opening this program to ensure " +
                "proper encoding and display.");
        User.initialise();
        Scanner scanner = new Scanner(System.in);
        boolean isExit = false;

        while (!isExit) {
            String input = InputManager.getNextCommand(scanner);
            if (input == null) {
                break;
            }
            try {
                isExit = parseCommand(input);
            } catch (FinanceException e) {
                OutputManager.printMessage(e.getMessage());
            }
        }
    }
}
