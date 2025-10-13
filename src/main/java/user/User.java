package user;

import bank.Bank;
import transaction.Transaction;
import utils.*;
import saveData.Storage;

import java.util.ArrayList;
import java.util.Scanner;

import static ui.OutputManager.printMessage;
import static ui.Parser.parseCommand;
import static utils.Currency.USD;
import static utils.Currency.EUR;
import static utils.Currency.JPY;
import static utils.Currency.GBP;
import static utils.Currency.CNY;

public class User {
    public static ArrayList<Transaction> transactions;
    public static ArrayList<Bank> banks;
    private static Storage storage = new Storage(); // single shared storage

    public static void main(String[] args) {
        transactions = new ArrayList<>(storage.getTransactions());
        banks = new ArrayList<>(storage.getBanks());
        printMessage("Welcome to finance manager V1.0!\n" +
                "What can I do for you today?");
        Scanner scanner = new Scanner(System.in);

       /* addTransaction(new Transaction(13.10F, Category.FOOD, new Date(5, Month.NOV, 0), USD));
        addTransaction(new Transaction(200.40F, Category.TRANSPORT, new Date(0, Month.DEC, Integer.MAX_VALUE), JPY));
        addTransaction(new Transaction(0.00F, Category.FOOD, new Date(35, Month.JUL, 10), GBP));
        addTransaction(new Transaction(10F, Category.RECREATION, new Date(10, Month.JAN, 2024), CNY));

        addBank(new Bank(0, JPY, 1000.0f, 0.01f));
        addBank(new Bank(1, USD, 846, 1));      // All exchange rates are compared to USD
        addBank(new Bank(2, CNY, 124, 0.14f));
        addBank(new Bank(2, EUR, 50, 0.6f));

        */

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
        storage.addTransaction(transaction); // also add to storage
    }

    /**
     * Adds a bank balance to the user's record
     */
    public static void addBank(Bank bank){
        banks.add(bank);
        storage.addBank(bank);
    }

    /**
     * Deletes a transaction from the user's record
     *
     */
    public static void deleteTransaction(int index){
        ArrayList<Transaction> transactions = getTransactions();

        int realIndex = index - 1;

        if (realIndex < 0 || realIndex >= transactions.size()) {
            System.out.println("Invalid transaction index. Please enter a number between 1 and " + transactions.size());
            return;
        }
        Transaction removedTransaction = transactions.remove(realIndex);
        System.out.println("Deleted transaction: " + removedTransaction.toString());
    }

    /**
     * Adds a budget to the user's record
     */
    public static void addBudget(String categoryStr, float amount, Currency currency, Month month) {
        Category cat = Category.toCategory(categoryStr);
        if (cat == null) {
            System.out.println("Invalid category!");
            return;
        }
        Budget budget = new Budget(cat, amount, currency, month);
        storage.addBudget(budget);
    }

    public static Storage getStorage() {
        return storage;
    }

    public static ArrayList<Transaction> getTransactions() {
        return transactions;
    }


    public static ArrayList<Bank> getBanks() {
        return banks;
    }
}
