package Model;

import java.sql.*;

/**
 * DBMSConnection is used throughout the application for establishing a connection with the database.
 */
public class DBMSConnection {

    /**
     * @return The connection to the database.
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://wgudb.ucertify.com:3306", "U07XGR", "53689157036");
        String query = "USE WJ07XGR";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeQuery(query);
        } catch (Exception e) {e.printStackTrace();}
        return conn;
    }
}
