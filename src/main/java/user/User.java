package user;

import bank.Bank;
import transaction.Transaction;
import ui.FinanceException;
import utils.Budget;
import utils.Category;
import utils.Month;
import savedata.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static ui.OutputManager.printMessage;

public class User {
    public static ArrayList<Transaction> transactions;
    public static ArrayList<Bank> banks;
    public static ArrayList<Budget> budgets;
    private static Storage storage = new Storage(); // single shared storage

    public static void initialise() {
        transactions = storage.loadTransactions();
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        banks = storage.loadBanks();
        if (banks == null) {
            banks = new ArrayList<>();
        }
        budgets = storage.loadBudgets();
        if (budgets == null) {
            budgets = new ArrayList<>();
        }
        printMessage("Welcome to finance manager V1.0!\n" +
                "What can I do for you today?");

    }

    /**
     * Adds a transaction to the user's record
     */
    public static void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        storage.saveTransactions(transactions); // also add to storage
    }

    /**
     * Adds a bank balance to the user's record
     */
    public static void addBank(Bank bank){
        banks.add(bank);
        storage.saveBanks(banks);
    }

    /**
     * Adds a budget to the user's record
     */
    public static void addBudget(Budget budget) {
        budgets.add(budget);
        storage.saveBudgets(budgets);
    }

    /**
     * Deletes a transaction from the user's record
     *
     */
    public static void deleteTransaction(int index) throws FinanceException {
        ArrayList<Transaction> transactions = getTransactions();

        int realIndex = index - 1;

        if (realIndex < 0 || realIndex >= transactions.size()) {
            throw new FinanceException("Invalid index. Please enter a number between 1 and " + transactions.size());
        }
        Transaction removedTransaction = transactions.remove(realIndex);
        printMessage("Deleted transaction: " + removedTransaction.toString());
        storage.saveTransactions(transactions);
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

    public static ArrayList<Budget> getBudgets() {
        return budgets;
    }

    public static Storage getStorage() {
        return storage;
    }

    public static float getBudgetAmount(Category category, Month month) {
        for (Budget b : budgets) {
            if (b.getCategory() == category && b.getMonth() == month) {
                return b.getBudget();
            }
        }
        return 0f;
    }
}
