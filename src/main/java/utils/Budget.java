package utils;

import bank.Bank;

public class Budget {
    private final Category category;
    private float balance = 0.0F;
    private float initialAmount = 0.0F;
    private final Currency currency;
    private final Month month;
    private final Bank bank;

    public Budget(Category category, float initialAmount, Currency currency, Month month, Bank bank) {
        this.category = category;
        this.initialAmount = initialAmount;
        this.balance = initialAmount; // initialize balance
        this.currency = currency;
        this.month = month;
        this.bank = bank;
    }

    /**
     * Gets the amount remaining in the user's budget for this category
     *
     * @return remaining space in budget
     */
    public float getRemainingAmount() {
        return balance;
    }

    public float getBudget() {
        return initialAmount;
    }

    public Month getMonth() {
        return month;
    }

    public Bank getBank() {
        return bank;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Category getCategory() {
        return category;
    }

    public void setBudget(float amount) {
        this.initialAmount = amount;
        this.balance = amount;
    }
}
