package pethotel.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Member / staff registration form, styled to match the rest of the app.
 *
 * <p>NOTE: there is currently no account-storage model (User/Customer with
 * username &amp; password) wired up in the pethotel project, so this panel
 * validates the input and shows a confirmation message, but does not save
 * the new account anywhere yet. Hook {@link #submitRegister()} up to a real
 * controller once one exists.</p>
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
        setLayout(new BorderLayout());
        setBackground(UIStyle.COLOR_BACKGROUND);

        add(createHeader(), BorderLayout.NORTH);
        add(createForm(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(UIStyle.COLOR_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JLabel title = new JLabel("Register");
        title.setFont(UIStyle.FONT_TITLE);
        title.setForeground(java.awt.Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Create a new member account");
        subtitle.setFont(UIStyle.FONT_SUBTITLE);
        subtitle.setForeground(new java.awt.Color(226, 225, 253));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        return header;
    }

    private JPanel createForm() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UIStyle.COLOR_BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JPanel card = new JPanel(new GridLayout(6, 2, 10, 10));
        card.setBackground(UIStyle.COLOR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        fullNameField = new JTextField();
        phoneField = new JTextField();
        petTypeField = new JTextField();

        addField(card, "Username:", usernameField);
        addField(card, "Password:", passwordField);
        addField(card, "Confirm Password:", confirmPasswordField);
        addField(card, "Full Name:", fullNameField);
        addField(card, "Phone:", phoneField);
        addField(card, "Pet Type:", petTypeField);

        content.add(card);
        content.add(Box.createVerticalStrut(14));
        content.add(createButtonRow());

        return content;
    }

    private void addField(JPanel parent, String labelText, Component field) {
        JLabel label = new JLabel(labelText);
        label.setFont(UIStyle.FONT_BODY);
        label.setForeground(UIStyle.COLOR_TEXT_DARK);
        parent.add(label);
        parent.add(field);
    }

    private JPanel createButtonRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(UIStyle.FONT_BUTTON);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> closeWindow());

        registerButton = new JButton("Register");
        registerButton.setFont(UIStyle.FONT_BUTTON);
        registerButton.setBackground(UIStyle.COLOR_PRIMARY);
        registerButton.setForeground(java.awt.Color.WHITE);
        registerButton.setOpaque(true);
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(e -> submitRegister());

        row.add(cancelButton);
        row.add(registerButton);
        return row;
    }

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

    private void closeWindow() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }
}
