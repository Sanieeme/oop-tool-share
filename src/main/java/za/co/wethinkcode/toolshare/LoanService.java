package za.co.wethinkcode.toolshare;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;

public class LoanService {

    public static final int MAX_ACTIVE_LOANS = 3;

    private final LoanRepository repository;
    private final Clock clock;

    public LoanService(LoanRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    public Loan borrow(long memberId, long toolId) {
        if (repository.countActiveLoansForMember(memberId) >= MAX_ACTIVE_LOANS) {
            throw new LoanRefusedException("Member " + memberId + " has reached the loan limit");
        }
        if (repository.isToolOnLoan(toolId)) {
            throw new LoanRefusedException("Tool " + toolId + " is already on loan");
        }
        return repository.save(new Loan(null, toolId, memberId, LocalDate.now(clock), null));
    }

    public Loan returnTool(long loanId) {
        Loan loan = repository.findById(loanId)
                .orElseThrow(() -> new NoSuchElementException("No loan with id " + loanId));
        if (!loan.isActive()) {
            throw new LoanRefusedException("Loan " + loanId + " was already returned");
        }
        return repository.save(loan.returnedOn(LocalDate.now(clock)));
    }

    /** How many days late the tool is, judged on the day it is returned. */
    public static int overdueDays(Loan loan, Tool tool, LocalDate returnedOn) {
        long daysHeld = ChronoUnit.DAYS.between(loan.borrowedOn(), returnedOn);
        return (int) (daysHeld - tool.maxLoanDays());
    }
}
