package pethotel.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Pet-owner lookup / entry screen - adapted from the user-supplied
 * {@code Projectownerpanel}. This now takes the place of the old staff
 * "Login" screen in {@link MainFrame}'s card layout: search an existing
 * owner by phone number (auto-fills their info), or type in a new one,
 * then continue to room selection. "Register" opens {@link RegisterPanel}
 * as a popup.
 *
 * <p>{@link #findOwner(String)} still uses the same two hardcoded owners
 * as the original file - there is no CustomerController/Customer model
 * wired up yet, so this can't look up or save real data. Replace
 * {@link #findOwner(String)} and {@link #goToNextPage()} once a real
 * customer model/controller exists.</p>
 */
public class OwnerPanel extends JPanel {

    // Search
    private JTextField searchPhoneField;
    private JButton searchButton;

    // Owner information
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField petTypeField;

    // Navigation
    private JButton backButton;
    private JButton nextButton;
    private JButton registerButton;

    private final Runnable onBack;
    private final Runnable onNext;

    public OwnerPanel(Runnable onBack, Runnable onNext) {
        this.onBack = onBack;
        this.onNext = onNext;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        createTopPanel();
        createFormPanel();
        createBottomPanel();

        setButtonActions();
    }

    // ==========================================
    // Top panel: Back button + Search row
    // ==========================================

    private void createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel backRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("< Back");
        backRow.add(backButton);

        JPanel searchPanel = new JPanel();
        JLabel searchLabel = new JLabel("Search by phone:");
        searchPhoneField = new JTextField(15);
        searchButton = new JButton("Search");

        searchPanel.add(searchLabel);
        searchPanel.add(searchPhoneField);
        searchPanel.add(searchButton);

        topPanel.add(backRow, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
    }

    // ==========================================
    // Owner Information
    // ==========================================

    private void createFormPanel() {

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Pet Owner Information"));

        formPanel.add(new JLabel("Full Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

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

        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        registerButton = new JButton("Register");
        leftPanel.add(registerButton);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nextButton = new JButton("Next >>");
        rightPanel.add(nextButton);

        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // ==========================================
    // Button Actions
    // ==========================================

    private void setButtonActions() {
        backButton.addActionListener(e -> onBack.run());
        searchButton.addActionListener(e -> searchOwner());
        nextButton.addActionListener(e -> goToNextPage());
        registerButton.addActionListener(e -> openRegisterDialog());
    }

    // ==========================================
    // Search Owner
    // ==========================================

    private void searchOwner() {

        String phone = searchPhoneField.getText().trim();

        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a phone number.");
            return;
        }

        Owner owner = findOwner(phone);

        if (owner != null) {
            nameField.setText(owner.getName());
            phoneField.setText(owner.getPhone());
            petTypeField.setText(owner.getPetType());

            JOptionPane.showMessageDialog(this, "Owner found.");
        } else {
            nameField.setText("");
            phoneField.setText("");
            petTypeField.setText("");
            phoneField.setText(phone);

            JOptionPane.showMessageDialog(this,
                    "Owner not found. Please enter new information.");
        }
    }

    // ==========================================
    // Find Owner (hardcoded demo data - no Customer model/controller yet)
    // ==========================================

    private Owner findOwner(String phone) {

        if (phone.equals("0987654321")) {
            return new Owner("Panuwat", "0987654321", "Dog");
        }

        if (phone.equals("0812345678")) {
            return new Owner("Natnicha", "0812345678", "Cat");
        }

        return null;
    }

    // ==========================================
    // Next Button
    // ==========================================

    private void goToNextPage() {

        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String petType = petTypeField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name.");
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

        onNext.run();
    }

    // ==========================================
    // Register Button
    // ==========================================

    private void openRegisterDialog() {
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
        RegisterDialog dialog = new RegisterDialog(owner);
        dialog.setVisible(true);
    }

    // ==========================================
    // Owner Class
    // ==========================================

    private static class Owner {

        private final String name;
        private final String phone;
        private final String petType;

        Owner(String name, String phone, String petType) {
            this.name = name;
            this.phone = phone;
            this.petType = petType;
        }

        String getName() {
            return name;
        }

        String getPhone() {
            return phone;
        }

        String getPetType() {
            return petType;
        }
    }
}
