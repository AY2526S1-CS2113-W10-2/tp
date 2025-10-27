package ui;

import commands.AddBankCommand;
import commands.AddBudgetCommand;
import commands.Command;
import commands.AddTransactionCommand;
import commands.DeleteTransactionCommand;
import commands.ExitCommand;
import commands.ListBudgetsCommand;
import commands.SummaryCommand;
import commands.ListRecentTransactionsCommand;
import commands.ListBanksCommand;
import commands.ATM;

import user.User;

import java.util.ArrayList;

import static ui.OutputManager.printMessage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Parser {
    private static final Logger logger = Logger.getLogger(Parser.class.getName());

    /**
     * Parses the command string, and redirects control to the appropriate function to execute
     *
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

        ArrayList<String> arguments = new ArrayList<>(commandList.subList(1, commandList.size()));

        Command cmd = null;
        switch (comm){
        case "login":
            if (!User.isLoggedIn) {
                int bankId = Integer.parseInt(commandList.get(1));
                if (bankId >= User.banks.size() || User.banks.get(bankId).getId() != bankId) {
                    throw new FinanceException("Bank not found");
                }
                User.currBank = User.banks.get(bankId);
                User.isLoggedIn = true;
                printMessage("Successfully logged into bank " + bankId);
                OutputManager.showCurrentBank(User.currBank);
            } else {
                printMessage("Already logged into a bank. Please logout first.");
            }
            break;
        case "logout":
            if (User.isLoggedIn) {
                User.currBank = null;
                User.isLoggedIn = false;
                printMessage("Logged out successfully.");
            } else {
                printMessage("You are not logged into any bank.");
            }
            break;
        case "exit":
            logger.info("Executing 'exit' command");
            cmd = new ExitCommand();
            break;
        case "summary":
            logger.info("Executing 'summary' command");
            cmd = new SummaryCommand(arguments);
            break;
        case "addbank":
            logger.info("Executing 'addbank' command");
            cmd = new AddBankCommand(arguments);
            break;
        case "add":
            logger.info("Executing 'add' command");
            if(User.isLoggedIn) {
                cmd = new AddTransactionCommand(arguments);
            } else{
                logger.info("Please login to a bank to execute this command");
                throw new FinanceException("Please login to a bank to execute this command");
            }
            break;
        case "list":
            logger.info("Executing 'list' command");
            cmd = new ListRecentTransactionsCommand(); // always execute
            break;

        case "listbanks":
            logger.info("Executing 'listbanks' command");
            if(!User.isLoggedIn) {
                cmd = new ListBanksCommand();
            } else{
                logger.info("Please logout to execute this command");
                throw new FinanceException("Please logout to execute this command");
            }
            break;
        case "delete":
            logger.info("Executing 'delete' command");
            if(User.isLoggedIn) {
                cmd = new DeleteTransactionCommand(arguments);
            } else{
                logger.info("Please login to a bank to execute this command");
                throw new FinanceException("Please login to a bank to execute this command");
            }
            break;
        case "addbudget":
            logger.info("Executing 'addbudget' command");
            cmd = new AddBudgetCommand(arguments);
            break;
        case "listbudget":
            logger.info("Executing 'listbudget' command");
            cmd = new ListBudgetsCommand(arguments);
            break;
        case "deposit":
            logger.info("Executing 'deposit' command");
            if(User.isLoggedIn) {
                cmd = new ATM(arguments, User.currBank, true, false);
            } else{
                logger.info("Please login to a bank to execute this command");
                throw new FinanceException("Please login to a bank to execute this command");
            }
            break;
        case "withdraw":
            logger.info("Executing 'withdraw' command");
            if(User.isLoggedIn) {
                cmd = new ATM(arguments, User.currBank, false, true);
            } else{
                logger.info("Please login to a bank to execute this command");
                throw new FinanceException("Please login to a bank to execute this command");
            }
            break;
        default:
            logger.log(Level.WARNING,"Unknown command entered: " + comm);
            printMessage("Does not match any known command.");
            throw new FinanceException("Does not match any known command.");
        }
        if (cmd != null) {
            cmd.execute();
            OutputManager.showCurrentBank(User.currBank);
            return cmd.shouldExit();
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
     * @return arrayList where 1st element is 1st substring, 2nd element is the remainder
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
