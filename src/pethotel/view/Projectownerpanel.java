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
    private JTextField addressField;
    private JTextField emailField;

    // Next button
    private JButton nextButton;

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
                new GridLayout(4, 2, 10, 10)
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

        // Address
        formPanel.add(
                new JLabel("Address:")
        );

        addressField = new JTextField();

        formPanel.add(addressField);

        // Email
        formPanel.add(
                new JLabel("Email:")
        );

        emailField = new JTextField();

        formPanel.add(emailField);

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

        nextButton = new JButton(
                "Next >>"
        );

        bottomPanel.add(nextButton);

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

            addressField.setText(
                    owner.getAddress()
            );

            emailField.setText(
                    owner.getEmail()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Owner found."
            );

        } else {

            nameField.setText("");
            phoneField.setText("");
            addressField.setText("");
            emailField.setText("");

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
                    "Samutsongkhram",
                    "Panuwat@gmail.com"
            );
        }

        if (phone.equals("0959283611")) {

            return new Owner(
                    "Natnicha Wangkaewhiran",
                    "0959283611",
                    "Samutsongkhram",
                    "Natnicha@gmail.com"
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

        String address =
                addressField
                        .getText()
                        .trim();

        String email =
                emailField
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

        Owner owner = new Owner();

        owner.setName(name);
        owner.setPhone(phone);
        owner.setAddress(address);
        owner.setEmail(email);

        JOptionPane.showMessageDialog(
                this,
                "Information is correct.\n"
                + "Name: " + owner.getName() + "\n"
                + "Phone: " + owner.getPhone() + "\n"
                + "Address: " + owner.getAddress() + "\n"
                + "Email: " + owner.getEmail()
        );
    }

    // ==========================================
    // Owner Class
    // ==========================================

    private static class Owner {

        private String name;
        private String phone;
        private String address;
        private String email;

        public Owner() {
        }

        public Owner(
                String name,
                String phone,
                String address,
                String email
        ) {
            this.name = name;
            this.phone = phone;
            this.address = address;
            this.email = email;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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