import java.util.Date;

public enum Month {
    JANUARY(0,0),
    FEBRUARY(1,31),
    MARCH(2,59),
    APRIL(3,90),
    MAY(4,120),
    JUNE(5,151),
    JULY(6,181),
    AUGUST(7,212),
    SEPTEMBER(8,243),
    OCTOBER(9,273),
    NOVEMBER(10,304),
    DECEMBER(11,334);

    private int month;
    private int startingDay;

    Month(int month, int startingDay) {

        this.month = month;
        this.startingDay = startingDay;
    }

    public static Month getMonthInstance(int monthIndex) {
        if (monthIndex > DECEMBER.getStartingDay() || monthIndex < JANUARY.getStartingDay()) {
            throw new IllegalArgumentException("Unexpected month index received, the valid interval is [0,11]");
        }
        return values()[monthIndex];
    }

    public static int dayOfYear(Date arg) {
        int result = getMonthInstance(arg.getMonth()).getStartingDay();
        result += arg.getDate();
        if (isLeapYear(arg)) {
            result++;
        }
        return result;
    }

    private static boolean isLeapYear(Date arg) {
        return (arg.getYear() % 4 == 0) && ((arg.getYear() % 100 != 0) ||
                ((arg.getYear() + 1900) % 400 == 0));
    }

    public int getStartingDay() {
        return startingDay;
    }
}
