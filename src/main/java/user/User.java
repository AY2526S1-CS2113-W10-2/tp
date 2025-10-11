package user;

import bank.Bank;
import transaction.Transaction;
import utils.Category;
import utils.Date;
import utils.Month;

import java.util.ArrayList;
import java.util.Scanner;

import static ui.OutputManager.printMessage;
import static ui.Parser.parseCommand;
import static utils.Currency.*;

public class User {
    public static ArrayList<Transaction> transactions = new ArrayList<>();
    public static ArrayList<Bank> banks = new ArrayList<>();

    public static void main(String[] args) {
        printMessage("Welcome to finance manager V1.0!\n" +
                "What can I do for you today?");
        Scanner scanner = new Scanner(System.in);

        addTransaction(new Transaction(13.10F, Category.FOOD, new Date(5, Month.NOV, 0), USD));
        addTransaction(new Transaction(200.40F, Category.TRANSPORT, new Date(0, Month.DEC, Integer.MAX_VALUE), JPY));
        addTransaction(new Transaction(0.00F, Category.FOOD, new Date(35, Month.JUL, 10), GBP));
        addTransaction(new Transaction(10F, Category.RECREATION, new Date(10, Month.JAN, 2024), CNY));

        while(true){
            String str = scanner.nextLine();
            parseCommand(str);
        }
    }

    /**
     * Adds a transaction to the user's record
     */
    public static void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }

    /**
     * Deletes a transaction from the user's record
     *
     */
    private static void deleteTransaction(){

    }

    /**
     * Adds a budget to the user's record
     */
    private static void addBudget(){

    }

    public static ArrayList<Transaction> getTransactions() {
        return transactions;
    }
}
