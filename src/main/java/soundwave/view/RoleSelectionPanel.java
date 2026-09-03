package soundwave.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
 * Panel for selecting the application role (User or Admin) with a modern look.
 */
public final class RoleSelectionPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    // Palette di colori moderna (ispirata al mondo della musica)
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color PRIMARY_COLOR = new Color(74, 119, 255); // Blu acceso
    private static final Color TEXT_DARK = new Color(33, 37, 41);
    private static final Color TEXT_MUTED = new Color(108, 117, 125);

    private static final float TITLE_FONT_SIZE = 26f;
    private static final float SUBTITLE_FONT_SIZE = 14f;
    private static final float BUTTON_FONT_SIZE = 13f;

    private static final int ICON_FONT_SIZE = 36;
    private static final int BORDER_SIZE = 40;
    private static final int TITLE_MARGIN = 15;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 40;
    private static final int VERTICAL_SPACING = 20;

    private final JButton btnUtente;
    private final JButton btnAdmin;

    /**
     * Builds a new RoleSelectionPanel.
     */
    public RoleSelectionPanel() {
        super();
        this.setLayout(new BorderLayout(0, TITLE_MARGIN));
        this.setBackground(BACKGROUND_COLOR);
        this.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));

        // Intestazione con titolo e icona testuale/emblema
        final JPanel northPanel = new JPanel(new BorderLayout(0, 5));
        northPanel.setBackground(BACKGROUND_COLOR);

        final JLabel iconLabel = new JLabel("🎵", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, ICON_FONT_SIZE));
        northPanel.add(iconLabel, BorderLayout.NORTH);

        final JLabel titleLabel = new JLabel("SoundWave", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setForeground(TEXT_DARK);
        northPanel.add(titleLabel, BorderLayout.CENTER);

        this.add(northPanel, BorderLayout.NORTH);

        // Pannello centrale per sottotitolo e pulsanti
        final JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        final GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;

        // Sottotitolo
        final JLabel subtitleLabel = new JLabel("Seleziona il tuo profilo per iniziare", SwingConstants.CENTER);
        subtitleLabel.setFont(subtitleLabel.getFont().deriveFont(SUBTITLE_FONT_SIZE));
        subtitleLabel.setForeground(TEXT_MUTED);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, VERTICAL_SPACING, 0);
        centerPanel.add(subtitleLabel, gbc);

        // Pulsante Utente
        this.btnUtente = createStyledButton("Accesso Utente", false);
        gbc.gridy = 1;
        centerPanel.add(this.btnUtente, gbc);

        // Pulsante Admin
        this.btnAdmin = createStyledButton("Accesso Amministratore", true);
        gbc.gridy = 2;
        centerPanel.add(this.btnAdmin, gbc);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Helper to create custom styled buttons.
     *
     * @param text the text to display on the button.
     * @param isPrimary whether the button represents the primary action style.
     * @return the styled JButton instance.
     */
    private JButton createStyledButton(final String text, final boolean isPrimary) {
        final JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setFocusPainted(false);
        button.setFont(button.getFont().deriveFont(Font.BOLD, BUTTON_FONT_SIZE));

        if (isPrimary) {
            button.setBackground(PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(TEXT_DARK);
        }

        return button;
    }

    /**
     * Adds an action listener to the user button.
     *
     * @param listener the action listener to add.
     */
    public void addUtenteListener(final ActionListener listener) {
        this.btnUtente.addActionListener(listener);
    }

    /**
     * Adds an action listener to the admin button.
     *
     * @param listener the action listener to add.
     */
    public void addAdminListener(final ActionListener listener) {
        this.btnAdmin.addActionListener(listener);
    }
}
