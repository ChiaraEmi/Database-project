package soundwave.view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Panel for selecting the application role (User or Admin).
 */
public final class RoleSelectionPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JButton btnUtente;
    private final JButton btnAdmin;

    /**
     * Creates a new RoleSelectionPanel.
     */
    public RoleSelectionPanel() {
        setLayout(new GridBagLayout());

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        final JLabel titleLabel = new JLabel("Benvenuto in SoundWave", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridy = 0;
        add(titleLabel, gbc);

        final JLabel subtitleLabel = new JLabel("Seleziona il tuo ruolo per accedere:", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 1;
        add(subtitleLabel, gbc);

        btnUtente = new JButton("Utente");
        btnUtente.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 2;
        add(btnUtente, gbc);

        btnAdmin = new JButton("Amministratore");
        btnAdmin.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 3;
        add(btnAdmin, gbc);
    }

    /**
     * Adds an ActionListener for the Utente button.
     *
     * @param listener the action listener to attach
     */
    public void addUtenteListener(final ActionListener listener) {
        btnUtente.addActionListener(listener);
    }

    /**
     * Adds an ActionListener for the Admin button.
     *
     * @param listener the action listener to attach
     */
    public void addAdminListener(final ActionListener listener) {
        btnAdmin.addActionListener(listener);
    }
}
