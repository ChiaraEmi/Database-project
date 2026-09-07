package soundwave.view;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

/**
 * Dialog for registering a new user in the Soundwave system.
 * This dialog collects user information including username, name, surname,
 * email, password, birth date, and country. It validates the input before
 * allowing registration.
 */
public final class RegisterDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private static final int DIALOG_WIDTH = 400;
    private static final int DIALOG_HEIGHT = 420;
    private static final int LAYOUT_GAP = 10;
    private static final int GRID_GAP = 8;
    private static final int BORDER_SIZE = 15;
    private static final int BUTTON_GAP = 15;
    private static final int BUTTON_PADDING = 10;

    private final JTextField txtUsername;
    private final JTextField txtName;
    private final JTextField txtSurname;
    private final JTextField txtEmail;
    private final JPasswordField txtPassword;
    private final JTextField txtBirthDate;
    private final JTextField txtCountry;
    private final JButton btnRegister;
    private final JButton btnCancel;

    private boolean registered;

    /**
     * Constructs a new RegisterDialog.
     *
     * @param parent the parent frame
     */
    public RegisterDialog(final JFrame parent) {
        super(parent, "Registra Nuovo Utente", true);

        setLayout(new BorderLayout(LAYOUT_GAP, LAYOUT_GAP));
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(parent);

        final JPanel panel = new JPanel(new GridLayout(0, 2, LAYOUT_GAP, GRID_GAP));
        panel.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));

        panel.add(new JLabel("Username:"));
        this.txtUsername = new JTextField();
        panel.add(txtUsername);

        panel.add(new JLabel("Nome:"));
        this.txtName = new JTextField();
        panel.add(txtName);

        panel.add(new JLabel("Cognome:"));
        this.txtSurname = new JTextField();
        panel.add(txtSurname);

        panel.add(new JLabel("Email:"));
        this.txtEmail = new JTextField();
        panel.add(txtEmail);

        panel.add(new JLabel("Password:"));
        this.txtPassword = new JPasswordField();
        panel.add(txtPassword);

        panel.add(new JLabel("Data Nascita (YYYY-MM-DD):"));
        this.txtBirthDate = new JTextField();
        panel.add(txtBirthDate);

        panel.add(new JLabel("Paese:"));
        this.txtCountry = new JTextField();
        panel.add(txtCountry);

        add(panel, BorderLayout.CENTER);

        // Pulsanti
        final JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, BUTTON_GAP, BUTTON_PADDING));
        this.btnRegister = new JButton("Registra");
        this.btnCancel = new JButton("Annulla");
        btnPanel.add(btnRegister);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
    }

    /**
     * Gets the username entered by the user.
     *
     * @return the username, trimmed of leading/trailing whitespace
     */
    public String getUsername() {
        return txtUsername.getText().trim();
    }

    /**
     * Gets the name entered by the user.
     *
     * @return the name, trimmed of leading/trailing whitespace
     */
    public String getName() {
        return txtName.getText().trim();
    }

    /**
     * Gets the surname entered by the user.
     *
     * @return the surname, trimmed of leading/trailing whitespace
     */
    public String getSurname() {
        return txtSurname.getText().trim();
    }

    /**
     * Gets the email entered by the user.
     *
     * @return the email, trimmed of leading/trailing whitespace
     */
    public String getEmail() {
        return txtEmail.getText().trim();
    }

    /**
     * Gets the password entered by the user.
     *
     * @return the password as a String
     */
    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    /**
     * Gets the birth date entered by the user.
     *
     * @return the birth date as a LocalDate
     * @throws DateTimeParseException if the date format is invalid
     */
    public LocalDate getBirthDate() {
        return LocalDate.parse(txtBirthDate.getText().trim());
    }

    /**
     * Gets the country entered by the user.
     *
     * @return the country, trimmed of leading/trailing whitespace
     */
    public String getCountry() {
        return txtCountry.getText().trim();
    }

    /**
     * Adds a listener to be called when the register button is clicked.
     *
     * @param onRegister the listener to call on successful registration
     */
    public void addRegisterListener(final Runnable onRegister) {
        btnRegister.addActionListener(e -> {
            // Validazione base
            if (getUsername().isEmpty()) {
                showError("Inserisci un username.");
                return;
            }
            if (getName().isEmpty()) {
                showError("Inserisci il nome.");
                return;
            }
            if (getSurname().isEmpty()) {
                showError("Inserisci il cognome.");
                return;
            }
            if (getEmail().isEmpty()) {
                showError("Inserisci l'email.");
                return;
            }
            if (getPassword().isEmpty()) {
                showError("Inserisci la password.");
                return;
            }
            if (getBirthDate() == null) {
                showError("Formato data non valido. Usa YYYY-MM-DD.");
                return;
            }
            if (getCountry().isEmpty()) {
                showError("Inserisci il paese.");
                return;
            }

            registered = true;
            if (onRegister != null) {
                onRegister.run();
            }
        });
    }

    /**
     * Checks if the user has successfully registered.
     *
     * @return true if registration was successful, false otherwise
     */
    public boolean isRegistered() {
        return registered;
    }

    /**
     * Shows an error message dialog.
     *
     * @param message the error message to display
     */
    public void showError(final String message) {
        JOptionPane.showMessageDialog(this, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a success message dialog.
     *
     * @param message the success message to display
     */
    public void showSuccess(final String message) {
        JOptionPane.showMessageDialog(this, message, "Successo", JOptionPane.INFORMATION_MESSAGE);
    }
}
