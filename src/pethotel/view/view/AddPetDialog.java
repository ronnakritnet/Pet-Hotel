package pethotel.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pethotel.model.Pet;

/** Popup form to add a new pet for the current customer. Not persisted until the booking is saved. */
public class AddPetDialog extends JDialog {

    private JComboBox<String> petTypeCombo;
    private JTextField nameField;
    private JTextField breedField;
    private JTextField weightField;

    public AddPetDialog(Frame owner, Consumer<Pet> onAdded) {
        super(owner, "Add Pet", true);
        setLayout(new BorderLayout());
        setSize(360, 300);
        setLocationRelativeTo(owner);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        petTypeCombo = new JComboBox<>(new String[]{"DOG", "CAT"});
        nameField = new JTextField();
        breedField = new JTextField();
        weightField = new JTextField();

        form.add(new JLabel("Pet Type:"));
        form.add(petTypeCombo);
        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Breed:"));
        form.add(breedField);
        form.add(new JLabel("Weight (kg):"));
        form.add(weightField);

        JButton addButton = new JButton("Add");
        addButton.setFont(UIStyle.FONT_BUTTON);
        addButton.setBackground(UIStyle.COLOR_PRIMARY);
        addButton.setForeground(java.awt.Color.WHITE);
        addButton.setOpaque(true);
        addButton.setBorderPainted(false);
        addButton.addActionListener(e -> {
            Pet pet = validateAndBuild();
            if (pet != null) {
                onAdded.accept(pet);
                dispose();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(UIStyle.FONT_BUTTON);
        cancelButton.addActionListener(e -> dispose());

        JPanel buttons = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        buttons.add(cancelButton);
        buttons.add(addButton);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private Pet validateAndBuild() {
        String name = nameField.getText().trim();
        String breed = breedField.getText().trim();
        String weightText = weightField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the pet's name.");
            return null;
        }
        if (breed.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the breed.");
            return null;
        }
        double weight;
        try {
            weight = Double.parseDouble(weightText);
            if (weight <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid weight in kg.");
            return null;
        }

        String petType = (String) petTypeCombo.getSelectedItem();
        // petId is assigned for real once the booking is saved (CustomerController); use a
        // temporary placeholder id until then so equals()/lists behave sanely in the UI.
        return new Pet(petType, "NEW-" + System.nanoTime(), name, breed, weight);
    }
}
