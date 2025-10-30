package storage;

import bank.Bank;
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

public class Storage {
    private static final Logger logger = Logger.getLogger(Storage.class.getName());

    private static final String TRANSACTION_FILE = "transactions.txt";
    private static final String BUDGET_FILE = "budgets.txt";
    private static final String BANK_FILE = "banks.txt";


    public Storage() {
        logger.log(Level.INFO, "Initialising storage - loading saved data");

        logger.log(Level.INFO, "Storage initialized successfully.");

    }

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


    public void loadTransactions() {
        File file = new File(TRANSACTION_FILE);
        if (!file.exists()) {
            logger.warning("No transaction file found. Returning null.");

            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            logger.info("Loading transactions from file...");

            String line;
            ArrayList<Transaction> transactions = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 8) {
                    logger.warning("Skipping malformed transaction line: " + line);

                    continue;
                }

                int bankId = Integer.parseInt(parts[0]);
                String tag = parts[1];
                Category category = Category.toCategory(parts[2]);
                float value = Float.parseFloat(parts[3]);
                if (value < 0) {
                    logger.log(Level.WARNING, "Skipping invalid transaction with negative value: " + line);
                    continue;
                }

                ParsedTransactionInfo info = getParsedTransactionInfo(parts);

                try {
                    Transaction transaction = new Transaction(value, category, new Date(info.day(),
                            info.month(), info.year()), info.currency(), tag);

                    Bank bankToBeLoadedTo = User.getBanks().get(bankId);
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


    public ArrayList<Budget> loadBudgets() {
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
                if (parts.length != 5) {
                    logger.log(Level.WARNING, "Skipping malformed budget line: " + line);
                    continue;
                }

                int bankId = Integer.parseInt(parts[0]);
                Bank bank = bankId == -1 ? null : User.getBanks().stream().filter(
                        b -> b.getId() == bankId).findFirst().orElse(null);

                parseBudgets(parts, budgets, bank);
            }
            return budgets;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void parseBudgets(String[] parts, ArrayList<Budget> budgets, Bank bank) {
        Category category = Category.toCategory(parts[1]);
        Month month = Month.valueOf(parts[2]);
        float amount = Float.parseFloat(parts[3]);
        Currency currency = Currency.valueOf(parts[4]);

        budgets.add(new Budget(category, amount, currency, month, bank));
    }


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
                if (parts.length != 4) {
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

    private static void parseBanks(String[] parts, ArrayList<Bank> banks) {
        int id = Integer.parseInt(parts[0]);
        Currency currency = Currency.valueOf(parts[1]);
        float balance = Float.parseFloat(parts[2]);
        float exchangeRate = Float.parseFloat(parts[3]);

        banks.add(new Bank(id, currency, balance, exchangeRate));
    }

    private static void writeBanks(Bank b, PrintWriter pw) {
        pw.println(b.getId() + "|" +
                b.getCurrency().name() + "|" +
                b.getBalance() + "|" +
                b.getExchangeRate());
    }
    private static void writeBudgets(Budget b, PrintWriter pw, int bankId) {
        pw.println(bankId + "|" +
                b.getCategory().name() + "|" +
                b.getMonth().name() + "|" +
                b.getBudget() + "|" +
                b.getCurrency().name());
    }
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
