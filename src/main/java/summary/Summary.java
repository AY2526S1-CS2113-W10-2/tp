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

        logger.log(Level.INFO, "Generating summary for month: " + month + ", Currency: " + currency.name() + ", ConvertAll: " + isConvertAll);

        Currency displayCurrency;
        List<Transaction> monthlyTransactions = new ArrayList<>();

        Month monthEnum = Month.valueOf(month.toUpperCase());

        if (bank != null) {
            // Logged in → show this bank only
            displayCurrency = bank.getCurrency();

            monthlyTransactions = getBankTransactions(bank, monthEnum);
        } else {
            // Logged out → aggregate banks based on convertAll flag
            displayCurrency = currency;

            if (isConvertAll) {
                // summary JAN → include ALL banks, convert to SGD
                for (Bank b : User.getBanks()) {
                    getAllTransactions(b, monthlyTransactions, monthEnum);
                }
            } else {
                // summary JAN <currency> → include only banks of that currency
                for (Bank b : User.getBanks()) {
                    if (b.getCurrency() == currency) {
                        getAllTransactions(b, monthlyTransactions, monthEnum);
                    }
                }
            }
        }

        // ===============================
        // Category-based Spending & Budget
        // ===============================
        Map<Category, Float> spendingByCategory = new HashMap<>();
        Map<Category, Float> budgetByCategory = new HashMap<>();

        for (Category cat : Category.values()) {
            // Spending
            float spent = 0f;
            for (Transaction t : monthlyTransactions) {
                if (t.getCategory() == cat) {
                    if (isConvertAll) {
                        // summary JAN (all banks): convert to SGD
                        spent = getTotalSpend(t, spent, displayCurrency);
                    } else {
                        // Logged in OR specific currency: no conversion needed
                        spent += t.getValue();
                    }
                }
            }
            spendingByCategory.put(cat, spent);

            // Budget
            float budget = 0f;
            for (Bank b : User.getBanks()) {
                Budget bgt = User.getBudgetForBank(cat, monthEnum, b);
                if (bgt != null) {
                    if (bank != null) {
                        // Logged in → bank-specific
                        if (b == bank) {
                            budget = displayCurrencyBudget(budget, bgt);
                        }
                    } else {
                        if (isConvertAll) {
                            // summary JAN → convert all budgets to SGD
                            budget = convertBudgets(budget, bgt, displayCurrency);
                        } else if (b.getCurrency() == currency) {
                            // summary JAN <currency> → budgets only of that currency (no conversion)
                            budget = displayCurrencyBudget(budget, bgt);
                        }
                    }
                }
            }
            budgetByCategory.put(cat, budget);
        }

        String summaryOutput = OutputManager.printSummary(month, monthlyTransactions, spendingByCategory, budgetByCategory, displayCurrency, isConvertAll);

        OutputManager.printMessage(summaryOutput);
    }

    private static void getAllTransactions(Bank b, List<Transaction> monthlyTransactions, Month monthEnum) {
        monthlyTransactions.addAll(b.getTransactions().stream().filter(t -> t.getDate().getMonth() == monthEnum).collect(Collectors.toList()));
    }

    private static List<Transaction> getBankTransactions(Bank bank, Month monthEnum) {
        List<Transaction> monthlyTransactions;
        monthlyTransactions = bank.getTransactions().stream().filter(t -> t.getDate().getMonth() == monthEnum).collect(Collectors.toList());
        return monthlyTransactions;
    }

    private static float getTotalSpend(Transaction t, float spent, Currency displayCurrency) {
        spent += t.getValue() * Currency.getExchangeRateToSGD(t.getCurrency()) / Currency.getExchangeRateToSGD(displayCurrency);
        return spent;
    }

    private static float displayCurrencyBudget(float budget, Budget bgt) {
        budget += bgt.getBudget();
        return budget;
    }

    private static float convertBudgets(float budget, Budget bgt, Currency displayCurrency) {
        budget += bgt.getBudget() * Currency.getExchangeRateToSGD(bgt.getCurrency()) / Currency.getExchangeRateToSGD(displayCurrency);
        return budget;
    }
}
