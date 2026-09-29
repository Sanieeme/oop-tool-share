package za.co.wethinkcode.toolshare;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/** Q7.4 - Q7.6. These tests need a working createSchema (Q7.1 - Q7.3) first. */
class QueriesTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = Database.connect(":memory:");
        DatabaseSchema.createSchema(connection);
        try (Statement st = connection.createStatement()) {
            st.execute("INSERT INTO members (id, full_name, email) VALUES "
                    + "(1, 'Ayanda Dlamini', 'ayanda@example.com'),"
                    + "(2, 'Bongani Khumalo', 'bongani@example.com'),"
                    + "(3, 'Cebo Naidoo', 'cebo@example.com')");
            st.execute("INSERT INTO tools (id, name, category, deposit, max_loan_days) VALUES "
                    + "(1, 'Claw Hammer', 'HAND', 20, 7),"
                    + "(2, 'Cordless Drill', 'POWER', 100, 3),"
                    + "(3, 'Spade', 'GARDEN', 15, 5),"
                    + "(4, 'Hedge Trimmer', 'GARDEN', 15, 5),"
                    + "(5, 'Wheelbarrow', 'GARDEN', 15, 5)");
            st.execute("INSERT INTO loans (id, tool_id, member_id, borrowed_on, returned_on) VALUES "
                    + "(1, 1, 1, '2026-03-01', NULL),"
                    + "(2, 2, 1, '2026-03-02', '2026-03-04'),"
                    + "(3, 3, 1, '2026-03-05', NULL),"
                    + "(4, 1, 2, '2026-02-01', '2026-02-05'),"
                    + "(5, 2, 3, '2026-03-10', NULL),"
                    + "(6, 3, 2, '2026-01-10', '2026-01-12')");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void activeLoans_listsOnlyUnreturnedLoans_oldestFirst() throws SQLException {
        List<String> rows = run(Queries.ACTIVE_LOANS, "full_name", "tool_name", "borrowed_on");
        assertThat(rows).containsExactly(
                "Ayanda Dlamini|Claw Hammer|2026-03-01",
                "Ayanda Dlamini|Spade|2026-03-05",
                "Cebo Naidoo|Cordless Drill|2026-03-10");
    }

    @Test
    void busyMembers_areThoseWithTwoOrMoreLoansEver_mostFirst() throws SQLException {
        List<String> rows = run(Queries.BUSY_MEMBERS, "full_name", "loan_count");
        assertThat(rows).containsExactly("Ayanda Dlamini|3", "Bongani Khumalo|2");
    }

    @Test
    void neverBorrowed_listsToolsWithNoLoans_alphabetically() throws SQLException {
        List<String> rows = run(Queries.NEVER_BORROWED, "tool_name");
        assertThat(rows).containsExactly("Hedge Trimmer", "Wheelbarrow");
    }

    private List<String> run(String sql, String... columns) throws SQLException {
        assertThat(sql).as("query has not been written yet").isNotBlank();
        List<String> rows = new ArrayList<>();
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                List<String> cells = new ArrayList<>();
                for (String column : columns) {
                    cells.add(rs.getString(column));
                }
                rows.add(String.join("|", cells));
            }
        }
        return rows;
    }
}
