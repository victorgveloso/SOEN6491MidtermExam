import java.util.Date;

public class DisabilitySite {
	private Reading[] _readings = new Reading[1000];
	private static final Dollars FUEL_TAX_CAP = new Dollars (0.10);
	private static final double TAX_RATE = 0.05;
	private Zone _zone;
	private static final int CAP = 200;

	public DisabilitySite(Zone _zone) {
		this._zone = _zone;
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
		int usage = _readings[i-1].amount() - _readings[i-2].amount();
		Date end = _readings[i-1].date();
		Date start = _readings[i-2].date();
		start.setDate(start.getDate() + 1); //set to begining of period
		return charge(usage, start, end);
	}

	private Dollars charge(int fullUsage, Date start, Date end) {
		Dollars result;
		double summerFraction;
		int usage = Math.min(fullUsage, CAP);
		result = ResidentialSite.getDollars(usage, start, end, _zone);
		result = result.plus(new Dollars (Math.max(fullUsage - usage, 0) * 0.062));
		result = result.plus(result.times(TAX_RATE));
		Dollars fuel = new Dollars(fullUsage * 0.0175);
		result = result.plus(fuel);
		result = result.plus(fuel.times(TAX_RATE).minus(FUEL_TAX_CAP)).max(Dollars.ZERO);
		return result;
	}

	int dayOfYear(Date arg) {
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
		default :
			throw new IllegalArgumentException();
		}
		result += arg.getDate();
		//check leap year
		if ((arg.getYear()%4 == 0) && ((arg.getYear() % 100 != 0) ||
				((arg.getYear() + 1900) % 400 == 0)))
		{
			result++;
		}
		return result;
	}
}
