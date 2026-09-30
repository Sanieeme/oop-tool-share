package za.co.wethinkcode.toolshare;

/**
 * TODO (Q2.1): implement this class. See the README for the full specification.
 * The class compiles as it stands, but it does not do its job yet.
 */
public class PowerTool extends Tool {
    private int wattage;

    public PowerTool(long id, String name, int wattage) {
        super(id, name);
        // TODO
        if (wattage <= 0) {
            throw new IllegalArgumentException("Wattage must be positive");
        }
        this.wattage = wattage;
    }

    public int getWattage() {
        return wattage;
    }

    @Override
    public String category() {
//        throw new UnsupportedOperationException("TODO");
        return "POWER";
    }

    @Override
    public double deposit() {
//        throw new UnsupportedOperationException("TODO");
        return 100.00;
    }

    @Override
    public int maxLoanDays() {
//        throw new UnsupportedOperationException("TODO");
        return 3;
    }
}
