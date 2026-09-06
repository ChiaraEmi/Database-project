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

import soundwave.data.Album;
import soundwave.data.Artist;

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
    private final javax.swing.DefaultListModel<String> exploreSongsModel = new javax.swing.DefaultListModel<>();
    private final javax.swing.JList<String> exploreSongsList = new javax.swing.JList<>(this.exploreSongsModel);
    private final JButton btnAddLikeFromExplore = new JButton("Aggiungi Like");
    private final JTextField txtArtistSearchQuery = new JTextField(FIELD_COLUMNS);
    private final JButton btnSearchArtist = new JButton("Cerca Artisti");
    private final JComboBox<Artist> comboArtistResults = new JComboBox<>();
    private final JButton btnViewArtistProfile= new JButton("Visualizza Profilo Artista");
    final JButton btnFollowArtist = new JButton("Segui Artista");

    // --- Tab 3: Album & Recensioni ---
    private final JTextField txtAlbumSearchQuery = new JTextField(FIELD_COLUMNS);
    private final JButton btnSearchAlbum = new JButton("Cerca Album");
    private final JComboBox<Album> comboAlbumResults = new JComboBox<>();
    private final JButton btnViewAlbum = new JButton("Visualizza Album");
    private final JButton btnSearchAlbumReviews = new JButton("Visualizza Recensioni dell'Album");
    private final JButton btnToggleRecensione = new JButton("Aggiungi / Modifica Recensione");

    // --- Tab 4: Libreria & Playlist ---

    // --- Tab 4: Libreria & Playlist ---
    private final JTextField txtPlaylistName = new JTextField(FIELD_COLUMNS);
    private final JButton btnCreatePlaylist = new JButton("Crea Nuova Playlist");
    private final javax.swing.DefaultListModel<String> librarySongsModel = new javax.swing.DefaultListModel<>();
    private final javax.swing.JList<String> librarySongsList = new javax.swing.JList<>(this.librarySongsModel);
    private final JButton btnToggleLike = new JButton("Rimuovi Like");
    
    // --- Tab 5: Statistiche & Ascolti ---
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

        // Intestazione principale
        final JLabel titleLabel = new JLabel("Area Utente", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(TITLE_FONT_SIZE));
        this.add(titleLabel, BorderLayout.NORTH);

        // Schede principali dell'utente
        final JTabbedPane mainTabbedPane = new JTabbedPane();
        mainTabbedPane.addTab("Abbonamento", createSubscriptionTab());
        mainTabbedPane.addTab("Esplora", createExploreTab());
        mainTabbedPane.addTab("Libreria", createLibraryTab());
        mainTabbedPane.addTab("Statistiche Personali", createStatsTab());
        mainTabbedPane.addTab("Album & Recensioni",createAlbumTab());

        this.add(mainTabbedPane, BorderLayout.CENTER);

        // Barra inferiore di ritorno
        final JPanel bottomPanel = new JPanel();
        bottomPanel.add(this.btnBack);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates the tab for subscription management (OP 2, 4, 5).
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
     */
    private JPanel createExploreTab() {
        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.anchor = GridBagConstraints.WEST;

        // Sezione Genere
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Genere:"), gbc);
        gbc.gridx = 1;
        panel.add(this.comboGenre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(this.btnFilterByGenre, gbc);

        // --- LISTA DEI BRANI FILTRATI (Posizionata sotto il filtro) ---
        this.exploreSongsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        final JScrollPane scrollPaneSongs = new JScrollPane(this.exploreSongsList);
        scrollPaneSongs.setPreferredSize(new Dimension(300, 100)); // Dimensione della box con la lista
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(scrollPaneSongs, gbc);

        // --- PULSANTE AGGIUNGI LIKE (Inizialmente disabilitato) ---
        this.btnAddLikeFromExplore.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(this.btnAddLikeFromExplore, gbc);

        // Ascoltatore di selezione: abilita il pulsante solo se viene selezionata una riga della lista
        this.exploreSongsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                final boolean isSelected = !this.exploreSongsList.isSelectionEmpty();
                this.btnAddLikeFromExplore.setEnabled(isSelected);
            }
        });

        // Sezione Profilo Artista
        gbc.gridwidth = 1;
    gbc.gridx = 0;
    gbc.gridy = 4;
    panel.add(new JLabel("Cerca Artista:"), gbc);
    gbc.gridx = 1;
    panel.add(this.txtArtistSearchQuery, gbc);

    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.gridwidth = 2;
    panel.add(this.btnSearchArtist, gbc); // Cerca e riempie la tendina sotto

    // Tendina con i risultati della ricerca artisti
    gbc.gridx = 0;
    gbc.gridy = 6;
    gbc.gridwidth = 2;
    this.comboArtistResults.setPreferredSize(new Dimension(220, 25));
    panel.add(this.comboArtistResults, gbc);

    // Pulsante per visualizzare il profilo dell'artista selezionato nella tendina
    gbc.gridx = 0;
    gbc.gridy = 7;
    gbc.gridwidth = 2;
    panel.add(this.btnViewArtistProfile, gbc);

    // Pulsante Segui Artista
    this.btnFollowArtist.setEnabled(false);
    gbc.gridx = 0;
    gbc.gridy = 8;
    gbc.gridwidth = 2;
    panel.add(this.btnFollowArtist, gbc);

    return panel;
    }

    /**
     * Creates the tab for personal library and playlists (OP 12, 14).
     */
    private JPanel createLibraryTab() {
        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.anchor = GridBagConstraints.WEST;

        // Nuova Playlist
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Nome Playlist:"), gbc);
        gbc.gridx = 1;
        panel.add(this.txtPlaylistName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(this.btnCreatePlaylist, gbc);

        // --- LISTA DEI BRANI PREFERITI (Libreria) ---
        this.librarySongsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        final JScrollPane scrollPaneLibrarySongs = new JScrollPane(this.librarySongsList);
        scrollPaneLibrarySongs.setPreferredSize(new Dimension(300, 100));
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(scrollPaneLibrarySongs, gbc);

        // --- PULSANTE RIMUOVI LIKE (Inizialmente disabilitato) ---
        this.btnToggleLike.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(this.btnToggleLike, gbc);

        // Ascoltatore di selezione: abilita il pulsante solo se viene selezionata una riga della lista
        this.librarySongsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                final boolean isSelected = !this.librarySongsList.isSelectionEmpty();
                this.btnToggleLike.setEnabled(isSelected);
            }
        });

        return panel;
    }

    /**
     * Creates the tab for reviews (OP 16,17,18)
     */
    private JPanel createAlbumTab() {
        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.anchor = GridBagConstraints.WEST;

        // Sezione Ricerca Album
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Cerca Album:"), gbc);
        
        gbc.gridx = 1;
        panel.add(this.txtAlbumSearchQuery, gbc);

        // Pulsante Cerca Album
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(this.btnSearchAlbum, gbc);

        // Tendina Risultati Album
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        this.comboAlbumResults.setPreferredSize(new Dimension(220, 25));
        panel.add(this.comboAlbumResults, gbc);

        // Pulsante Visualizza Album
        this.btnViewAlbum.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(this.btnViewAlbum, gbc);

        // Pulsante Visualizza Recensioni dell'Album
        this.btnSearchAlbumReviews.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(this.btnSearchAlbumReviews, gbc);

        // Pulsante Aggiungi / Modifica Recensione
        this.btnToggleRecensione.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(this.btnToggleRecensione, gbc);

        // Ascoltatore tendina album per abilitare i pulsanti operativi
        this.comboAlbumResults.addActionListener(e -> {
            final boolean hasSelected = this.comboAlbumResults.getSelectedItem() != null;
            this.btnViewAlbum.setEnabled(hasSelected);
            this.btnSearchAlbumReviews.setEnabled(hasSelected);
            this.btnToggleRecensione.setEnabled(hasSelected);
        });

        return panel;
    }

    /**
     * Creates the tab for personal listening statistics (OP 21).
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

    public String getSelectedGenre() {
        return (String) this.comboGenre.getSelectedItem();
    }
    
    public String getSelectedExploreSong() {
        return this.exploreSongsList.getSelectedValue();
    }

    public String getArtistSearchQuery() {
        return this.txtArtistSearchQuery.getText();
    }

    public Artist getSelectedArtist() {
        return (Artist) this.comboArtistResults.getSelectedItem();
    }

    public String getAlbumSearchQuery() {
        return this.txtAlbumSearchQuery.getText();
    }

    public Album getSelectedAlbum() {
        return (Album) this.comboAlbumResults.getSelectedItem();
    }

    public String getPlaylistName() {
        return this.txtPlaylistName.getText();
    }

    public String getSelectedLibrarySong() {
        return this.librarySongsList.getSelectedValue();
    }

    /* --- Setter per popolare la vista --- */

    public void setExploreSongs(final java.util.List<String> songs) {
        this.exploreSongsModel.clear();
        for (final String song : songs) {
            this.exploreSongsModel.addElement(song);
        }
        this.btnAddLikeFromExplore.setEnabled(false);
    }

    public void setLikedSongs(final java.util.List<String> songs) {
        this.librarySongsModel.clear();
        for (final String song : songs) {
            this.librarySongsModel.addElement(song);
        }
        this.btnToggleLike.setEnabled(false);
    }

    public void setArtistSearchResults(final java.util.List<Artist> artists) {
        this.comboArtistResults.removeAllItems();
        for (final Artist a : artists) {
            this.comboArtistResults.addItem(a);
        }
    }

    public void setAlbumSearchResults(final java.util.List<Album> albums) {
        this.comboAlbumResults.removeAllItems();
        for (final Album a : albums) {
            this.comboAlbumResults.addItem(a);
        }
    }

    public void setPersonalStatsOutput(final String text) {
        this.txtStatsOutput.setText(text);
    }

    public void setFollowButtonEnabled(final boolean enabled) {
        this.btnFollowArtist.setEnabled(enabled);
    }

    /* --- Metodi per registrare gli Listener --- */

    public void addActivateSubscriptionListener(final ActionListener listener) {
        this.btnActivateSubscription.addActionListener(listener);
    }

    public void addRedeemBonusListener(final ActionListener listener) {
        this.btnRedeemBonus.addActionListener(listener);
    }

    public void addViewSubscriptionStatusListener(final ActionListener listener) {
        this.btnViewSubscriptionStatus.addActionListener(listener);
    }

    public void addFilterByGenreListener(final ActionListener listener) {
        this.btnFilterByGenre.addActionListener(listener);
    }

    public void addExploreLikeListener(final ActionListener listener) {
        this.btnAddLikeFromExplore.addActionListener(listener);
    }

    public void addSearchArtistListener(final ActionListener listener) {
        this.btnSearchArtist.addActionListener(listener);
    }

    public void addViewArtistProfileListener(final ActionListener listener) {
        this.btnViewArtistProfile.addActionListener(listener);
    }

    public void addFollowArtistListener(final ActionListener listener) {
        this.btnFollowArtist.addActionListener(listener);
    }

    // --- Listener Album & Recensioni ---
    public void addSearchAlbumListener(final ActionListener listener) {
        this.btnSearchAlbum.addActionListener(listener);
    }

    public void addViewAlbumListener(final ActionListener listener) {
        this.btnViewAlbum.addActionListener(listener);
    }

    public void addSearchAlbumReviewsListener(final ActionListener listener) {
        this.btnSearchAlbumReviews.addActionListener(listener);
    }

    public void addToggleRecensioneListener(final ActionListener listener) {
        this.btnToggleRecensione.addActionListener(listener);
    }

    public void addCreatePlaylistListener(final ActionListener listener) {
        this.btnCreatePlaylist.addActionListener(listener);
    }

    public void addToggleLikeListener(final ActionListener listener) {
        this.btnToggleLike.addActionListener(listener);
    }

    public void addFetchPersonalStatsListener(final ActionListener listener) {
        this.btnFetchPersonalStats.addActionListener(listener);
    }

    public void addBackListener(final ActionListener listener) {
        this.btnBack.addActionListener(listener);
    }
}
