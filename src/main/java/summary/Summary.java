package summary;

import bank.Bank;
import storage.Storage;
import transaction.Transaction;
import user.User;
import utils.Budget;
import utils.Category;
import ui.OutputManager;
import utils.Currency;
import utils.Month;
import logger.AppLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.logging.Logger;

/**
 * Handles the generation of financial summaries for a given month.
 * This class retrieves transactions, calculates spending, and compares it
 * against budgets to produce a formatted report for output.
 * <p>
 * It supports summaries by individual banks, specific currencies, or all
 * currencies combined.
 * </p>
 */

//@@author kevinlokewy
public class Summary {
    private static final Logger logger = AppLogger.getLogger();


    private final Storage storage;

    public Summary(Storage storage) {
        assert storage != null : "Storage object cannot be null";
        this.storage = storage;
        logger.log(Level.INFO, "Summary instance created successfully.");
    }
    /**
     * Displays a formatted monthly financial summary for the given month, currency, and bank.
     *
     * @param month        the target month (string format, e.g. "JANUARY")
     * @param bank         the bank to generate summary for (nullable)
     * @param currency     the currency for display or filtering
     * @param isConvertAll whether to aggregate and convert all currencies into the display currency
     * @throws AssertionError if the {@code month} is null or blank
     */

    //@@author kevinlokewy
    public void showMonthlySummary(String month, Bank bank, Currency currency, boolean isConvertAll) {
        assert month != null && !month.isBlank() : "Month input cannot be null or empty";

        logger.log(Level.INFO, "Generating summary for month: " + month + ", Currency: " + currency.name()
                + ", ConvertAll: " + isConvertAll);

        Month monthEnum = Month.valueOf(month.toUpperCase());

        Currency displayCurrency = determineDisplayCurrency(bank, currency);
        List<Transaction> monthlyTransactions = getMonthlyTransactions(bank, monthEnum, currency, isConvertAll);

        Map<Category, Float> spendingByCategory = calculateSpendingByCategory(monthlyTransactions,
                displayCurrency, isConvertAll);

        Map<Category, Float> budgetByCategory = calculateBudgetByCategory(monthEnum, bank, displayCurrency,
                currency, isConvertAll);

        String summaryOutput = OutputManager.printSummary(month, monthlyTransactions, spendingByCategory,
                budgetByCategory, displayCurrency, isConvertAll);

        OutputManager.printMessage(summaryOutput);
    }

    /**
     * Determines the currency to display the summary in.
     *
     * @param bank     the selected bank (nullable)
     * @param currency the user-selected currency
     * @return the currency to display amounts in
     */

    //@@author kevinlokewy
    private Currency determineDisplayCurrency(Bank bank, Currency currency) {
        return (bank != null) ? bank.getCurrency() : currency;
    }

    /**
     * Retrieves all transactions for the specified month based on the user’s selection.
     *
     * @param bank         the bank to query (nullable)
     * @param monthEnum    the month to retrieve transactions for
     * @param currency     the selected currency
     * @param isConvertAll whether to aggregate all banks and convert to one currency
     * @return a list of filtered {@link Transaction} objects
     */

    //@@author kevinlokewy
    private List<Transaction> getMonthlyTransactions(
            Bank bank, Month monthEnum, Currency currency, boolean isConvertAll) {

        if (bank != null) {
            return getBankTransactions(bank, monthEnum);
        }

        if (isConvertAll) {
            return getAllBanksTransactions(monthEnum);
        }

        return getCurrencySpecificTransactions(monthEnum, currency);
    }

    /**
     * Retrieves all transactions across all banks for the specified month.
     *
     * @param monthEnum the month to filter transactions by
     * @return a combined list of transactions across all banks
     */
    //@@author kevinlokewy
    private List<Transaction> getAllBanksTransactions(Month monthEnum) {
        List<Transaction> monthlyTransactions = new ArrayList<>();
        for (Bank b : User.getBanks()) {
            getAllTransactions(b, monthlyTransactions, monthEnum);
        }
        return monthlyTransactions;
    }

    /**
     * Retrieves transactions for a specific currency across all banks.
     *
     * @param monthEnum the target month
     * @param currency  the desired currency
     * @return a list of transactions matching the currency and month
     */
    //@@author kevinlokewy
    private List<Transaction> getCurrencySpecificTransactions(Month monthEnum, Currency currency) {
        List<Transaction> monthlyTransactions = new ArrayList<>();
        for (Bank b : User.getBanks()) {
            if (b.getCurrency() == currency) {
                getAllTransactions(b, monthlyTransactions, monthEnum);
            }
        }
        return monthlyTransactions;
    }

    /**
     * Computes total spending by category for a list of transactions.
     *
     * @param monthlyTransactions the list of monthly transactions
     * @param displayCurrency     the currency used for display
     * @param isConvertAll        whether conversion should be applied across currencies
     * @return a map of {@link Category} to total spending amount
     */

    //@@author kevinlokewy
    private Map<Category, Float> calculateSpendingByCategory(
            List<Transaction> monthlyTransactions, Currency displayCurrency, boolean isConvertAll) {

        Map<Category, Float> spendingByCategory = new HashMap<>();

        for (Category cat : Category.values()) {
            float spent = calculateSpendingForCategory(cat, monthlyTransactions, displayCurrency, isConvertAll);
            spendingByCategory.put(cat, spent);
        }

        return spendingByCategory;
    }

