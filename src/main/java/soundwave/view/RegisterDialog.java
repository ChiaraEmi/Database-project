package soundwave.view;

import java.time.LocalDate;

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

public class RegisterDialog extends JDialog {

    private final JTextField txtUsername;
    private final JTextField txtName;
    private final JTextField txtSurname;
    private final JTextField txtEmail;
    private final JPasswordField txtPassword;
    private final JTextField txtBirthDate;
    private final JTextField txtCountry;
    private final JButton btnRegister;
    private final JButton btnCancel;
    
    private boolean registered = false;
    
    public RegisterDialog(final JFrame parent) {
        super(parent, "Registra Nuovo Utente", true);
        
        setLayout(new BorderLayout(10, 10));
        setSize(400, 420);
        setLocationRelativeTo(parent);
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
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
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        this.btnRegister = new JButton("Registra");
        this.btnCancel = new JButton("Annulla");
        btnPanel.add(btnRegister);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
        
        btnCancel.addActionListener(e -> dispose());
    }
    
    public String getUsername() { return txtUsername.getText().trim(); }
    public String getName() { return txtName.getText().trim(); }
    public String getSurname() { return txtSurname.getText().trim(); }
    public String getEmail() { return txtEmail.getText().trim(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }
    public LocalDate getBirthDate() { return LocalDate.parse(txtBirthDate.getText().trim()); }
    public String getCountry() { return txtCountry.getText().trim(); }
    
    public void addRegisterListener(Runnable onRegister) {
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
    
    public boolean isRegistered() { return registered; }

    public void showError(final String message) {
        JOptionPane.showMessageDialog(this, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(final String message) {
        JOptionPane.showMessageDialog(this, message, "Successo", JOptionPane.INFORMATION_MESSAGE);
    }

}
