import java.util.Date;

public class Zone {
	private String _name;
	private Date _summerEnd;
	private Date _summerStart;
	private double _winterRate;
	private double _summerRate;

	public Zone (String name, double summerRate, double winterRate,
			Date summerStart, Date summerEnd) {
		_name = name;
		_summerRate = summerRate;
		_winterRate = winterRate;
		_summerStart = summerStart;
		_summerEnd = summerEnd;
	}

	public Zone register() {
		Registry.getInstance().addZone(this);
		return this;
	}

	public static Zone get (String name) {
		return Registry.getInstance().getZone(name);
	}

	public String name() {
		return _name;
	}

	public Date summerEnd() {
		return _summerEnd;
	}

	public Date summerStart() {
		return _summerStart;
	}

	public double winterRate() {
		return _winterRate;
	}

	public double summerRate() {
		return _summerRate;
	}

	public Dollars computeSeasonRate(int usage, Date start, Date end) {
		double summerFraction;
		Dollars result;
		if (start.after(summerEnd()) || end.before(summerStart()))
			summerFraction = 0;
		else if (!start.before(summerStart()) && !start.after(summerEnd()) &&
				!end.before(summerStart()) && !end.after(summerEnd()))
			summerFraction = 1;
		else { // part in summer part in winter
			double summerDays;
			if (start.before(summerStart()) || start.after(summerEnd())) {
				// end is in the summer
				summerDays = AbstractSite.dayOfYear(end) - AbstractSite.dayOfYear(summerStart()) + 1;
			} else {
				// start is in summer
				summerDays = AbstractSite.dayOfYear(summerEnd()) - AbstractSite.dayOfYear(start) + 1;
			}
			summerFraction = summerDays / (AbstractSite.dayOfYear(end) - AbstractSite.dayOfYear(start) + 1);
		}
		result = new Dollars((usage * summerRate() * summerFraction) +
				(usage * winterRate() * (1 - summerFraction)));
		return result;
	}
}
