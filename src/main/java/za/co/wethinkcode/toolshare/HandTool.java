package za.co.wethinkcode.toolshare;

public class HandTool extends Tool {

    public static final double DEPOSIT = 20.00;
    public static final int MAX_LOAN_DAYS = 7;

    public HandTool(long id, String name) {
        super(id, name);
    }

    @Override
    public String category() {
        return "HAND";
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
