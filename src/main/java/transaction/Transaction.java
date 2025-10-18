package transaction;

import ui.FinanceExceptions;
import utils.Category;
import utils.Currency;
import utils.Date;

public class Transaction {
    private final float value;
    private final Category category;
    private final Date date;
    private final Currency currency;

    public Transaction(float value, Category category, Date date, Currency currency) throws FinanceExceptions {
        if (value < 0) {
            throw new FinanceExceptions("Value must be positive");
        }
        requireNonNull(category, "Category cannot be null");
        requireNonNull(date, "Date cannot be null");
        requireNonNull(currency, "Currency cannot be null");
        this.value = value;
        this.category = category;
        this.date = date;
        this.currency = currency;
    }

    private static void requireNonNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public float getValue() {
        return value;
    }

    public Category getCategory() {
        return category;
    }

    public Currency getCurrency(){
        return currency;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return this.currency.getSymbolSemiVerbose() + value +
                " spent on " + category +
                " on " + date.getLongDate();
    }
}
