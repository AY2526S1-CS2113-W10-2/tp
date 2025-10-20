package summary;

import savedata.Storage;
import transaction.Transaction;
import user.User;
import utils.Category;
import ui.OutputManager;
import utils.Month;
import ui.FinanceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.logging.Logger;


public class Summary {
    public static final int MIN_COMMAND_LENGTH = 2;
    private static final Logger logger = Logger.getLogger(Summary.class.getName());

    private final Storage storage;

    public Summary(Storage storage) {
        assert storage != null : "Storage object cannot be null";
        this.storage = storage;
        logger.log(Level.INFO, "Summary instance created successfully.");
    }

    public void showMonthlySummary(String month) {
        assert month != null && !month.isBlank() : "Month input cannot be null or empty";
        logger.log(Level.INFO, "Generating summary for month: " + month);

        List<Transaction> monthlyTransactions = User.getTransactions().stream()
                .filter(t -> t.getDate().getMonth() == Month.valueOf(month.toUpperCase()))
                .collect(Collectors.toList());

        logger.log(Level.INFO, "Total transactions found for " + month + ": " + monthlyTransactions.size());


        Map<Category, Float> spendingByCategory = new HashMap<>();
        Map<Category, Float> budgetByCategory = new HashMap<>();

        Month monthEnum = Month.valueOf(month.toUpperCase());

        for (Category cat : Category.values()) {
            // Total spent in this category this month
            float spent = (float) monthlyTransactions.stream()
                    .filter(t -> t.getCategory() == cat)
                    .mapToDouble(Transaction::getValue)
                    .sum();
            spendingByCategory.put(cat, spent);

            // Budget for this category this month
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

    public static void handleSummary(ArrayList<String> commandList) throws FinanceException {
        try {
            logger.log(Level.INFO, "Handling summary command with arguments: " + commandList);

            if (commandList.size() < MIN_COMMAND_LENGTH) {
                logger.log(Level.WARNING, "Invalid command length: " + commandList.size());
                throw new FinanceException("Please provide a month! \n Usage: summary <month>" );
            }

            String monthInput = commandList.get(1);
            Summary summary = new Summary(User.getStorage());
            summary.showMonthlySummary(monthInput);

        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Invalid month name provided:" + e.getMessage());
            throw new FinanceException("Invalid month name. Please try again (e.g., summary JAN).");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error in generating summary:" + e.getMessage());
            throw new FinanceException("Error generating summary: " + e.getMessage());
        }
    }
}
