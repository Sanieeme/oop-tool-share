package za.co.wethinkcode.toolshare;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSchema {

    private DatabaseSchema() {
    }

    /**
     * TODO (Q7.1 - Q7.3): create the members, tools and loans tables described in
     * resources/erd.png, with every constraint the ERD and the README call for.
     * Execute one CREATE TABLE statement per table:
     *
     *     statement.execute("CREATE TABLE ... ");
     *
     * Create members and tools BEFORE loans.
     */
    public static void createSchema(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // TODO
        }
    }
}
