package ui;

import bank.Bank;
import transaction.Transaction;
import utils.Budget;
import utils.Category;
import utils.Currency;
import utils.Month;


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
                                      Map<Category, Float> budgetByCategory,
                                      Currency displayCurrency,
                                      boolean convertAll) {

        StringBuilder strb = new StringBuilder();

        // Center the title
        String title = "Summary for " + month;
        int width = 40;
        int padding = (width - title.length()) / 2;
        strb.append(" ".repeat(Math.max(0, padding))).append(title).append("\n");

        // Add note about conversion based on convertAll flag
        if (convertAll) {
            strb.append("All transactions, spending, and budgets are converted to SGD.\n");
        } else {
            strb.append("Amounts are displayed in ").append(displayCurrency.name()).append(".\n");
        }

        // --- Recent Transactions ---
        strb.append("--- Recent Transactions ---\n");
        if (transactions.isEmpty()) {
            strb.append("No transactions this month.\n");
        } else {
            for (int i = 0; i < transactions.size(); i++) {
                Transaction t = transactions.get(i);
                float convertedValue;

                if (convertAll) {
                    convertedValue = t.getValue()
                            * Currency.getExchangeRateToSGD(t.getCurrency())
                            / Currency.getExchangeRateToSGD(displayCurrency);
                } else {
                    convertedValue = t.getValue();
                }

                strb.append("[")
                        .append(i + 1)
                        .append("] ")
                        .append(displayCurrency.getSymbol())
                        .append(String.format("%.2f", convertedValue))
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

            strb.append(String.format("%-12s : %s%.2f / %s%.2f",
                    cat.name(),
                    displayCurrency.getSymbol(), spent,
                    displayCurrency.getSymbol(), budget));

            if (budget > 0 && spent > budget) {
                strb.append(" (Over budget!)");
            }
            strb.append("\n");
        }

        // --- Total Spend ---
        float totalSpend = 0f;
        for (Transaction t : transactions) {
            if (convertAll) {
                totalSpend += t.getValue()
                        * Currency.getExchangeRateToSGD(t.getCurrency())
                        / Currency.getExchangeRateToSGD(displayCurrency);
            } else {
                totalSpend += t.getValue();
            }
        }
        strb.append("\nTotal spend this month: ")
                .append(displayCurrency.getSymbol())
                .append(String.format("%.2f", totalSpend))
                .append("\n");

        return strb.toString();
    }



    /**
     * Prints a list of the budgets the user has set for the month
     *
     * @return String representing the user's budgets
     */
    public static String listBudget(ArrayList<Budget> budgets, Month month) {
        StringBuilder strb = new StringBuilder();
        strb.append("Budgets for ").append(month).append(":");
        for (Category category : Category.values()) {
            boolean found = false;
            for (Budget budget : budgets) {
                if (budget.getCategory() == category && budget.getMonth() == month) {
                    strb.append("\n  ")
                            .append(category.name().toLowerCase())
                            .append(": ")
                            .append(budget.getBudget())
                            .append(" ")
                            .append(budget.getCurrency());
                    found = true;
                    break;
                }
            }
            if (!found) {
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
    public static void printMessage(String str) {
        System.out.println("  ===== SYSTEM =====");
        System.out.println(str);
        System.out.println("  ====== USER ======");
    }

    public static void showWelcomeMessage(ArrayList<Bank> banks) {
        StringBuilder sb = new StringBuilder();
        sb.append("Welcome to Finance Manager V1.0!\n");

        if (banks == null || banks.isEmpty()) {
            sb.append("You have not added any banks yet.\n")
                    .append("Use the 'addbank' command to link a bank or 'login' to access an existing one.");
        } else {
            sb.append("You have the following banks linked:\n");
            for (Bank b : banks) {
                Currency c = b.getCurrency();
                String balanceStr = String.format("%.2f", b.getBalance());

                sb.append("ID: ").append(b.getId()).append(" | ");

                if (c.getDuplicateSymbol()) {
                    // If duplicate, show symbol + amount + (currency code)
                    sb.append(c.getSymbol()).append(balanceStr)
                            .append(" (").append(c.name()).append(")");
                } else {
                    // Otherwise just symbol + amount
                    sb.append(c.getSymbol()).append(balanceStr);
                }

                sb.append("\n");
            }
            sb.append("What can I do for you today?");
        }

        printMessage(sb.toString());
    }
    public static void showCurrentBank(Bank currBank) {
        if (currBank == null) {
            printMessage("You are not logged into any bank.");
        } else {
            Currency c = currBank.getCurrency();
            String balanceStr = c.getDuplicateSymbol()
                    ? c.getSymbol() + currBank.getBalance() + " (" + c.name() + ")"
                    : c.getSymbol() + currBank.getBalance();
            printMessage("Currently logged into bank ID: " + currBank.getId() + "\n" +
                    "Balance: " + balanceStr);
        }
    }

}



