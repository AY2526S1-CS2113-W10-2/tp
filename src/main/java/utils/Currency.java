package utils;

public enum Currency {
    USD("United States Dollar", "$"),
    EUR("European Euro", "€"),
    JPY("Japanese Yen", "¥"),
    GBP("British Pound", "£"),
    CNY("Chinese Yuan", "¥");

    private final String longName;
    private final String symbol;

    Currency(String longName, String symbol) {
        this.longName = longName;
        this.symbol = symbol;
    }

    public String getLongName() {
        return longName;
    }

    public String getSymbol() {
        return symbol;
    }
}