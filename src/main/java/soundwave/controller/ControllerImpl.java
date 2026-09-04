package soundwave.controller;

import soundwave.data.Artist;
import soundwave.data.DAOException;
import soundwave.data.SongInput;
import soundwave.data.User;
import soundwave.model.Model;
import soundwave.view.View;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    @Override
    public boolean userClickedCreatePlaylist(final String username, final String playlistName, 
                                             final String visibility, final boolean isCollaborative) {

        if (username == null || username.isBlank() || playlistName == null || playlistName.isBlank()) {
            final String errorMessage = "Compila i campi obbligatori per creare la playlist (Username e Nome Playlist).";
            LOGGER.log(Level.WARNING, errorMessage);
            this.view.showError(errorMessage);
            return false;
        }

        try {
            this.model.insertPlaylist(username, playlistName, visibility, isCollaborative);
            return true;
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to create playlist", e);
            this.view.showError("Errore durante la creazione della playlist.");
            return false;
        }
    }

    @Override
    public boolean userClickedAddTrackToPlaylist(final int playlistCode, final int trackCode) {

        if (playlistCode <= 0 || trackCode <= 0) {
            final String errorMessage = "Impossibile aggiungere il brano: playlist o brano non validi.";
            LOGGER.log(Level.WARNING, errorMessage);
            this.view.showError(errorMessage);
            return false;
        }

        try {
            this.model.addTrackToPlaylist(playlistCode, trackCode);
            return true;
        } catch (final DAOException e) {
            LOGGER.log(Level.SEVERE, "Failed to add track to playlist", e);
            this.view.showError("Errore durante l'aggiunta del brano alla playlist.");
            return false;
        }
    }

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
    public void adminRequestedGlobalStats(final int year) {
        if (year <= 0) {
            final String errorMessage = "Inserisci un anno valido per visualizzare le statistiche globali.";
            LOGGER.log(Level.WARNING, errorMessage);
            this.view.showError(errorMessage);
            return;
        }

        try {
            final String mostPlayedArtist = this.model.getMostPlayedArtist(year);
            final String mostPlayedGenre = this.model.getMostPlayedGenre(year);
            final List<String> usersAboveAvg = this.model.getUsersAboveAverageListens(year);
            final List<String> albumsAboveAvg = this.model.getAlbumsAboveGlobalAverage();

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
            this.view.showError("Errore durante il caricamento delle statistiche globali.");
        }
    }

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
