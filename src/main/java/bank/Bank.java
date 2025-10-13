package bank;

import utils.Currency;

public class Bank {
    private final int id;
    private final Currency currency;        // The currency this bank trades in
    private float balance;                  // This user's balance in this bank account
    private float exchangeRate;             // The current exchange rate from this currency to USD

    public Bank(int id, Currency currency, float balance, float exchangeRate) {
        this.id = id;
        this.currency = currency;
        this.balance = balance;
        this.exchangeRate = exchangeRate;
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

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setExchangeRate(float exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public String toString() {
        return "Bank Account " + id +
                " in " + currency.getSymbolVerbose() +
                " with balance " + balance +
                " and exchangeRate " + exchangeRate;
    }
}
