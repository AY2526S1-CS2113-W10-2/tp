package ui;

import bank.Bank;
import transaction.Transaction;
import utils.Budget;
import utils.Category;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutputManager {

    /**
     * Prints a summary of the user's recent activity
     *
     * @return String representing the user's activity
     */
    public static String printSummary(String month, List<Transaction> transactions,
                                      Map<Category, Float> spendingByCategory,
                                      Map<Category, Float> budgetByCategory) {
        StringBuilder strb = new StringBuilder();

        // Center the title
        String title = "Summary for " + month;
        int width = 40; // total width for centering
        int padding = (width - title.length()) / 2;
        strb.append(" ".repeat(Math.max(0, padding))).append(title).append("\n\n");

        // --- Recent Transactions ---
        strb.append("--- Recent Transactions ---\n");
        if (transactions.isEmpty()) {
            strb.append("No transactions this month.\n");
        } else {
            for (int i = 0; i < transactions.size(); i++) {
                Transaction t = transactions.get(i);
                strb.append("[")
                        .append(i + 1) // 1-based index
                        .append("] ")
                        .append(t.getCurrency().name())
                        .append("$")
                        .append(t.getValue())
                        .append(" spent on ")
                        .append(t.getCategory().name())
                        .append(" on ")
                        .append(t.getDate().getDay())
                        .append("th of ")
                        .append(t.getDate().getMonth().name())
                        .append(", ")
                        .append(t.getDate().getYear())
                        .append("\n");
            }
        }

        // --- Category Totals ---
        strb.append("\n--- Category Totals (Spent / Budget) ---\n");
        for (Category cat : Category.values()) {
            float spent = spendingByCategory.getOrDefault(cat, 0f);
            float budget = budgetByCategory.getOrDefault(cat, 0f);
            strb.append(String.format("%-12s : $%.2f / $%.2f", cat.name(), spent, budget));
            if (budget > 0 && spent > budget) {
                strb.append(" (Over budget!)");
            }
            strb.append("\n");
        }

        return strb.toString();
    }




    /**
     * Prints a list of the recent transactions the user has done
     *
     * @return String representing the user's activity
     */
    public static String listRecentTransactions(ArrayList<Transaction> transactions, int numToList) {
        StringBuilder strb = new StringBuilder();
        strb.append("  Recent Transactions:");

        int size = transactions.size();
        int start = Math.max(0, size - numToList); // last `numToList` transactions

        for (int i = start; i < size; i++) {
            Transaction t = transactions.get(i);
            strb.append("\n  [").append(i + 1).append("] ") // 1-based index
                    .append(t.getCurrency().name())
                    .append("$")
                    .append(t.getValue())
                    .append(" spent on ")
                    .append(t.getCategory().name())
                    .append(" on ")
                    .append(t.getDate().getDay())
                    .append("th of ")
                    .append(t.getDate().getMonth().name())
                    .append(", ")
                    .append(t.getDate().getYear());
        }
        return strb.toString();
    }


    /**
     * Prints a list of the recent transactions the user has done
     *
     * @return String representing the user's activity
     */
    public static String listBanks(ArrayList<Bank> banks, int numToList){
        StringBuilder strb = new StringBuilder();
        strb.append("  Your Bank Accounts are:");
        for (int i = 0; i < banks.size(); i++){
            strb.append("\n  ").append(banks.get(i).toString());
        }
        return strb.toString();
    }

    public static String listBudget() {
        StringBuilder strb = new StringBuilder();
        strb.append("Current budget: ");
        for (Category category : Category.values()) {
            Budget budget = category.getBudget();
            if (budget != null) {
                strb.append("\n  ")
                    .append(category.name().toLowerCase())
                    .append(": ")
                    .append(budget.getBudget());
            } else {
                strb.append("\n  ")
                    .append(category.name().toLowerCase())
                    .append(": no budget set yet");
            }
        }
        return strb.toString();
    }

    /**
     * Prints a formatted message to terminal. Ensure entire response to user query is on one string
     *
     * @param str The string to be printed in the software format
     */
    public static void printMessage(String str){
        System.out.println("  ===== SYSTEM =====");
        System.out.println(str);
        System.out.println("  ====== USER ======");
    }
}
