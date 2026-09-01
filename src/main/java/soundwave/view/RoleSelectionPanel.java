package soundwave.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Panel for selecting the application role (User or Admin).
 */
public final class RoleSelectionPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final float TITLE_FONT_SIZE = 24f;
    private static final float SUBTITLE_FONT_SIZE = 14f;
    private static final int BORDER_SIZE = 40;
    private static final int TITLE_MARGIN = 15;
    private static final int BUTTON_WIDTH = 180;
    private static final int BUTTON_HEIGHT = 35;
    private static final int INSET_GAP = 8;
    private static final int SUBTITLE_BOTTOM_INSET = 15;

    private final JButton btnUtente;
    private final JButton btnAdmin;

    /**
     * Builds a new RoleSelectionPanel.
     */
    public RoleSelectionPanel() {
        super();
        this.setLayout(new BorderLayout(0, TITLE_MARGIN));
        this.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));

        // Titolo in alto
        final JLabel titleLabel = new JLabel("Benvenuto in SoundWave", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(TITLE_FONT_SIZE));
        this.add(titleLabel, BorderLayout.NORTH);

        // Pannello centrale per sottotitolo e pulsanti raggruppati
        final JPanel centerPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;

        // Sottotitolo (riga 0)
        final JLabel subtitleLabel = new JLabel("Seleziona il tuo ruolo per accedere:", SwingConstants.CENTER);
        subtitleLabel.setFont(subtitleLabel.getFont().deriveFont(SUBTITLE_FONT_SIZE));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, SUBTITLE_BOTTOM_INSET, 0); // Spazio sotto il sottotitolo
        centerPanel.add(subtitleLabel, gbc);

        final Dimension buttonSize = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);

        // Pulsante Utente (riga 1)
        this.btnUtente = new JButton("Utente");
        this.btnUtente.setPreferredSize(buttonSize);
        gbc.gridy = 1;
        centerPanel.add(this.btnUtente, gbc);

        // Pulsante Admin (riga 2)
        this.btnAdmin = new JButton("Amministratore");
        this.btnAdmin.setPreferredSize(buttonSize);
        gbc.gridy = 2;
        centerPanel.add(this.btnAdmin, gbc);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Adds an ActionListener for the Utente button.
     *
     * @param listener the action listener to attach
     */
    public void addUtenteListener(final ActionListener listener) {
        this.btnUtente.addActionListener(listener);
    }

    /**
     * Adds an ActionListener for the Admin button.
     *
     * @param listener the action listener to attach
     */
    public void addAdminListener(final ActionListener listener) {
        this.btnAdmin.addActionListener(listener);
    }
}
