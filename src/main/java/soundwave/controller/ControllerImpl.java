package soundwave.controller;

import soundwave.data.Artist;
import soundwave.data.DAOException;
import soundwave.data.LikeBrani;
import soundwave.data.Plan;
import soundwave.data.Playlist;
import soundwave.data.Podcast;
import soundwave.data.SongInput;
import soundwave.data.User;
import soundwave.model.Model;
import soundwave.view.ActivateSubscriptionDialog;
import soundwave.view.View;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.logging.Level;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Concrete implementation of the {@link Controller} interface.
 */
public final class ControllerImpl implements Controller {

    private static final Logger LOGGER = Logger.getLogger(ControllerImpl.class.getName());

    private static final int ARTIST_CODE_INDEX = 4;
    private static final int GENRES_INDEX = 5;

    private static final String NEW_LINE = "\n";
    private static final String SECTION_CLOSE_SUFFIX = ") ===";
    private static final int INITIAL_BUILDER_CAPACITY = 512;

    private final Model model;
    private final View view;

    /**
     * Constructs a new ControllerImpl.
     *
     * @param model the application model.
     * @param view the application view.
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP2", 
        justification = "Model and View are managed externally as architectural references."
    )
    public ControllerImpl(final Model model, final View view) {
        Objects.requireNonNull(model, "Controller created with null model");
        Objects.requireNonNull(view, "Controller created with null view");
        this.model = model;
        this.view = view;
    }

    // ==========================================
    // ABBONAMENTI E PROMOZIONI (da Versione 1)
    // ==========================================

    @Override
    public void adminClickedSavePromotion(final String code, final String name, final String description, final String startDate, final String endDate, final String discountType, 
                                          final String discountValueStr, final String rqrMonths, final String planCodesStr) {
        try {
            final LocalDate start = LocalDate.parse(startDate);
            final LocalDate end = LocalDate.parse(endDate);
            final double discountValue = Double.parseDouble(discountValueStr);
            final Integer requiredMonths = (rqrMonths == null || rqrMonths.isBlank()) ? null : Integer.parseInt(rqrMonths);
            final List<Integer> planCodes = new ArrayList<>();
            if (planCodesStr != null && !planCodesStr.isBlank()) {
                for (final String codePlan : planCodesStr.split(",")) {
                    planCodes.add(Integer.parseInt(codePlan.trim()));
                }
            }

            if (start.isAfter(end)) {
                this.view.showError("La data inizio non può essere dopo la data fine");
                return;
            }
            if (planCodes.isEmpty()) {
                this.view.showError("Devi specificare almeno un piano di abbonamento");
                return;
            }
            if (discountValue <= 0.0) {
                this.view.showError("Il valore dello sconto deve essere maggiore di 0");
                return;
            }

            this.model.insertPromotion(code, name, description, start, end, discountType, discountValue, requiredMonths, planCodes);
            this.view.showSuccess("Promozione creata con successo");
        } catch (final java.time.format.DateTimeParseException e) {
            LOGGER.log(Level.WARNING, "Invalid date format for promotion", e);
            this.view.showError("Formato data non valido. Usa YYYY-MM-DD.");
        } catch (final NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid number format for promotion", e);
            this.view.showError("Valore numerico non valido. Controlla sconto, mesi richiesti e codici piani.");
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save promotion", e);
            this.view.showError("Impossibile salvare la promozione nel database.");
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error saving promotion", e);
            this.view.showError("Errore imprevisto: " + e.getMessage());
        }
    }

    @Override
    public void userRequestedSubscriptionPlans(final String username) {
        try {
            final List<Plan> plans = this.model.getSubscriptioPlans();
            this.view.showActivateSubsriptionDialog(username, plans);
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load subscription plans", e);
            this.view.showError("Impossibile caricare i piani di abbonamento");
        }
    }

    @Override
    public void userActivateSubscription(final String username, final ActivateSubscriptionDialog.SubscriptionData data) {
        try {
            final int subscriptionCode = this.model.activateSubscription(username, data.planCode, data.paymentMethod, data.promoCode, data.inviteCode, data.autoRenew);
            this.view.showSuccessAndCloseDialog("Sottoscrizione attivata con successo! Codice: " + subscriptionCode);
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to activate subscription", e);
            this.view.showError("Impossibile attivare la sottoscrizione: " + e.getMessage());
        }
    }

    @Override
    public void verifyInviteCode(final String inviteCode, final Consumer<Boolean> callback) {
        try {
            boolean exists = this.model.verifyInviteCode(inviteCode);
            callback.accept(exists);
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to verify invite code", e);
            callback.accept(false);
        }
    }

    @Override
    public void verifyPromotionCode(final String promoCode, final int planCode, final Consumer<Object[]> callback) {
        try {
            Object[] result = this.model.verifyPromotionCode(promoCode, planCode);
            callback.accept(result);
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to verify promotion code", e);
            callback.accept(new Object[]{false, 0, null, 0});
        } 
    }

    @Override
    public List<Object[]> getSubscriptionData(final String username) {
        try {
            return this.model.getSubscriptionData(username);
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load subscription data", e);
            this.view.showError("Impossibile caricare i dati: " + e.getMessage());
            return List.of();
        }
    }

    // ==========================================
    // AUTENTICAZIONE E GESTIONE ARTISTI/CONTENUTI
    // ==========================================

    @Override
    public boolean userLoggedIn(final String username) {
        if (username == null || username.isBlank()) {
            final String errorMessage = "Inserisci un username valido.";
            LOGGER.log(Level.WARNING, errorMessage);
            this.view.showError(errorMessage);
            return false;
        }

        try {
            final User user = this.model.findUser(username); 

            if (user != null) {
                LOGGER.log(Level.INFO, "User successfully logged in: {0}", username);
                this.view.openUserPanel(username);
                return true;
            } else {
                final String errorMessage = "Utente non trovato nel database. Verifica il nome inserito.";
                LOGGER.log(Level.WARNING, errorMessage);
                this.view.showError(errorMessage);
                return false;
            }
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to verify user existence during login", e);
            this.view.showError("Errore durante la verifica dell'utente nel database.");
            return false;
        }
    }

    @Override
    public boolean adminClickedSaveArtist(final String stageName, final String name, final String surname, 
                                          final String birthDateStr, final String provenanceCountry, 
                                          final String biography, final int startYear, final String artistType) {

        if (stageName == null || stageName.isBlank() || provenanceCountry == null || provenanceCountry.isBlank()
            || artistType == null || artistType.isBlank() || startYear <= 0) {
            final String errorMessage = "Compila i campi obbligatori (Nome d'arte, Paese, Anno e Tipo Artista).";
            LOGGER.log(Level.WARNING, errorMessage);
            this.view.showError(errorMessage);
            return false;
        }

        try {
            final String realName = (name == null || name.isBlank()) ? null : name;
            final String realSurname = (surname == null || surname.isBlank()) ? null : surname;
            final LocalDate birthDate = birthDateStr == null || birthDateStr.isBlank() 
                ? null : LocalDate.parse(birthDateStr);
            final String bio = (biography == null || biography.isBlank()) ? null : biography;

            this.model.insertArtist(stageName, realName, realSurname, birthDate, provenanceCountry,
                                    bio, startYear, artistType);

            return true;
        } catch (final java.time.format.DateTimeParseException | DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save artist", e);
            this.view.showError("Errore durante il salvataggio dell'artista nel database.");
            return false;
        }
    }

    @Override
    public boolean adminClickedSaveAlbumWithSongs(final int artistCode, final String title, final String releaseDate, 
                                                  final String recordCompany, final String rawSongsText) {

        if (artistCode <= 0 || title == null || title.isBlank() || releaseDate == null || releaseDate.isBlank()
            || recordCompany == null || recordCompany.isBlank() || rawSongsText == null || rawSongsText.isBlank()) {
            final String errorMessage = "Compila i campi obbligatori (Artista, Titolo, Data e Casa Discografica).";
            LOGGER.log(Level.WARNING, errorMessage);
            this.view.showError(errorMessage);
            return false;
        }

        try {
            final List<SongInput> songs = parseSongsInput(rawSongsText);
            this.model.insertAlbumWithSongs(artistCode, title, releaseDate, recordCompany, songs);
            return true;
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save album with songs", e);
            this.view.showError("Errore durante il salvataggio dell'album con i brani.");
            return false;
        }
    }

    @Override
    public List<Artist> getAlbumArtists() {
        try {
            return this.model.getAlbumArtists();
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load album authors", e);
            this.view.showError("Errore durante il caricamento degli autori di album.");
            return List.of();
        }
    }

    @Override
    public List<Artist> getPodcastAuthors() {
        try {
            return this.model.getPodcastAuthors();
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load podcast authors", e);
            this.view.showError("Errore durante il caricamento degli autori di podcast.");
            return List.of();
        }
    }

    @Override
    public List<Podcast> getPodcasts() {
        try {
            return this.model.getPodcasts();
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load podcasts", e);
            this.view.showError("Errore durante il caricamento dei podcast.");
            return List.of();
        }
    }

    @Override
    public boolean adminClickedSavePodcast(final int artistCode, final String name, 
                                         final String description, final String category) {

        if (artistCode <= 0 || name == null || name.isBlank() || category == null || category.isBlank()) {
            final String errorMessage = "Compila i campi obbligatori del podcast (Artista, Nome e Categoria).";
            LOGGER.log(Level.WARNING, errorMessage);
            this.view.showError(errorMessage);
            return false;
        }

        try {
            if (!this.model.isPodcastAuthor(artistCode)) {
                final String errorMessage = "L'artista selezionato non è abilitato come autore di podcast.";
                LOGGER.log(Level.WARNING, errorMessage);
                this.view.showError(errorMessage);
                return false;
            }

            final String desc = (description == null || description.isBlank()) ? null : description;
            this.model.insertPodcast(artistCode, name, desc, category);
            return true;
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save podcast", e);
            this.view.showError("Errore durante il salvataggio del podcast.");
            return false;
        }
    }

    @Override
    public boolean adminClickedSaveEpisode(final int podcastCode, final String title, 
                                         final int duration, final String description, 
                                         final int episodeNumber) {

        if (podcastCode <= 0 || title == null || title.isBlank() || duration <= 0 || episodeNumber <= 0) {
            final String errorMessage = "Compila i campi obbligatori dell'episodio (Podcast, Titolo, Durata e Numero Episodio).";
            LOGGER.log(Level.WARNING, errorMessage);
            this.view.showError(errorMessage);
            return false;
        }

        try {
            final String desc = (description == null || description.isBlank()) ? null : description;
            this.model.insertEpisode(podcastCode, title, duration, desc, episodeNumber);
            return true;
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save episode", e);
            this.view.showError("Errore durante il salvataggio dell'episodio.");
            return false;
        }
    }

    @Override
    public boolean userGeneratedListeningEvent(final String username, final int contentCode, 
                                             final String device, final int eventDuration) {

        if (username == null || username.isBlank() || contentCode <= 0 || device == null || device.isBlank() 
            || eventDuration <= 0) {
            final String errorMessage = "Compila tutti i campi obbligatori (Username, Codice Contenuto, Dispositivo e Durata).";
            LOGGER.log(Level.WARNING, errorMessage);
            this.view.showError(errorMessage);
            return false;
        }

        try {
            this.model.insertListeningEvent(username, contentCode, device, eventDuration);
            return true;
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to generate listening event", e);
            this.view.showError("Errore durante la registrazione dell'evento di ascolto.");
            return false;
        }
    }

    // ==========================================
    // PLAYLIST, PREFERITI E LIBRERIA UTENTE
    // ==========================================

    @Override
    public boolean userClickedCreatePlaylist(final String username, final String playlistName, 
                                        final String visibility, final boolean isCollaborative) {
        if (playlistName == null || playlistName.isBlank()) {
            final String errorMessage = "Inserisci un nome valido per la playlist.";
            LOGGER.log(Level.WARNING, errorMessage);
            this.view.showError(errorMessage);
            return false;
        }

        try {
            final int playlistId = this.model.insertPlaylist(username, playlistName.trim(), visibility, isCollaborative);

            if (playlistId > 0) {
                LOGGER.log(Level.INFO, "Playlist ''{0}'' created successfully for user {1}", 
                            new Object[]{playlistName, username});
                this.view.showSuccess("Playlist creata con successo!");

                final List<Playlist> updatedPlaylists = this.model.getUserPlaylists(username);
                this.view.getUserPanel().setUserPlaylists(updatedPlaylists);

                return true;
            } else {
                final String errorMessage = "Impossibile creare la playlist nel database.";
                LOGGER.log(Level.WARNING, errorMessage);
                this.view.showError(errorMessage);
                return false;
            }
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to create playlist", e);
            this.view.showError("Errore durante la creazione della playlist.");
            return false;
        }
    }

    @Override
    public List<Playlist> getUserPlaylists(final String username) {
        if (username == null || username.isBlank()) {
            final String errorMessage = "Username non valido per il caricamento delle playlist.";
            LOGGER.log(Level.WARNING, errorMessage);
            this.view.showError(errorMessage);
            return List.of();
        }

        try {
            return this.model.getUserPlaylists(username);
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load playlists for user: " + username, e);
            this.view.showError("Errore durante il caricamento delle playlist dell'utente.");
            return List.of();
        }
    }

    @Override
    public boolean userClickedAddTrackToPlaylist(final String username, final int playlistCode, final int trackCode) {
        if (username == null || username.isBlank() || playlistCode <= 0 || trackCode <= 0) {
            final String errorMessage = "Parametri non validi per l'aggiunta del brano.";
            LOGGER.log(Level.WARNING, errorMessage);
            this.view.showError(errorMessage);
            return false;
        }

        try {
            final boolean success = this.model.addTrackToPlaylist(username, playlistCode, trackCode);
            if (success) {
                this.view.showSuccess("Brano aggiunto alla playlist con successo!");
            } else {
                final String errorMessage = "Impossibile aggiungere il brano: verifica di avere i permessi o che il brano non sia già presente.";
                this.view.showError(errorMessage);
            }
            return success;
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to add track to playlist", e);
            this.view.showError("Errore durante l'aggiunta del brano alla playlist.");
            return false;
        }
    }

    @Override
    public boolean userClickedRemoveTrackFromPlaylist(final String username, final int playlistCode, final int trackCode) {
        if (username == null || username.isBlank() || playlistCode <= 0 || trackCode <= 0) {
            final String errorMessage = "Parametri non validi per la rimozione del brano.";
            LOGGER.log(Level.WARNING, errorMessage);
            this.view.showError(errorMessage);
            return false;
        }

        try {
            final boolean success = this.model.removeTrackFromPlaylist(username, playlistCode, trackCode);
            if (success) {
                this.view.showSuccess("Brano rimosso dalla playlist con successo!");
            } else {
                this.view.showError("Il brano selezionato non fa parte di questa playlist o non hai i permessi.");
            }
            return success;
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to remove track from playlist", e);
            this.view.showError("Errore durante la rimozione del brano dalla playlist.");
            return false;
        }
    }

    @Override
    public List<LikeBrani> getUserLikedTracks(final String username) {
        if (username == null || username.isBlank()) {
            final String errorMessage = "Username non valido per il caricamento dei brani preferiti.";
            LOGGER.log(Level.WARNING, errorMessage);
            this.view.showError(errorMessage);
            return List.of();
        }

        try {
            return this.model.getLikedTracks(username);
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load liked tracks for user: " + username, e);
            this.view.showError("Errore durante il caricamento dei brani preferiti.");
            return List.of();
        }
    }

    @Override
    public boolean userClickedLikeTrack(final String username, final int trackCode) {
        if (username == null || username.isBlank() || trackCode <= 0) {
            final String errorMessage = "Parametri non validi per aggiungere il like al brano.";
            LOGGER.log(Level.WARNING, errorMessage);
            this.view.showError(errorMessage);
            return false;
        }

        try {
            this.model.likeTrack(username, trackCode);
            this.view.showSuccess("Brano aggiunto ai preferiti con successo!");
            return true;
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to like track", e);
            this.view.showError("Impossibile aggiungere il brano ai preferiti (potrebbe essere già presente).");
            return false;
        }
    }

    @Override
    public boolean userClickedUnlikeTrack(final String username, final int trackCode) {
        if (username == null || username.isBlank() || trackCode <= 0) {
            final String errorMessage = "Parametri non validi per rimuovere il like dal brano.";
            LOGGER.log(Level.WARNING, errorMessage);
            this.view.showError(errorMessage);
            return false;
        }

        try {
            final boolean success = this.model.unlikeTrack(username, trackCode);
            if (success) {
                this.view.showSuccess("Brano rimosso dai preferiti con successo!");
            } else {
                this.view.showError("Il brano selezionato non è presente tra i preferiti.");
            }
            return success;
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to unlike track", e);
            this.view.showError("Errore durante la rimozione del brano dai preferiti.");
            return false;
        }
    }

    // ==========================================
    // AMMINISTRAZIONE E STATISTICHE
    // ==========================================

    @Override
    public void adminClickedLoadUsers() {
        try {
            final List<User> users = this.model.loadUsers();
            this.view.showUsers(users);
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load users", e);
            this.view.showError("Errore durante il caricamento degli utenti.");
        }
    }

    @Override
    public void adminRequestedGlobalAlbums() {
        try {
            final List<String> albumsAboveAvg = this.model.getAlbumsAboveGlobalAverage();

            final StringBuilder sb = new StringBuilder(INITIAL_BUILDER_CAPACITY);
            sb.append("=== Album sopra la media globale delle recensioni ===")
              .append(NEW_LINE);

            if (albumsAboveAvg != null && !albumsAboveAvg.isEmpty()) {
                for (final String a : albumsAboveAvg) {
                    sb.append("• ")
                      .append(a)
                      .append(NEW_LINE);
                }
            } else {
                sb.append("Nessun album trovato.")
                  .append(NEW_LINE);
            }

            this.view.showGlobalAlbumsStats(sb.toString());

        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load global albums", e);
            this.view.showError("Errore durante il caricamento degli album globali.");
        }
    }

    @Override
    public void adminRequestedYearlyStats(final int year) {
        if (year <= 0) {
            final String errorMessage = "Inserisci un anno valido per visualizzare le statistiche.";
            LOGGER.log(Level.WARNING, errorMessage);
            this.view.showError(errorMessage);
            return;
        }

        try {
            final String mostPlayedArtist = this.model.getMostPlayedArtist(year);
            final String mostPlayedGenre = this.model.getMostPlayedGenre(year);
            final List<String> usersAboveAvg = this.model.getUsersAboveAverageListens(year);

            final StringBuilder sb = new StringBuilder(INITIAL_BUILDER_CAPACITY);
            sb.append("=== Artista più ascoltato (Anno ")
              .append(year)
              .append(SECTION_CLOSE_SUFFIX)
              .append(NEW_LINE)
              .append(mostPlayedArtist != null ? mostPlayedArtist : "Nessun dato")
              .append(NEW_LINE)
              .append(NEW_LINE)
              .append("=== Genere più ascoltato (Anno ")
              .append(year)
              .append(SECTION_CLOSE_SUFFIX)
              .append(NEW_LINE)
              .append(mostPlayedGenre != null ? mostPlayedGenre : "Nessun dato")
              .append(NEW_LINE)
              .append(NEW_LINE)
              .append("=== Utenti sopra la media ascolti (Anno ")
              .append(year)
              .append(SECTION_CLOSE_SUFFIX)
              .append(NEW_LINE);

            if (usersAboveAvg != null && !usersAboveAvg.isEmpty()) {
                for (final String u : usersAboveAvg) {
                    sb.append("• ")
                      .append(u)
                      .append(NEW_LINE);
                }
            } else {
                sb.append("Nessun utente trovato.")
                  .append(NEW_LINE);
            }

            this.view.showYearlyStats(sb.toString());

        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load yearly stats", e);
            this.view.showError("Errore durante il caricamento delle statistiche annuali.");
        }
    }

    // ==========================================
    // METODI PRIVATI DI SUPPORTO
    // ==========================================

    private List<SongInput> parseSongsInput(final String rawText) {
        final List<SongInput> songList = new ArrayList<>();
        if (rawText == null || rawText.isBlank()) {
            return songList; 
        }

        final String[] lines = rawText.split(NEW_LINE);
        for (final String line : lines) {
            if (!line.isBlank()) {
                final String[] parts = line.split(",");
                if (parts.length >= 4) {
                    final String songTitle = parts[0].trim();
                    final int duration = Integer.parseInt(parts[1].trim());
                    final int trackNumber = Integer.parseInt(parts[2].trim());
                    final String description = parts[3].trim();

                    final int artistCodeForSong = parts.length > ARTIST_CODE_INDEX
                        ? Integer.parseInt(parts[ARTIST_CODE_INDEX].trim())
                        : 0;

                    final List<String> genres;
                    if (parts.length > GENRES_INDEX && !parts[GENRES_INDEX].isBlank()) {
                        genres = Arrays.asList(parts[GENRES_INDEX].trim().split(";"));
                    } else {
                        genres = List.of();
                    }

                    songList.add(new SongInput(
                        songTitle, 
                        duration, 
                        description, 
                        trackNumber, 
                        artistCodeForSong, 
                        genres
                    ));
                }
            }
        }
        return songList;
    }
}
