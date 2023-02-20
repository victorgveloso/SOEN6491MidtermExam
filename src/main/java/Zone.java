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

	public void register() {
		Registry.getInstance().addZone(this);
	}

	public String name() {
		return _name;
	}

	public Dollars computeSeasonRate(int usage, Date start, Date end) {
		double summerFraction;
		Dollars result;
		summerFraction = computeSummerFraction(start, end);
		result = new Dollars((usage * _summerRate * summerFraction) +
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
		if (start.before(_summerStart) || start.after(_summerEnd)) {
			// end is in the summer
			summerDays = Month.dayOfYear(end) - Month.dayOfYear(_summerStart) + 1;
		} else {
			// start is in summer
			summerDays = Month.dayOfYear(_summerEnd) - Month.dayOfYear(start) + 1;
		}
		return summerDays;
	}

	private boolean isWinterPeriod(Date start, Date end) {
		return start.after(_summerEnd) || end.before(_summerStart);
	}

}
