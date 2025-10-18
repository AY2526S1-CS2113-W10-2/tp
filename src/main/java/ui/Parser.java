package ui;

import bank.Bank;
import transaction.Transaction;
import user.User;
import utils.Category;
import utils.Currency;
import utils.Date;
import utils.Month;
import summary.Summary;

import java.util.ArrayList;

import static summary.Summary.handleSummary;
import static ui.OutputManager.listBanks;
import static ui.OutputManager.listBudget;
import static ui.OutputManager.listRecentTransactions;
import static ui.OutputManager.printMessage;
import static user.User.*;

public class Parser {
    /**
     * Parses the command string, and redirects control to the appropriate function to execute
     * @param command
     */
    public static boolean parseCommand(String command) throws FinanceExceptions {
        ArrayList<String> commandList = splitCommand(command);
        String comm = commandList.get(0).toLowerCase();
        switch (comm){
        case "exit":
            printMessage("Exiting program. Goodbye!");
            return true;
        case "summary":
            handleSummary(commandList);
            break;
        case "addbank":
            addBankToUser(commandList);
            break;
        case "add":
            addTransactionToUser(commandList);
            break;
        case "list":
            printMessage(listRecentTransactions(User.getTransactions(), 10));
            break;
        case "listbanks":
            printMessage(listBanks(User.getBanks(), 10));
            break;
        case "delete":
            deleteTransactionFromUser(commandList);
            break;
        case "addbudget":
            addBudgetToUser(commandList);
            break;
        case "listbudget":
            printMessage(listBudget());
            break;
        default:
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
        ArrayList<String> commands = new ArrayList<>();
        while(true){
            ArrayList<String> split = recursiveGetCommand(command);
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
