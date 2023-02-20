import java.util.Date;

public class ResidentialSite extends AbstractSite {
	private Zone _zone;

	ResidentialSite (Zone zone) {
		_zone = zone;
	}

	@Override
	protected Dollars charge(int usage, Date start, Date end) {
		Dollars result;
		// Find out how much of period is in the summer
		result = _zone.computeSeasonRate(usage, start, end);
		result = result.plus(result.times(TAX_RATE));
		Dollars fuel = new Dollars(usage * FUEL_RATE);
		result = result.plus(fuel);
		result = result.plus(fuel.times(TAX_RATE));
		return result;
	}

}
