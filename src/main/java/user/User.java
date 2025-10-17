package user;

import bank.Bank;
import transaction.Transaction;
import utils.*;
import saveData.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ui.OutputManager.printMessage;

public class User {
    public static ArrayList<Transaction> transactions;
    public static ArrayList<Bank> banks;
    public static ArrayList<Budget> budgets;
    private static Storage storage = new Storage(); // single shared storage

    public static void initialise() {
        transactions = new ArrayList<>(storage.getTransactions());
        banks = new ArrayList<>(storage.getBanks());
        budgets = new ArrayList<>();                                  //TODO: load budgets from file
        printMessage("Welcome to finance manager V1.0!\n" +
                "What can I do for you today?");

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
            printMessage("Invalid transaction index. Please enter a number between 1 and " + transactions.size());
            return;
        }
        Transaction removedTransaction = transactions.remove(realIndex);
        printMessage("Deleted transaction: " + removedTransaction.toString());
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
        //storage.addBudget(budget);
        budgets.add(budget);
        System.out.println(budgets.toString());
    }

    public static void deleteTransactionFromUser(ArrayList<String> commandList) {
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

    public static void addTransactionToUser(ArrayList<String> commandList) {
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

    public static void addBankToUser(ArrayList<String> commandList){
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

    public static Storage getStorage() {
        return storage;
    }

    public static Map<Category, Float> spendingByCategory() {
        Map<Category, Float> spendingMap = new HashMap<>();

        // Initialize all categories with 0 spending
        for (Category category : Category.values()) {
            float budgetSpent = category.getBudget().getBudget() - category.getBudget().getRemainingAmount();
            spendingMap.put(category, budgetSpent);
        }

        return spendingMap;
    }

    public static Map<Category, Float> budgetByCategory(){
        Map<Category, Float> budgetMap = new HashMap<>();

        // Initialize all categories with 0 spending
        for (Category category : Category.values()) {
            float budget = category.getBudget().getBudget();
            budgetMap.put(category, budget);
        }

        return budgetMap;
    }

    public static ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public static ArrayList<Bank> getBanks() {
        return banks;
    }
}
