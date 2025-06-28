import java.sql.*;

/**
 * DBConnection is responsible for establishing a connection to the
 *  MySQL database used in the Student Management System.
 */
public class DatabaseConnection {

    // Database URL, username, and password constants
    public static final String URL = "jdbc:mysql://localhost:3306/smartstudent";
    public static final String USER = "root";
    public static final String PASSWORD = "Ayushis@2635";

    /**
     * Establishes and returns a connection to the MySQL database.
     * @return Connection object to interact with the database
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Loads the MySQL JDBC driver class
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // if driver class not found,prints error for debugging
            System.out.println("JDBC driver not found");
            e.printStackTrace();
        }

        // Returns a new database connection using provided credentials
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
