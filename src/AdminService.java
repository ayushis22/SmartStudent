import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * AdminService acts as a bridge between the UI and data access layer (StudentDAO).
 * It encapsulates core logic for student management and statistics.
 */
public class AdminService {
    private StudentDAO studentDAO = new StudentDAO();

    // CRUD Operations

    public void addStudent(Student student) throws SQLException {
        studentDAO.insertStudent(student);
    }

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }

    // Statistics

    public int getTotalStudents() throws SQLException {
        return studentDAO.getTotalStudents();
    }

    public int getHighestMarks() throws SQLException {
        return studentDAO.getHighestMarks();
    }

    public int getLowestMarks() throws SQLException {
        return studentDAO.getLowestMarks();
    }

    public Map<String, Integer> getDepartmentWiseCount() throws SQLException {
        return studentDAO.getDepartmentWiseCount();
    }

    // Authentication

    public boolean isValidAdminLogin(String username, String password) throws SQLException {
        return studentDAO.isValidLogin(username, password);
    }

    // Export

    public void exportToCSV(String filename) throws Exception {
        studentDAO.exportToCSV(filename);
    }
}

