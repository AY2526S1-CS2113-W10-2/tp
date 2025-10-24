package summary;

import bank.Bank;
import savedata.Storage;
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

    public void showMonthlySummary(String month, Bank bank, Currency currency, boolean convertAll) {
        assert month != null && !month.isBlank() : "Month input cannot be null or empty";

        logger.log(Level.INFO, "Generating summary for month: " + month + ", Currency: " + currency.name() + ", ConvertAll: " + convertAll);

        Currency displayCurrency;
        List<Transaction> monthlyTransactions = new ArrayList<>();

        Month monthEnum = Month.valueOf(month.toUpperCase());

        if (bank != null) {
            // Logged in → show this bank only
            displayCurrency = bank.getCurrency();

            monthlyTransactions = bank.getTransactions().stream()
                    .filter(t -> t.getDate().getMonth() == monthEnum)
                    .collect(Collectors.toList());
        } else {
            // Logged out → aggregate banks based on convertAll flag
            displayCurrency = currency;

            if (convertAll) {
                // summary JAN → include ALL banks, convert to SGD
                for (Bank b : User.getBanks()) {
                    monthlyTransactions.addAll(
                            b.getTransactions().stream()
                                    .filter(t -> t.getDate().getMonth() == monthEnum)
                                    .collect(Collectors.toList())
                    );
                }
            } else {
                // summary JAN <currency> → include only banks of that currency
                for (Bank b : User.getBanks()) {
                    if (b.getCurrency() == currency) {
                        monthlyTransactions.addAll(
                                b.getTransactions().stream()
                                        .filter(t -> t.getDate().getMonth() == monthEnum)
                                        .collect(Collectors.toList())
                        );
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
                    if (convertAll) {
                        // summary JAN (all banks): convert to SGD
                        spent += t.getValue()
                                * Currency.getExchangeRateToSGD(t.getCurrency())
                                / Currency.getExchangeRateToSGD(displayCurrency);
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
                        if (b == bank) budget += bgt.getBudget();
                    } else {
                        if (convertAll) {
                            // summary JAN → convert all budgets to SGD
                            budget += bgt.getBudget()
                                    * Currency.getExchangeRateToSGD(bgt.getCurrency())
                                    / Currency.getExchangeRateToSGD(displayCurrency);
                        } else if (b.getCurrency() == currency) {
                            // summary JAN <currency> → budgets only of that currency (no conversion)
                            budget += bgt.getBudget();
                        }
                    }
                }
            }
            budgetByCategory.put(cat, budget);
        }

        String summaryOutput = OutputManager.printSummary(
                month,
                monthlyTransactions,
                spendingByCategory,
                budgetByCategory,
                displayCurrency,
                convertAll
        );

        OutputManager.printMessage(summaryOutput);
    }
}