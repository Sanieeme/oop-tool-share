package za.co.wethinkcode.toolshare;

import java.util.Optional;

public interface LoanRepository {

    int countActiveLoansForMember(long memberId);

    boolean isToolOnLoan(long toolId);

    /** Stores the loan. A loan with a null id is given a new id; the stored loan is returned. */
    Loan save(Loan loan);

    Optional<Loan> findById(long loanId);
}
