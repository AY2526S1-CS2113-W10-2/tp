package summary;

import saveData.Storage;
import transaction.Transaction;
import user.User;
import utils.Category;
import ui.OutputManager;
import utils.Month;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ui.OutputManager.printMessage;

public class Summary {
    private final Storage storage;

    public Summary(Storage storage) {
        this.storage = storage;
    }

    public void showMonthlySummary(String month) {
        List<Transaction> monthlyTransactions = User.getTransactions().stream()
                .filter(t -> t.getDate().getMonth() == Month.valueOf(month.toUpperCase()))
                .collect(Collectors.toList());

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


        String summaryOutput = OutputManager.printSummary(
                month,
                monthlyTransactions,
                spendingByCategory,
                budgetByCategory
        );

        OutputManager.printMessage(summaryOutput);
    }

    public static void handleSummary(ArrayList<String> commandList) {
        try {
            if (commandList.size() < 2) {
                printMessage("Please provide a month! \n Usage: summary <month>" );
                return;
            }

            String monthInput = commandList.get(1);
            Summary summary = new Summary(User.getStorage());
            summary.showMonthlySummary(monthInput);

        } catch (IllegalArgumentException e) {
            printMessage("Invalid month name. Please try again (e.g., summary JAN).");
        } catch (Exception e) {
            printMessage("Error generating summary: " + e.getMessage());
        }
    }
}
