package ui;

import transaction.Transaction;
import user.User;
import utils.Category;
import utils.Currency;
import utils.Date;
import utils.Month;

import java.util.ArrayList;

import static ui.OutputManager.listRecentTransactions;
import static ui.OutputManager.printMessage;

public class Parser {
    /**
     * Parses the command string, and redirects control to the appropriate function to execute
     * @param command
     */
    public static void parseCommand(String command){
        ArrayList<String> commandList = splitCommand(command);
        String comm = commandList.get(0);
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
        case "add":
            addTransaction(commandList);
            break;
        case "list":
            printMessage(listRecentTransactions(User.getTransactions(), 10));
            break;
        default:
            printMessage("Does not match known command.");
        }
    }

    private static void addTransaction(ArrayList<String> commandList){
        try{
            Category category = Category.toCategory(commandList.get(1));
            float value = Float.parseFloat(commandList.get(2));
            Date date = new Date(0, Month.JAN,0);       // todo: parse date from string
            Currency currency = Currency.toCurrency(commandList.get(4));
            Transaction trans = new Transaction(value, category, date, currency);
            User.addTransaction(trans);
            printMessage("  Added Transaction: " + trans.toString());
        }catch (Exception e) {
            printMessage("  Sorry! Wrong format. Try 'add <category> <value> <date> <currency>' \n  e.g. 'add food 4.50 10/4/2024 JPY'\n  " + e);
        }

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
