package ui;

import bank.Bank;
import transaction.Transaction;
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
        strb.append("===== Summary for ").append(month).append(" =====\n");

        if (transactions.isEmpty()) {
            strb.append("No transactions found for this month.\n");
        } else {
            strb.append("\n--- Transactions ---\n");
            for (int i = 0; i < transactions.size(); i++) {
                strb.append("[").append(i).append("] ").append(transactions.get(i).toString()).append("\n");
            }
        }

        strb.append("\n--- Category Totals ---\n");
        for (Category cat : spendingByCategory.keySet()) {
            float spent = spendingByCategory.get(cat);
            float budget = budgetByCategory.getOrDefault(cat, 0f);
            strb.append(String.format("%-15s : $%.2f / $%.2f", cat, spent, budget));
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
    public static String listRecentTransactions(ArrayList<Transaction> transactions, int numToList){
        StringBuilder strb = new StringBuilder();
        strb.append("  Recent Transactions:");
        for (int i = 0; i < transactions.size(); i++){
            strb.append("\n  ").append(transactions.get(i).toString());
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
