package utils;

public enum Category {
    FOOD(new Budget()),
    TRANSPORT(new Budget()),
    RECREATION(new Budget());

    private Budget budget;

    private Category(Budget budget) {
        this.budget = budget;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(float budget) {
        this.budget.initialAmount = budget;
        this.budget.balance = budget;
    }

    public static Category toCategory(String str) {
        return switch (str) {
            case "f", "food" -> FOOD;
            case "t", "transport" -> TRANSPORT;
            case "r", "recreation" -> RECREATION;
            default -> null;
        };
    }
}
