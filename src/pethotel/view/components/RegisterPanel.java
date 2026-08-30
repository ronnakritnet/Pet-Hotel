/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */



package com.mycompany.projectownerpanel;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {

    // Register information
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField fullNameField;
    private JTextField phoneField;
    private JTextField petTypeField;

    // Buttons
    private JButton registerButton;
    private JButton cancelButton;

    public RegisterPanel() {

        setLayout(new BorderLayout(10, 10));

        setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        createFormPanel();
        createBottomPanel();

        setButtonActions();
    }

    // ==========================================
    // Register Form
    // ==========================================

    private void createFormPanel() {

        JPanel formPanel = new JPanel(
                new GridLayout(6, 2, 10, 10)
        );

        formPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Member Registration"
                )
        );

        // Username
        formPanel.add(
                new JLabel("Username:")
        );

        usernameField = new JTextField();

        formPanel.add(usernameField);

        // Password
        formPanel.add(
                new JLabel("Password:")
        );

        passwordField = new JPasswordField();

        formPanel.add(passwordField);

        // Confirm Password
        formPanel.add(
                new JLabel("Confirm Password:")
        );

        confirmPasswordField = new JPasswordField();

        formPanel.add(confirmPasswordField);

        // Full Name
        formPanel.add(
                new JLabel("Full Name:")
        );

        fullNameField = new JTextField();

        formPanel.add(fullNameField);

        // Phone
        formPanel.add(
                new JLabel("Phone:")
        );

        phoneField = new JTextField();

        formPanel.add(phoneField);

        // Pet Type
        formPanel.add(
                new JLabel("Pet Type:")
        );

        petTypeField = new JTextField();

        formPanel.add(petTypeField);

        add(formPanel, BorderLayout.CENTER);
    }

    // ==========================================
    // Bottom Panel
    // ==========================================

    private void createBottomPanel() {

        JPanel bottomPanel = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT
                )
        );

        cancelButton = new JButton(
                "Cancel"
        );

        registerButton = new JButton(
                "Register"
        );

        bottomPanel.add(cancelButton);
        bottomPanel.add(registerButton);

        add(
                bottomPanel,
                BorderLayout.SOUTH
        );
    }

    // ==========================================
    // Button Actions
    // ==========================================

    private void setButtonActions() {

        registerButton.addActionListener(
                e -> submitRegister()
        );

        cancelButton.addActionListener(
                e -> closeWindow()
        );
    }

    // ==========================================
    // Submit Register
    // ==========================================

    private void submitRegister() {

        String username =
                usernameField
                        .getText()
                        .trim();

        String password =
                new String(
                        passwordField.getPassword()
                ).trim();

        String confirmPassword =
                new String(
                        confirmPasswordField.getPassword()
                ).trim();

        String fullName =
                fullNameField
                        .getText()
                        .trim();

        String phone =
                phoneField
                        .getText()
                        .trim();

        String petType =
                petTypeField
                        .getText()
                        .trim();

        if (username.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a username."
            );

            return;
        }

        if (password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a password."
            );

            return;
        }

        if (!password.equals(confirmPassword)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Passwords do not match."
            );

            return;
        }

        if (fullName.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your full name."
            );

            return;
        }

        if (phone.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your phone number."
            );

            return;
        }

        if (petType.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your pet type."
            );

            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Registration successful.\n"
                + "Username: " + username + "\n"
                + "Full Name: " + fullName + "\n"
                + "Phone: " + phone + "\n"
                + "Pet Type: " + petType
        );

        closeWindow();
    }

    // ==========================================
    // Close Window
    // ==========================================

    private void closeWindow() {

        Window window =
                SwingUtilities.getWindowAncestor(this);

        if (window != null) {

            window.dispose();
        }
    }

    // ==========================================
    // Main Method
    // ==========================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame(
                    "Member Registration"
            );

            frame.setDefaultCloseOperation(
                    JFrame.DISPOSE_ON_CLOSE
            );

            frame.setSize(400, 400);

            frame.setLocationRelativeTo(null);

            frame.add(
                    new RegisterPanel()
            );

            frame.setVisible(true);
        });
    }
}
