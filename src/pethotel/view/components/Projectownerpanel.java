/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */



package com.mycompany.projectownerpanel;

import javax.swing.*;
import java.awt.*;

public class Projectownerpanel extends JPanel {

    // Search
    private JTextField searchPhoneField;
    private JButton searchButton;

    // Owner information
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField petTypeField;

    // Next button
    private JButton nextButton;

    // Register button
    private JButton registerButton;

    public Projectownerpanel() {

        setLayout(new BorderLayout(10, 10));

        setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        createSearchPanel();
        createFormPanel();
        createBottomPanel();

        setButtonActions();
    }

    // ==========================================
    // Search Panel
    // ==========================================

    private void createSearchPanel() {

        JPanel searchPanel = new JPanel();

        JLabel searchLabel = new JLabel(
                "Search by phone:"
        );

        searchPhoneField = new JTextField(15);

        searchButton = new JButton("Search");

        searchPanel.add(searchLabel);
        searchPanel.add(searchPhoneField);
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);
    }

    // ==========================================
    // Owner Information
    // ==========================================

    private void createFormPanel() {

        JPanel formPanel = new JPanel(
                new GridLayout(3, 2, 10, 10)
        );

        formPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Pet Owner Information"
                )
        );

        // Name
        formPanel.add(
                new JLabel("Full Name:")
        );

        nameField = new JTextField();

        formPanel.add(nameField);

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
                new BorderLayout()
        );

        // Left side: Register button
        JPanel leftPanel = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT
                )
        );

        registerButton = new JButton(
                "Register"
        );

        leftPanel.add(registerButton);

        // Right side: Next button
        JPanel rightPanel = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT
                )
        );

        nextButton = new JButton(
                "Next >>"
        );

        rightPanel.add(nextButton);

        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        add(
                bottomPanel,
                BorderLayout.SOUTH
        );
    }

    // ==========================================
    // Button Actions
    // ==========================================

    private void setButtonActions() {

        searchButton.addActionListener(
                e -> searchOwner()
        );

        nextButton.addActionListener(
                e -> goToNextPage()
        );

        registerButton.addActionListener(
                e -> goToRegisterPage()
        );
    }

    // ==========================================
    // Search Owner
    // ==========================================

    private void searchOwner() {

        String phone =
                searchPhoneField
                        .getText()
                        .trim();

        if (phone.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a phone number."
            );

            return;
        }

        Owner owner = findOwner(phone);

        if (owner != null) {

            nameField.setText(
                    owner.getName()
            );

            phoneField.setText(
                    owner.getPhone()
            );

            petTypeField.setText(
                    owner.getPetType()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Owner found."
            );

        } else {

            nameField.setText("");
            phoneField.setText("");
            petTypeField.setText("");

            phoneField.setText(phone);

            JOptionPane.showMessageDialog(
                    this,
                    "Owner not found. "
                    + "Please enter new information."
            );
        }
    }

    // ==========================================
    // Find Owner
    // ==========================================

    private Owner findOwner(String phone) {

        if (phone.equals("0982808030")) {

            return new Owner(
                    "Panuwat Pramraksa",
                    "0982808030",
                    "Dog"
            );
        }

        if (phone.equals("0959283611")) {

            return new Owner(
                    "Natnicha Wangkaewhiran",
                    "0959283611",
                    "Cat"
            );
        }

        return null;
    }

    // ==========================================
    // Next Button
    // ==========================================

    private void goToNextPage() {

        String name =
                nameField
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

        if (name.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your name."
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

        Owner owner = new Owner();

        owner.setName(name);
        owner.setPhone(phone);
        owner.setPetType(petType);

        JOptionPane.showMessageDialog(
                this,
                "Information is correct.\n"
                + "Name: " + owner.getName() + "\n"
                + "Phone: " + owner.getPhone() + "\n"
                + "Pet Type: " + owner.getPetType()
        );
    }

    // ==========================================
    // Register Button
    // ==========================================

    private void goToRegisterPage() {

        // TODO: Register
        JOptionPane.showMessageDialog(
                this,
                "Register"
        );
    }

    // ==========================================
    // Owner Class
    // ==========================================

    private static class Owner {

        private String name;
        private String phone;
        private String petType;

        public Owner() {
        }

        public Owner(
                String name,
                String phone,
                String petType
        ) {
            this.name = name;
            this.phone = phone;
            this.petType = petType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPetType() {
            return petType;
        }

        public void setPetType(String petType) {
            this.petType = petType;
        }
    }

    // ==========================================
    // Main Method
    // ==========================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame(
                    "Pet Owner Information"
            );

            frame.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );

            frame.setSize(600, 300);

            frame.setLocationRelativeTo(null);

            frame.add(
                    new Projectownerpanel()
            );

            frame.setVisible(true);
        });
    }
}
