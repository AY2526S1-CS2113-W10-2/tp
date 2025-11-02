package commands;

import bank.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transaction.Transaction;
import ui.FinanceException;
import user.User;
import utils.Category;
import utils.Currency;
import utils.Date;
import utils.Month;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilterCommandTest {
    private static final Path TX_FILE = Path.of("transactions.txt");
    private Bank bank0;

    @BeforeEach
    public void setUp() throws IOException, FinanceException {
        Files.deleteIfExists(TX_FILE);
        User.initialise();
        User.getBanks().clear();
        User.getBudgets().clear();

        // Set up a test bank
        bank0 = new Bank(0, Currency.SGD, 1000f, 1.0f);
        User.addBank(bank0);
        User.setCurrBank(bank0);
        User.setIsLoggedIn(true);

        // Add test transactions
        Transaction t1 = new Transaction(
                10.0f,
                Category.FOOD,
                new Date(1, Month.JAN, 2025),
                Currency.SGD,
                "Milo");
        Transaction t2 = new Transaction(
                25.0f,
                Category.ENTERTAINMENT,
                new Date(2, Month.JAN, 2025),
                Currency.SGD,
                "Game");
        Transaction t3 = new Transaction(
                5.0f,
                Category.FOOD,
                new Date(5, Month.JAN, 2025),
                Currency.SGD,
                "Bread");
        bank0.addTransactionToBank(t1);
        bank0.addTransactionToBank(t2);
        bank0.addTransactionToBank(t3);
    }

    @Test
    void filterByCategory_food_returnsOnlyFoodTransactions() throws FinanceException {
        FilterCommand cmd = new FilterCommand(new ArrayList<>(List.of("category", "FOOD")));
        cmd.execute();

        // Only FOOD transactions
        long foodCount = bank0.getTransactions().stream()
                .filter(t -> t.getCategory() == Category.FOOD)
                .count();

        assertEquals(2, foodCount);
    }

    @Test
    void filterCategory_caseInsensitive_returnsMatchingTransactions() throws FinanceException {
        FilterCommand cmd = new FilterCommand(new ArrayList<>(List.of("category", "food")));
        cmd.execute();

        long count = bank0.getTransactions().stream()
                .filter(t -> t.getCategory() == Category.FOOD)
                .count();

        assertEquals(2, count);
    }

    @Test
    void filterByCost_fiveToFifteen_returnsMatchingTransactions() throws FinanceException {
        FilterCommand cmd = new FilterCommand(new ArrayList<>(List.of("cost", "5", "15")));
        cmd.execute();

        long count = bank0.getTransactions().stream()
                .filter(t -> t.getValue() >= 5 && t.getValue() <= 15)
                .count();

        assertEquals(2, count);
    }

    @Test
    void filterByDate_jan1ToJan2_returnsMatchingTransactions() throws FinanceException {
        FilterCommand cmd = new FilterCommand(new ArrayList<>(List.of("date", "01/01", "02/01")));
        cmd.execute();

        long count = bank0.getTransactions().stream()
                .filter(t -> !t.getDate().isBefore(new Date(1, Month.JAN, 2025)) &&
                        !t.getDate().isAfter(new Date(2, Month.JAN, 2025)))
                .count();

        assertEquals(2, count); // t1=Jan1, t2=Jan2
    }

    @Test
    void filterByCategory_invalidCategory_throwsFinanceException() {
        assertThrows(FinanceException.class, () ->
                new FilterCommand(new ArrayList<>(List.of("category", "INVALID"))).execute()
        );
    }

    @Test
    void filterByCost_negativeValues_throwsFinanceException() {
        assertThrows(FinanceException.class, () ->
                new FilterCommand(new ArrayList<>(List.of("cost", "-5", "10"))).execute()
        );
    }
    @Test
    void filterByCost_maxLessThanMin_throwsFinanceException() {
        assertThrows(FinanceException.class, () ->
                new FilterCommand(new ArrayList<>(List.of("cost", "10", "5"))).execute()
        );
    }

    @Test
    void filterByCost_nonNumeric_throwsFinanceException() {
        assertThrows(FinanceException.class, () ->
                new FilterCommand(new ArrayList<>(List.of("cost", "abc", "10"))).execute()
        );

        assertThrows(FinanceException.class, () ->
                new FilterCommand(new ArrayList<>(List.of("cost", "5", "xyz"))).execute()
        );
    }

    @Test
    void filterByCost_tooManyDecimals_throwsFinanceException() {
        assertThrows(FinanceException.class, () ->
                new FilterCommand(new ArrayList<>(List.of("cost", "5.123", "10.00"))).execute()
        );

        assertThrows(FinanceException.class, () ->
                new FilterCommand(new ArrayList<>(List.of("cost", "5.00", "10.999"))).execute()
        );
    }

    @Test
    void filterByCost_noTransactions_returnsEmptyList() throws FinanceException {
        bank0.getTransactions().clear();

        FilterCommand cmd = new FilterCommand(new ArrayList<>(List.of("cost", "0", "100")));
        cmd.execute();

        assertEquals(0, bank0.getTransactions().size());
    }

    @Test
    void filterByDate_invalidDateFormat_throwsFinanceException() {
        assertThrows(FinanceException.class, () ->
                new FilterCommand(new ArrayList<>(List.of("date", "01-01", "02-01"))).execute()
        );
    }
}
