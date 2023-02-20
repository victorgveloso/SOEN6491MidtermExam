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

	public String name() {
		return _name;
	}

	public Date summerEnd() {
		return _summerEnd;
	}

	public Date summerStart() {
		return _summerStart;
	}

	public double summerRate() {
		return _summerRate;
	}

	public Dollars computeSeasonRate(int usage, Date start, Date end) {
		double summerFraction;
		Dollars result;
		summerFraction = computeSummerFraction(start, end);
		result = new Dollars((usage * summerRate() * summerFraction) +
				(usage * _winterRate * (1 - summerFraction)));
		return result;
	}

	private double computeSummerFraction(Date start, Date end) {
		if (isWinterPeriod(start, end))
			return 0;
		if (!isWinterPeriod(end, start) && !isWinterPeriod(start, end))
			return 1;
		// part in summer part in winter
		return computeSummerDays(start, end) / (Month.dayOfYear(end) - Month.dayOfYear(start) + 1);
	}

	private double computeSummerDays(Date start, Date end) {
		double summerDays;
		if (start.before(summerStart()) || start.after(summerEnd())) {
			// end is in the summer
			summerDays = Month.dayOfYear(end) - Month.dayOfYear(summerStart()) + 1;
		} else {
			// start is in summer
			summerDays = Month.dayOfYear(summerEnd()) - Month.dayOfYear(start) + 1;
		}
		return summerDays;
	}

	private boolean isWinterPeriod(Date start, Date end) {
		return start.after(summerEnd()) || end.before(summerStart());
	}

}
