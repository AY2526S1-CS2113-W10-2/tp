package utils;

public enum Category {
    FOOD,
    TRANSPORT,
    RECREATION;

    private Budget budget;

    // call this after all enums are created
    public void initBudget(float amount, Currency currency, Month month) {
        this.budget = new Budget(this, amount, currency, month);
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
        if (str == null) throw new IllegalArgumentException("Category cannot be null");
        str = str.trim().toLowerCase();

        return switch (str) {
            case "food" -> FOOD;
            case "transport" -> TRANSPORT;
            case "recreation" -> RECREATION;
            default -> throw new IllegalArgumentException("Unknown category: " + str);
        };
    }
}
