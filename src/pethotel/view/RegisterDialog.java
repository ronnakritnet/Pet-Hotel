package pethotel.view;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JDialog;

/** Thin JDialog wrapper so {@link RegisterPanel} can be opened as a popup from {@link LoginPanel}. */
public class RegisterDialog extends JDialog {

    public RegisterDialog(Frame owner) {
        super(owner, "Register", true);
        setLayout(new BorderLayout());
        setSize(420, 520);
        setLocationRelativeTo(owner);
        add(new RegisterPanel(), BorderLayout.CENTER);
    }
}
