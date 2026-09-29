package za.co.wethinkcode.toolshare;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DatabaseSchemaTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = Database.connect(":memory:");
        DatabaseSchema.createSchema(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    // ---- Q7.1 members and tools ------------------------------------------------

    @Test
    void members_hasIdAsPrimaryKey() throws SQLException {
        assertThat(columnFlags("members", "pk")).containsEntry("id", true);
    }

    @Test
    void members_fullNameAndEmailAreNotNull() throws SQLException {
        Map<String, Boolean> notNull = columnFlags("members", "notnull");
        assertThat(notNull).containsEntry("full_name", true).containsEntry("email", true);
    }

    @Test
    void tools_hasIdAsPrimaryKey() throws SQLException {
        assertThat(columnFlags("tools", "pk")).containsEntry("id", true);
    }

    @Test
    void tools_requiredColumnsAreNotNull() throws SQLException {
        Map<String, Boolean> notNull = columnFlags("tools", "notnull");
        assertThat(notNull)
                .containsEntry("name", true)
                .containsEntry("category", true)
                .containsEntry("deposit", true)
                .containsEntry("max_loan_days", true);
    }

    // ---- Q7.2 loans and its foreign keys ---------------------------------------

    @Test
    void loans_hasIdAsPrimaryKey() throws SQLException {
        assertThat(columnFlags("loans", "pk")).containsEntry("id", true);
    }

    @Test
    void loans_toolMemberAndBorrowedOnAreNotNull_butReturnedOnIsNullable() throws SQLException {
        Map<String, Boolean> notNull = columnFlags("loans", "notnull");
        assertThat(notNull)
                .containsEntry("tool_id", true)
                .containsEntry("member_id", true)
                .containsEntry("borrowed_on", true)
                .containsEntry("returned_on", false);
    }

    @Test
    void loans_declaresBothForeignKeys() throws SQLException {
        Map<String, String> fks = new HashMap<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("PRAGMA foreign_key_list(loans)")) {
            while (rs.next()) {
                assertThat(rs.getString("to")).isEqualTo("id");
                fks.put(rs.getString("from"), rs.getString("table"));
            }
        }
        assertThat(fks).containsEntry("member_id", "members").containsEntry("tool_id", "tools");
    }

    // ---- Q7.3 UNIQUE and CHECK constraints -------------------------------------

    @Test
    void members_emailMustBeUnique() throws SQLException {
        exec("INSERT INTO members (id, full_name, email) VALUES (1, 'A', 'a@example.com')");
        assertThatThrownBy(() ->
                exec("INSERT INTO members (id, full_name, email) VALUES (2, 'B', 'a@example.com')"))
                .isInstanceOf(SQLException.class);
    }

    @Test
    void tools_categoryMustBeHandPowerOrGarden() throws SQLException {
        exec("INSERT INTO tools (id, name, category, deposit, max_loan_days) VALUES (1, 'Drill', 'POWER', 100, 3)");
        assertThatThrownBy(() ->
                exec("INSERT INTO tools (id, name, category, deposit, max_loan_days) VALUES (2, 'X', 'MAGIC', 10, 3)"))
                .isInstanceOf(SQLException.class);
    }

    @Test
    void tools_depositCannotBeNegative_andLoanDaysMustBePositive() {
        assertThatThrownBy(() ->
                exec("INSERT INTO tools (id, name, category, deposit, max_loan_days) VALUES (1, 'X', 'HAND', -1, 3)"))
                .isInstanceOf(SQLException.class);
        assertThatThrownBy(() ->
                exec("INSERT INTO tools (id, name, category, deposit, max_loan_days) VALUES (2, 'Y', 'HAND', 10, 0)"))
                .isInstanceOf(SQLException.class);
    }

    // ---- helpers ---------------------------------------------------------------

    private void exec(String sql) throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute(sql);
        }
    }

    private Map<String, Boolean> columnFlags(String table, String flagColumn) throws SQLException {
        Map<String, Boolean> flags = new HashMap<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("PRAGMA table_info(" + table + ")")) {
            while (rs.next()) {
                flags.put(rs.getString("name"), rs.getInt(flagColumn) > 0);
            }
        }
        return flags;
    }
}
