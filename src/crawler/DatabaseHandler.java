package crawler;

import java.sql.*;

public class DatabaseHandler {

    public Connection connection = null;

    /**
     * Constructor to get the database connection object upon object creation
     */
    public DatabaseHandler() {
        try {
            // Update the path of the DB depending on the location of the db file
            String url = "jdbc:sqlite:src/crawler/crawler.db";
            connection = DriverManager.getConnection(url);
            System.out.println("Connection Established");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to run the Select Query
     */
    public ResultSet runSelectQuery(String sql) throws SQLException {
        Statement sta = connection.createStatement();
        return sta.executeQuery(sql);
    }

    /**
     * Helper method to remove all the records in the database table
     */
    public boolean truncateTableQuery(String sql) throws SQLException {
        Statement sta = connection.createStatement();
        return sta.execute(sql);
    }

    /**
     * Clean up and close the database connection after crawler has finished parsing
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        if (connection != null || !connection.isClosed()) {
            connection.close();
        }
    }
}
