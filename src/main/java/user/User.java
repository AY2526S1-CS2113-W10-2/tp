package user;

import bank.Bank;
import utils.Budget;
import utils.Category;
import utils.Month;
import storage.Storage;

import java.util.ArrayList;
import java.util.Map;

import static ui.OutputManager.showWelcomeMessage;

public class User {
    private static ArrayList<Bank> banks;
    private static ArrayList<Budget> budgets;
    private static boolean isLoggedIn;
    private static Bank currBank;
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


    public static ArrayList<Bank> getBanks() {
        return banks;
    }

    public static ArrayList<Budget> getBudgets() {
        return budgets;
    }

    public static Bank getCurrBank() {
        return currBank;
    }

    public static void setCurrBank(Bank currBank) {
        User.currBank = currBank;
    }

    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static void setIsLoggedIn(boolean isLoggedIn) {
        User.isLoggedIn = isLoggedIn;
    }

    public static Storage getStorage() {
        return storage;
    }

    public static Budget getBudgetForBank(Category category, Month month, Bank bank) {
        Map<Category, Budget> bank_budget = bank.getBudgets();
        if(bank_budget.get(category) != null){
            return bank_budget.get(category);
        }
        else {
            return null;
        }
    }
}
