import java.util.Date;

public class DisabilitySite extends AbstractSite {
	private static final Dollars FUEL_TAX_CAP = new Dollars (0.10);
	private Zone _zone;
	private static final int CAP = 200;

	public DisabilitySite(Zone _zone) {
		this._zone = _zone;
	}

	@Override
	protected Dollars charge(int fullUsage, Date start, Date end) {
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
}
