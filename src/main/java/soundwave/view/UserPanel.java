package soundwave.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Panel representing the main user dashboard, organized into functional tabs.
 */
public final class UserPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final float TITLE_FONT_SIZE = 22f;
    private static final int BORDER_SIZE = 20;
    private static final int TITLE_MARGIN = 10;
    private static final int FIELD_COLUMNS = 15;
    private static final int BUTTON_WIDTH = 220;
    private static final int BUTTON_HEIGHT = 35;
    private static final int INSET_GAP = 6;

    // --- Tab 1: Abbonamento ---
    private final JButton btnActivateSubscription = new JButton("Attiva Sottoscrizione");
    private final JButton btnRedeemBonus = new JButton("Riscatto con Crediti Bonus");
    private final JButton btnViewSubscriptionStatus = new JButton("Stato e Storico Transazioni");

    // --- Tab 2: Esplora Catalogo ---
    private final JComboBox<String> comboGenre = new JComboBox<>(new String[]{"Rock", "Pop", "Jazz", "Classica", "Hip Hop"});
    private final JButton btnFilterByGenre = new JButton("Filtra Brani per Genere");
    private final JTextField txtArtistProfileName = new JTextField(FIELD_COLUMNS);
    private final JButton btnSearchArtist = new JButton("Visualizza Profilo Artista");

    // --- Tab 3: Libreria & Playlist ---
    private final JTextField txtPlaylistName = new JTextField(FIELD_COLUMNS);
    private final JButton btnCreatePlaylist = new JButton("Crea Nuova Playlist");
    private final JButton btnToggleLike = new JButton("Aggiungi / Rimuovi Like");

    // --- Tab 4: Statistiche & Ascolti ---
    private final JButton btnFetchPersonalStats = new JButton("Visualizza Statistiche Annuali");
    private final JTextArea txtStatsOutput = new JTextArea(8, 30);

    private final JButton btnBack = new JButton("Torna alla Selezione Ruolo");

    /**
     * Builds a new UserPanel.
     */
    public UserPanel() {
        super();
        this.setLayout(new BorderLayout(0, TITLE_MARGIN));
        this.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));

        final JLabel titleLabel = new JLabel("Area Utente", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(TITLE_FONT_SIZE));
        this.add(titleLabel, BorderLayout.NORTH);

        final JTabbedPane mainTabbedPane = new JTabbedPane();
        mainTabbedPane.addTab("Abbonamento", createSubscriptionTab());
        mainTabbedPane.addTab("Esplora", createExploreTab());
        mainTabbedPane.addTab("Libreria", createLibraryTab());
        mainTabbedPane.addTab("Statistiche Personali", createStatsTab());

        this.add(mainTabbedPane, BorderLayout.CENTER);

        final JPanel bottomPanel = new JPanel();
        bottomPanel.add(this.btnBack);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates the tab for subscription management (OP 2, 4, 5).
     * 
     * @return the subscription panel.
     */
    private JPanel createSubscriptionTab() {
        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;

        final Dimension buttonSize = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

        this.btnActivateSubscription.setPreferredSize(buttonSize);
        gbc.gridy = 0;
        panel.add(this.btnActivateSubscription, gbc);

        this.btnRedeemBonus.setPreferredSize(buttonSize);
        gbc.gridy = 1;
        panel.add(this.btnRedeemBonus, gbc);

        this.btnViewSubscriptionStatus.setPreferredSize(buttonSize);
        gbc.gridy = 2;
        panel.add(this.btnViewSubscriptionStatus, gbc);

        return panel;
    }

    /**
     * Creates the tab for exploring catalog (OP 19, 20).
     * 
     * @return the explore panel.
     */
    private JPanel createExploreTab() {
        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Genere:"), gbc);
        gbc.gridx = 1;
        panel.add(this.comboGenre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(this.btnFilterByGenre, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Nome Artista:"), gbc);
        gbc.gridx = 1;
        panel.add(this.txtArtistProfileName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(this.btnSearchArtist, gbc);

        return panel;
    }

    /**
     * Creates the tab for personal library and playlists (OP 12, 14).
     * 
     * @return the library panel.
     */
    private JPanel createLibraryTab() {
        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nome Playlist:"), gbc);
        gbc.gridx = 1;
        panel.add(this.txtPlaylistName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(this.btnCreatePlaylist, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(this.btnToggleLike, gbc);

        return panel;
    }

    /**
     * Creates the tab for personal listening statistics.
     * 
     * @return the statistics panel.
     */
    private JPanel createStatsTab() {
        final JPanel panel = new JPanel(new BorderLayout(0, INSET_GAP));
        panel.setBorder(BorderFactory.createEmptyBorder(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP));

        this.btnFetchPersonalStats.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        panel.add(this.btnFetchPersonalStats, BorderLayout.NORTH);

        this.txtStatsOutput.setEditable(false);
        panel.add(new JScrollPane(this.txtStatsOutput), BorderLayout.CENTER);

        return panel;
    }

    /* --- Getter per i dati di input --- */

    /**
     * Gets the selected genre from the combo box.
     * 
     * @return the selected genre string.
     */
    public String getSelectedGenre() {
        return (String) this.comboGenre.getSelectedItem();
    }

    /**
     * Gets the artist name entered in the search field.
     * 
     * @return the searched artist name.
     */
    public String getSearchedArtist() {
        return this.txtArtistProfileName.getText();
    }

    /**
     * Gets the playlist name entered in the text field.
     * 
     * @return the playlist name.
     */
    public String getPlaylistName() {
        return this.txtPlaylistName.getText();
    }

    /**
     * Sets the text of the personal stats output area.
     * 
     * @param text the statistics text to display.
     */
    public void setPersonalStatsOutput(final String text) {
        this.txtStatsOutput.setText(text);
    }

    /* --- Metodi per registrare i Listener --- */

    /**
     * Adds an action listener to the activate subscription button.
     * 
     * @param listener the action listener.
     */
    public void addActivateSubscriptionListener(final ActionListener listener) {
        this.btnActivateSubscription.addActionListener(listener);
    }

    /**
     * Adds an action listener to the redeem bonus button.
     * 
     * @param listener the action listener.
     */
    public void addRedeemBonusListener(final ActionListener listener) {
        this.btnRedeemBonus.addActionListener(listener);
    }

    /**
     * Adds an action listener to the view subscription status button.
     * 
     * @param listener the action listener.
     */
    public void addViewSubscriptionStatusListener(final ActionListener listener) {
        this.btnViewSubscriptionStatus.addActionListener(listener);
    }

    /**
     * Adds an action listener to the filter by genre button.
     * 
     * @param listener the action listener.
     */
    public void addFilterByGenreListener(final ActionListener listener) {
        this.btnFilterByGenre.addActionListener(listener);
    }

    /**
     * Adds an action listener to the search artist button.
     * 
     * @param listener the action listener.
     */
    public void addSearchArtistListener(final ActionListener listener) {
        this.btnSearchArtist.addActionListener(listener);
    }

    /**
     * Adds an action listener to the create playlist button.
     * 
     * @param listener the action listener.
     */
    public void addCreatePlaylistListener(final ActionListener listener) {
        this.btnCreatePlaylist.addActionListener(listener);
    }

    /**
     * Adds an action listener to the toggle like button.
     * 
     * @param listener the action listener.
     */
    public void addToggleLikeListener(final ActionListener listener) {
        this.btnToggleLike.addActionListener(listener);
    }

    /**
     * Adds an action listener to the fetch personal stats button.
     * 
     * @param listener the action listener.
     */
    public void addFetchPersonalStatsListener(final ActionListener listener) {
        this.btnFetchPersonalStats.addActionListener(listener);
    }

    /**
     * Adds an action listener to the back button.
     * 
     * @param listener the action listener.
     */
    public void addBackListener(final ActionListener listener) {
        this.btnBack.addActionListener(listener);
    }
}
