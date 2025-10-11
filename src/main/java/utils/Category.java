package utils;

public enum Category {
    FOOD, TRANSPORT, RECREATION;

    public static Category toCategory(String str){
        return switch (str) {
        case "f", "food" -> FOOD;
        case "t", "transport" -> TRANSPORT;
        case "r", "recreation" -> RECREATION;
        default -> null;
        };
    }
}
