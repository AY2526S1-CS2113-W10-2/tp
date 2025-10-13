package utils;

public enum Month {
    JAN("January",   "01"),
    FEB("February",  "02"),
    MAR("March",     "03"),
    APR("April",     "04"),
    MAY("May",       "05"),
    JUN("June",      "06"),
    JUL("July",      "07"),
    AUG("August",    "08"),
    SEP("September", "09"),
    OCT("October",   "10"),
    NOV("November",  "11"),
    DEC("December",  "12");

    private final String longName;
    private final String number;

    Month(String longName, String number) {
        this.longName = longName;
        this.number = number;
    }

    public String getLongName() { return longName; }
    public String getNumber() { return number; }

    // Helper to parse input like "JAN" or "January"
    public static Month fromString(String input) {
        input = input.toUpperCase();
        for (Month m : values()) {
            if (m.name().equals(input) || m.longName.toUpperCase().equals(input)) {
                return m;
            }
        }
        throw new IllegalArgumentException("Invalid month: " + input);
    }

    public static Month fromNumber(String number) {
        for (Month month : Month.values()) {
            if (month.number.equals(number)) {
                return month;
            }
        }
        return null; // or throw an exception
    }
}
