/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */



package com.mycompany.projectownerpanel;

import javax.swing.*;
import java.awt.*;

public class ServicePanel extends JPanel {

    // ==========================================
    // Style Constants
    // ==========================================

    private static final Color COLOR_PRIMARY = new Color(79, 70, 229);
    private static final Color COLOR_BACKGROUND = new Color(245, 246, 250);
    private static final Color COLOR_CARD = Color.WHITE;
    private static final Color COLOR_TEXT_DARK = new Color(31, 41, 55);
    private static final Color COLOR_BORDER = new Color(224, 226, 232);

    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);

    // Service checkboxes
    private JCheckBox walkCheckBox;
    private JCheckBox groomCheckBox;
    private JCheckBox bathCheckBox;
    private JCheckBox otherCheckBox;
    private JTextField otherDetailField;

    // Footer buttons
    private JButton backButton;
    private JButton nextButton;

    public ServicePanel() {

        setLayout(new BorderLayout());

        setBackground(COLOR_BACKGROUND);

        createHeaderPanel();
        createContentPanel();
        createFooterPanel();
    }

    // ==========================================
    // Header
    // ==========================================

    private void createHeaderPanel() {

        JPanel header = new JPanel();

        header.setLayout(
                new BoxLayout(header, BoxLayout.Y_AXIS)
        );

        header.setBackground(COLOR_PRIMARY);

        header.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        JLabel titleLabel = new JLabel("Additional Services");

        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(
                "Step 4 of 4 - Anything extra for your pet?"
        );

        subtitleLabel.setFont(FONT_SUBTITLE);
        subtitleLabel.setForeground(new Color(226, 225, 253));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(titleLabel);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitleLabel);

        add(header, BorderLayout.NORTH);
    }

    // ==========================================
    // Content (service options card)
    // ==========================================

    private void createContentPanel() {

        JPanel content = new JPanel();

        content.setLayout(
                new BoxLayout(content, BoxLayout.Y_AXIS)
        );

        content.setBackground(COLOR_BACKGROUND);

        content.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        content.add(createServiceCard());

        add(content, BorderLayout.CENTER);
    }

    private JPanel createServiceCard() {

        JPanel card = new JPanel();

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setBackground(COLOR_CARD);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDER, 1),
                        BorderFactory.createEmptyBorder(18, 20, 18, 20)
                )
        );

        JLabel cardTitle = new JLabel("Choose your services");

        cardTitle.setFont(FONT_HEADING);
        cardTitle.setForeground(COLOR_TEXT_DARK);
        cardTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(cardTitle);
        card.add(Box.createVerticalStrut(14));

        walkCheckBox = createStyledCheckBox("Dog walking");
        groomCheckBox = createStyledCheckBox("Grooming");
        bathCheckBox = createStyledCheckBox("Bathing");
        otherCheckBox = createStyledCheckBox("Other");

        otherDetailField = new JTextField();

        otherDetailField.setFont(FONT_BODY);
        otherDetailField.setEnabled(false);

        otherDetailField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDER, 1),
                        BorderFactory.createEmptyBorder(6, 8, 6, 8)
                )
        );

        otherDetailField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 34)
        );

        otherCheckBox.addActionListener(
                e -> otherDetailField.setEnabled(
                        otherCheckBox.isSelected()
                )
        );

        JPanel otherRow = new JPanel(new BorderLayout(10, 0));

        otherRow.setOpaque(false);
        otherRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        otherRow.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 34)
        );

        otherRow.add(otherCheckBox, BorderLayout.WEST);
        otherRow.add(otherDetailField, BorderLayout.CENTER);

        card.add(walkCheckBox);
        card.add(Box.createVerticalStrut(10));
        card.add(groomCheckBox);
        card.add(Box.createVerticalStrut(10));
        card.add(bathCheckBox);
        card.add(Box.createVerticalStrut(10));
        card.add(otherRow);

        return card;
    }

    private JCheckBox createStyledCheckBox(String label) {

        JCheckBox checkBox = new JCheckBox(label);

        checkBox.setFont(FONT_BODY);
        checkBox.setForeground(COLOR_TEXT_DARK);
        checkBox.setOpaque(false);
        checkBox.setFocusPainted(false);
        checkBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        checkBox.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        return checkBox;
    }

    // ==========================================
    // Footer (Back / Next)
    // ==========================================

    private void createFooterPanel() {

        JPanel footer = new JPanel(new BorderLayout());

        footer.setBackground(COLOR_CARD);

        footer.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER),
                        BorderFactory.createEmptyBorder(15, 20, 15, 20)
                )
        );

        backButton = new JButton("< Back");

        backButton.setFont(FONT_BUTTON);
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(COLOR_TEXT_DARK);
        backButton.setFocusPainted(false);

        backButton.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDER, 1),
                        BorderFactory.createEmptyBorder(9, 25, 9, 25)
                )
        );

        backButton.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        nextButton = new JButton("Next >");

        nextButton.setFont(FONT_BUTTON);
        nextButton.setBackground(COLOR_PRIMARY);
        nextButton.setForeground(Color.WHITE);
        nextButton.setFocusPainted(false);
        nextButton.setOpaque(true);
        nextButton.setBorderPainted(false);

        nextButton.setBorder(
                BorderFactory.createEmptyBorder(10, 26, 10, 26)
        );

        nextButton.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        backButton.addActionListener(
                e -> goBack()
        );

        nextButton.addActionListener(
                e -> goToNextPage()
        );

        JPanel leftPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 0, 0)
        );

        leftPanel.setOpaque(false);
        leftPanel.add(backButton);

        JPanel rightPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 0, 0)
        );

        rightPanel.setOpaque(false);
        rightPanel.add(nextButton);

        footer.add(leftPanel, BorderLayout.WEST);
        footer.add(rightPanel, BorderLayout.EAST);

        add(footer, BorderLayout.SOUTH);
    }

    // ==========================================
    // Back Button
    // ==========================================

    private void goBack() {

        JOptionPane.showMessageDialog(
                this,
                "Going back to the previous step."
        );
    }

    // ==========================================
    // Next Button
    // ==========================================

    private void goToNextPage() {

        StringBuilder selectedServices = new StringBuilder();

        if (walkCheckBox.isSelected()) {

            selectedServices.append("- Dog walking\n");
        }

        if (groomCheckBox.isSelected()) {

            selectedServices.append("- Grooming\n");
        }

        if (bathCheckBox.isSelected()) {

            selectedServices.append("- Bathing\n");
        }

        if (otherCheckBox.isSelected()) {

            String detail =
                    otherDetailField
                            .getText()
                            .trim();

            if (detail.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please describe the other service."
                );

                return;
            }

            selectedServices.append("- Other: ")
                    .append(detail)
                    .append("\n");
        }

        if (selectedServices.length() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select at least one service."
            );

            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Selected services:\n" + selectedServices
        );
    }

    // ==========================================
    // Main Method
    // ==========================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame(
                    "Additional Services"
            );

            frame.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );

            frame.setSize(480, 420);

            frame.setLocationRelativeTo(null);

            frame.add(
                    new ServicePanel()
            );

            frame.setVisible(true);
        });
    }
}
