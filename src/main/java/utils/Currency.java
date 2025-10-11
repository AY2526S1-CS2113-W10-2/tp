package utils;

public enum Currency {
    USD("United States Dollar", "$", false),
    EUR("European Euro", "€", false),
    JPY("Japanese Yen", "¥", true),
    GBP("British Pound", "£", false),
    CNY("Chinese Yuan", "¥", true);

    private final String longName;
    private final String symbol;
    // Whether there exists another currency using this same symbol that may cause confusion
    private final boolean duplicateSymbol;

    Currency(String longName, String symbol, boolean duplicateSymbol) {
        this.longName = longName;
        this.symbol = symbol;
        this.duplicateSymbol = duplicateSymbol;
    }

    public String getLongName() {
        return longName;
    }

    public String getSymbol() {
        return symbol;
    }

    /**
     * Gets the symbol for this currency. For currencies that have the same symbol (e.g. JPY, CNY),
     * add the prefix to clarify.
     *
     * @return string of the currency symbol
     */
    public String getSymbolVerbose() {
        if(this.duplicateSymbol){
            return this + this.symbol;
        }else{
            return symbol;
        }

    }

    public boolean getDuplicateSymbol() {
        return duplicateSymbol;
    }

    public static Currency toCurrency(String str){
        return switch (str) {
            case "USD", "usd" -> USD;
            case "EUR", "eur" -> EUR;
            case "JPY", "jpy" -> JPY;
            case "GBP", "gbp" -> GBP;
            case "CNY", "cny" -> CNY;
            default -> null;
        };
    }
}
