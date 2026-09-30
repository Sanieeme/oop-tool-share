package za.co.wethinkcode.toolshare;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Q4.2: two of these tests currently FAIL. For each failing test, decide whether the
 * TEST or the PRODUCTION CODE is wrong, fix the right one, and explain in answers.txt.
 * A HandTool may be kept for 7 days.
 */
class LoanServiceOverdueTest {

    private final Tool hammer = new HandTool(1, "Claw Hammer");

    private Loan borrowedOn(LocalDate date) {
        return new Loan(1L, 1, 1, date, null);
    }

    @Test
    void returnedAfterDueDate_countsTheExtraDays() {
        Loan loan = borrowedOn(LocalDate.of(2026, 3, 1));
        assertThat(LoanService.overdueDays(loan, hammer, LocalDate.of(2026, 3, 12))).isEqualTo(4);
    }

    @Test
    void returnedEarly_isNotOverdue() {
        Loan loan = borrowedOn(LocalDate.of(2026, 3, 1));
        assertThat(LoanService.overdueDays(loan, hammer, LocalDate.of(2026, 3, 4))).isEqualTo(-4);
    }

    @Test
    void returnedTwoDaysLate_isTwoDaysOverdue() {
        Loan loan = borrowedOn(LocalDate.of(2026, 3, 1));
        assertThat(LoanService.overdueDays(loan, hammer, LocalDate.of(2026, 3, 10))).isEqualTo(2);
    }
}
