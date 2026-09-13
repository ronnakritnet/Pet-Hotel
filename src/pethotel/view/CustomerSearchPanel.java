package pethotel.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import pethotel.controller.CustomerController;
import pethotel.model.Customer;
import pethotel.model.Pet;

/**
 * Step 1 of the booking flow: search an existing pet owner by phone
 * number (data comes from resources/data/customers.json), or enter a
 * brand-new owner. Pick one of their registered pets or add a new one,
 * then continue to room selection.
 */
public class CustomerSearchPanel extends JPanel {

    private final CustomerController customerController;
    private final Runnable onBack;
    private final BiConsumer<Customer, Pet> onNext;

    private JTextField searchPhoneField;
    private JTextField nameField;
    private JTextField phoneField;

    private JPanel petListPanel;
    private final ButtonGroup petGroup = new ButtonGroup();
    private final List<Pet> visiblePets = new ArrayList<>();

    private Customer currentCustomer;

    public CustomerSearchPanel(CustomerController customerController, Runnable onBack,
            BiConsumer<Customer, Pet> onNext) {
        this.customerController = customerController;
        this.onBack = onBack;
        this.onNext = onNext;

        setLayout(new BorderLayout());
        setBackground(UIStyle.COLOR_BACKGROUND);

        add(createHeader(), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        startNewCustomer();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(UIStyle.COLOR_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JButton backButton = new JButton("< Back");
        backButton.setFont(UIStyle.FONT_BUTTON);
        backButton.setForeground(java.awt.Color.WHITE);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backButton.addActionListener(e -> onBack.run());

        JLabel title = new JLabel("Find Customer");
        title.setFont(UIStyle.FONT_TITLE);
        title.setForeground(java.awt.Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Search by phone number, or enter a new customer below");
        subtitle.setFont(UIStyle.FONT_SUBTITLE);
        subtitle.setForeground(new java.awt.Color(226, 225, 253));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(backButton);
        header.add(Box.createVerticalStrut(10));
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        return header;
    }

    private JPanel createBody() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UIStyle.COLOR_BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));

        content.add(createSearchCard());
        content.add(Box.createVerticalStrut(14));
        content.add(createCustomerCard());
        content.add(Box.createVerticalStrut(14));
        content.add(createPetsCard());

        JPanel northWrapper = new JPanel(new BorderLayout());
        northWrapper.setBackground(UIStyle.COLOR_BACKGROUND);
        northWrapper.add(content, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(northWrapper);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return wrapAsSingle(scroll);
    }

    private JPanel wrapAsSingle(Component comp) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UIStyle.COLOR_BACKGROUND);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private JPanel createSearchCard() {
        JPanel card = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)) {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
            }
        };
        card.setBackground(UIStyle.COLOR_CARD);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));

        JLabel label = new JLabel("Phone number:");
        label.setFont(UIStyle.FONT_BODY);

        searchPhoneField = new JTextField(16);
        searchPhoneField.setFont(UIStyle.FONT_BODY);

        JButton searchButton = new JButton("Search (\u0e04\u0e49\u0e19\u0e2b\u0e32)");
        searchButton.setFont(UIStyle.FONT_BUTTON);
        searchButton.setBackground(UIStyle.COLOR_PRIMARY);
        searchButton.setForeground(java.awt.Color.WHITE);
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(e -> searchByPhone());

        card.add(label);
        card.add(searchPhoneField);
        card.add(searchButton);
        return card;
    }

    private JPanel createCustomerCard() {
        JPanel card = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIStyle.COLOR_CARD);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));

        JLabel cardTitle = new JLabel("Customer Information");
        cardTitle.setFont(UIStyle.FONT_HEADING);
        cardTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLabel = new JLabel("Name (\u0e0a\u0e37\u0e48\u0e2d):");
        nameLabel.setFont(UIStyle.FONT_BODY);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel phoneLabel = new JLabel("Phone (\u0e40\u0e1a\u0e2d\u0e23\u0e4c):");
        phoneLabel.setFont(UIStyle.FONT_BODY);
        phoneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        phoneField = new JTextField();
        phoneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        phoneField.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(cardTitle);
        card.add(Box.createVerticalStrut(10));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(nameField);
        card.add(Box.createVerticalStrut(10));
        card.add(phoneLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(phoneField);
        return card;
    }

    private JPanel createPetsCard() {
        JPanel card = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIStyle.COLOR_CARD);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));

        JLabel cardTitle = new JLabel("Select Pet");
        cardTitle.setFont(UIStyle.FONT_HEADING);
        cardTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        petListPanel = new JPanel();
        petListPanel.setLayout(new BoxLayout(petListPanel, BoxLayout.Y_AXIS));
        petListPanel.setBackground(UIStyle.COLOR_CARD);
        petListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton addPetButton = new JButton("+ Add Pet (\u0e40\u0e1e\u0e34\u0e48\u0e21\u0e2a\u0e31\u0e15\u0e27\u0e4c\u0e40\u0e25\u0e35\u0e49\u0e22\u0e07)");
        addPetButton.setFont(UIStyle.FONT_BUTTON);
        addPetButton.setForeground(UIStyle.COLOR_PRIMARY);
        addPetButton.setContentAreaFilled(false);
        addPetButton.setBorderPainted(false);
        addPetButton.setFocusPainted(false);
        addPetButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addPetButton.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        addPetButton.addActionListener(e -> openAddPetDialog());

        card.add(cardTitle);
        card.add(Box.createVerticalStrut(8));
        card.add(petListPanel);
        card.add(Box.createVerticalStrut(6));
        card.add(addPetButton);
        return card;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(UIStyle.COLOR_CARD);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UIStyle.COLOR_BORDER),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        JButton nextButton = new JButton("Next >>");
        nextButton.setFont(UIStyle.FONT_BUTTON);
        nextButton.setBackground(UIStyle.COLOR_PRIMARY);
        nextButton.setForeground(java.awt.Color.WHITE);
        nextButton.setOpaque(true);
        nextButton.setBorderPainted(false);
        nextButton.setFocusPainted(false);
        nextButton.addActionListener(e -> goNext());

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(nextButton);

        footer.add(right, BorderLayout.EAST);
        return footer;
    }

    // ===================== Behaviour =====================

    private void startNewCustomer() {
        currentCustomer = null;
        nameField.setText("");
        phoneField.setText("");
        visiblePets.clear();
        rebuildPetList();
    }

    private void searchByPhone() {
        String phone = searchPhoneField.getText().trim();
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a phone number.");
            return;
        }

        Customer found = customerController.findCustomerByPhone(phone);
        if (found != null) {
            currentCustomer = found;
            nameField.setText(found.getName());
            phoneField.setText(found.getPhoneNumber());
            visiblePets.clear();
            visiblePets.addAll(found.getPets());
            rebuildPetList();
            JOptionPane.showMessageDialog(this, "Customer found: " + found.getName());
        } else {
            currentCustomer = null;
            nameField.setText("");
            phoneField.setText(phone);
            visiblePets.clear();
            rebuildPetList();
            JOptionPane.showMessageDialog(this, "No customer found for this phone number. "
                    + "Enter their details and add a pet below.");
        }
    }

    private void rebuildPetList() {
        petListPanel.removeAll();
        petGroup.getElements().asIterator().forEachRemaining(petGroup::remove);

        if (visiblePets.isEmpty()) {
            JLabel none = new JLabel("No pets on file yet - add one below.");
            none.setFont(UIStyle.FONT_SMALL);
            none.setForeground(UIStyle.COLOR_TEXT_LIGHT);
            none.setAlignmentX(Component.LEFT_ALIGNMENT);
            petListPanel.add(none);
        } else {
            for (int i = 0; i < visiblePets.size(); i++) {
                Pet pet = visiblePets.get(i);
                JRadioButton radio = new JRadioButton(pet.toString());
                radio.setFont(UIStyle.FONT_BODY);
                radio.setOpaque(false);
                radio.setAlignmentX(Component.LEFT_ALIGNMENT);
                radio.putClientProperty("pet", pet);
                petGroup.add(radio);
                petListPanel.add(radio);
                if (i == visiblePets.size() - 1) {
                    radio.setSelected(true);
                }
            }
        }
        petListPanel.revalidate();
        petListPanel.repaint();
    }

    private void openAddPetDialog() {
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
        AddPetDialog dialog = new AddPetDialog(owner, newPet -> {
            visiblePets.add(newPet);
            rebuildPetList();
        });
        dialog.setVisible(true);
    }

    private Pet getSelectedPet() {
        java.util.Enumeration<javax.swing.AbstractButton> elements = petGroup.getElements();
        while (elements.hasMoreElements()) {
            javax.swing.AbstractButton button = elements.nextElement();
            if (button.isSelected()) {
                return (Pet) button.getClientProperty("pet");
            }
        }
        return null;
    }

    private void goNext() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the customer's name.");
            return;
        }
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a phone number.");
            return;
        }

        Pet selectedPet = getSelectedPet();
        if (selectedPet == null) {
            JOptionPane.showMessageDialog(this, "Please select a pet, or add a new one.");
            return;
        }

        try {
            Customer customer = customerController.findCustomerByPhone(phone);
            if (customer == null) {
                customer = new Customer(name, phone);
                for (Pet pet : visiblePets) {
                    customer.addPet(pet);
                }
                customerController.addCustomer(customer);
            } else {
                customer.setName(name);
                for (Pet pet : visiblePets) {
                    boolean alreadyExists = false;
                    for (Pet p : customer.getPets()) {
                        if (p.getPetId() != null && p.getPetId().equals(pet.getPetId())) {
                            alreadyExists = true;
                            break;
                        }
                        if (p.getName().equalsIgnoreCase(pet.getName())
                                && p.getPetType().equalsIgnoreCase(pet.getPetType())) {
                            alreadyExists = true;
                            break;
                        }
                    }
                    if (!alreadyExists) {
                        customerController.addPet(customer, pet);
                    }
                }
                customerController.saveCustomers();
            }

            onNext.accept(customer, selectedPet);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not save customer info: " + ex.getMessage(),
                    "Save Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Resets the panel back to a blank search, ready for the next visitor. */
    public void reset() {
        searchPhoneField.setText("");
        startNewCustomer();
    }
}
