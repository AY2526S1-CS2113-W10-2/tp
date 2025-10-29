package utils;

//@@author Mack34021
public enum Category {
    FOOD,
    TRANSPORT,
    RECREATION,
    ENTERTAINMENT;

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
