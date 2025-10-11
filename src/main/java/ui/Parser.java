package ui;

import user.User;

import static ui.OutputManager.listRecentTransactions;
import static ui.OutputManager.printMessage;

public class Parser {
    /**
     * Parses the command string, and redirects control to the appropriate function to execute
     * @param command
     */
    public static void parseCommand(String command){

        // Gets first word of command string - the type of command
        int firstSpace = command.indexOf((" "));
        if (firstSpace < 0){
            firstSpace = command.length();
        }
        String comm = command.substring(0, firstSpace);

        switch (comm){
        case "this":
            printMessage("DO THIS");
            break;
        case "that":
            printMessage("DO THAT");
            break;
        case "what?":
            printMessage("DO WHAT?");
            break;
        case "list":
            printMessage(listRecentTransactions(User.getTransactions(), 10));
            break;
        default:
            printMessage("Does not match known command.");
        }
    }
}
