package utils;

public class Date {
    private static final int MAX_PARTS_LENGTH = 2;
    //  private static final int MIN_YEAR = 1900;
    // private static final int MAX_YEAR = 2100;
    final int day;
    final Month month;
    final int year;

    public Date(int day, Month month, int year) {
        if (month == null) {
            throw new IllegalArgumentException("Month cannot be null");
        }
        if (year <= 0) {
            throw new IllegalArgumentException("Year must be positive");
        }

        int maxDay = getDaysInMonth(month, year);
        if (day < 1 || day > maxDay) {
            throw new IllegalArgumentException("Invalid day " + day + " for " + month + " " + year);
        }

        this.day = day;
        this.month = month;
        this.year = year;
    }

    private static int getDaysInMonth(Month month, int year) {
        return switch (month) {
        case JAN, MAR, MAY, JUL, AUG, OCT, DEC -> 31;
        case APR, JUN, SEP, NOV -> 30;
        case FEB -> isLeapYear(year) ? 29 : 28;
        default -> throw new IllegalArgumentException("Unknown month: " + month);
        };
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
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
        String dayStr = String.format("%02d", day);
        String monthStr = month.getNumber();
        return dayStr + "/" + monthStr + "/" + year;
    }


    public String getLongDate() {
        return day + getDaySuffix(day) + " of " + month.getLongName() + ", " + year;
    }

    public static String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        return switch (day % 10) {
        case 1 -> "st";
        case 2 -> "nd";
        case 3 -> "rd";
        default -> "th";
        };
    }


    public static Date toDate(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Date string cannot be empty");
        }

        String[] parts = str.split("/");
        if (parts.length != MAX_PARTS_LENGTH) {
            throw new IllegalArgumentException("Invalid date format. Expected DD/MM");
        }

        try {
            int day = Integer.parseInt(parts[0]);
            int monthNum = Integer.parseInt(parts[1]);
            int year = java.time.LocalDate.now().getYear();

            /*   if (year < MIN_YEAR || year > MAX_YEAR) {
                throw new IllegalArgumentException("Year must be a 4-digit number (YYYY) " +
                        "between " + MIN_YEAR + " and " + MAX_YEAR);
            } */

            Month month = Month.fromNumber(monthNum);
            return new Date(day, month, year);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Date must contain only numbers: " + str);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date: " + str + " " + e.getMessage());
        }
    }

    public boolean isBefore(Date other) {
        if (this.year < other.year) {
            return true;
        }
        if (this.year > other.year) {
            return false;
        }
        if (this.month.ordinal() < other.month.ordinal()) {
            return true;
        }
        if (this.month.ordinal() > other.month.ordinal()) {
            return false;
        }
        return this.day < other.day;
    }

    public boolean isAfter(Date other) {
        if (this.year > other.year) {
            return true;
        }
        if (this.year < other.year) {
            return false;
        }
        if (this.month.ordinal() > other.month.ordinal()) {
            return true;
        }
        if (this.month.ordinal() < other.month.ordinal()) {
            return false;
        }
        return this.day > other.day;
    }
}
