import java.util.Date;

public abstract class AbstractSite {
    public static final double FUEL_RATE = 0.0175;
    protected static final double TAX_RATE = 0.05;
    private Reading[] _readings = new Reading[1000];

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
