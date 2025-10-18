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

import static ui.OutputManager.listBanks;
import static ui.OutputManager.listBudget;
import static ui.OutputManager.listRecentTransactions;
import static ui.OutputManager.printMessage;

public class Parser {
    /**
     * Parses the command string, and redirects control to the appropriate function to execute
     * @param command
     */
    public static boolean parseCommand(String command){
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
        case "addBudget":
            addBudgetToUser(commandList);
            break;
        case "budget":
            addMonthlyBudget(commandList);
            break;
        case "listBudget":
            printMessage(listBudget());
            break;
        default:
            printMessage("Does not match known command.");
        }
        return false;
    }


    private static void deleteTransactionFromUser(ArrayList<String> commandList) {
        try {
            if (commandList.size() < 2) {
                printMessage("Usage: delete <transaction_index>");
                return;
            }

            int index = Integer.parseInt(commandList.get(1));

            User.deleteTransaction(index);

        } catch (NumberFormatException e) {
            printMessage("Invalid input. Please enter a numeric transaction index.");
        } catch (Exception e) {
            printMessage("Error deleting transaction: " + e.getMessage());
        }
    }

    private static void addTransactionToUser(ArrayList<String> commandList) {
        try {
            Category category = Category.toCategory(commandList.get(1));
            float value = Float.parseFloat(commandList.get(2));
            if (value < 0) {
                throw new IllegalArgumentException("Invalid value. Please enter a positive numeric value.");
            }

            // Parse date (format: DD/MM/YYYY)
            String[] dateParts = commandList.get(3).split("/");
            if (dateParts.length != 3) {
                throw new IllegalArgumentException("Date format must be DD/MM/YYYY");
            }

            int day = Integer.parseInt(dateParts[0]);
            int monthNum = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            // Convert numeric month to enum (1 = JAN, 2 = FEB, ...)
            Month month = Month.values()[monthNum - 1];

            Date date = new Date(day, month, year);

            Currency currency = Currency.toCurrency(commandList.get(4));

            Transaction trans = new Transaction(value, category, date, currency);
            User.addTransaction(trans);
            printMessage("  Added Transaction: " + trans.toString());

        } catch (Exception e) {
            printMessage("  Sorry! Wrong format. Try 'add <category> <value> <date> <currency>' \n" +
                    "  e.g. 'add food 4.50 10/4/2024 JPY'\n  " + e);
        }
    }

    private static void addBankToUser(ArrayList<String> commandList){
        try{
            float balance       = Float.parseFloat(commandList.get(1));
            Currency currency   = Currency.toCurrency(commandList.get(2));
            float exchangeRate  = Float.parseFloat(commandList.get(3));
            Bank bank = new Bank(User.getBanks().size(), currency, balance, exchangeRate);
            User.addBank(bank);
            printMessage("  Added " + bank.toString());
        }catch (Exception e) {
            printMessage("  Sorry! Wrong format. Try 'addBank <balance> <currency> <exchangerate>' \n" +
                    "  e.g. 'addBank 1000.00 JPY 0.01'\n  " + e);
        }

    }

    public static void addBudgetToUser(ArrayList<String> commandList){
        try{
            String category = commandList.get(1);
            float amount = Float.parseFloat(commandList.get(2));
            Currency currency = Currency.toCurrency(commandList.get(3));
            Month month = Month.valueOf(commandList.get(4).toUpperCase());

            User.addBudget(category, amount, currency, month);  // now matches the method signature

            printMessage("Added budget of " + amount + " " + currency + " for " + category + " in " + month);
        }catch (Exception e) {
            printMessage("  Sorry! Wrong format. Try 'addBudget <category> <amount> <currency> <month>' \n" +
                    "  e.g. 'addBudget food 200 SGD JAN'\n  " + e);
        }
    }

    private static void addMonthlyBudget(ArrayList<String> commandList) {
        try {
            String category = commandList.get(1);
            float amount = Float.parseFloat(commandList.get(2));
            Month month = Month.valueOf(commandList.get(3).toUpperCase());
            User.addBudget(category, amount, Currency.SGD, month); // assuming default currency or pass as arg
            printMessage("Added budget of $" + amount + " for " + category + " in " + month);
        } catch (Exception e) {
            printMessage("Usage: budget <category> <amount> <month> \n" +
                    "e.g. budget food 200 JAN\n" + e);
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
    private static void handleSummary(ArrayList<String> commandList) {
        try {
            if (commandList.size() < 2) {
                printMessage("Please provide a month! \n Usage: summary <month>" );
                return;
            }

            String monthInput = commandList.get(1);
            Summary summary = new Summary(User.getStorage());
            summary.showMonthlySummary(monthInput);

        } catch (IllegalArgumentException e) {
            printMessage("Invalid month name. Please try again (e.g., summary JAN).");
        } catch (Exception e) {
            printMessage("Error generating summary: " + e.getMessage());
        }
    }

}
