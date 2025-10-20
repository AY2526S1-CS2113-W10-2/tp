package ui;

import user.User;

import java.util.ArrayList;

import static summary.Summary.handleSummary;
import static ui.OutputManager.listBanks;
import static ui.OutputManager.listBudget;
import static ui.OutputManager.listRecentTransactions;
import static ui.OutputManager.printMessage;
import static user.User.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parser {
    private static final Logger logger = Logger.getLogger(Parser.class.getName());

    /**
     * Parses the command string, and redirects control to the appropriate function to execute
     * @param command The user input command.
     * @return true if the command is 'exit', false otherwise.
     */
    public static boolean parseCommand(String command) throws FinanceException {
        assert command != null : "Command should not be null";
        assert !command.trim().isEmpty() : "Command should not be empty";

        logger.log(Level.INFO, "Received command: {0}", command);


        ArrayList<String> commandList = splitCommand(command);

        assert !commandList.isEmpty() : "Command list should not be empty after splitting";

        String comm = commandList.get(0).toLowerCase();
        logger.log(Level.FINE, "Parsed main command: {0}", comm);

        switch (comm){
        case "exit":
            logger.info("Executing 'exit' command");
            printMessage("Exiting program. Goodbye!");
            return true;
        case "summary":
            logger.info("Executing 'summary' command");
            handleSummary(commandList);
            break;
        case "addbank":
            logger.info("Executing 'addbank' command");
            addBankToUser(commandList);
            break;
        case "add":
            logger.info("Executing 'add' command");
            addTransactionToUser(commandList);
            break;
        case "list":
            logger.info("Executing 'list' command");
            printMessage(listRecentTransactions(User.getTransactions(), 10));
            break;
        case "listbanks":
            logger.info("Executing 'listbanks' command");
            printMessage(listBanks(User.getBanks(), 10));
            break;
        case "delete":
            logger.info("Executing 'delete' command");
            deleteTransactionFromUser(commandList);
            break;
        case "addbudget":
            logger.info("Executing 'addbudget' command");
            addBudgetToUser(commandList);
            break;
        case "listbudget":
            logger.info("Executing 'listbudget' command");
            printMessage(listBudget());
            break;
        default:
            logger.log(Level.WARNING,"Unknown command entered: " + comm);
            printMessage("Does not match any known command.");
        }
        return false;
    }

    /**
     * Takes a String, splits into arrayList of substring
     * E.g. "command arg1 arg2 arg3" -> {"command", "arg1", "arg2", "arg3"}
     *
     * @param command The string to be split
     * @return arrayList where 1st element is 1st substring, 2nd element is 2nd substring...
     */
    private static ArrayList<String> splitCommand(String command){
        assert command != null : "Command should not be null";

        ArrayList<String> commands = new ArrayList<>();
        while(true){
            ArrayList<String> split = recursiveGetCommand(command);
            assert !split.isEmpty() : "Split result should not be empty";

            commands.add(split.get(0));
            if (split.size() == 1){
                break;
            }
            command = split.get(1);
        }
        System.out.println(commands);
        return commands;
    }

    /**
     * Takes a String, splits into arrayList of first substring, remaining string
     * E.g. "command arg1 arg2 arg3" -> {"command", "arg1 arg2 arg3"}
     *
     * @param command The string to be split
     * @return arrayList where 1st element is 1st substring, 2nd element is remainder
     */
    private static ArrayList<String> recursiveGetCommand(String command){
        assert command != null : "Command should not be null";

        ArrayList<String> retVal = new ArrayList<>();
        int firstSpace = command.indexOf((" "));
        if (firstSpace < 0){
            firstSpace = command.length();
        }
        retVal.add(command.substring(0, firstSpace));
        if (firstSpace + 1 <= command.length()){
            retVal.add(command.substring(firstSpace + 1).trim());
        }
        return retVal;
    }
}
