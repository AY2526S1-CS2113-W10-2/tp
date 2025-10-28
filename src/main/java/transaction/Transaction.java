package transaction;

import ui.FinanceException;
import utils.Category;
import utils.Currency;
import utils.Date;

public class Transaction {
    private final float value;
    private final Category category;
    private final Date date;
    private final Currency currency;
    private final String tag;

    public Transaction(float value, Category category, Date date, Currency currency, String tag) throws FinanceException {
        if (value < 0) {
            throw new FinanceException("Value must be positive");
        }
        requireNonNull(category, "Category cannot be null");
        requireNonNull(date, "Date cannot be null");
        requireNonNull(currency, "Currency cannot be null");
        this.value = value;
        this.category = category;
        this.date = date;
        this.currency = currency;
        this.tag = tag;
    }

    private static void requireNonNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public String getTag() { return tag; }

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
                " | " + this.tag + "(" + category + ")" +
                " | " + date.getLongDate();
    }
}