    /**
     * Computes total spending by category for a list of transactions.
     *
     * @param monthlyTransactions the list of monthly transactions
     * @param displayCurrency     the currency used for display
     * @param isConvertAll        whether conversion should be applied across currencies
     * @return a map of {@link Category} to total spending amount
     */
    //@@author kevinlokewy
    private float calculateSpendingForCategory(
            Category category, List<Transaction> monthlyTransactions, Currency displayCurrency, boolean isConvertAll) {
        float spent = 0f;

        for (Transaction t : monthlyTransactions) {
            if (t.getCategory() != category) {
                continue;
            }

            if (isConvertAll) {
                spent = getTotalSpend(t, spent, displayCurrency);
            } else {
                spent += t.getValue();
            }
        }

        return spent;
    }

    /**
     * Computes the budget allocated per category for the specified month and currency scope.
     *
     * @param monthEnum       the month to evaluate
     * @param bank            the bank to filter by (nullable)
     * @param displayCurrency the display currency
     * @param currency        the currency filter
     * @param isConvertAll    whether to include and convert all currencies
     * @return a map of {@link Category} to allocated budget
     */
    //@@author kevinlokewy
    private Map<Category, Float> calculateBudgetByCategory(
            Month monthEnum, Bank bank, Currency displayCurrency, Currency currency, boolean isConvertAll) {
        Map<Category, Float> budgetByCategory = new HashMap<>();

        for (Category cat : Category.values()) {
            float budget = calculateBudgetForCategory(cat, monthEnum, bank, displayCurrency, currency, isConvertAll);
            budgetByCategory.put(cat, budget);
        }

        return budgetByCategory;
    }

    /**
     * Calculates the total budget for a specific category across banks.
     */

    //@@author kevinlokewy
    private float calculateBudgetForCategory(Category category, Month monthEnum, Bank bank,
                                             Currency displayCurrency, Currency currency, boolean isConvertAll) {
        float budget = 0f;

        if (bank == null) {  // logged out, multiple banks
            for (Bank b : User.getBanks()) {
                if (!isConvertAll && b.getCurrency() != currency) {
                    continue; // skip banks that don't match the requested currency
                }

                Budget bgt = User.getBudgetForBank(category, monthEnum, b);
                if (bgt == null) continue;

                if (isConvertAll) {
                    // Convert each bank's remaining budget to displayCurrency (SGD)
                    budget += convertBudgets(bgt.getRemainingAmount(), bgt, displayCurrency);
                } else {
                    // If currency-specific, add remaining budget only for matching currency banks
                    budget += bgt.getRemainingAmount();
                }
            }
        } else{
            Budget bgt = User.getBudgetForBank(category, monthEnum, bank);
            if (bgt != null) {
                budget += bgt.getRemainingAmount();
            }
        }

        return budget;
    }

    /**
     * Helper method to compute budget values depending on user context (bank, currency, conversion).
     */
    //@@author kevinlokewy

    private float calculateBudgetAmount(
            Budget bgt, Bank b, Bank bank, Currency displayCurrency, Currency currency, boolean isConvertAll) {
        if (bank != null) {
            return calculateLoggedInBudget(bgt, b, bank);
        }

        if (isConvertAll) {
            return convertBudgets(0f, bgt, displayCurrency);
        }

        if (b.getCurrency() == currency) {
            return displayCurrencyBudget(0f, bgt);
        }

        return 0f;
    }

    /**
     * Returns the applicable budget for the logged-in user’s current bank.
     */
    //@@author kevinlokewy
    private float calculateLoggedInBudget(Budget bgt, Bank b, Bank bank) {
        if (b == bank) {
            return displayCurrencyBudget(0f, bgt);
        }
        return 0f;
    }

    //@@author kevinlokewy
    private static void getAllTransactions(Bank b, List<Transaction> monthlyTransactions, Month monthEnum) {
        monthlyTransactions.addAll(b.getTransactions().stream()
                .filter(t -> t.getDate().getMonth() == monthEnum)
                .toList());
    }

    /**
     * Appends all transactions of a specific month from a bank to a shared list.
     */
    //@@author kevinlokewy
    private static List<Transaction> getBankTransactions(Bank bank, Month monthEnum) {
        return bank.getTransactions().stream()
                .filter(t -> t.getDate().getMonth() == monthEnum)
                .collect(Collectors.toList());
    }

    //@@author kevinlokewy
    private static float getTotalSpend(Transaction t, float spent, Currency displayCurrency) {
        spent += t.getValue() * Currency.getExchangeRateToSGD(t.getCurrency())
                / Currency.getExchangeRateToSGD(displayCurrency);
        return spent;
    }

    //@@author kevinlokewy
    private static float displayCurrencyBudget(float budget, Budget bgt) {
        budget += bgt.getBudget();
        return budget;
    }

    //@@author kevinlokewy
    private static float convertBudgets(float remainingAmount, Budget bgt, Currency displayCurrency) {
        return remainingAmount * Currency.getExchangeRateToSGD(bgt.getCurrency())
                / Currency.getExchangeRateToSGD(displayCurrency);
    }
}
