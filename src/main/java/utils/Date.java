package utils;

public class Date {
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
        try {
            String[] parts = str.split("/");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid date format. Expected DD/MM/YYYY");
            }
            int day = Integer.parseInt(parts[0]);
            int monthNum = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            if (year < 1000 || year > 9999) {
                throw new IllegalArgumentException("Year must be a 4-digit number (YYYY)");
            }
            Month month = Month.fromNumber(monthNum);
            return new Date(day, month, year);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date: " + str + " Expected DD/MM/YYYY");
        }
    }
}
