package ui;

import bank.Bank;
import transaction.Transaction;

import java.util.ArrayList;

public class OutputManager {

    /**
     * Prints a summary of the user's recent activity
     *
     * @return String representing the user's activity
     */
    public static String printSummary(){
        // todo
        return null;
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
