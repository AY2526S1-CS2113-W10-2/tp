package utils;

public class Budget {
    Category category;
    public float balance = 0.0F;
    float initialAmount = 0.0F;

    /**
     * Gets the amount remaining in the user's budget for this category
     *
     * @return remaining space in budget
     */
    public float getRemainingAmount(){
        return 0;
    }
    public float getBudget(){
        return initialAmount;
    }

    public Category getCategory(){
        return category;
    }
}
