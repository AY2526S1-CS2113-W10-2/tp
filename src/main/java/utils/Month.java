package utils;

public enum Month {
    JAN("January",   "01"),
    FEB("Febuary",   "02"),
    MAR("March",     "03"),
    APR("April",     "04"),
    MAY("May",       "05"),
    JUN("June",      "06"),
    JUL("July",      "07"),
    Aug("August",    "08"),
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

    public String getLongName() {
        return longName;
    }

    public String getNumber() {
        return number;
    }
}
