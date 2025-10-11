package utils;

public class Date {
    final int day;
    final Month month;
    final int year;

    public Date(int day, Month month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public Month getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getShortDate() {
        String dayPrint = (day < 10) ? "0" + day : day + "";
        return day + "/" + month.getNumber() + "/" + year;
    }

    public String getLongDate() {
        return day + "th of " + month.getLongName() + ", " + year;      // todo: fix for 1st, 2nd, etc.
    }

    public static Date toDate(String str){
        // todo: parse String to date object
        return null;
    }
}
