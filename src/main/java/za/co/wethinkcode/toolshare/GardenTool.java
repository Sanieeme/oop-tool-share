package za.co.wethinkcode.toolshare;

public class GardenTool extends Tool {

    public static final double DEPOSIT = 15.00;
    public static final int MAX_LOAN_DAYS = 5;

    public GardenTool(long id, String name) {
        super(id, name);
    }

    @Override
    public String category() {
        return "GARDEN";
    }

    @Override
    public double deposit() {
        return DEPOSIT;
    }

    @Override
    public int maxLoanDays() {
        return MAX_LOAN_DAYS;
    }
}
