package storage;

import bank.Bank;
import logger.AppLogger;
import transaction.Transaction;
import ui.FinanceException;
import user.User;
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

//@@author kevinlokey
public class Storage {
    public static final int TRANSACTION_DATA_LENGTH = 8;
    public static final int BUDGET_DATA_LENGTH = 5;
    public static final int BANK_DATA_LENGTH = 4;
    private static final Logger logger = AppLogger.getLogger();
    private static final String TRANSACTION_FILE = "transactions.txt";
    private static final String BUDGET_FILE = "budgets.txt";
    private static final String BANK_FILE = "banks.txt";


    /**
     * Handles all persistent storage operations for the Finance application.
     * <p>
     * This class is responsible for saving and loading {@link Transaction},
     * {@link Budget}, and {@link Bank} objects from text files. Each type of data
     * is stored in its own file (transactions.txt, budgets.txt, banks.txt)
     * using a simple delimiter-separated format.
     * </p>
     *
     * <p>File layout examples:</p>
     * <ul>
     *     <li><b>Transaction:</b> bankId|tag|category|value|day|month|year|currency</li>
     *     <li><b>Budget:</b> bankId|category|month|amount|currency</li>
     *     <li><b>Bank:</b> id|currency|balance|exchangeRate</li>
     * </ul>
     */

    //@@author kevinlokey
    public Storage() {
        logger.log(Level.INFO, "Initialising storage - loading saved data");

        logger.log(Level.INFO, "Storage initialized successfully.");

    }

    /**
     * Saves all transactions from the given list of banks to disk.
     *
     * @param banks The list of {@link Bank} objects whose transactions will be saved.
     */

