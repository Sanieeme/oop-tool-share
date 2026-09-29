package za.co.wethinkcode.toolshare;

/**
 * TODO (Q2.1): implement this class. See the README for the full specification.
 * The class compiles as it stands, but it does not do its job yet.
 */
public class PowerTool extends Tool {

    public PowerTool(long id, String name, int wattage) {
        super(id, name);
        // TODO
    }

    public int getWattage() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public String category() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public double deposit() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public int maxLoanDays() {
        throw new UnsupportedOperationException("TODO");
    }
}
