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

    public void showMonthlySummary(String month, Bank bank, Currency currency) {
        assert month != null && !month.isBlank() : "Month input cannot be null or empty";

        String headerMessage;
        Currency displayCurrency;
        List<Transaction> monthlyTransactions = new ArrayList<>();

        Month monthEnum = Month.valueOf(month.toUpperCase());

        if (bank != null) {
            // Logged in → show this bank only
            displayCurrency = bank.getCurrency();
            headerMessage = "Summary for " + month + " for Bank " + bank.getId();

            monthlyTransactions = bank.getTransactions().stream()
                    .filter(t -> t.getDate().getMonth() == monthEnum)
                    .collect(Collectors.toList());
        } else {
            // Logged out → aggregate all banks
            if (currency == null) {
                displayCurrency = Currency.SGD;
                headerMessage = "Summary for " + month + "\nAll transactions, spending and budgeting are converted to SGD";
            } else {
                displayCurrency = currency;
                headerMessage = "Summary for " + month + " (Showing only " + currency.name() + " transactions)";
            }

            for (Bank b : User.getBanks()) {
                if (currency == null || b.getCurrency() == currency) {
                    monthlyTransactions.addAll(
                            b.getTransactions().stream()
                                    .filter(t -> t.getDate().getMonth() == monthEnum)
                                    .collect(Collectors.toList())
                    );
                }
            }
        }

        OutputManager.printMessage(headerMessage);

        // ===============================
        // Category-based Spending & Budget
        // ===============================
        Map<Category, Float> spendingByCategory = new HashMap<>();
        Map<Category, Float> budgetByCategory = new HashMap<>();

        for (Category cat : Category.values()) {
            // Spending: convert each transaction to displayCurrency
            float spent = 0f;
            for (Transaction t : monthlyTransactions) {
                if (t.getCategory() == cat) {
                    spent += t.getValue()
                            * Currency.getExchangeRateToSGD(t.getCurrency())
                            / Currency.getExchangeRateToSGD(displayCurrency);
                }
            }
            spendingByCategory.put(cat, spent);

            // Budget
            float budget = 0f;

            if (bank != null) {
                // Logged in → use bank-specific budget
                budget = User.getBudgetAmount(cat, monthEnum, bank);
            } else {
                // Logged out → aggregate budgets from all banks in SGD
                for (Bank b : User.getBanks()) {
                    Budget bgt = User.getBudgetForBank(cat, monthEnum, b); // you may need to implement this
                    if (bgt != null) {
                        // convert to SGD
                        float amountInSGD = bgt.getBudget() * Currency.getExchangeRateToSGD(bgt.getCurrency());
                        budget += amountInSGD;
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
                displayCurrency
        );

        OutputManager.printMessage(summaryOutput);
    }
}
