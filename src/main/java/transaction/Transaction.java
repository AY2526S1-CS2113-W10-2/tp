package transaction;

import utils.Category;
import utils.Currency;
import utils.Date;

public class Transaction {
    private final float value;
    private final Category category;
    private final Date date;
    private final Currency currency;

    public Transaction(float value, Category category, Date date, Currency currency) {
        this.value = value;
        this.category = category;
        this.date = date;
        this.currency = currency;
    }

    public float getValue() {
        return value;
    }

    public Category getCategory() {
        return category;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return this.currency.getSymbolVerbose() + value +
                " spent on " + category +
                " on " + date.getLongDate();
    }
}
