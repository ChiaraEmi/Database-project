package soundwave.view;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import soundwave.controller.Controller;
import soundwave.data.Artist;
import soundwave.data.Playlist;
import soundwave.data.Podcast;
import soundwave.data.Plan;
import soundwave.data.User;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import soundwave.data.Album;

/**
 * Implementation of the {@link View} interface.
 */
public final class ViewImpl extends JFrame implements View {

    private static final long serialVersionUID = 1L;
    private static final int CURRENT_YEAR = 2026;
    private static final String FRAME_NAME = "Soundwave";
    private static final String ROLE_SELECTION_CARD = "ROLE_SELECTION";
    private static final String USER_CARD = "USER";
    private static final String ADMIN_CARD = "ADMIN";
    private static final String FORMAT_ERROR = "Format Error";
    private static final Logger LOGGER = Logger.getLogger(ViewImpl.class.getName());

    private final CardLayout layout = new CardLayout();
    private final JPanel mainPanel = new JPanel(layout);

    private final RoleSelectionPanel roleSelectionPanel;
    private final AdminPanel adminPanel;
    private UserPanel userPanel;

    private transient Controller controller;

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

        this.userPanel = new UserPanel("Ospite");
        mainPanel.add(userPanel, USER_CARD);

        setContentPane(mainPanel);
        pack();
        setMinimumSize(getPreferredSize());
        setLocationRelativeTo(null);
    }

    @Override
    public void setController(final Controller controller) {
        this.controller = controller;

        initUserPanelListeners();

        this.roleSelectionPanel.addUtenteListener(e -> {
            final String username = JOptionPane.showInputDialog(
                this, 
                "Inserisci il tuo username:", 
                "Login Utente", 
                JOptionPane.QUESTION_MESSAGE
            );

            if (this.controller != null && username != null && !username.isBlank()) {
                this.controller.userLoggedIn(username.trim());
            }
        });

        this.roleSelectionPanel.addAdminListener(e -> {
            showPanel(ADMIN_CARD);
            if (this.controller != null) {
                final List<Artist> artists = this.controller.getAlbumArtists();
                final List<Artist> authors = this.controller.getPodcastAuthors();
                final List<Podcast> podcasts = this.controller.getPodcasts();
                this.adminPanel.setAlbumArtists(artists);
                this.adminPanel.setPodcastAuthors(authors);
                this.adminPanel.setPodcasts(podcasts);
            }
        });
        this.adminPanel.addBackListener(e -> showPanel(ROLE_SELECTION_CARD));
        this.userPanel.addBackListener(e -> showPanel(ROLE_SELECTION_CARD));

        this.adminPanel.addFetchUsersListener(e -> {
            if (this.controller != null) {
                this.controller.adminClickedLoadUsers();
            }
        });

        // --- Inserimento Promozione (OP 6) ---
        this.adminPanel.addSavePromotionListener(e -> {
            if (this.controller != null) {
                final String code = this.adminPanel.getPromoCode();
                final String name = this.adminPanel.getPromoName();
                final String description = this.adminPanel.getPromoDescription();
                final String startDate = this.adminPanel.getPromoStartDate();
                final String endDate = this.adminPanel.getPromoEndDate();
                final String discountType = this.adminPanel.getDiscountType();
                final String discountValue = this.adminPanel.getDiscountValue();
                final String requiredMonths = this.adminPanel.getRequiredMonths();
                final String planCodes = this.adminPanel.getPromoPlanCodes();

                final boolean success = this.controller.adminClickedSavePromotion(
                    code, name, description, startDate, endDate, discountType, discountValue, 
                    requiredMonths, planCodes
                );

                if (success) {
                    showSuccess("Promozione inserita con successo!");
                    this.adminPanel.clearAllForms();
                }
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
                    LOGGER.log(Level.SEVERE, "Invalid artist start year format", ex);
                    JOptionPane.showMessageDialog(this, "Please enter a valid start year (e.g., 2020).", 
                                                FORMAT_ERROR, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                final boolean success = this.controller.adminClickedSaveArtist(
                    stageName, name, surname, birthDate, country, biography, startYear, artistType
                );

                if (success) {
                    showSuccess("Artista inserito con successo!");
                    this.adminPanel.clearAllForms();

                    if (this.controller != null) {
                        this.adminPanel.setPodcastAuthors(this.controller.getPodcastAuthors());
                        this.adminPanel.setAlbumArtists(this.controller.getAlbumArtists());
                    }
                }
            }
        });

        this.adminPanel.addSaveAlbumListener(e -> {
            if (this.controller != null) {
                int artistCode = 0;
                try {
                    if (!this.adminPanel.getAlbumArtistCode().isBlank()) {
                        artistCode = Integer.parseInt(this.adminPanel.getAlbumArtistCode());
                    }
                } catch (final NumberFormatException ex) {
                    LOGGER.log(Level.SEVERE, "Invalid album artist code format", ex);
                    JOptionPane.showMessageDialog(this, "Artist code must be a valid number.", 
                                                FORMAT_ERROR, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                final String title = this.adminPanel.getAlbumTitle();
                final String releaseDate = this.adminPanel.getAlbumReleaseDate();
                final String label = this.adminPanel.getAlbumLabel();
                final String rawSongsText = this.adminPanel.getAlbumSongsInput();

                final boolean success = this.controller.adminClickedSaveAlbumWithSongs(
                    artistCode, title, releaseDate, label, rawSongsText
                );

                if (success) {
                    showSuccess("Album e brani inseriti con successo!");
                    this.adminPanel.clearAllForms();
                }
            }
        });

        this.adminPanel.addSavePodcastListener(e -> {
            if (this.controller != null) {
                int artistCode = 0;
                try {
                    if (!this.adminPanel.getPodcastArtistCode().isBlank()) {
                        artistCode = Integer.parseInt(this.adminPanel.getPodcastArtistCode());
                    }
                } catch (final NumberFormatException ex) {
                    LOGGER.log(Level.SEVERE, "Invalid podcast artist code format", ex);
                    JOptionPane.showMessageDialog(this, "Podcast artist code must be a valid number.", 
                                                FORMAT_ERROR, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                final String name = this.adminPanel.getPodcastName();
                final String description = this.adminPanel.getPodcastDescription();
                final String category = this.adminPanel.getPodcastCategory();

                final boolean success = this.controller.adminClickedSavePodcast(artistCode, name, description, category);

                if (success) {
                    showSuccess("Podcast inserito con successo!");
                    this.adminPanel.clearAllForms();

                    if (this.controller != null) {
                        final List<Podcast> updatedPodcasts = this.controller.getPodcasts();
                        this.adminPanel.setPodcasts(updatedPodcasts);
                    }
                }
            }
        });

        this.adminPanel.addSaveEpisodeListener(e -> {
            if (this.controller != null) {
                int podcastCode = 0;
                try {
                    if (!this.adminPanel.getEpisodePodcastCode().isBlank()) {
                        podcastCode = Integer.parseInt(this.adminPanel.getEpisodePodcastCode());
                    }
                } catch (final NumberFormatException ex) {
                    LOGGER.log(Level.SEVERE, "Invalid episode podcast code format", ex);
                    JOptionPane.showMessageDialog(this, "Podcast code must be a valid number.", 
                                                FORMAT_ERROR, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                final String title = this.adminPanel.getEpisodeTitle();

                int duration = 0;
                try {
                    if (!this.adminPanel.getEpisodeDuration().isBlank()) {
                        duration = Integer.parseInt(this.adminPanel.getEpisodeDuration());
                    }
                } catch (final NumberFormatException ex) {
                    LOGGER.log(Level.SEVERE, "Invalid episode duration format", ex);
                    JOptionPane.showMessageDialog(this, "Duration in seconds must be a valid number.", 
                                                FORMAT_ERROR, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                final String description = this.adminPanel.getEpisodeDescription();

                int episodeNumber = 0;
                try {
                    if (!this.adminPanel.getEpisodeNumber().isBlank()) {
                        episodeNumber = Integer.parseInt(this.adminPanel.getEpisodeNumber());
                    }
                } catch (final NumberFormatException ex) {
                    LOGGER.log(Level.SEVERE, "Invalid episode number format", ex);
                    JOptionPane.showMessageDialog(this, "Episode number must be a valid integer.", 
                                                FORMAT_ERROR, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                final boolean success = this.controller.adminClickedSaveEpisode(
                    podcastCode, title, duration, description, episodeNumber
                );

                if (success) {
                    showSuccess("Episodio inserito con successo!");
                    this.adminPanel.clearAllForms();
                }
            }
        });

        this.adminPanel.addFetchGlobalAlbumsListener(e -> {
            if (this.controller != null) {
                this.controller.adminRequestedGlobalAlbums(); 
            }
        });

        this.adminPanel.addFetchYearlyStatsListener(e -> {
            if (this.controller != null) {
                int year = CURRENT_YEAR;
                try {
                    final Object rawYear = this.adminPanel.getStatsYear();
                    if (rawYear instanceof Integer) {
                        year = (Integer) rawYear;
                    } else if (rawYear instanceof String && !((String) rawYear).isBlank()) {
                        year = Integer.parseInt((String) rawYear);
                    } else if (rawYear != null) {
                        year = Integer.parseInt(rawYear.toString());
                    }
                } catch (final NumberFormatException ex) {
                    LOGGER.log(Level.SEVERE, "Invalid stats year format", ex);
                    JOptionPane.showMessageDialog(this, "Reference year must be a valid number.", 
                                                FORMAT_ERROR, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                this.controller.adminRequestedYearlyStats(year);
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

    @Override
    public void openUserPanel(final String username) {
        this.userPanel.setCurrentUsername(username);

        if (this.controller != null) {
            final List<Playlist> playlists = this.controller.getUserPlaylists(username);
            this.userPanel.setUserPlaylists(playlists);

            final var likedTracks = this.controller.getUserLikedTracks(username);
            final List<String> formattedTracks = likedTracks.stream()
                .map(l -> "[" + l.getTrackCode() + "] " + l.getTrackTitle())
                .toList();
            this.userPanel.setLikedTracks(formattedTracks);

            final List<String> followedArtists = this.controller.getFollowedArtists(username);
            this.userPanel.setFollowedArtists(followedArtists);
        }

        showPanel(USER_CARD);
    }

    /**
     * Aggiorna le liste dei brani preferiti e degli artisti seguiti nel pannello utente.
     * 
     * @param username l'utente corrente.
     */
    public void refreshUserData(final String username) {
        if (this.controller != null) {
            final var likedTracks = this.controller.getUserLikedTracks(username);
            final List<String> formattedTracks = likedTracks.stream()
                .map(l -> "[" + l.getTrackCode() + "] " + l.getTrackTitle())
                .toList();
            this.userPanel.setLikedTracks(formattedTracks);

            final List<String> followedArtists = this.controller.getFollowedArtists(username);
            this.userPanel.setFollowedArtists(followedArtists);
        }
    }

    @Override
    public void showUsers(final List<User> users) {
        final List<Object[]> rows = new ArrayList<>();
        for (final User user : users) {
            rows.add(new Object[] {
                user.getUsername(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getCountry(),
                user.getBonusCredit(),
            });
        }
        this.adminPanel.setUsersTableData(rows);
    }

    @Override
    public void showPersonalStats(final String statsText) {
        this.userPanel.setPersonalStatsOutput(statsText);
    }

    @Override
    public void showGlobalAlbumsStats(final String statsText) {
        this.adminPanel.setGlobalAlbumsOutputText(statsText);
    }

    @Override
    public void showYearlyStats(final String statsText) {
        this.adminPanel.setYearlyStatsOutputText(statsText);
    }

    @Override
    public void showActivateSubsriptionDialog(final String username, final List<Plan> plans) {
        this.userPanel.showActivateSubscriptionDialog(username, plans, data -> {
            if (this.controller != null) {
                this.controller.userActivateSubscription(username, data);
            }
        });
    }

    @Override
    public void setAlbumArtists(final List<Artist> artists) {
        this.adminPanel.setAlbumArtists(artists);
    }

    @Override
    public void setPodcastAuthors(final List<Artist> authors) {
        this.adminPanel.setPodcastAuthors(authors);
    }

    @Override
    public void setPodcasts(final List<Podcast> podcasts) {
        this.adminPanel.setPodcasts(podcasts);
    }

    @Override
    public void showError(final String message) {
        JOptionPane.showMessageDialog(this, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    @Override 
    public void showSuccess(final String message) {
        JOptionPane.showMessageDialog(this, message, "Successo", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override 
    public void showSuccessAndCloseDialog(final String message) {
        JOptionPane.showMessageDialog(this, message, "Successo", JOptionPane.INFORMATION_MESSAGE);
        this.userPanel.closeActivateSubscriptionDialog();
    }

    /**
     * Gets the role selection panel.
     *
     * @return the role selection panel.
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "UI panels are stateful components managed as internal view references."
    )
    public RoleSelectionPanel getRoleSelectionPanel() {
        return this.roleSelectionPanel;
    }

    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "UI panels are stateful components managed as internal view references."
    )
    @Override 
    public UserPanel getUserPanel() {
        return userPanel;
    }

    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "UI panels are stateful components managed as internal view references."
    )
    @Override 
    public AdminPanel getAdminPanel() {
        return adminPanel;
    }

    private void initUserPanelListeners() {
        this.userPanel.setController(this.controller);
        this.userPanel.addBackListener(e -> showPanel(ROLE_SELECTION_CARD));

        this.userPanel.addCreatePlaylistListener(e -> {
            if (this.controller != null) {
                final String playlistName = this.userPanel.getPlaylistName();
                final String visibility = this.userPanel.getPlaylistVisibility();
                final boolean isCollaborative = this.userPanel.isPlaylistCollaborative();
                final String currentUsername = this.userPanel.getCurrentUsername();

                final boolean success = this.controller.userClickedCreatePlaylist(
                    currentUsername, playlistName, visibility, isCollaborative
                );

                if (success) {
                    this.userPanel.clearAllForms();
                }
            }
        });

        this.userPanel.addAddTrackListener(e -> {
            if (this.controller != null) {
                final String currentUsername = this.userPanel.getCurrentUsername();

                final Playlist selectedPlaylist = this.userPanel.getSelectedUserPlaylist();
                if (selectedPlaylist == null) {
                    showError("Seleziona una playlist dalla lista.");
                    return;
                }
                final int playlistCode = selectedPlaylist.getPlaylistCode();

                int trackCode = 0;
                try {
                    if (!this.userPanel.getAddTrackCode().isBlank()) {
                        trackCode = Integer.parseInt(this.userPanel.getAddTrackCode());
                    }
                } catch (final NumberFormatException ex) {
                    LOGGER.log(Level.SEVERE, "Invalid track code format", ex);
                    JOptionPane.showMessageDialog(this, "Track code must be a valid number.", 
                                                FORMAT_ERROR, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                final boolean success = this.controller.userClickedAddTrackToPlaylist(
                    currentUsername, playlistCode, trackCode
                );

                if (success) {
                    this.userPanel.clearAllForms();
                }
            }
        });

        this.userPanel.addRemoveTrackListener(e -> {
            if (this.controller != null) {
                final String currentUsername = this.userPanel.getCurrentUsername();

                final Playlist selectedPlaylist = this.userPanel.getSelectedRemovePlaylist();
                if (selectedPlaylist == null) {
                    showError("Seleziona una playlist dalla lista.");
                    return;
                }
                final int playlistCode = selectedPlaylist.getPlaylistCode();

                int trackCode = 0;
                try {
                    if (!this.userPanel.getRemoveTrackCode().isBlank()) {
                        trackCode = Integer.parseInt(this.userPanel.getRemoveTrackCode());
                    }
                } catch (final NumberFormatException ex) {
                    LOGGER.log(Level.SEVERE, "Invalid track code format", ex);
                    JOptionPane.showMessageDialog(this, "Track code must be a valid number.", 
                                                FORMAT_ERROR, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                final boolean success = this.controller.userClickedRemoveTrackFromPlaylist(
                    currentUsername, playlistCode, trackCode
                );

                if (success) {
                    this.userPanel.clearAllForms();
                }
            }
        });

        this.userPanel.addActivateSubscriptionListener(e -> {
            if (this.controller != null) {
                final String currentUsername = this.userPanel.getCurrentUsername();
                this.controller.userRequestedSubscriptionPlans(currentUsername);
            }
        });

        this.userPanel.addViewSubscriptionStatusListener(e -> {
            if (this.controller != null) {
                final String currentUsername = this.userPanel.getCurrentUsername();
                this.userPanel.showSubscriptionStatusDialog(currentUsername);
            }
        });

        this.userPanel.addFetchPersonalStatsListener(e -> {
            if (this.controller != null) {
                final String currentUsername = this.userPanel.getCurrentUsername();
                int year = CURRENT_YEAR;
                try {
                    final Object rawYear = this.userPanel.getStatsYear();
                    if (rawYear instanceof Integer) {
                        year = (Integer) rawYear;
                    } else if (rawYear instanceof String && !((String) rawYear).isBlank()) {
                        year = Integer.parseInt((String) rawYear);
                    } else if (rawYear != null) {
                        year = Integer.parseInt(rawYear.toString());
                    }
                } catch (final NumberFormatException ex) {
                    LOGGER.log(Level.SEVERE, "Invalid personal stats year format", ex);
                    JOptionPane.showMessageDialog(this, "L'anno di riferimento deve essere un numero valido.", 
                                                FORMAT_ERROR, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                this.controller.userRequestedPersonalStats(currentUsername, year);
            }
        });
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
