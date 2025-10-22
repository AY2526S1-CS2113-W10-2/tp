package commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transaction.Transaction;
import ui.FinanceException;
import user.User;
import utils.Category;
import utils.Currency;
import utils.Date;
import utils.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AddTransactionCommandTest {

    @BeforeEach
    public void setUp() {
        User.initialise();

        // Clear previous transactions and banks
        User.getTransactions().clear();
        User.getBanks().clear();

        // Reset budgets if set
        for (Category category : Category.values()) {
            if (category.getBudget() != null) {
                category.getBudget().setBudget(0f);
            }
        }
    }

    @Test
    public void addTransaction_validInput_transactionAddedSuccessfully() throws FinanceException {
        Transaction transaction = new Transaction(
                10.5f,
                Category.FOOD,
                new Date(10, Month.AUG, 2024),
                Currency.SGD
        );

        User.addTransaction(transaction);

        // Assert the transaction was added
        assertEquals(1, User.getTransactions().size());

        Transaction storedTransaction = User.getTransactions().get(0);
        assertEquals(Category.FOOD, storedTransaction.getCategory());
        assertEquals(10.5f, storedTransaction.getValue());
    }

    @Test
    public void addTransaction_negativeValue_throwsException() {
        assertThrows(FinanceException.class, () -> {
            new Transaction(
                    -5.0f,
                    Category.FOOD,
                    new Date(1, Month.JAN, 2025),
                    Currency.SGD
            );
        });

    }

    @Test
    public void addTransaction_invalidCategory_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Category.toCategory("invalidCategory");
        });
    }

    @Test
    public void addTransaction_nullDate_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(
                    10.0f,
                    Category.FOOD,
                    null,
                    Currency.SGD
            );
        });
    }
}
