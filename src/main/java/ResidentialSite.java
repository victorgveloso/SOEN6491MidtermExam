import java.util.Date;

public class ResidentialSite extends AbstractSite {
	private Zone _zone;

	ResidentialSite (Zone zone) {
		_zone = zone;
	}

	@Override
	protected Dollars charge(int usage, Date start, Date end) {
		Dollars result;
		double summerFraction;
		// Find out how much of period is in the summer
		result = computeSeasonRate(usage, start, end, _zone);
		result = result.plus(result.times(TAX_RATE));
		Dollars fuel = new Dollars(usage * 0.0175);
		result = result.plus(fuel);
		result = result.plus(fuel.times(TAX_RATE));
		return result;
	}

}
