package za.co.wethinkcode.toolshare;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryLoanRepository implements LoanRepository {

    private final Map<Long, Loan> loans = new LinkedHashMap<>();
    private long nextId = 1;

    @Override
    public int countActiveLoansForMember(long memberId) {
        return (int) loans.values().stream()
                .filter(l -> l.memberId() == memberId && l.isActive())
                .count();
    }

    @Override
    public boolean isToolOnLoan(long toolId) {
        return loans.values().stream().anyMatch(l -> l.toolId() == toolId && l.isActive());
    }

    @Override
    public Loan save(Loan loan) {
        Loan stored = loan.id() == null ? loan.withId(nextId++) : loan;
        loans.put(stored.id(), stored);
        return stored;
    }

    @Override
    public Optional<Loan> findById(long loanId) {
        return Optional.ofNullable(loans.get(loanId));
    }
}
