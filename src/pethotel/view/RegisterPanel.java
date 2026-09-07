package pethotel.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Member registration form - adapted from the user-supplied RegisterPanel.
 * Opened as a popup from {@link OwnerPanel} via {@link RegisterDialog}.
 *
 * <p>Still UI-only: there is no account model/controller to persist a new
 * member into yet, so this validates the input and shows a confirmation
 * message only. Hook {@link #submitRegister()} up to a real controller
 * once one exists.</p>
 */
public class RegisterPanel extends JPanel {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField fullNameField;
    private JTextField phoneField;
    private JTextField petTypeField;

    private JButton registerButton;
    private JButton cancelButton;

    public RegisterPanel() {

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        createFormPanel();
        createBottomPanel();

        setButtonActions();
    }

    // ==========================================
    // Register Form
    // ==========================================

    private void createFormPanel() {

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Member Registration"));

        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        formPanel.add(confirmPasswordField);

        formPanel.add(new JLabel("Full Name:"));
        fullNameField = new JTextField();
        formPanel.add(fullNameField);

        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Pet Type:"));
        petTypeField = new JTextField();
        formPanel.add(petTypeField);

        add(formPanel, BorderLayout.CENTER);
    }

    // ==========================================
    // Bottom Panel
    // ==========================================

    private void createBottomPanel() {

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        cancelButton = new JButton("Cancel");
        registerButton = new JButton("Register");

        bottomPanel.add(cancelButton);
        bottomPanel.add(registerButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // ==========================================
    // Button Actions
    // ==========================================

    private void setButtonActions() {
        registerButton.addActionListener(e -> submitRegister());
        cancelButton.addActionListener(e -> closeWindow());
    }

    // ==========================================
    // Submit Register
    // ==========================================

    private void submitRegister() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
        String fullName = fullNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String petType = petTypeField.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a username.");
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a password.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }

        if (fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your full name.");
            return;
        }

        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your phone number.");
            return;
        }

        if (petType.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your pet type.");
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Registration successful.\n"
                        + "Username: " + username + "\n"
                        + "Full Name: " + fullName + "\n"
                        + "Phone: " + phone + "\n"
                        + "Pet Type: " + petType);

        closeWindow();
    }

    // ==========================================
    // Close Window
    // ==========================================

    private void closeWindow() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }
}
