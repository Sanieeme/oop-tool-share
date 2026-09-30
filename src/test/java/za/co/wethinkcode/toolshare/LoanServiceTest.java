package za.co.wethinkcode.toolshare;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * TODO (Q4.1): write the unit tests for LoanService here.
 * See the README for what must be covered. Use hand-written fakes and a fixed
 * java.time.Clock - no mocking library, no real clock, no real database.
 */
class LoanServiceTest {
    private InMemoryLoanRepository repository;
    private LoanService service;
    private Clock clock;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLoanRepository();

        clock = Clock.fixed(
                Instant.parse("2026-09-30T10:00:00Z"),
                ZoneOffset.UTC
        );

        service = new LoanService(repository, clock);
    }

    @Test
    void memberMayNotHaveMoreThanMaximumActiveLoans() {
        long memberId = 1L;

        service.borrow(memberId, 1L);
        service.borrow(memberId, 2L);
        service.borrow(memberId, 3L);

        assertThatThrownBy(() ->
                service.borrow(memberId, 4L)
        )
                .isInstanceOf(LoanRefusedException.class)
                .hasMessageContaining("reached the loan limit");
    }

    @Test
    void toolAlreadyOnActiveLoanCannotBeBorrowedAgain() {
        long firstMemberId = 1L;
        long secondMemberId = 2L;
        long toolId = 100L;

        service.borrow(firstMemberId, toolId);

        assertThatThrownBy(() ->
                service.borrow(secondMemberId, toolId)
        )
                .isInstanceOf(LoanRefusedException.class)
                .hasMessageContaining("already on loan");
    }

    @Test
    void returningToolMarksLoanAsNoLongerActive() {
        long memberId = 1L;
        long toolId = 100L;

        Loan loan = service.borrow(memberId, toolId);

        Loan returnedLoan = service.returnTool(loan.id());

        assertThat(returnedLoan.isActive()).isFalse();
    }

    @Test
    void loanThatHasAlreadyBeenReturnedCannotBeReturnedAgain() {
        long memberId = 1L;
        long toolId = 100L;

        Loan loan = service.borrow(memberId, toolId);

        service.returnTool(loan.id());

        assertThatThrownBy(() ->
                service.returnTool(loan.id())
        )
                .isInstanceOf(LoanRefusedException.class)
                .hasMessageContaining("already returned");
    }

    @Test
    void returningLoanThatDoesNotExistFailsClearly() {
        long nonExistentLoanId = 999L;

        assertThatThrownBy(() ->
                service.returnTool(nonExistentLoanId)
        )
                .isInstanceOf(java.util.NoSuchElementException.class)
                .hasMessageContaining("No loan with id 999");
    }
}