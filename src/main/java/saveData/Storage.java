package saveData;
import transaction.Transaction;
import utils.Budget;
import utils.Category;

import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final List<Transaction> transactions = new ArrayList<>();
    private final List<Budget> budgets = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void deleteTransaction(int index) {
        if (index >= 0 && index < transactions.size()) {
            transactions.remove(index);
        } else {
            System.out.println("Invalid transaction index.");
        }
    }

    public void addBudget(Budget b) {
        budgets.add(b);
    }

    public List<Budget> getBudgets() {
        return budgets;
    }

    public double getBudgetForCategory(Category category) {
        for (Budget b : budgets) {
            if (b.getCategory() == category) {
                return b.getBudget();
            }
        }
        return 0.0;
    }
    public float getBudget(Category category) {
        for (Budget b : budgets) {
            if (b.getCategory().equals(category)) {
                return b.getBudget();  // Budget.amount
            }
        }
        return 0f; // default if no budget set
    }

}
