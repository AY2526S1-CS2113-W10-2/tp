package bank;

import transaction.Transaction;
import ui.FinanceException;
import utils.Currency;

import java.util.ArrayList;

public class Bank {
    private final int id;
    private final Currency currency;        // The currency this bank trades in
    private float balance;                  // This user's balance in this bank account
    private float exchangeRate;             // The current exchange rate from this currency to USD
    private ArrayList<Transaction> transactions;

    public Bank(int id, Currency currency, float balance, float exchangeRate) {
        this.id = id;
        this.currency = currency;
        this.setBalance(balance);
        this.setExchangeRate(exchangeRate);
        this.transactions = new ArrayList<>();
    }

    public Currency getCurrency() {
        return currency;
    }

    public int getId() {
        return id;
    }

    public float getBalance() {
        return balance;
    }

    public float getExchangeRate() {
        return exchangeRate;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransactionToBank(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public Transaction deleteTransactionFromBank(int index) {
        if (index < 0 || index >= transactions.size()) {
            throw new IllegalArgumentException("Transaction index out of range");
        }

        Transaction removed = transactions.remove(index);

        // Restore balance
        float newBalance = this.balance + removed.getValue();
        this.setBalance(newBalance);

        return removed;
    }

    public void setBalance(float balance) {
        System.out.println("Setting new balance of " + balance);
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative.");
        }
        if (balance > Float.MAX_VALUE){
            this.balance = Float.MAX_VALUE;
        }else{
            this.balance = balance;
        }
    }

    public void setExchangeRate(float exchangeRate) {
        if (exchangeRate < 0) {
            throw new IllegalArgumentException("Exchange Rate cannot be negative");
        }
        this.exchangeRate = exchangeRate;
    }

    @Override
    public String toString() {
        return String.format(
                "Bank Account %d in %s with balance %s%.2f and exchangeRate %.6f",
                id,
                currency,
                currency.getSymbol(),
                balance,
                exchangeRate
        );
    }
}
