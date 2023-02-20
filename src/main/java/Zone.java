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

	public double winterRate() {
		return _winterRate;
	}

	public double summerRate() {
		return _summerRate;
	}

	public Dollars computeSeasonRate(int usage, Date start, Date end) {
		double summerFraction;
		Dollars result;
		summerFraction = computeSummerFraction(start, end);
		result = new Dollars((usage * summerRate() * summerFraction) +
				(usage * winterRate() * (1 - summerFraction)));
		return result;
	}

	private double computeSummerFraction(Date start, Date end) {
		if (isWinterPeriod(start, end))
			return 0;
		if (!isWinterPeriod(end, start) && !isWinterPeriod(start, end))
			return 1;
		// part in summer part in winter
		return computeSummerDays(start, end) / (dayOfYear(end) - dayOfYear(start) + 1);
	}

	private double computeSummerDays(Date start, Date end) {
		double summerDays;
		if (start.before(summerStart()) || start.after(summerEnd())) {
			// end is in the summer
			summerDays = dayOfYear(end) - dayOfYear(summerStart()) + 1;
		} else {
			// start is in summer
			summerDays = dayOfYear(summerEnd()) - dayOfYear(start) + 1;
		}
		return summerDays;
	}

	private boolean isWinterPeriod(Date start, Date end) {
		return start.after(summerEnd()) || end.before(summerStart());
	}

	private static int dayOfYear(Date arg) {
		int result = Month.getMonthInstance(arg.getMonth()).getStartingDay();
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
}
