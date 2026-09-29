package za.co.wethinkcode.toolshare;

/** The JSON shape of a loan in the API. Dates are ISO-8601 strings. */
public record LoanResponse(long id, long toolId, long memberId, String borrowedOn) {

    public static LoanResponse from(Loan loan) {
        return new LoanResponse(loan.id(), loan.toolId(), loan.memberId(), loan.borrowedOn().toString());
    }
}
