package utils;

import user.User;

public enum Category {
    FOOD,
    TRANSPORT,
    RECREATION,
    ENTERTAINMENT;

    private Budget budget;

    // call this after all enums are created
    public void initBudget(float amount, Currency currency, Month month) {
        this.budget = new Budget(this, amount, currency, month, User.currBank);
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(float amount) {
        if (budget == null) {
            throw new IllegalStateException("Budget not initialized for " + this);
        }
        budget.setBudget(amount);
    }
    public static Category toCategory(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        str = str.trim().toLowerCase();

        return switch (str) {
        case "food" -> FOOD;
        case "transport" -> TRANSPORT;
        case "recreation" -> RECREATION;
        case "entertainment" -> ENTERTAINMENT;
        default -> throw new IllegalArgumentException("Unknown category: " + str);
        };
    }
}
