package soundwave.view;

import java.awt.CardLayout;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import soundwave.controller.Controller;
import soundwave.data.Album;
import soundwave.data.Artist;

/**
 * Implementation of the {@link View} interface.
 */
public final class ViewImpl extends JFrame implements View {

    private static final long serialVersionUID = 1L;
    public static final String FRAME_NAME = "Soundwave";
    private static final String ROLE_SELECTION_CARD = "ROLE_SELECTION";
    private static final String USER_CARD = "USER";
    private static final String ADMIN_CARD = "ADMIN";

    private final CardLayout layout = new CardLayout();
    private final JPanel mainPanel = new JPanel(layout);

    private final RoleSelectionPanel roleSelectionPanel;
    private final UserPanel userPanel;
    private final AdminPanel adminPanel;

    private Controller controller;

    /**
     * Builds a new ViewImpl with a custom close action.
     *
     * @param onClose the action to execute when the window closes
     */
    public ViewImpl(final Runnable onClose) {
        this();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(final java.awt.event.WindowEvent e) {
                onClose.run();
            }
        });
    }

    /**
     * Builds a new ViewImpl.
     */
    public ViewImpl() {
        setTitle(FRAME_NAME);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.roleSelectionPanel = new RoleSelectionPanel();
        mainPanel.add(roleSelectionPanel, ROLE_SELECTION_CARD);

        this.adminPanel = new AdminPanel();
        mainPanel.add(adminPanel, ADMIN_CARD);

        this.userPanel = new UserPanel();
        mainPanel.add(userPanel, USER_CARD);

        setContentPane(mainPanel);
        pack();
        setMinimumSize(getPreferredSize());
        setLocationRelativeTo(null);
    }

    @Override
    public void setController(final Controller controller) {
        this.controller = controller;

        // --- Navigazione tra i pannelli ---
        this.roleSelectionPanel.addUtenteListener(e -> showPanel(USER_CARD));
        this.roleSelectionPanel.addAdminListener(e -> showPanel(ADMIN_CARD));
        this.adminPanel.addBackListener(e -> showPanel(ROLE_SELECTION_CARD));
        this.userPanel.addBackListener(e -> showPanel(ROLE_SELECTION_CARD));

        // --- Caricamento Utenti (Admin) ---
        this.adminPanel.addFetchUsersListener(e -> {
            if (this.controller != null) {
                this.controller.adminClickedLoadUsers();
            }
        });

        // --- Inserimento Artista (OP 7) ---
        this.adminPanel.addSaveArtistListener(e -> {
            if (this.controller != null) {
                final String stageName = this.adminPanel.getArtistStageName();
                final String name = this.adminPanel.getArtistRealName();
                final String surname = this.adminPanel.getArtistRealSurname();
                final String birthDate = this.adminPanel.getArtistBirthDate();
                final String country = this.adminPanel.getArtistProvenanceCountry();
                final String biography = this.adminPanel.getArtistBiography();
                final String artistType = this.adminPanel.getArtistType();
                
                int startYear = 0;
                try {
                    if (!this.adminPanel.getArtistStartYear().isBlank()) {
                        startYear = Integer.parseInt(this.adminPanel.getArtistStartYear());
                    }
                } catch (final NumberFormatException ex) {
                    // Gestione errore formato anno
                }

                this.controller.adminClickedSaveArtist(
                    stageName, name, surname, birthDate, country, biography, startYear, artistType
                );
            }
        });

        // --- Inserimento Album e Brani (OP 8) ---
        this.adminPanel.addSaveAlbumListener(e -> {
            if (this.controller != null) {
                int artistCode = 0;
                try {
                    if (!this.adminPanel.getAlbumArtistCode().isBlank()) {
                        artistCode = Integer.parseInt(this.adminPanel.getAlbumArtistCode());
                    }
                } catch (final NumberFormatException ex) {
                    // Gestione errore formato codice artista
                }

                final String title = this.adminPanel.getAlbumTitle();
                final String releaseDate = this.adminPanel.getAlbumReleaseDate();
                final String label = this.adminPanel.getAlbumLabel();
                final String rawSongsText = this.adminPanel.getAlbumSongsInput();

                this.controller.adminClickedSaveAlbumWithSongs(artistCode, title, releaseDate, label, rawSongsText);
            }
        });
        
        // --- Inserimento Podcast (OP 9) ---
        this.adminPanel.addSavePodcastListener(e -> {
            if (this.controller != null) {
                int artistCode = 0;
                try {
                    if (!this.adminPanel.getPodcastArtistCode().isBlank()) {
                        artistCode = Integer.parseInt(this.adminPanel.getPodcastArtistCode());
                    }
                } catch (final NumberFormatException ex) {
                    // Gestione errore formato codice artista
                }

                final String name = this.adminPanel.getPodcastName();
                final String description = this.adminPanel.getPodcastDescription();
                final String category = this.adminPanel.getPodcastCategory();

                this.controller.adminClickedSavePodcast(artistCode, name, description, category);
            }
        });
        
        // --- Aggiungi Like da Esplora (OP 14) ---
        this.userPanel.addExploreLikeListener(e -> {
            if (this.controller != null) {
                final String selectedSong = this.userPanel.getSelectedExploreSong();
                int contentCode = parseContentCode(selectedSong);
                this.controller.userClickedAddLike("user", contentCode);
            }
        });

        // --- Rimuovi Like dalla Libreria (OP 14) ---
        this.userPanel.addToggleLikeListener(e -> {
            if (this.controller != null) {
                final String selectedSong = this.userPanel.getSelectedLibrarySong();
                int contentCode = parseContentCode(selectedSong);
                this.controller.userClickedRemoveLike("user", contentCode);
            }
        });

        // --- Cerca Artista (Esplora) ---
        this.userPanel.addSearchArtistListener(e -> {
            if (this.controller != null) {
                final String artistName = this.userPanel.getArtistSearchQuery();
                this.controller.userClickedSearchArtists(artistName); 
            }
        });

        // --- Visualizza Profilo Artista ---
        this.userPanel.addViewArtistProfileListener(e -> {
            if (this.controller != null) {
                final Artist selectedArtist = this.userPanel.getSelectedArtist();
                if (selectedArtist != null) {
                    this.controller.userClickedViewArtistProfile(selectedArtist.getArtistCode());
                }
            }
        });

        // --- Segui Artista (Esplora) ---
        this.userPanel.addFollowArtistListener(e -> {
            if (this.controller != null) {
                final Artist selectedArtist = this.userPanel.getSelectedArtist();
                if (selectedArtist != null) {
                    this.controller.userClickedFollowArtist(selectedArtist.getArtistCode());
                }
            }
        });

        // --- Filtra brani per genere (OP 20) ---
        this.userPanel.addFilterByGenreListener(e -> {
            if (this.controller != null) {
                final String genre = this.userPanel.getSelectedGenre();
                this.controller.userClickedFilterSongsByGenre(genre);
            }
        });

        // --- Album & Recensioni ---
        this.userPanel.addSearchAlbumListener(e -> {
            if (this.controller != null) {
                final String query = this.userPanel.getAlbumSearchQuery();
                this.controller.userClickedSearchAlbums(query);
            }
        });

        this.userPanel.addViewAlbumListener(e -> {
            if (this.controller != null) {
                final Album selectedAlbum = this.userPanel.getSelectedAlbum();
                if (selectedAlbum != null) {
                    this.controller.userClickedViewAlbum(selectedAlbum.getAlbumCode());
                }
            }
        });

        this.userPanel.addSearchAlbumReviewsListener(e -> {
            if (this.controller != null) {
                final Album selectedAlbum = this.userPanel.getSelectedAlbum();
                if (selectedAlbum != null) {
                    this.controller.userClickedViewAlbumReviews(selectedAlbum.getAlbumCode());
                }
            }
        });

        this.userPanel.addToggleRecensioneListener(e -> {
            if (this.controller != null) {
                final Album selectedAlbum = this.userPanel.getSelectedAlbum();
                if (selectedAlbum != null) {
                    this.controller.userClickedToggleReview(selectedAlbum.getAlbumCode());
                }
            }
        });

        // --- Statistiche Globali (OP 22) ---
        this.adminPanel.addFetchStatsListener(e -> {
            if (this.controller != null) {
                int year = 2026;
                try {
                    if (!this.adminPanel.getStatsYear().isBlank()) {
                        year = Integer.parseInt(this.adminPanel.getStatsYear());
                    }
                } catch (final NumberFormatException ex) {
                    // Gestione formato anno non valido
                }
                this.adminPanel.setStatsOutputText(""); // Pulizia o chiamata controller
                this.controller.adminRequestedGlobalStats(year);
            }
        });
    }

    @Override
    public void start() {
        SwingUtilities.invokeLater(() -> this.setVisible(true));
    }

    @Override
    public void showPanel(final String panelName) {
        layout.show(mainPanel, panelName);
    }

    public RoleSelectionPanel getRoleSelectionPanel() {
        return this.roleSelectionPanel;
    }

    @Override
    public void showUsers(final java.util.List<soundwave.data.User> users) {
        final StringBuilder sb = new StringBuilder();
        for (final soundwave.data.User user : users) {
            sb.append(user.toString()).append("\n");
        }
        this.adminPanel.setUsersOutputText(sb.toString());
    }

    @Override
    public void showGlobalStats(final String statsText) {
        this.adminPanel.setStatsOutputText(statsText);
    }

    @Override
    public void showLikedSongs(final java.util.List<String> songs) {
        this.userPanel.setLikedSongs(songs);
    }

    @Override
    public void showFilteredSongs(final java.util.List<String> songs) {
        this.userPanel.setExploreSongs(songs);
    }

    @Override
    public void showArtistSearchResults(final java.util.List<Artist> artists) {
        this.userPanel.setArtistSearchResults(artists);
    }

    @Override
    public void showArtistProfile(final soundwave.data.Artist artist) {
        final String details = "Nome d'arte: " + artist.getStageName() + 
                               "\nPaese: " + artist.getCountry() + 
                               "\nAnno inizio: " + artist.getStartYear() + 
                               "\nBiografia: " + artist.getBiography();
        JOptionPane.showMessageDialog(this, details, "Profilo Artista", JOptionPane.INFORMATION_MESSAGE);
        this.userPanel.setFollowButtonEnabled(true);
    }

    /* --- Implementazione dei metodi View per Album e Recensioni --- */

    @Override
    public void showAlbumSearchResults(final List<Album> albums) {
        this.userPanel.setAlbumSearchResults(albums);
    }

    @Override
    public void showAlbumDetails(final Album.DAO.AlbumWithSongs albumInfo) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Album: ").append(albumInfo.getAlbum().getTitle()).append("\n");
        sb.append("Artista: ").append(albumInfo.getArtistName()).append("\n");
        sb.append("Anno: ").append(albumInfo.getAlbum().getReleaseDate()).append("\n");
        sb.append("Casa Discografica: ").append(albumInfo.getAlbum().getRecordCompany()).append("\n");
        sb.append("Media Voti: ").append(albumInfo.getAlbum().getAverageRating()).append("\n");
        sb.append("Durata Totale: ").append(albumInfo.getAlbum().getTotalDuration()).append("s\n\n");
        sb.append("--- TRACKLIST ---\n");
        
        for (final Album.DAO.AlbumSong song : albumInfo.getSongs()) {
            sb.append(song.getTrackNumber()).append(". ")
              .append(song.getTitle())
              .append(" (").append(song.getDurationSeconds()).append("s)\n");
        }

        JOptionPane.showMessageDialog(
            this,
            sb.toString(),
            "Dettagli Album",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void showAlbumReviews(final List<String> reviews) {
        if (reviews.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Non ci sono recensioni per questo album.",
                "Recensioni Album",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        final javax.swing.JList<String> reviewList = new javax.swing.JList<>(reviews.toArray(new String[0]));
        final JScrollPane scrollPane = new JScrollPane(reviewList);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 200));

        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "Recensioni dell'Album",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public Object[] showReviewInputDialog() {
        final JComboBox<Integer> comboRating = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        final JTextField txtComment = new JTextField(20);

        final Object[] message = {
            "Voto (da 1 a 5):", comboRating,
            "Commento:", txtComment
        };

        final int option = JOptionPane.showConfirmDialog(
            this,
            message,
            "Aggiungi / Modifica Recensione",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            return new Object[]{ comboRating.getSelectedItem(), txtComment.getText() };
        }
        return null;
    }

    /**
     * Metodo di supporto per estrarre l'ID numerico (contentCode) dall'inizio della stringa del brano.
     */
    private int parseContentCode(final String songString) {
        if (songString != null && !songString.isBlank()) {
            try {
                final String idPart = songString.split("[-:]")[0].trim();
                return Integer.parseInt(idPart);
            } catch (final Exception ex) {
                // Fallback in caso di formato stringa differente
            }
        }
        return 1;
    }
}