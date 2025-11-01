package utils;

public enum Currency {
    MYR("Malaysian Ringgit", "RM", false),
    VND("Vietnamese Dong", "d", false),
    JPY("Japanese Yen", "¥", false),
    IDR("Indonesian Rupiah", "Rp", false),
    SGD("Singapore Dollar", "$",false),
    THB("Thai Baht", "฿", false);


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
        case THB: return 0.04f;          // example rate: 1 THB = 0.04 SGD
        case JPY: return 0.0085f;       // 1 JPY = 0.0085 SGD
        case VND: return 0.000049f;    // 1 VND = 0.000049 SGD
        case IDR: return 0.000078f;   // 1 IDR = 0.000078 SGD
        case MYR: return 0.31f;      // 1 MYR = 0.31 SGD
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
        case "MYR", "myr" -> MYR;
        case "THB", "thb" -> THB;
        case "JPY", "jpy" -> JPY;
        case "IDR", "idr" -> IDR;
        case "VND", "vnd" -> VND;
        case "SGD", "sgd" -> SGD;
        default -> null;
        };
    }
}
