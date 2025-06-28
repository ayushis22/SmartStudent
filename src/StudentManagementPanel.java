import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.List;

public class StudentManagementPanel extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField nameField, rollField, deptField, emailField, phoneField;
    private JTextField sub1Field, sub2Field, sub3Field;
    private JLabel emailErrorLabel = new JLabel();
    private JLabel phoneErrorLabel = new JLabel();
    private JLabel nameErrorLabel = new JLabel();

    private StudentDAO dao = new StudentDAO();

    public StudentManagementPanel() {
        setTitle("Student Management Dashboard");
        setSize(950, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ----------------- Top Panel for Form Inputs -----------------

// ----------------- Top Panel using GridBagLayout -----------------
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        nameField = new JTextField(10); rollField = new JTextField(10);
        deptField = new JTextField(10); emailField = new JTextField(10); phoneField = new JTextField(10);
        sub1Field = new JTextField(10); sub2Field = new JTextField(10); sub3Field = new JTextField(10);

// Error labels
        emailErrorLabel = new JLabel(); phoneErrorLabel = new JLabel(); nameErrorLabel = new JLabel();
        styleErrorLabel(emailErrorLabel);
        styleErrorLabel(phoneErrorLabel);
        styleErrorLabel(nameErrorLabel);

// Row 1 - Name | Roll No
        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Name"), gbc);
        gbc.gridx = 1; form.add(nameField, gbc);
        gbc.gridx = 2; gbc.gridwidth = 2; form.add(nameErrorLabel, gbc); gbc.gridwidth = 1;
        gbc.gridx = 4; form.add(new JLabel("Roll No"), gbc);
        gbc.gridx = 5; form.add(rollField, gbc);

// Row 2 - Department | Email
        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Department"), gbc);
        gbc.gridx = 1; form.add(deptField, gbc);
        gbc.gridx = 4; form.add(new JLabel("Email"), gbc);
        gbc.gridx = 5; form.add(emailField, gbc);
        gbc.gridx = 6; gbc.gridwidth = 2; form.add(emailErrorLabel, gbc); gbc.gridwidth = 1;

// Row 3 - Phone
        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Phone"), gbc);
        gbc.gridx = 1; form.add(phoneField, gbc);
        gbc.gridx = 2; gbc.gridwidth = 2; form.add(phoneErrorLabel, gbc); gbc.gridwidth = 1;

// Row 4 - Subject 1 | Subject 2 | Subject 3
        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Subject 1"), gbc);
        gbc.gridx = 1; form.add(sub1Field, gbc);
        gbc.gridx = 2; form.add(new JLabel("Subject 2"), gbc);
        gbc.gridx = 3; form.add(sub2Field, gbc);
        gbc.gridx = 4; form.add(new JLabel("Subject 3"), gbc);
        gbc.gridx = 5; form.add(sub3Field, gbc);

        add(form, BorderLayout.NORTH);

        // ----------------- Middle Panel for Search + Buttons -----------------
        JPanel midPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel();
        JTextField rollSearchField = new JTextField(8);
        JTextField deptSearchField = new JTextField(8);
        JTextField marksSearchField = new JTextField(8);
        JButton searchBtn = new JButton("Search");
        JButton resetBtn = new JButton("Reset");

        searchPanel.add(new JLabel("Roll No:")); searchPanel.add(rollSearchField);
        searchPanel.add(new JLabel("Department:")); searchPanel.add(deptSearchField);
        searchPanel.add(new JLabel("Marks >")); searchPanel.add(marksSearchField);
        searchPanel.add(searchBtn); searchPanel.add(resetBtn);

        midPanel.add(searchPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Name", "Roll No", "Dept", "Email", "Phone", "Sub1", "Sub2", "Sub3"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        midPanel.add(scrollPane, BorderLayout.CENTER);

        add(midPanel, BorderLayout.CENTER);

        // ----------------- Bottom Panel for Buttons -----------------
        JPanel buttons = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");
        JButton statsBtn = new JButton("Show Stats");
        JButton exportBtn = new JButton("Export CSV");
        JButton logoutBtn = new JButton("Logout");

        buttons.add(addBtn); buttons.add(updateBtn); buttons.add(deleteBtn);
        buttons.add(clearBtn); buttons.add(statsBtn); buttons.add(exportBtn);
        buttons.add(logoutBtn);

        add(buttons, BorderLayout.SOUTH);

        // ----------------- Load Data -----------------
        loadStudents();

        // ----------------- Actions -----------------
        addBtn.addActionListener(e -> addStudent());
        updateBtn.addActionListener(e -> updateStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
        clearBtn.addActionListener(e -> clearFields());
        statsBtn.addActionListener(e -> showStats());
        exportBtn.addActionListener(e -> exportCSV());
        logoutBtn.addActionListener(e -> handleLogout());

        searchBtn.addActionListener(e -> {
            String roll = rollSearchField.getText().trim();
            String dept = deptSearchField.getText().trim();
            String marksText = marksSearchField.getText().trim();
            Integer marks = null;
            if (!marksText.isEmpty()) {
                try {
                    marks = Integer.parseInt(marksText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Marks must be a number.");
                    return;
                }
            }
            performSearch(roll, dept, marks);
        });

        resetBtn.addActionListener(e -> {
            rollSearchField.setText("");
            deptSearchField.setText("");
            marksSearchField.setText("");
            loadStudents();
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                nameField.setText(model.getValueAt(row, 1).toString());
                rollField.setText(model.getValueAt(row, 2).toString());
                deptField.setText(model.getValueAt(row, 3).toString());
                emailField.setText(model.getValueAt(row, 4).toString());
                phoneField.setText(model.getValueAt(row, 5).toString());
                sub1Field.setText(model.getValueAt(row, 6).toString());
                sub2Field.setText(model.getValueAt(row, 7).toString());
                sub3Field.setText(model.getValueAt(row, 8).toString());
            }
        });

        // ----------------- Dynamic Field Validations -----------------
        emailField.getDocument().addDocumentListener(new LiveValidator(() -> {
            String email = emailField.getText();
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                emailErrorLabel.setText("Invalid email format.");
            } else {
                emailErrorLabel.setText("");
            }
        }));

        phoneField.getDocument().addDocumentListener(new LiveValidator(() -> {
            String phone = phoneField.getText();
            if (!phone.matches("\\d{10}")) {
                phoneErrorLabel.setText("Must be 10 digits.");
            } else {
                phoneErrorLabel.setText("");
            }
        }));

        nameField.getDocument().addDocumentListener(new LiveValidator(() -> {
            String name = nameField.getText();
            if (!name.matches("[a-zA-Z ]+")) {
                nameErrorLabel.setText("Only alphabets & spaces allowed.");
            } else {
                nameErrorLabel.setText("");
            }
        }));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void styleErrorLabel(JLabel label) {
        label.setForeground(Color.RED);
        label.setFont(new Font("SansSerif", Font.PLAIN, 11));
    }

    private void loadStudents() {
        try {
            model.setRowCount(0);
            List<Student> students = dao.getAllStudents();
            for (Student s : students) {
                int[] m = s.getSubjectMarks();
                model.addRow(new Object[]{s.getId(), s.getName(), s.getRollNo(), s.getDepartment(), s.getEmail(), s.getPhone(), m[0], m[1], m[2]});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load students.");
        }
    }

    private void clearFields() {
        nameField.setText(""); rollField.setText(""); deptField.setText("");
        emailField.setText(""); phoneField.setText("");
        sub1Field.setText(""); sub2Field.setText(""); sub3Field.setText("");
        nameErrorLabel.setText(""); emailErrorLabel.setText(""); phoneErrorLabel.setText("");
    }

    private void addStudent() {
        try {
            String name = nameField.getText().trim();
            String roll = rollField.getText().trim();
            String dept = deptField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String sub1 = sub1Field.getText().trim();
            String sub2 = sub2Field.getText().trim();
            String sub3 = sub3Field.getText().trim();

            if (name.isEmpty() || roll.isEmpty() || dept.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            if (!isValidInput(name, email, phone)) return;

            int[] marks = {
                    Integer.parseInt(sub1), Integer.parseInt(sub2), Integer.parseInt(sub3)
            };

            Student s = new Student(name, roll, dept, email, phone, marks);
            if (dao.isEmailExists(email)) {
                JOptionPane.showMessageDialog(this, "A student with this email already exists.");
                return;
            }
            dao.insertStudent(s);
            loadStudents(); clearFields();
            JOptionPane.showMessageDialog(this, "Student added.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric marks.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add student.");
        }
    }

    private boolean isValidInput(String name, String email, String phone) {
        if (!name.matches("[a-zA-Z ]+")) {
            return false;
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return false;
        }
        if (!phone.matches("\\d{10}")) {
            return false;
        }
        return true;
    }

    private void updateStudent() {
        try {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a student to update.");
                return;
            }
            int id = (int) model.getValueAt(row, 0);
            Student s = new Student(
                    id,
                    nameField.getText(), rollField.getText(), deptField.getText(),
                    emailField.getText(), phoneField.getText(),
                    new int[]{Integer.parseInt(sub1Field.getText()), Integer.parseInt(sub2Field.getText()), Integer.parseInt(sub3Field.getText())}
            );
            dao.updateStudent(s);
            loadStudents(); clearFields();
            JOptionPane.showMessageDialog(this, "Student updated.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Update failed.");
        }
    }

    private void deleteStudent() {
        try {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a student to delete.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this student?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            int id = (int) model.getValueAt(row, 0);
            dao.deleteStudent(id);
            loadStudents(); clearFields();
            JOptionPane.showMessageDialog(this, "Student deleted.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Delete failed.");
        }
    }

    private void showStats() {
        try {
            int total = dao.getTotalStudents();
            int max = dao.getHighestMarks();
            int min = dao.getLowestMarks();
            Map<String, Integer> deptMap = dao.getDepartmentWiseCount();

            StringBuilder msg = new StringBuilder();
            msg.append("Total Students: ").append(total).append("\n");
            msg.append("Highest Marks: ").append(max).append("\n");
            msg.append("Lowest Marks: ").append(min).append("\n\nDepartment-wise Count:\n");
            for (String dept : deptMap.keySet()) {
                msg.append(dept).append(": ").append(deptMap.get(dept)).append("\n");
            }
            JOptionPane.showMessageDialog(this, msg.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching stats.");
        }
    }

    private void exportCSV() {
        try {
            dao.exportToCSV("students.csv");
            JOptionPane.showMessageDialog(this, "Exported to students.csv");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Export failed.");
        }
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginForm(); dispose();
        }
    }

    private void performSearch(String roll, String dept, Integer marksThreshold) {
        try {
            model.setRowCount(0);
            List<Student> students = dao.getAllStudents();
            for (Student s : students) {
                boolean matches = true;

                if (!roll.isEmpty() && !s.getRollNo().toLowerCase().contains(roll.toLowerCase())) matches = false;
                if (!dept.isEmpty() && !s.getDepartment().toLowerCase().contains(dept.toLowerCase())) matches = false;
                if (marksThreshold != null && s.getTotalMarks() <= marksThreshold) matches = false;

                if (matches) {
                    int[] m = s.getSubjectMarks();
                    model.addRow(new Object[]{s.getId(), s.getName(), s.getRollNo(), s.getDepartment(), s.getEmail(), s.getPhone(), m[0], m[1], m[2]});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Search failed.");
        }
    }

    /** Utility class to attach live field validation */
    class LiveValidator implements DocumentListener {
        private Runnable action;
        public LiveValidator(Runnable action) {
            this.action = action;
        }
        public void insertUpdate(DocumentEvent e) { action.run(); }
        public void removeUpdate(DocumentEvent e) { action.run(); }
        public void changedUpdate(DocumentEvent e) { action.run(); }
    }
}
