package za.co.wethinkcode.toolshare;

import java.time.LocalDate;

/** One borrowing of one tool by one member. {@code id} is null until the loan is saved. */
public record Loan(Long id, long toolId, long memberId, LocalDate borrowedOn, LocalDate returnedOn) {

    public boolean isActive() {
        return returnedOn == null;
    }

    public Loan withId(long newId) {
        return new Loan(newId, toolId, memberId, borrowedOn, returnedOn);
    }

    public Loan returnedOn(LocalDate date) {
        return new Loan(id, toolId, memberId, borrowedOn, date);
    }
}
