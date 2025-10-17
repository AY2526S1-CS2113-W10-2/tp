package saveData;

import bank.Bank;
import transaction.Transaction;
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
import java.util.List;

public class Storage {
    private static final String TRANSACTION_FILE = "transactions.txt";
    private static final String BUDGET_FILE = "budgets.txt";
    private static final String BANK_FILE = "banks.txt";

    /*private final List<Transaction> transactions = new ArrayList<>();
    private final List<Budget> budgets = new ArrayList<>();
    private final List<Bank> banks = new ArrayList<>();*/


    public Storage() {
        loadTransactions();
        loadBudgets();
        loadBanks();
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
        try (PrintWriter pw = new PrintWriter(new FileWriter(TRANSACTION_FILE))) {
            for (Transaction t : transactions) {
                pw.println(t.getCategory().name() + "|" +
                        t.getValue() + "|" +
                        t.getDate().getDay() + "|" +
                        t.getDate().getMonth().name() + "|" +
                        t.getDate().getYear() + "|" +
                        t.getCurrency().name());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Transaction> loadTransactions() {
        //transactions.clear();
        File file = new File(TRANSACTION_FILE);
        if (!file.exists()) {
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            ArrayList<Transaction> transactions = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 6) {
                    continue;
                }

                Category category = Category.toCategory(parts[0]);
                float value = Float.parseFloat(parts[1]);
                if (value < 0) {
                    continue;
                }

                int day = Integer.parseInt(parts[2]);
                Month month = Month.valueOf(parts[3]);
                int year = Integer.parseInt(parts[4]);
                Currency currency = Currency.valueOf(parts[5]);

                transactions.add(new Transaction(value, category, new Date(day, month, year), currency));
            }
            return transactions;
        } catch (IOException e) {
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
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            ArrayList<Budget> budgets = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 4) {
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
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            ArrayList<Bank> banks = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 4) {
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
            e.printStackTrace();
        }
        return null;
    }
}
