package summary;

import saveData.Storage;
import transaction.Transaction;
import utils.Category;
import ui.OutputManager;
import utils.Month;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Summary {
    private final Storage storage;

    public Summary(Storage storage) {
        this.storage = storage;
    }

    public void showMonthlySummary(String month) {
        List<Transaction> monthlyTransactions = storage.getTransactions().stream()
                .filter(t -> t.getDate().getMonth() == Month.valueOf(month.toUpperCase()))
                .collect(Collectors.toList());

        Map<Category, Float> spendingByCategory = new HashMap<>();
        for (Transaction t : monthlyTransactions) {
            spendingByCategory.put(
                    t.getCategory(),
                    spendingByCategory.getOrDefault(t.getCategory(), 0f) + t.getValue()
            );
        }

        Map<Category, Float> budgetByCategory = new HashMap<>();
        for (Category cat : spendingByCategory.keySet()) {
            budgetByCategory.put(cat, storage.getBudget(cat));
        }

        String summaryOutput = OutputManager.printSummary(
                month,
                monthlyTransactions,
                spendingByCategory,
                budgetByCategory
        );

        OutputManager.printMessage(summaryOutput);
    }
}
