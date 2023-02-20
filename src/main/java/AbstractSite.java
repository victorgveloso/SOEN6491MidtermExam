import java.util.Date;

public abstract class AbstractSite {
    protected static final double TAX_RATE = 0.05;
    private Reading[] _readings = new Reading[1000];

    public static Dollars computeSeasonRate(int usage, Date start, Date end, Zone zone) {
        double summerFraction;
        Dollars result;
        if (start.after(zone.summerEnd()) || end.before(zone.summerStart()))
            summerFraction = 0;
        else if (!start.before(zone.summerStart()) && !start.after(zone.summerEnd()) &&
                !end.before(zone.summerStart()) && !end.after(zone.summerEnd()))
            summerFraction = 1;
        else { // part in summer part in winter
            double summerDays;
            if (start.before(zone.summerStart()) || start.after(zone.summerEnd())) {
                // end is in the summer
                summerDays = AbstractSite.dayOfYear(end) - AbstractSite.dayOfYear(zone.summerStart()) + 1;
            } else {
                // start is in summer
                summerDays = AbstractSite.dayOfYear(zone.summerEnd()) - AbstractSite.dayOfYear(start) + 1;
            }
            summerFraction = summerDays / (AbstractSite.dayOfYear(end) - AbstractSite.dayOfYear(start) + 1);
        }
        result = new Dollars((usage * zone.summerRate() * summerFraction) +
                (usage * zone.winterRate() * (1 - summerFraction)));
        return result;
    }

    static int dayOfYear(Date arg) {
        int result;
        switch (arg.getMonth()) {
            case 0:
                result = 0;
                break;
            case 1:
                result = 31;
                break;
            case 2:
                result = 59;
                break;
            case 3:
                result = 90;
                break;
            case 4:
                result = 120;
                break;
            case 5:
                result = 151;
                break;
            case 6:
                result = 181;
                break;
            case 7:
                result = 212;
                break;
            case 8:
                result = 243;
                break;
            case 9:
                result = 273;
                break;
            case 10:
                result = 304;
                break;
            case 11:
                result = 334;
                break;
            default:
                throw new IllegalArgumentException();
        }
        result += arg.getDate();
        //check leap year
        if ((arg.getYear() % 4 == 0) && ((arg.getYear() % 100 != 0) ||
                ((arg.getYear() + 1900) % 400 == 0))) {
            result++;
        }
        return result;
    }

    public void addReading(Reading newReading) {
        // add reading to end of array
        int i = 0;
        while (_readings[i] != null) i++;
        _readings[i] = newReading;
    }

    public Dollars charge() {
        // find last reading
        int i = 0;
        while (_readings[i] != null) i++;
        int usage = _readings[i - 1].amount() - _readings[i - 2].amount();
        Date end = _readings[i - 1].date();
        Date start = _readings[i - 2].date();
        start.setDate(start.getDate() + 1); //set to begining of period
        return charge(usage, start, end);
    }

    protected abstract Dollars charge(int usage, Date start, Date end);
}
