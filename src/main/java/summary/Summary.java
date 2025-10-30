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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.logging.Logger;


public class Summary {
    private static final Logger logger = Logger.getLogger(Summary.class.getName());

    private final Storage storage;

    public Summary(Storage storage) {
        assert storage != null : "Storage object cannot be null";
        this.storage = storage;
        logger.log(Level.INFO, "Summary instance created successfully.");
    }

    public void showMonthlySummary(String month, Bank bank, Currency currency, boolean isConvertAll) {
        assert month != null && !month.isBlank() : "Month input cannot be null or empty";

        logger.log(Level.INFO, "Generating summary for month: " + month + ", Currency: "
                + currency.name() + ", ConvertAll: " + isConvertAll);

        Month monthEnum = Month.valueOf(month.toUpperCase());

        Currency displayCurrency = determineDisplayCurrency(bank, currency);
        List<Transaction> monthlyTransactions = getMonthlyTransactions(bank, monthEnum, currency, isConvertAll);

        Map<Category, Float> spendingByCategory = calculateSpendingByCategory(
                monthlyTransactions, displayCurrency, isConvertAll
        );
        Map<Category, Float> budgetByCategory = calculateBudgetByCategory(
                monthEnum, bank, displayCurrency, currency, isConvertAll
        );

        String summaryOutput = OutputManager.printSummary(
                month, monthlyTransactions, spendingByCategory, budgetByCategory, displayCurrency, isConvertAll
        );
        OutputManager.printMessage(summaryOutput);
    }

    private Currency determineDisplayCurrency(Bank bank, Currency currency) {
        return (bank != null) ? bank.getCurrency() : currency;
    }

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

    private List<Transaction> getAllBanksTransactions(Month monthEnum) {
        List<Transaction> monthlyTransactions = new ArrayList<>();
        for (Bank b : User.getBanks()) {
            getAllTransactions(b, monthlyTransactions, monthEnum);
        }
        return monthlyTransactions;
    }

    private List<Transaction> getCurrencySpecificTransactions(Month monthEnum, Currency currency) {
        List<Transaction> monthlyTransactions = new ArrayList<>();
        for (Bank b : User.getBanks()) {
            if (b.getCurrency() == currency) {
                getAllTransactions(b, monthlyTransactions, monthEnum);
            }
        }
        return monthlyTransactions;
    }

    private Map<Category, Float> calculateSpendingByCategory(
            List<Transaction> monthlyTransactions, Currency displayCurrency, boolean isConvertAll) {
        Map<Category, Float> spendingByCategory = new HashMap<>();

        for (Category cat : Category.values()) {
            float spent = calculateSpendingForCategory(cat, monthlyTransactions, displayCurrency, isConvertAll);
            spendingByCategory.put(cat, spent);
        }

        return spendingByCategory;
    }

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

    private Map<Category, Float> calculateBudgetByCategory(
            Month monthEnum, Bank bank, Currency displayCurrency, Currency currency, boolean isConvertAll) {
        Map<Category, Float> budgetByCategory = new HashMap<>();

        for (Category cat : Category.values()) {
            float budget = calculateBudgetForCategory(cat, monthEnum, bank, displayCurrency, currency, isConvertAll);
            budgetByCategory.put(cat, budget);
        }

        return budgetByCategory;
    }

    private float calculateBudgetForCategory(
            Category category, Month monthEnum, Bank bank,
            Currency displayCurrency, Currency currency, boolean isConvertAll) {
        float budget = 0f;

        for (Bank b : User.getBanks()) {
            Budget bgt = User.getBudgetForBank(category, monthEnum, b);
            if (bgt == null) {
                continue;
            }

            budget += calculateBudgetAmount(bgt, b, bank, displayCurrency, currency, isConvertAll);
        }

        return budget;
    }

    private float calculateBudgetAmount(
            Budget bgt, Bank b, Bank bank, Currency displayCurrency,
            Currency currency, boolean isConvertAll) {
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

    private float calculateLoggedInBudget(Budget bgt, Bank b, Bank bank) {
        if (b == bank) {
            return displayCurrencyBudget(0f, bgt);
        }
        return 0f;
    }

    private static void getAllTransactions(Bank b, List<Transaction> monthlyTransactions, Month monthEnum) {
        monthlyTransactions.addAll(b.getTransactions().stream()
                .filter(t -> t.getDate().getMonth() == monthEnum)
                .collect(Collectors.toList()));
    }

    private static List<Transaction> getBankTransactions(Bank bank, Month monthEnum) {
        return bank.getTransactions().stream()
                .filter(t -> t.getDate().getMonth() == monthEnum)
                .collect(Collectors.toList());
    }

    private static float getTotalSpend(Transaction t, float spent, Currency displayCurrency) {
        float tCurr = Currency.getExchangeRateToSGD(t.getCurrency());
        float dispCurr = Currency.getExchangeRateToSGD(displayCurrency);
        spent += t.getValue() * tCurr / dispCurr;
        return spent;
    }

    private static float displayCurrencyBudget(float budget, Budget bgt) {
        budget += bgt.getBudget();
        return budget;
    }

    private static float convertBudgets(float budget, Budget bgt, Currency displayCurrency) {
        float bgtCurr = Currency.getExchangeRateToSGD(bgt.getCurrency());
        float dispCurr = Currency.getExchangeRateToSGD(displayCurrency);
        budget += bgt.getBudget() * bgtCurr / dispCurr;
        return budget;
    }
}
