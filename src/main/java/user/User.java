package user;

import bank.Bank;
import transaction.Transaction;
import utils.Budget;
import utils.Category;
import utils.Month;
import storage.Storage;

import java.util.ArrayList;

import static ui.OutputManager.showWelcomeMessage;

public class User {
    public static ArrayList<Transaction> transactions;
    public static ArrayList<Bank> banks;
    public static ArrayList<Budget> budgets;
    public static boolean isLoggedIn;
    public static Bank currBank;
    private static Storage storage = new Storage(); // single shared storage

    public static void initialise() {
        banks = storage.loadBanks();
        if (banks == null) {
            banks = new ArrayList<>();
        }
        storage.loadTransactions();

        budgets = storage.loadBudgets();
        if (budgets == null) {
            budgets = new ArrayList<>();
        }
        showWelcomeMessage(banks);
        currBank = null;
        isLoggedIn = false;
    }

    /**
     * Adds a bank balance to the user's record
     */
    public static void addBank(Bank bank) {
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

    public static float getBudgetAmount(Category category, Month month, Bank bank) {
        float total = 0f;
        for (Budget b : budgets) {
            if (b.getCategory() == category && b.getMonth() == month) {
                if (bank == null || b.getBank() == bank) {
                    total += b.getBudget();
                }
            }
        }
        return total;
    }

    public static Budget getBudgetForBank(Category category, Month month, Bank bank) {
        for (Budget b : budgets) {
            if (b.getCategory() == category && b.getMonth() == month && b.getBank() == bank) {
                return b;
            }
        }
        return null;
    }
}
