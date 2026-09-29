package za.co.wethinkcode.toolshare;

/** Thrown when a business rule prevents a loan or a return. */
public class LoanRefusedException extends RuntimeException {

    public LoanRefusedException(String message) {
        super(message);
    }
}
