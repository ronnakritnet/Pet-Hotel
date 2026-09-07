package pethotel.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;

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
 * Login screen with a yellow header, as requested. Credentials are a
 * hardcoded staff login (staff / 1234) since there is no user-account
 * model yet - swap out {@link #checkCredentials} if one is added later.
 *
 * <p>A "Register" link sits below the Login button and opens
 * {@link RegisterDialog}. Registration is UI-only for now (it shows a
 * confirmation message but does not persist anywhere) since there is no
 * staff/customer account store to save into yet.</p>
 */
public class LoginPanel extends JPanel {

    private static final String VALID_USERNAME = "staff";
    private static final String VALID_PASSWORD = "1234";

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPanel(Runnable onBack, Runnable onLoginSuccess) {
        setLayout(new BorderLayout());
        setBackground(UIStyle.COLOR_BACKGROUND);

        add(createHeader(onBack), BorderLayout.NORTH);
        add(createForm(onLoginSuccess), BorderLayout.CENTER);
    }

    private JPanel createHeader(Runnable onBack) {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(UIStyle.COLOR_YELLOW);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JButton backButton = new JButton("< Back");
        backButton.setFont(UIStyle.FONT_BUTTON);
        backButton.setForeground(UIStyle.COLOR_YELLOW_TEXT);
        backButton.setFocusPainted(false);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backButton.addActionListener(e -> onBack.run());

        JLabel title = new JLabel("Login");
        title.setFont(UIStyle.FONT_TITLE);
        title.setForeground(UIStyle.COLOR_YELLOW_TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("For Pet Hotel Staff");
        subtitle.setFont(UIStyle.FONT_SUBTITLE);
        subtitle.setForeground(UIStyle.COLOR_YELLOW_TEXT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(backButton);
        header.add(Box.createVerticalStrut(10));
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        return header;
    }

    private JPanel createForm(Runnable onLoginSuccess) {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UIStyle.COLOR_BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIStyle.COLOR_CARD);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(24, 24, 24, 24)));

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(UIStyle.FONT_BODY);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(UIStyle.FONT_BODY);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(UIStyle.FONT_BUTTON);
        loginButton.setBackground(UIStyle.COLOR_YELLOW);
        loginButton.setForeground(UIStyle.COLOR_YELLOW_TEXT);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.addActionListener(e -> attemptLogin(onLoginSuccess));

        JButton registerButton = new JButton("Register");
        registerButton.setFont(UIStyle.FONT_BUTTON);
        registerButton.setForeground(UIStyle.COLOR_PRIMARY);
        registerButton.setContentAreaFilled(false);
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerButton.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> openRegisterDialog());

        card.add(userLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(14));
        card.add(passLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(20));
        card.add(loginButton);
        card.add(Box.createVerticalStrut(10));
        card.add(registerButton);

        content.add(card);
        return content;
    }

    private void attemptLogin(Runnable onLoginSuccess) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password)) {
            JOptionPane.showMessageDialog(this, "Login successful");
            onLoginSuccess.run();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegisterDialog() {
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
        RegisterDialog dialog = new RegisterDialog(owner);
        dialog.setVisible(true);
    }
}
