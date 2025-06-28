//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
/**
 * Main class serves as the entry point for the SmartStudent Management System.
 * It launches the login form where the admin can authenticate.
 */
import java.nio.file.*;
import java.sql.*;

public class Main {

    public static void main(String[] args) {
        try {
            runSqlScript("src/student.sql");
        } catch (Exception e) {
            System.out.println("Error executing SQL script:");
            e.printStackTrace();
        }

        // Launch the admin login form GUI
        new LoginForm();
    }

    public static void runSqlScript(String path) throws Exception {
        String sql = Files.readString(Paths.get(path)); // read full file
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Split and execute each statement individually
            for (String statement : sql.split(";")) {
                statement = statement.trim();
                if (!statement.isEmpty()) {
                    stmt.execute(statement);
                }
            }

            System.out.println("SQL script executed successfully.");
        }
    }
}
