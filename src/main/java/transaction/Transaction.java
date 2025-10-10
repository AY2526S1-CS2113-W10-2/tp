package transaction;

import utils.Category;
import utils.Date;

public class Transaction {
    private final float value;
    private final Category category;
    private final Date date;

    public Transaction(float value, Category category, Date date) {
        this.value = value;
        this.category = category;
        this.date = date;
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
}
