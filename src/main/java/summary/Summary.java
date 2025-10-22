package summary;

import bank.Bank;
import savedata.Storage;
import transaction.Transaction;
import user.User;
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

    public void showMonthlySummary(String month, Bank bank, Currency currency) {
        assert month != null && !month.isBlank() : "Month input cannot be null or empty";
        logger.log(Level.INFO, "Generating summary for month: " + month +
                (bank != null ? (" for bank ID " + bank.getId()) :
                        (currency != null ? (" for currency: " + currency.name()) : " across all banks")));

        Month monthEnum = Month.valueOf(month.toUpperCase());
        List<Transaction> monthlyTransactions = new ArrayList<>();

        // Case 1: Bank is specified
        if (bank != null) {
            logger.log(Level.INFO, "Processing transactions for bank ID: " + bank.getId());
            monthlyTransactions = bank.getTransactions().stream()
                    .filter(t -> t.getDate().getMonth() == monthEnum)
                    .collect(Collectors.toList());
        }
        // Case 2: Bank is null — use currency
        else {
            logger.log(Level.INFO, "No specific bank provided. Aggregating user’s transactions.");

            List<Bank> userBanks = User.getBanks();
            for (Bank b : userBanks) {
                // If currency is specified, only use banks with that currency
                if (currency != null) {
                    if (b.getCurrency() == currency) {
                        monthlyTransactions.addAll(
                                b.getTransactions().stream()
                                        .filter(t -> t.getDate().getMonth() == monthEnum)
                                        .collect(Collectors.toList())
                        );
                    }
                } else {
                    // No currency restriction — collect all
                    monthlyTransactions.addAll(
                            b.getTransactions().stream()
                                    .filter(t -> t.getDate().getMonth() == monthEnum)
                                    .collect(Collectors.toList())
                    );
                }
            }
        }

        logger.log(Level.INFO, "Total transactions found for " + month + ": " + monthlyTransactions.size());

        // ===============================
        // Category-based Spending & Budget
        // ===============================
        Map<Category, Float> spendingByCategory = new HashMap<>();
        Map<Category, Float> budgetByCategory = new HashMap<>();

        for (Category cat : Category.values()) {
            float spent = (float) monthlyTransactions.stream()
                    .filter(t -> t.getCategory() == cat)
                    .mapToDouble(Transaction::getValue)
                    .sum();
            spendingByCategory.put(cat, spent);

            float budget = User.getBudgetAmount(cat, monthEnum);
            budgetByCategory.put(cat, budget);
        }

        assert !spendingByCategory.isEmpty() : "Spending map should not be empty";
        assert !budgetByCategory.isEmpty() : "Budget map should not be empty";

        String summaryOutput = OutputManager.printSummary(
                month,
                monthlyTransactions,
                spendingByCategory,
                budgetByCategory
        );

        logger.log(Level.INFO, "Summary generated successfully for month: " + month);
        OutputManager.printMessage(summaryOutput);
    }
}