    //@@author kevinlokey
    public void saveTransactions(ArrayList<Bank> banks) {
        assert banks != null : "Banks list should not be null";

        try (PrintWriter pw = new PrintWriter(new FileWriter(TRANSACTION_FILE))) {
            for (Bank bank : banks) {
                assert bank != null : "Bank should not be null";
                assert bank.getTransactions() != null : "Bank must have a valid transactions list";

                for (Transaction t : bank.getTransactions()) {
                    assert t != null : "Transaction object should not be null";

                    writeTransactions(bank, t, pw);
                }

                logger.log(Level.INFO, "Saved {0} transactions for bank ID {1} to {2}",
                        new Object[]{bank.getTransactions().size(), bank.getId(), TRANSACTION_FILE});
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save transactions", e);
            e.printStackTrace();
        }
    }


    /**
     * Loads all transactions from the transaction file into their corresponding banks.
     * <p>
     * Transactions are matched to banks based on their stored bank ID. If a transaction
     * refers to a non-existent bank or contains invalid data, it is skipped with a warning.
     * </p>
     */

    //@@author kevinlokey
    public void loadTransactions() {
        File file = new File(TRANSACTION_FILE);
        if (transactionFileDoesNotExist(file)) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            logger.info("Loading transactions from file...");

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != TRANSACTION_DATA_LENGTH) {
                    logger.warning("Skipping malformed transaction line: " + line);

                    continue;
                }

                ParseTransactionData data = getParseTransactionData(parts);
                if (data.value() < 0) {
                    logger.log(Level.WARNING, "Skipping invalid transaction with negative value: " + line);
                    continue;
                }

                ParsedTransactionInfo info = getParsedTransactionInfo(parts);

                try {
                    Transaction transaction = new Transaction(data.value(), data.category(), new Date(info.day(),
                            info.month(), info.year()), info.currency(), data.tag());

                    Bank bankToBeLoadedTo = User.getBanks().get(data.bankId());
                    bankToBeLoadedTo.getTransactions().add(transaction);
                } catch (FinanceException | IllegalArgumentException e) {
                    logger.log(Level.WARNING, "Skipping invalid transaction: " + line, e);
                    printMessage("Skipping transaction:" + e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading transactions file", e);
            e.printStackTrace();
        }
    }
    /**
     * Parses transaction metadata (bank ID, tag, category, and value) from a line of text.
     *
     * @param parts The split string array representing a transaction.
     * @return A record containing parsed transaction data.
     */

    //@@author kevinlokewy
    private static ParseTransactionData getParseTransactionData(String[] parts) {
        int bankId = Integer.parseInt(parts[0]);
        String tag = parts[1];
        Category category = Category.toCategory(parts[2]);
        float value = Float.parseFloat(parts[3]);
        ParseTransactionData data = new ParseTransactionData(bankId, tag, category, value);
        return data;
    }

    private record ParseTransactionData(int bankId, String tag, Category category, float value) {
    }

    /**
     * Checks whether the transaction file exists.
     *
     * @param file The file object representing the transaction file.
     * @return {@code true} if the file does not exist; {@code false} otherwise.
     */

    //@@author kevinlokewy
    private static boolean transactionFileDoesNotExist(File file) {
        if (!file.exists()) {
            logger.warning("No transaction file found. Returning null.");

            return true;
        }
        return false;
    }

    /**
     * Parses the remaining transaction information (date and currency) from a line of text.
     *
     * @param parts The split string array representing a transaction.
     * @return A record containing parsed transaction date and currency information.
     */

    //@@author kevinlokewy
    private static ParsedTransactionInfo getParsedTransactionInfo(String[] parts) {
        int day = Integer.parseInt(parts[4]);
        Month month = Month.valueOf(parts[5]);
        int year = Integer.parseInt(parts[6]);
        Currency currency = Currency.valueOf(parts[7]);
        ParsedTransactionInfo info = new ParsedTransactionInfo(day, month, year, currency);
        return info;
    }

    private record ParsedTransactionInfo(int day, Month month, int year, Currency currency) {
    }

    /**
     * Saves all budgets to disk.
     *
     * @param budgets The list of {@link Budget} objects to save.
     */

    //@@author kevinlokey
    public void saveBudgets(ArrayList<Budget> budgets) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(BUDGET_FILE))) {
            for (Budget b : budgets) {
                int bankId = b.getBank() != null ? b.getBank().getId() : -1; // -1 for logged-out/global
                writeBudgets(b, pw, bankId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //@@author kevinlokey
    public ArrayList<Budget> loadBudgets() {
        File file = new File(BUDGET_FILE);
        if (budgetFileDoesNotExist(file)) {
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            logger.info("Loading budgets from file...");
            String line;
            ArrayList<Budget> budgets = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != BUDGET_DATA_LENGTH) {
                    logger.log(Level.WARNING, "Skipping malformed budget line: " + line);
                    continue;
                }

                int bankId = Integer.parseInt(parts[0]);
                Bank bank = bankId == -1 ? null : User.getBanks().stream().filter(b ->
                        b.getId() == bankId).findFirst().orElse(null);

                parseBudgets(parts, budgets, bank);
            }
            return budgets;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Checks whether the budget file exists.
     *
     * @param file The file object representing the budget file.
     * @return {@code true} if the file does not exist; {@code false} otherwise.
     */

    //@@author kevinlokewy
    private static boolean budgetFileDoesNotExist(File file) {
        if (!file.exists()) {
            logger.log(Level.WARNING, "No budget file found. Returning null.");
            return true;
        }
        return false;
    }

    /**
     * Parses and creates a {@link Budget} from the given string array, then adds it to the provided list.
     *
     * @param parts   The split string array representing a budget line.
     * @param budgets The list to add the parsed budget to.
     * @param bank    The associated bank, or {@code null} for global budgets.
     */

    //@@author kevinlokey
    private static void parseBudgets(String[] parts, ArrayList<Budget> budgets, Bank bank) {
        Category category = Category.toCategory(parts[1]);
        Month month = Month.valueOf(parts[2]);
        float amount = Float.parseFloat(parts[3]);
        Currency currency = Currency.valueOf(parts[4]);
        Budget b = new Budget(category, amount, currency, month, bank);
        if (bank != null) {
            bank.addBudgetToBank(b);
        }
        budgets.add(b);  // <- Add to list so loadBudgets() returns it
    }



    //@@author kevinlokey
    public void saveBanks(ArrayList<Bank> banks) {
        assert banks != null : "Banks list should not be null";

        try (PrintWriter pw = new PrintWriter(new FileWriter(BANK_FILE))) {
            for (Bank b : banks) {
                //    System.out.println(b.getBalance());
                writeBanks(b, pw);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //@@author kevinlokey
    public ArrayList<Bank> loadBanks() {
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
                if (parts.length != BANK_DATA_LENGTH) {
                    logger.log(Level.WARNING, "Skipping malformed bank line: " + line);
                    continue;
                }

                parseBanks(parts, banks);
            }
            return banks;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading banks file", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Parses and creates a {@link Bank} from the given string array, then adds it to the provided list.
     *
     * @param parts The split string array representing a bank line.
     * @param banks The list to add the parsed bank to.
     */

    //@@author kevinlokey
    private static void parseBanks(String[] parts, ArrayList<Bank> banks) {
        int id = Integer.parseInt(parts[0]);
        Currency currency = Currency.valueOf(parts[1]);
        float balance = Float.parseFloat(parts[2]);
        float exchangeRate = Float.parseFloat(parts[3]);

        banks.add(new Bank(id, currency, balance, exchangeRate));
    }

    /**
     * Writes a single bank entry to file.
     *
     * @param b  The {@link Bank} to write.
     * @param pw The {@link PrintWriter} used to write the line.
     */

    //@@author kevinlokey
    private static void writeBanks(Bank b, PrintWriter pw) {
        pw.println(b.getId() + "|" +
                b.getCurrency().name() + "|" +
                b.getBalance() + "|" +
                b.getExchangeRate());
    }

    /**
     * Writes a single budget entry to file.
     *
     * @param b      The {@link Budget} to write.
     * @param pw     The {@link PrintWriter} used to write the line.
     * @param bankId The associated bank ID, or -1 for global budgets.
     */

    //@@author kevinlokey
    private static void writeBudgets(Budget b, PrintWriter pw, int bankId) {
        pw.println(bankId + "|" +
                b.getCategory().name() + "|" +
                b.getMonth().name() + "|" +
                b.getBudget() + "|" +
                b.getCurrency().name());
    }

    //@@author kevinlokey
    private static void writeTransactions(Bank bank, Transaction t, PrintWriter pw) {
        pw.println(bank.getId() + "|" +
                t.getTag() + "|" +
                t.getCategory().name() + "|" +
                t.getValue() + "|" +
                t.getDate().getDay() + "|" +
                t.getDate().getMonth().name() + "|" +
                t.getDate().getYear() + "|" +
                t.getCurrency().name());
    }
}
