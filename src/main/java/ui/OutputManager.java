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
    //@@author kevinlokewy
    public static String printSummary(String month, List<Transaction> transactions,
                                      Map<Category, Float> spendingByCategory,
                                      Map<Category, Float> budgetByCategory,
                                      Currency displayCurrency,
                                      boolean isConvertAll) {

        StringBuilder strb = new StringBuilder();

        // Center the title
        appendCentreTitle(month, strb);

        // Add note about conversion based on convertAll flag
        appendCurrencyInfo(displayCurrency, isConvertAll, strb);

        // --- Recent Transactions ---
        appendRecentTransactions(transactions, displayCurrency, isConvertAll, strb);

        // --- Category Totals ---
        appendCategoryTotals(spendingByCategory, budgetByCategory, displayCurrency, strb);

        // --- Total Spend ---
        appendTotalSpend(transactions, displayCurrency, isConvertAll, strb);

        return strb.toString();
    }

    private static void appendTotalSpend(
            List<Transaction> transactions, Currency displayCurrency, boolean isConvertAll, StringBuilder strb) {
        float totalSpend = 0f;
        for (Transaction t : transactions) {
            if (isConvertAll) {
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
    }

    private static void appendCategoryTotals(
            Map<Category, Float> spendingByCategory, Map<Category, Float> budgetByCategory,
            Currency displayCurrency, StringBuilder strb) {
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
    }

    private static void appendRecentTransactions(
            List<Transaction> transactions, Currency displayCurrency, boolean isConvertAll, StringBuilder strb) {
        strb.append("--- Recent Transactions ---\n");
        if (transactions.isEmpty()) {
            strb.append("No transactions this month.\n");
        } else {
            for (int i = 0; i < transactions.size(); i++) {
                Transaction t = transactions.get(i);
                float convertedValue;

                if (isConvertAll) {
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
    }

    private static void appendCurrencyInfo(Currency displayCurrency, boolean isConvertAll, StringBuilder strb) {
        if (isConvertAll) {
            strb.append("All transactions, spending, and budgets are converted to SGD.\n");
        } else {
            strb.append("Amounts are displayed in ").append(displayCurrency.name()).append(".\n");
        }
    }

    private static void appendCentreTitle(String month, StringBuilder strb) {
        String title = "Summary for " + month;
        int width = 40;
        int padding = (width - title.length()) / 2;
        strb.append(" ".repeat(Math.max(0, padding))).append(title).append("\n");
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
            boolean isFound = false;
            for (Budget budget : budgets) {
                if (budget.getCategory() == category && budget.getMonth() == month) {
                    strb.append("\n  ")
                            .append(category.name().toLowerCase())
                            .append(": ")
                            .append(budget.getBudget())
                            .append(" ")
                            .append(budget.getCurrency());
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                strb.append("\n  ")
                        .append(category.name().toLowerCase())
                        .append(": no budget set yet");
            }
        }
        return strb.toString();
    }

    public static String listSearch(String keyword, ArrayList<Transaction> transactions) {
        StringBuilder strb = new StringBuilder();
        strb.append("Search results for keyword '")
                .append(keyword)
                .append("':");

        boolean isFound = false;

        for (Transaction t : transactions) {
            if (t.getCategory().name().toLowerCase().contains(keyword)
                    || t.toString().toLowerCase().contains(keyword)) {
                strb.append("\n  ")
                        .append(t.getTag()).append("(")
                        .append(t.getCategory()).append(") | ")
                        .append(t.getDate().getLongDate()).append(" | ")
                        .append(t.getValue()).append(" ")
                        .append(t.getCurrency());
                isFound = true;
            }
        }

        if (!isFound) {
            strb.append("\n  No matching transactions found.");
        }

        return strb.toString();
    }

    public static String listFilter(String filterType, ArrayList<Transaction> filtered) {
        StringBuilder strb = new StringBuilder();
        strb.append("Filtered transactions by ")
                .append(filterType)
                .append(":");

        if (filtered == null || filtered.isEmpty()) {
            strb.append("\n  No transactions match the filter criteria.");
            return strb.toString();
        }

        for (Transaction t : filtered) {
            strb.append("\n  ")
                    .append(t.getTag()).append("(")
                    .append(t.getCategory()).append(") | ")
                    .append(t.getDate().getLongDate()).append(" | ")
                    .append(t.getValue()).append(" ")
                    .append(t.getCurrency());
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
            appendNoBanksMessage(sb);
        } else {
            appendBanksList(banks, sb);
        }

        printMessage(sb.toString());
    }

    private static void appendBanksList(ArrayList<Bank> banks, StringBuilder sb) {
        sb.append("You have the following banks linked:\n");
        for (Bank b : banks) {
            formatBankSummary(b, sb);
        }
        sb.append("What can I do for you today?");
    }

    private static void formatBankSummary(Bank b, StringBuilder sb) {
        Currency c = b.getCurrency();
        String balanceStr = String.format("%.2f", b.getBalance());

        sb.append("ID: ").append(b.getId()).append(" | ");

        formatBalance(sb, c, balanceStr);

        sb.append("\n");
    }

    private static void formatBalance(StringBuilder sb, Currency c, String balanceStr) {
        if (c.getDuplicateSymbol()) {
            // If duplicate, show symbol + amount + (currency code)
            sb.append(c.getSymbol()).append(balanceStr)
                    .append(" (").append(c.name()).append(")");
        } else {
            // Otherwise just symbol + amount
            sb.append(c.getSymbol()).append(balanceStr);
        }
    }

    private static void appendNoBanksMessage(StringBuilder sb) {
        sb.append("You have not added any banks yet.\n")
                .append("Use the 'addbank' command to link a bank or 'login' to access an existing one.");
    }

    public static void showCurrentBank(Bank currBank) {
        if (currBank == null) {
            printMessage("You are not logged into any bank.");
        } else {
            Currency c = currBank.getCurrency();
            StringBuilder sb = new StringBuilder();

            sb.append("Currently logged into bank ID: ").append(currBank.getId()).append("\n")
                    .append("Balance: ");
            formatBalance(sb, c, String.format("%.2f", currBank.getBalance()));
            printMessage(sb.toString());
        }
    }

}



