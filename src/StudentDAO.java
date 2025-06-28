import java.sql.*;
import java.util.*;

/**
 * Data Access Object (DAO) class for handling all database operations
 * related to the Student entity and admin authentication.
 */
public class StudentDAO {

    /**
     * Inserts a new student record into the database.
     * @param student Student object containing the details to be inserted
     * @throws SQLException if a database error or constraint violation occurs
     */
    public void insertStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students(name, roll_no, department, email, phone, subject1, subject2, subject3) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, student.getName());
            ps.setString(2, student.getRollNo());
            ps.setString(3, student.getDepartment());
            ps.setString(4, student.getEmail());
            ps.setString(5, student.getPhone());
            int[] marks = student.getSubjectMarks();
            ps.setInt(6, marks[0]);
            ps.setInt(7, marks[1]);
            ps.setInt(8, marks[2]);
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Student already exists.", e);
        }
    }

    /**
     * Updates an existing student record based on ID.
     * @param student Student object with updated values
     * @throws SQLException if a database error occurs
     */
    public void updateStudent(Student student) throws SQLException {
        String sql = "UPDATE students SET name=?, roll_no=?, department=?, email=?, phone=?, subject1=?, subject2=?, subject3=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, student.getName());
            ps.setString(2, student.getRollNo());
            ps.setString(3, student.getDepartment());
            ps.setString(4, student.getEmail());
            ps.setString(5, student.getPhone());
            int[] marks = student.getSubjectMarks();
            ps.setInt(6, marks[0]);
            ps.setInt(7, marks[1]);
            ps.setInt(8, marks[2]);
            ps.setInt(9, student.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Deletes a student record by ID.
     * @param id Student ID to be deleted
     * @throws SQLException if a database error occurs
     */
    public void deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Retrieves all student records from the database.
     * @return List of Student objects
     * @throws SQLException if a database error occurs
     */
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int[] marks = {rs.getInt("subject1"), rs.getInt("subject2"), rs.getInt("subject3")};
                students.add(new Student(
                        rs.getInt("id"), rs.getString("name"), rs.getString("roll_no"),
                        rs.getString("department"), rs.getString("email"), rs.getString("phone"), marks
                ));
            }
        }
        return students;
    }

    /**
     * Returns total number of students in the system.
     */
    public int getTotalStudents() throws SQLException {
        String sql = "SELECT COUNT(*) FROM students";
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Returns highest total marks scored by any student.
     */
    public int getHighestMarks() throws SQLException {
        String sql = "SELECT MAX(subject1 + subject2 + subject3) FROM students";
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Returns lowest total marks scored by any student.
     */
    public int getLowestMarks() throws SQLException {
        String sql = "SELECT MIN(subject1 + subject2 + subject3) FROM students";
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Returns a department-wise student count.
     */
    public Map<String, Integer> getDepartmentWiseCount() throws SQLException {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT department, COUNT(*) FROM students GROUP BY department";
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                map.put(rs.getString(1), rs.getInt(2));
            }
        }
        return map;
    }

    /**
     * Validates admin login against the `admins` table.
     * @param username admin username
     * @param password admin password
     * @return true if credentials match, false otherwise
     */
    public boolean isValidLogin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    /** Checks if a student with the given email already exists */
    public boolean isEmailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM students WHERE email = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    /**
     * Exports all student data to a CSV file.
     * @param filename name of the CSV file to write
     */
    public void exportToCSV(String filename) throws SQLException, java.io.IOException {
        List<Student> students = getAllStudents();
        try (java.io.FileWriter writer = new java.io.FileWriter(filename)) {
            writer.write("ID,Name,RollNo,Dept,Email,Phone,Subject1,Subject2,Subject3,Total,Grade\n");
            for (Student s : students) {
                int[] marks = s.getSubjectMarks();
                writer.write(s.getId() + "," + s.getName() + "," + s.getRollNo() + "," + s.getDepartment() + "," +
                        s.getEmail() + "," + s.getPhone() + "," + marks[0] + "," + marks[1] + "," + marks[2] + "," +
                        s.getTotalMarks() + "," + s.getGrade() + "\n");
            }
        }
    }
}
