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
		double summerDays = computeSummerDays(start, end);
		return summerDays / (dayOfYear(end) - dayOfYear(start) + 1);
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
}
