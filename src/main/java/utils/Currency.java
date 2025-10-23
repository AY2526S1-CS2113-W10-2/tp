package utils;

public enum Currency {
    USD("United States Dollar", "$", false),
    EUR("European Euro", "€", false),
    JPY("Japanese Yen", "¥", true),
    GBP("British Pound", "£", false),
    SGD("Singapore Dollar", "$",true),
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

    public static float getExchangeRateToSGD(Currency c) {
        switch (c) {
        case SGD: return 1.0f;
        case USD: return 1.29f;   // example rate: 1 USD = 1.29 SGD
        case JPY: return 0.0085f; // 1 JPY = 0.0085 SGD
        case EUR: return 1.50f;   // 1 EUR = 1.50 SGD
        default: return 1.0f;
        }
    }


    /**
     * Gets the symbol for this currency. For currencies that have the same symbol (e.g. JPY, CNY),
     * add the prefix to clarify.
     *
     * @return string of the currency symbol
     */
    public String getSymbolSemiVerbose() {
        if(this.duplicateSymbol){
            return this + this.symbol;
        }else{
            return symbol;
        }
    }

    /**
     * Gets the symbol for this currency. For all currencies, add the prefix to clarify.
     *
     * @return string of the currency symbol
     */
    public String getSymbolVerbose() {
        return this + this.symbol;
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
        case "SGD", "sgd" -> SGD;
        default -> null;
        };
    }
}
