import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.SQLException;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private StudentDAO studentDAO = new StudentDAO();

    public LoginForm() {
        setTitle("SmartStudent - Admin Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Set Look and Feel to Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        // Fonts
        Font labelFont = new Font("SansSerif", Font.PLAIN, 14);
        Font inputFont = new Font("SansSerif", Font.PLAIN, 14);
        Font buttonFont = new Font("SansSerif", Font.BOLD, 14);

        // Panel to hold all components
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Admin Login"));
        panel.setLayout(new GroupLayout(panel));
        GroupLayout layout = (GroupLayout) panel.getLayout();
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Components
        JLabel heading = new JLabel("Welcome to SmartStudent");
        heading.setFont(new Font("SansSerif", Font.BOLD, 16));
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(labelFont);
        usernameField = new JTextField();
        usernameField.setFont(inputFont);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(labelFont);
        passwordField = new JPasswordField();
        passwordField.setFont(inputFont);

        loginButton = new JButton("Login");
        loginButton.setFont(buttonFont);
        loginButton.addActionListener(e -> handleLogin());

        // Layout arrangement
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(heading)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(userLabel)
                                .addComponent(passLabel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(usernameField, 200, 200, 200)
                                .addComponent(passwordField, 200, 200, 200)))
                .addComponent(loginButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(heading)
                .addGap(20)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(userLabel)
                        .addComponent(usernameField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(passLabel)
                        .addComponent(passwordField))
                .addGap(20)
                .addComponent(loginButton)
        );

        add(panel);
        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.");
            return;
        }

        try {
            if (studentDAO.isValidLogin(username, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                new StudentManagementPanel();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error.");
        }
    }
}
