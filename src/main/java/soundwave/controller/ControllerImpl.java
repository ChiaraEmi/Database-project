package soundwave.controller;

import soundwave.data.DAOException;
import soundwave.model.Model;
import soundwave.view.View;
import java.util.Objects;
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

    private static final String SECTION_FOOTER_SUFFIX = ") ===\n";
    private static final String NEW_LINE = "\n";
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

    @Override
    public void adminClickedSaveArtist(final String stageName, final String name, final String surname, 
                                       final String birthDateStr, final String provenanceCountry, 
                                       final String biography, final int startYear, final String artistType) {

        if (stageName == null || stageName.isBlank() || provenanceCountry == null || provenanceCountry.isBlank()
            || artistType == null || artistType.isBlank() || startYear <= 0) {
            final String errorMessage = "Compila i campi obbligatori (Nome d'arte, Paese, Anno e Tipo Artista).";
            LOGGER.log(Level.WARNING, errorMessage);
            // If you want to show it on the view, you can uncomment/use:
            // this.view.showError(errorMessage);
            return;
        }

        try {
            final String realName = (name == null || name.isBlank()) ? null : name;
            final String realSurname = (surname == null || surname.isBlank()) ? null : surname;
            final java.time.LocalDate birthDate = birthDateStr == null || birthDateStr.isBlank() 
                ? null : java.time.LocalDate.parse(birthDateStr);
            final String bio = (biography == null || biography.isBlank()) ? null : biography;
            this.model.insertArtist(stageName, realName, realSurname, birthDate, 
                                    provenanceCountry, bio, startYear, artistType);
        } catch (final java.time.format.DateTimeParseException | DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save artist", e);
        }
    }

    @Override
    public void adminClickedSaveAlbumWithSongs(final int artistCode, final String title, final String releaseDate, 
                                               final String recordCompany, final String rawSongsText) {

        if (artistCode <= 0 || title == null || title.isBlank() || releaseDate == null || releaseDate.isBlank()
            || recordCompany == null || recordCompany.isBlank() || rawSongsText == null || rawSongsText.isBlank()) {
            final String errorMessage = "Compila i campi obbligatori (Artista, Titolo, Data e Casa Discografica).";
            LOGGER.log(Level.WARNING, errorMessage);
            // this.view.showError(errorMessage);
            return;
        }

        try {
            final java.util.List<soundwave.data.SongInput> songs = parseSongsInput(rawSongsText);
            this.model.insertAlbumWithSongs(artistCode, title, releaseDate, recordCompany, songs);
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save album with songs", e);
        }
    }

    @Override
    public void adminClickedSavePodcast(final int artistCode, final String name, 
                                        final String description, final String category) {

        if (artistCode <= 0 || name == null || name.isBlank() || category == null || category.isBlank()) {
            final String errorMessage = "Compila i campi obbligatori del podcast (Artista, Nome e Categoria).";
            LOGGER.log(Level.WARNING, errorMessage);
            // this.view.showError(errorMessage);
            return;
        }

        try {
            final String desc = (description == null || description.isBlank()) ? null : description;
            this.model.insertPodcast(artistCode, name, desc, category);
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save podcast", e);
        }
    }

    @Override
    public void adminClickedSaveEpisode(final int podcastCode, final String title, 
                                        final int duration, final String description, 
                                        final int episodeNumber) {

        if (podcastCode <= 0 || title == null || title.isBlank() || duration <= 0 || episodeNumber <= 0) {
            final String errorMessage = "Compila i campi obbligatori dell'episodio (Podcast, Titolo, Durata e Numero Episodio).";
            LOGGER.log(Level.WARNING, errorMessage);
            // this.view.showError(errorMessage);
            return;
        }

        try {
            final String desc = (description == null || description.isBlank()) ? null : description;
            this.model.insertEpisode(podcastCode, title, duration, desc, episodeNumber);
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save episode", e);
        }
    }

    @Override
    public void userGeneratedListeningEvent(final String username, final int contentCode, 
                                            final String device, final int eventDuration) {

        if (username == null || username.isBlank() || contentCode <= 0 || device == null || device.isBlank() 
            || eventDuration <= 0) {
            final String errorMessage = "Compila tutti i campi obbligatori (Username, Codice Contenuto, Dispositivo e Durata).";
            LOGGER.log(Level.WARNING, errorMessage);
            // this.view.showError(errorMessage);
            return;
        }

        try {
            this.model.insertListeningEvent(username, contentCode, device, eventDuration);
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to generate listening event", e);
        }
    }

    @Override
    public void userClickedCreatePlaylist(final String username, final String playlistName, 
                                         final String visibility, final boolean isCollaborative) {

        if (username == null || username.isBlank() || playlistName == null || playlistName.isBlank()) {
            final String errorMessage = "Compila i campi obbligatori per creare la playlist (Username e Nome Playlist).";
            LOGGER.log(Level.WARNING, errorMessage);
            // this.view.showError(errorMessage);
            return;
        }

        try {
            this.model.insertPlaylist(username, playlistName, visibility, isCollaborative);
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to create playlist", e);
        }
    }

    @Override
    public void userClickedAddTrackToPlaylist(final int playlistCode, final int trackCode) {

        if (playlistCode <= 0 || trackCode <= 0) {
            final String errorMessage = "Impossibile aggiungere il brano: playlist o brano non validi.";
            LOGGER.log(Level.WARNING, errorMessage);
            // this.view.showError(errorMessage);
            return;
        }

        try {
            this.model.addTrackToPlaylist(playlistCode, trackCode);
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to add track to playlist", e);
        }
    }

    /**
     * Handles the request to load and view the list of system users.
     */
    @Override
    public void adminClickedLoadUsers() {
        try {
            final java.util.List<soundwave.data.User> users = this.model.loadUsers();
            this.view.showUsers(users);
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load users", e);
        }
    }

    @Override
    public void adminRequestedGlobalStats(final int year) {

        if (year <= 0) {
            final String errorMessage = "Inserisci un anno valido per visualizzare le statistiche globali.";
            LOGGER.log(Level.WARNING, errorMessage);
            // this.view.showError(errorMessage);
            return;
        }

        try {
            final String mostPlayedArtist = this.model.getMostPlayedArtist(year);
            final String mostPlayedGenre = this.model.getMostPlayedGenre(year);
            final java.util.List<String> usersAboveAvg = this.model.getUsersAboveAverageListens(year);
            final java.util.List<String> albumsAboveAvg = this.model.getAlbumsAboveGlobalAverage();

            final StringBuilder sb = new StringBuilder(INITIAL_BUILDER_CAPACITY);
            sb.append("=== Artista più ascoltato (Anno ")
              .append(year)
              .append(SECTION_FOOTER_SUFFIX)
              .append(mostPlayedArtist != null ? mostPlayedArtist : "Nessun dato")
              .append(NEW_LINE)
              .append(NEW_LINE)
              .append("=== Genere più ascoltato (Anno ")
              .append(year)
              .append(SECTION_FOOTER_SUFFIX)
              .append(mostPlayedGenre != null ? mostPlayedGenre : "Nessun dato")
              .append(NEW_LINE)
              .append(NEW_LINE)
              .append("=== Utenti sopra la media ascolti (Anno ")
              .append(year)
              .append(SECTION_FOOTER_SUFFIX);

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

            sb.append(NEW_LINE)
              .append("=== Album sopra la media globale delle recensioni ===")
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

            this.view.showGlobalStats(sb.toString());

        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load global stats", e);
        }
    }

    /**
     * Helper method to parse raw text from the text area into a list of SongInput objects.
     * Expected format per line: Title, DurationInSeconds, TrackNumber, Description, ArtistCodeForSong, Genre1;Genre2
     *
     * @param rawText the raw string retrieved from the songs text area.
     * 
     * @return a list of parsed SongInput items.
     */
    private java.util.List<soundwave.data.SongInput> parseSongsInput(final String rawText) {
        final java.util.List<soundwave.data.SongInput> songList = new java.util.ArrayList<>();
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

                    final java.util.List<String> genres;
                    if (parts.length > GENRES_INDEX && !parts[GENRES_INDEX].isBlank()) {
                        genres = java.util.Arrays.asList(parts[GENRES_INDEX].trim().split(";"));
                    } else {
                        genres = java.util.List.of();
                    }

                    songList.add(new soundwave.data.SongInput(
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
