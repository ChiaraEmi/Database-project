package soundwave.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import soundwave.data.Playlist;
import javax.swing.SwingUtilities;

import soundwave.controller.Controller;
import soundwave.data.Album;
import soundwave.data.Artist;
import soundwave.data.Plan;

/**
 * Panel representing the main user dashboard, organized into functional tabs.
 */
public final class UserPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final float TITLE_FONT_SIZE = 22f;
    private static final int BORDER_SIZE = 20;
    private static final int TITLE_MARGIN = 10;
    private static final int FIELD_COLUMNS = 15;
    private static final int SMALL_FIELD_COLUMNS = 5;
    private static final int BUTTON_WIDTH = 220;
    private static final int BUTTON_HEIGHT = 35;
    private static final int INSET_GAP = 6;
    private static final int PREFERRED_SCROLL_PANE_WIDTH = 280;
    private static final int INSET_TOP_LARGE = 12;

    private String currentUsername;

    private final JLabel userLabel = new JLabel();
    private transient Controller controller;

    private ActivateSubscriptionDialog activedialog;
    private SubscriptionStatusDialog statusDialog;

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

    // --- Nuovi componenti per la ricerca contenuti in Esplora ---
    private final JTextField txtContentSearchQuery = new JTextField(FIELD_COLUMNS);
    private final JButton btnSearchContent = new JButton("Cerca Contenuto");
    private final DefaultListModel<String> exploreContentResultsModel = new DefaultListModel<>();
    private final JList<String> exploreContentResultsList = new JList<>(this.exploreContentResultsModel);
    private final JButton btnPlayContent = new JButton("▶ Play");
    private List<soundwave.data.Content> currentContents = new java.util.ArrayList<>();

    // --- Tab 3: Album & Recensioni ---
    private final JTextField txtAlbumSearchQuery = new JTextField(FIELD_COLUMNS);
    private final JButton btnSearchAlbum = new JButton("Cerca Album");
    private final JComboBox<Album> comboAlbumResults = new JComboBox<>();
    private final JButton btnViewAlbum = new JButton("Visualizza Album");
    private final JButton btnSearchAlbumReviews = new JButton("Visualizza Recensioni dell'Album");
    private final JButton btnToggleRecensione = new JButton("Aggiungi / Modifica Recensione");

    // --- Tab 4: Libreria & Playlist ---
    private final JTextField txtPlaylistName = new JTextField(FIELD_COLUMNS);
    private final DefaultListModel<String> likedTracksListModel = new DefaultListModel<>();
    private final JList<String> likedTracksList = new JList<>(this.likedTracksListModel);
    private final DefaultListModel<String> followedArtistsListModel = new DefaultListModel<>();
    private final JList<String> followedArtistsList = new JList<>(this.followedArtistsListModel);
    private final JComboBox<String> comboVisibility = new JComboBox<>(new String[]{"Privata", "Pubblica"});
    private final JCheckBox chkCollaborative = new JCheckBox("Collaborativa");
    private final JButton btnCreatePlaylist = new JButton("Crea Nuova Playlist");
    private final JButton btnToggleLike = new JButton("Aggiungi / Rimuovi Like");
    private final javax.swing.DefaultListModel<String> librarySongsModel = new javax.swing.DefaultListModel<>();
    private final javax.swing.JList<String> librarySongsList = new javax.swing.JList<>(this.librarySongsModel);

    // Campi per Aggiunta Brano in Playlist (Tendina 1)
    private final JComboBox<Playlist> comboUserPlaylists = new JComboBox<>();
    private final JTextField txtAddTrackCode = new JTextField(SMALL_FIELD_COLUMNS);
    private final JButton btnAddTrack = new JButton("Aggiungi Brano");

    // Campi per Rimozione Brano da Playlist (Tendina 2 separata)
    private final JComboBox<Playlist> removeTrackPlaylistCombo = new JComboBox<>();
    private final JTextField txtRemoveTrackCode = new JTextField(SMALL_FIELD_COLUMNS);
    private final JButton btnRemoveTrack = new JButton("Rimuovi Brano");

    // --- Tab 5: Statistiche & Ascolti ---
    private final JButton btnFetchPersonalStats = new JButton("Visualizza Statistiche Annuali");
    private final JComboBox<Integer> comboStatsYear = new JComboBox<>(
        new Integer[]{2026, 2025, 2024});

    private final JTextArea txtStatsOutput = new JTextArea(8, 30);

    private final JButton btnBack = new JButton("Torna alla Selezione Ruolo");

    /**
     * Builds a new UserPanel for the specified user.
     * 
     * @param username the username of the logged-in user.
     */
    public UserPanel(final String username) {
        super();
        this.currentUsername = username;
        this.setLayout(new BorderLayout(0, TITLE_MARGIN));
        this.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));

        final JPanel headerPanel = new JPanel(new BorderLayout());

        final JLabel titleLabel = new JLabel("Area Utente", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(TITLE_FONT_SIZE));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        this.userLabel.setText("Utente: " + this.currentUsername);
        headerPanel.add(this.userLabel, BorderLayout.EAST);

        this.add(headerPanel, BorderLayout.NORTH);

        final JTabbedPane mainTabbedPane = new JTabbedPane();
        mainTabbedPane.addTab("Abbonamento", createSubscriptionTab());
        mainTabbedPane.addTab("Esplora", createExploreTab());
        mainTabbedPane.addTab("Libreria", createLibraryTab());
        mainTabbedPane.addTab("Statistiche Personali", createStatsTab());
        mainTabbedPane.addTab("Album & Recensioni",createAlbumTab());

        this.add(mainTabbedPane, BorderLayout.CENTER);

        final JPanel bottomPanel = new JPanel();
        bottomPanel.add(this.btnBack);
        this.add(bottomPanel, BorderLayout.SOUTH);

        setupPlaylistComboBox(this.comboUserPlaylists);
        setupPlaylistComboBox(this.removeTrackPlaylistCombo);
    }

    /**
     * Sets the application controller.
     * 
     * @param controller the controller instance to set.
     */
    public void setController(final Controller controller) {
        this.controller = controller;
    }

    /**
     * Creates the tab for subscription management.
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

    private JPanel createExploreTab() {
        // Pannello principale diviso in due parti (Sinistra e Destra)
        final JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP));

        // --- COLONNA DI SINISTRA (Genere, Brani, Artisti) ---
        final JPanel leftCol = new JPanel(new GridBagLayout());
        final GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbcLeft.anchor = GridBagConstraints.WEST;

        gbcLeft.gridx = 0; gbcLeft.gridy = 0;
        leftCol.add(new JLabel("Genere:"), gbcLeft);
        gbcLeft.gridx = 1;
        leftCol.add(this.comboGenre, gbcLeft);

        gbcLeft.gridx = 0; gbcLeft.gridy = 1; gbcLeft.gridwidth = 2;
        leftCol.add(this.btnFilterByGenre, gbcLeft);

        this.exploreSongsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        final JScrollPane scrollPaneSongs = new JScrollPane(this.exploreSongsList);
        scrollPaneSongs.setPreferredSize(new Dimension(250, 90));
        gbcLeft.gridx = 0; gbcLeft.gridy = 2; gbcLeft.gridwidth = 2;
        leftCol.add(scrollPaneSongs, gbcLeft);

        this.btnAddLikeFromExplore.setEnabled(false);
        gbcLeft.gridx = 0; gbcLeft.gridy = 3; gbcLeft.gridwidth = 2;
        leftCol.add(this.btnAddLikeFromExplore, gbcLeft);

        // Sezione Artista
        gbcLeft.gridwidth = 1;
        gbcLeft.gridx = 0; gbcLeft.gridy = 4;
        leftCol.add(new JLabel("Cerca Artista:"), gbcLeft);
        gbcLeft.gridx = 1;
        leftCol.add(this.txtArtistSearchQuery, gbcLeft);

        gbcLeft.gridx = 0; gbcLeft.gridy = 5; gbcLeft.gridwidth = 2;
        leftCol.add(this.btnSearchArtist, gbcLeft);

        this.comboArtistResults.setPreferredSize(new Dimension(200, 25));
        gbcLeft.gridx = 0; gbcLeft.gridy = 6; gbcLeft.gridwidth = 2;
        leftCol.add(this.comboArtistResults, gbcLeft);

        gbcLeft.gridx = 0; gbcLeft.gridy = 7; gbcLeft.gridwidth = 2;
        leftCol.add(this.btnViewArtistProfile, gbcLeft);

        this.btnFollowArtist.setEnabled(false);
        gbcLeft.gridx = 0; gbcLeft.gridy = 8; gbcLeft.gridwidth = 2;
        leftCol.add(this.btnFollowArtist, gbcLeft);


        // --- COLONNA DI DESTRA (Ricerca Contenuti e Play) ---
        final JPanel rightCol = new JPanel(new GridBagLayout());
        final GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbcRight.anchor = GridBagConstraints.WEST;

        gbcRight.gridx = 0; gbcRight.gridy = 0;
        rightCol.add(new JLabel("Cerca Contenuto:"), gbcRight);
        
        gbcRight.gridx = 1;
        rightCol.add(this.txtContentSearchQuery, gbcRight);

        gbcRight.gridx = 0; gbcRight.gridy = 1; gbcRight.gridwidth = 2;
        rightCol.add(this.btnSearchContent, gbcRight);

        this.exploreContentResultsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        final JScrollPane scrollPaneContentSearch = new JScrollPane(this.exploreContentResultsList);
        scrollPaneContentSearch.setPreferredSize(new Dimension(280, 150));
        
        gbcRight.gridx = 0; gbcRight.gridy = 2; gbcRight.gridwidth = 2;
        rightCol.add(scrollPaneContentSearch, gbcRight);

        this.btnPlayContent.setEnabled(false);
        gbcRight.gridx = 0; gbcRight.gridy = 3; gbcRight.gridwidth = 2;
        rightCol.add(this.btnPlayContent, gbcRight);


        // Listener per i componenti (rimangono identici)
        this.exploreContentResultsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                this.btnPlayContent.setEnabled(!this.exploreContentResultsList.isSelectionEmpty());
            }
        });

        this.exploreSongsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                final boolean isSelected = !this.exploreSongsList.isSelectionEmpty();
                this.btnAddLikeFromExplore.setEnabled(isSelected);
            }
        });

        // Aggiungiamo le due colonne al pannello principale del tab
        panel.add(leftCol);
        panel.add(rightCol);

        return panel;
    }
    
    /**
     * Creates the tab for personal library and playlists, showing liked tracks on the left
     * and management controls on the right.
     * 
     * @return the library panel.
     */
    private JPanel createLibraryTab() {
        final JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP));

        // Pannello di sinistra diviso tra Brani Preferiti e Artisti Seguiti
        final JPanel leftContainer = new JPanel(new GridLayout(2, 1, 0, 10));
        
        final JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("I tuoi Brani Preferiti"));
        this.likedTracksList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        leftPanel.add(new JScrollPane(this.likedTracksList), BorderLayout.CENTER);
        leftContainer.add(leftPanel);

        final JPanel artistsPanel = new JPanel(new BorderLayout());
        artistsPanel.setBorder(BorderFactory.createTitledBorder("Artisti Seguiti"));
        this.followedArtistsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        artistsPanel.add(new JScrollPane(this.followedArtistsList), BorderLayout.CENTER);
        leftContainer.add(artistsPanel);

        final JScrollPane leftScrollPane = new JScrollPane(leftContainer);
        leftScrollPane.setPreferredSize(new Dimension(PREFERRED_SCROLL_PANE_WIDTH, 0));
        panel.add(leftScrollPane, BorderLayout.WEST);

        // Pannello di destra con i controlli esistenti
        final JPanel rightControlsPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        rightControlsPanel.add(createPlaylistManagementSubPanel(), gbc);

        gbc.gridy++;
        rightControlsPanel.add(createTrackManagementSubPanel(), gbc);

        gbc.gridy++;
        gbc.weighty = 1.0; 
        rightControlsPanel.add(new JPanel(), gbc);

        panel.add(rightControlsPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Sub-panel for playlist creation and general actions.
     * 
     * @return the playlist management sub-panel.
     */
    private JPanel createPlaylistManagementSubPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Gestione Playlist"));
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, 8, INSET_GAP, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Nome Playlist:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(this.txtPlaylistName, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Visibilità:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(this.comboVisibility, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(this.chkCollaborative, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(this.btnCreatePlaylist, gbc);

        gbc.gridy++;
        panel.add(this.btnToggleLike, gbc);

        return panel;
    }

    /**
     * Sub-panel for adding and removing tracks.
     * 
     * @return the track management sub-panel.
     */
    private JPanel createTrackManagementSubPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Modifica Contenuti Playlist"));
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, 8, INSET_GAP, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Aggiungi a:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(this.comboUserPlaylists, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Codice Brano:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(this.txtAddTrackCode, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(this.btnAddTrack, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(INSET_TOP_LARGE, 8, INSET_GAP, 8);
        panel.add(new JLabel("------------------------------------"), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(INSET_GAP, 8, INSET_GAP, 8);
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Rimuovi da:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(this.removeTrackPlaylistCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Codice Brano:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(this.txtRemoveTrackCode, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(this.btnRemoveTrack, gbc);

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
     * Creates the tab for personal listening statistics.
     * 
     * @return the statistics panel.
     */
    private JPanel createStatsTab() {
        final JPanel panel = new JPanel(new BorderLayout(0, INSET_GAP));
        panel.setBorder(BorderFactory.createEmptyBorder(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP));

        final JPanel topStatsPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        topStatsPanel.add(new JLabel("Anno di riferimento:"), gbc);
        
        gbc.gridx = 1;
        topStatsPanel.add(this.comboStatsYear, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        this.btnFetchPersonalStats.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        topStatsPanel.add(this.btnFetchPersonalStats, gbc);

        panel.add(topStatsPanel, BorderLayout.NORTH);

        this.txtStatsOutput.setEditable(false);
        panel.add(new JScrollPane(this.txtStatsOutput), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Gets the current logged-in username.
     * 
     * @return the username string.
     */
    public String getCurrentUsername() {
        return this.currentUsername;
    }

    /**
     * Gets the selected genre from the combo box.
     * 
     * @return the selected genre string.
     */
    public String getSelectedGenre() {
        return (String) this.comboGenre.getSelectedItem();
    }

    /**
     * Gets the playlist name entered in the text field.
     * 
     * @return the playlist name string.
     */
    public String getPlaylistName() {
        return this.txtPlaylistName.getText().trim();
    }

    /**
     * Gets the selected playlist visibility.
     * 
     * @return the visibility string.
     */
    public String getPlaylistVisibility() {
        return (String) this.comboVisibility.getSelectedItem();
    }

    /**
     * Checks whether the playlist is marked as collaborative.
     * 
     * @return true if collaborative, false otherwise.
     */
    public boolean isPlaylistCollaborative() {
        return this.chkCollaborative.isSelected();
    }

    /**
     * Gets the selected playlist from the addition combo box.
     * 
     * @return the selected Playlist object, or null if none.
     */
    public Playlist getSelectedUserPlaylist() {
        return (Playlist) this.comboUserPlaylists.getSelectedItem();
    }

    /**
     * Gets the selected playlist from the removal combo box.
     * 
     * @return the selected Playlist object, or null if none.
     */
    public Playlist getSelectedRemovePlaylist() {
        return (Playlist) this.removeTrackPlaylistCombo.getSelectedItem();
    }

    /** 
     * Gets the track code for adding.
     * 
     * @return the track code string.
     */
    public String getAddTrackCode() {
        return this.txtAddTrackCode.getText().trim();
    }

    /** 
     * Gets the track code for removing.
     * 
     * @return the track code string.
     */
    public String getRemoveTrackCode() {
        return this.txtRemoveTrackCode.getText().trim();
    }

    /**
     * Gets the statistics year from the dropdown menu.
     * 
     * @return the statistics year as a String.
     */
    public String getStatsYear() {
        final Integer selectedYear = (Integer) this.comboStatsYear.getSelectedItem();
        return selectedYear != null ? selectedYear.toString() : "";
    }

    /**
     * Sets the current username for this user panel and updates any relevant UI components.
     * 
     * @param username the username to set.
     */
    public void setCurrentUsername(final String username) {
        this.currentUsername = username;
        if (this.userLabel != null) {
            this.userLabel.setText("Utente: " + this.currentUsername);
            this.userLabel.repaint();
        }
    }

    /**
     * Sets the text of the personal stats output area.
     * 
     * @param text the statistics text to display.
     */
    public void setPersonalStatsOutput(final String text) {
        this.txtStatsOutput.setText(text);
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

    public String getSelectedLibrarySong() {
        return this.librarySongsList.getSelectedValue();
    }

    public String getContentSearchQuery() {
        return this.txtContentSearchQuery.getText().trim();
    }

    public String getSelectedExploreContent() {
        return this.exploreContentResultsList.getSelectedValue();
    }

    public int getSelectedContentDuration() {
        final int selectedIndex = this.exploreContentResultsList.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < this.currentContents.size()) {
            // Sostituisci .getDuration() con il nome effettivo del metodo presente nella tua classe Content
            return this.currentContents.get(selectedIndex).getDuration(); 
        }
        return 0; 
    }

    public void setContentSearchResults(final List<soundwave.data.Content> contents) {
        this.currentContents = contents != null ? contents : new java.util.ArrayList<>();
        
        final List<String> displayItems = this.currentContents.stream()
            .map(c -> c.getContentCode() + " - " + c.getTitle() + " [" + c.getContentType() + "]")
            .toList();
            
        this.exploreContentResultsList.setListData(displayItems.toArray(new String[0]));
    }

    public void addSearchContentListener(final ActionListener listener) {
        this.btnSearchContent.addActionListener(listener);
    }

    public void addPlayContentListener(final ActionListener listener) {
        this.btnPlayContent.addActionListener(listener);
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

    public void setFollowButtonEnabled(final boolean enabled) {
        this.btnFollowArtist.setEnabled(enabled);
    }

    /**
     * Sets the available user playlists in both combo boxes (addition and removal).
     * 
     * @param playlists the list of playlists.
     */
    public void setUserPlaylists(final List<Playlist> playlists) {
        this.comboUserPlaylists.removeAllItems();
        this.removeTrackPlaylistCombo.removeAllItems();
        for (final Playlist p : playlists) {
            this.comboUserPlaylists.addItem(p);
            this.removeTrackPlaylistCombo.addItem(p);
        }
    }

    /**
     * Sets the list of liked tracks strings to display in the UI.
     * 
     * @param likedTracks the list of formatted track strings.
     */
    public void setLikedTracks(final List<String> likedTracks) {
        this.likedTracksListModel.clear();
        for (final String track : likedTracks) {
            this.likedTracksListModel.addElement(track);
        }
    }

    /**
     * Sets the list of followed artists strings to display in the UI.
     * 
     * @param followedArtists the list of formatted artist names.
     */
    public void setFollowedArtists(final List<String> followedArtists) {
        this.followedArtistsListModel.clear();
        for (final String artist : followedArtists) {
            this.followedArtistsListModel.addElement(artist);
        }
    }

    /**
     * Adds an action listener for the activate subscription button.
     * 
     * @param listener the action listener to add.
     */
    public void addActivateSubscriptionListener(final ActionListener listener) {
        this.btnActivateSubscription.addActionListener(listener);
    }

    /**
     * Adds an action listener for the redeem bonus button.
     * 
     * @param listener the action listener to add.
     */
    public void addRedeemBonusListener(final ActionListener listener) {
        this.btnRedeemBonus.addActionListener(listener);
    }

    /**
     * Adds an action listener for the view subscription status button.
     * 
     * @param listener the action listener to add.
     */
    public void addViewSubscriptionStatusListener(final ActionListener listener) {
        this.btnViewSubscriptionStatus.addActionListener(listener);
    }

    /**
     * Adds an action listener for the filter by genre button.
     * 
     * @param listener the action listener to add.
     */
    public void addFilterByGenreListener(final ActionListener listener) {
        this.btnFilterByGenre.addActionListener(listener);
    }

    public void addExploreLikeListener(final ActionListener listener) {
        this.btnAddLikeFromExplore.addActionListener(listener);
    }

    /**
     * Adds an action listener for the search artist button.
     * 
     * @param listener the action listener to add.
     */
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

    /**
     * Adds an action listener for the create playlist button.
     * 
     * @param listener the action listener to add.
     */
    public void addCreatePlaylistListener(final ActionListener listener) {
        this.btnCreatePlaylist.addActionListener(listener);
    }

    /**
     * Adds an action listener for the toggle like button.
     */
    public void addToggleLikeListener(final ActionListener listener) {
        this.btnToggleLike.addActionListener(listener);
    }

    /**
     * Adds an action listener for the add track button.
     * 
     * @param listener the action listener to add.
     */
    public void addAddTrackListener(final ActionListener listener) {
        this.btnAddTrack.addActionListener(listener);
    }

    /**
     * Adds an action listener for the remove track button.
     * 
     * @param listener the action listener to add.
     */
    public void addRemoveTrackListener(final ActionListener listener) {
        this.btnRemoveTrack.addActionListener(listener);
    }

    /**
     * Adds an action listener for the fetch personal stats button.
     * 
     * @param listener the action listener to add.
     */
    public void addFetchPersonalStatsListener(final ActionListener listener) {
        this.btnFetchPersonalStats.addActionListener(listener);
    }

    /**
     * Adds an action listener for the back button.
     * 
     * @param listener the action listener to add.
     */
    public void addBackListener(final ActionListener listener) {
        this.btnBack.addActionListener(listener);
    }

    /**
     * Clears all text fields in the user panel.
     */
    public void clearAllForms() {
        final JTextComponent[] textComponents = {
            this.txtPlaylistName, 
            this.txtAddTrackCode, 
            this.txtRemoveTrackCode,
        };

        for (final JTextComponent component : textComponents) {
            component.setText("");
        }
    }

    /**
     * Configures a combo box to display the playlist name and handles collaborative formatting.
     * 
     * @param comboBox the JComboBox to configure.
     */
    private void setupPlaylistComboBox(final JComboBox<Playlist> comboBox) {
        comboBox.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(
                    final JList<?> list, 
                    final Object value, 
                    final int index, 
                    final boolean isSelected, 
                    final boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Playlist) {
                    final Playlist playlist = (Playlist) value;
                    String displayName = playlist.getPlaylistName();

                    if (!playlist.getUsername().equals(UserPanel.this.currentUsername)) {
                        displayName += " (di " + playlist.getUsername() + " - Collaborativa)";
                    }
                    setText(displayName);
                }
                return this;
            }
        });
    }

    /**
     * Displays the activate subscription dialog with available plans and listeners.
     * 
     * @param username the username.
     * @param plans the list of available plans.
     * @param onActivate the consumer action triggered upon activation.
     */
    public void showActivateSubscriptionDialog(final String username, final List<Plan> plans, 
                                                final Consumer<ActivateSubscriptionDialog.SubscriptionData> onActivate) {
        final JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

        this.activedialog = new ActivateSubscriptionDialog(parent, username, plans);

        activedialog.addApplyPromotionListener(promoCode -> {
            if (this.controller != null) {
                final int planCode = activedialog.getSelectedPlanCode();
                if (planCode <= 0) {
                    activedialog.showError("Seleziona prima un piano di abbonamento.");
                    return;
                }
                this.controller.verifyPromotionCode(promoCode, planCode, result -> {
                    final boolean valid = (boolean) result[0];
                    if (valid) {
                        final double discountValue = (double) result[1];
                        final String discountType = (String) result[2];
                        final double originalPrice = (double) result[3];

                        double discountedPrice = 0.0;
                        if ("Percentuale".equals(discountType)) {
                            discountedPrice = originalPrice * (1 - discountValue / 100.0);
                        } else if ("Fisso".equals(discountType)) {
                            discountedPrice = Math.max(0, originalPrice - discountValue);
                        } 

                        activedialog.updatePriceWithDiscount(discountedPrice);
                        activedialog.setPromoCodeApplied(true);
                        activedialog.showSuccess("Promozione valida");
                    } else {
                        activedialog.showError("Codice promozionale non valido.");
                    }
                });
            }
        });

        activedialog.addActivateListener(onActivate);

        activedialog.addVerifyInviteListener(inviteCode -> {
            if (this.controller != null) {
                this.controller.verifyInviteCode(inviteCode, isValid -> {
                    if (isValid) {
                        final double currentPrice = activedialog.getCurrentPrice();
                        final double discountedPrice = currentPrice * 0.80;
                        activedialog.updatePriceWithDiscount(discountedPrice);
                        activedialog.setInviteCodeVerified(true);
                        activedialog.showSuccess("Codice invito valido! Sconto del 20% applicato.");
                    } else {
                        activedialog.showError("Codice invito non valido.");
                    }
                });
            }
        });

        activedialog.setVisible(true);
    }

    /**
     * Closes the activate subscription dialog if it is open.
     */
    public void closeActivateSubscriptionDialog() {
        if (this.activedialog != null) {
            this.activedialog.closeDialog();
            this.activedialog = null;
        }
    }

    /**
     * Displays the subscription status dialog for the specified user.
     * 
     * @param username the username.
     */
    public void showSubscriptionStatusDialog(final String username) {
        if (this.statusDialog != null && this.statusDialog.isVisible()) {
            this.statusDialog.dispose();
        }

        this.statusDialog = new SubscriptionStatusDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this), 
            username
        );

        this.statusDialog.setOnRenew(subscriptionCode -> {
            System.out.println("OnRenew chiamato per: " + subscriptionCode); 
            if (this.controller != null) {
                this.controller.renewSubscriptionNow(username, subscriptionCode);
            }
        });

        this.statusDialog.setOnToggleAutoRenew(subscriptionCode -> {
            System.out.println("OnToggleAutoRenew chiamato per: " + subscriptionCode); 
            if (this.controller != null) {
                boolean currentState = this.controller.getAutoRenewStatus(username, subscriptionCode);
                this.controller.toggleAutoRenew(username, subscriptionCode, !currentState);
            }
        });
        
        // Carica i dati
        loadSubscriptionData(username);

        this.statusDialog.setOnRefresh(() -> {
            loadSubscriptionData(username);
            this.statusDialog.showSuccess("Dati aggiornati");
        });

        this.statusDialog.setVisible(true);
    }

    /**
     * Loads subscription data from the controller and populates the status dialog.
     * 
     * @param username the username.
     */
    private void loadSubscriptionData(final String username) {
        if (this.controller != null) {
            try {
                final List<Object[]> data = this.controller.getSubscriptionData(username);
                if (this.statusDialog != null) {
                    this.statusDialog.clear();

                    if (data.isEmpty()) {
                        this.statusDialog.addSubscriptionBlock(
                            0, "Nessuna sottoscrizione trovata", "", 
                            "", "", false, "", "", List.of()
                        );
                    } else {
                        for (final Object[] sub : data) {
                            final int subCode = (int) sub[0];
                            final String planType = (String) sub[1];
                            final String startDate = (String) sub[2];
                            final String endDate = (String) sub[3];
                            final String status = (String) sub[4];
                            final boolean autoRenew = (boolean) sub[5];
                            final String promoCode = (String) sub[6];
                            final String inviteCode = (String) sub[7];
                            @SuppressWarnings("unchecked")
                            List<String> transactions = (List<String>) sub[8];

                            this.statusDialog.addSubscriptionBlock(
                                subCode, planType, startDate, endDate, status,
                                autoRenew, promoCode, inviteCode, transactions
                            );
                        }
                    }
                    this.statusDialog.refreshUI();
                }
            } catch (final Exception e) {
                e.printStackTrace();
                if (this.statusDialog != null) {
                    this.statusDialog.showError("Errore durante il caricamento dei dati.");
                }
            }
        }
    }

    public void showRedeemBonusDialog(final String username, final List<Plan> plans, 
                                  final int bonusCredits,
                                  final Consumer<RedeemBonusDialog.RedeemData> onRedeem) {
        
        final JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        final RedeemBonusDialog dialog = new RedeemBonusDialog(parent, username, plans, bonusCredits);
        
        dialog.addRedeemListener(onRedeem);
        dialog.addCancelListener(() -> {
            System.out.println("[DEBUG] Riscatto annullato");
        });
        
        dialog.setVisible(true);
    }


}
