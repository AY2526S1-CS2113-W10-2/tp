package saveData;

import bank.Bank;
import transaction.Transaction;
import ui.FinanceException;
import utils.Budget;
import utils.Category;
import utils.Currency;
import utils.Date;
import utils.Month;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ui.OutputManager.printMessage;

public class Storage {
    private static final Logger logger = Logger.getLogger(Storage.class.getName());

    private static final String TRANSACTION_FILE = "transactions.txt";
    private static final String BUDGET_FILE = "budgets.txt";
    private static final String BANK_FILE = "banks.txt";

    /*private final List<Transaction> transactions = new ArrayList<>();
    private final List<Budget> budgets = new ArrayList<>();
    private final List<Bank> banks = new ArrayList<>();*/


    public Storage() {
        logger.log(Level.INFO, "Initialising storage - loading saved data");
        loadTransactions();
        loadBudgets();
        loadBanks();
        logger.log(Level.INFO, "Storage initialized successfully.");

    }

    // -------------------- Transactions --------------------
    /*public void addTransaction(Transaction t) {
        transactions.add(t);
        saveTransactions();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void deleteTransaction(int index) {
        if (index >= 0 && index < transactions.size()) {
            transactions.remove(index);
            saveTransactions();
        } else {
            System.out.println("Invalid transaction index.");
        }
    }*/

    public void saveTransactions(ArrayList<Transaction> transactions) {
        assert transactions != null : "Transactions list should not be null";

        try (PrintWriter pw = new PrintWriter(new FileWriter(TRANSACTION_FILE))) {
            for (Transaction t : transactions) {
                assert t != null : "Transaction object should not be null";

                pw.println(t.getCategory().name() + "|" +
                        t.getValue() + "|" +
                        t.getDate().getDay() + "|" +
                        t.getDate().getMonth().name() + "|" +
                        t.getDate().getYear() + "|" +
                        t.getCurrency().name());
            }
            logger.log(Level.INFO, "Saved {0} transactions to {1}", new Object[]{transactions.size(), TRANSACTION_FILE});

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save transactions", e);
            e.printStackTrace();
        }
    }

    public ArrayList<Transaction> loadTransactions() {
        //transactions.clear();
        File file = new File(TRANSACTION_FILE);
        if (!file.exists()) {
            logger.warning("No transaction file found. Returning null.");

            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            logger.info("Loading transactions from file...");

            String line;
            ArrayList<Transaction> transactions = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 6) {
                    logger.warning("Skipping malformed transaction line: " + line);

                    continue;
                }

                Category category = Category.toCategory(parts[0]);
                float value = Float.parseFloat(parts[1]);
                if (value < 0) {
                    logger.log(Level.WARNING, "Skipping invalid transaction with negative value: " + line);
                    continue;
                }

                int day = Integer.parseInt(parts[2]);
                Month month = Month.valueOf(parts[3]);
                int year = Integer.parseInt(parts[4]);
                Currency currency = Currency.valueOf(parts[5]);

                try {
                    transactions.add(new Transaction(value, category, new Date(day, month, year), currency));
                } catch (FinanceException | IllegalArgumentException e) {
                    logger.log(Level.WARNING, "Skipping invalid transaction: " + line, e);
                    printMessage("Skipping transaction:" + e.getMessage());
                }
            }
            return transactions;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading transactions file", e);
            e.printStackTrace();
        }
        return null;
    }

    // -------------------- Budgets --------------------
    /*public void addBudget(Budget b) {
        // If a budget already exists for the same category and month, replace it
        budgets.removeIf(existing ->
                existing.getCategory() == b.getCategory() &&
                        existing.getMonth() == b.getMonth()
        );
        budgets.add(b);
        saveBudgets();
    }

    public float getBudgetAmount(Category category, Month month) {
        for (Budget b : budgets) {
            if (b.getCategory() == category && b.getMonth() == month) {
                return b.getBudget();
            }
        }
        return 0f;
    }

    public Currency getBudgetCurrency(Category category, Month month) {
        for (Budget b : budgets) {
            if (b.getCategory() == category && b.getMonth() == month) {
                return b.getCurrency();
            }
        }
        return null;
    }*/

    public void saveBudgets(ArrayList<Budget> budgets) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(BUDGET_FILE))) {
            for (Budget b : budgets) {
                pw.println(b.getCategory().name() + "|" +
                        b.getMonth().name() + "|" +
                        b.getBudget() + "|" +
                        b.getCurrency().name());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Budget> loadBudgets() {
        //budgets.clear();
        File file = new File(BUDGET_FILE);
        if (!file.exists()) {
            logger.log(Level.WARNING, "No budget file found. Returning null.");
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            logger.info("Loading budgets from file...");

            String line;
            ArrayList<Budget> budgets = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 4) {
                    logger.log(Level.WARNING, "Skipping malformed budget line: " + line);
                    continue;
                }

                Category category = Category.toCategory(parts[0]);
                Month month = Month.valueOf(parts[1]);
                float amount = Float.parseFloat(parts[2]);
                Currency currency = Currency.valueOf(parts[3]);

                budgets.add(new Budget(category, amount, currency, month));
            }
            return budgets;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // -------------------- Banks --------------------
    /*public void addBank(Bank b) {
        banks.add(b);
        saveBanks();
    }

    public List<Bank> getBanks() {
        return banks;
    }*/

    public void saveBanks(ArrayList<Bank> banks) {
        assert banks != null : "Banks list should not be null";

        try (PrintWriter pw = new PrintWriter(new FileWriter(BANK_FILE))) {
            for (Bank b : banks) {
                pw.println(b.getId() + "|" +
                        b.getCurrency().name() + "|" +
                        b.getBalance() + "|" +
                        b.getExchangeRate());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Bank> loadBanks() {
        //banks.clear();
        File file = new File(BANK_FILE);
        if (!file.exists()) {
            logger.info("No bank file found. Returning null.");
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            logger.info("Loading banks from file...");

            String line;
            ArrayList<Bank> banks = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 4) {
                    logger.log(Level.WARNING, "Skipping malformed bank line: " + line);
                    continue;
                }

                int id = Integer.parseInt(parts[0]);
                Currency currency = Currency.valueOf(parts[1]);
                float balance = Float.parseFloat(parts[2]);
                float exchangeRate = Float.parseFloat(parts[3]);

                banks.add(new Bank(id, currency, balance, exchangeRate));
            }
            return banks;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading banks file", e);
            e.printStackTrace();
        }
        return null;
    }
}
