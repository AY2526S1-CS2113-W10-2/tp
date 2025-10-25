package bank;

import transaction.Transaction;
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
        this.balance = balance;
        this.exchangeRate = exchangeRate;
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

    public ArrayList<Transaction> getTransactions(){
        return transactions;
    }

    public void addTransactionToBank(Transaction transaction){
        this.transactions.add(transaction);
    }

    public void deleteTransactionFromBank(int index) {
        this.transactions.remove(index);
    }

    public void setBalance(float balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        this.balance = balance;
    }

    public void setExchangeRate(float exchangeRate) {
        if (exchangeRate < 0) {
            throw new IllegalArgumentException("Exchange Rate cannot be negative");
        }
        this.exchangeRate = exchangeRate;
    }

    @Override
    public String toString() {
        return "Bank Account " + id +
                " in " + currency.getSymbolVerbose() +
                " with balance " + this.getCurrency().getSymbol() + balance +
                " and exchangeRate " + exchangeRate;
    }
}
