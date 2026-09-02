package soundwave.controller;

import soundwave.data.DAOException;
import soundwave.model.Model;
import soundwave.view.View;
import java.util.Objects;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Concrete implementation of the {@link Controller} interface.
 */
public final class ControllerImpl implements Controller {

    private final Model model;
    private final View view;

    /**
     * Constructs a new ControllerImpl.
     *
     * @param model the application model
     * @param view the application view
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
        try {
            // Converte la stringa in LocalDate (assumendo il formato standard YYYY-MM-DD)
            final java.time.LocalDate birthDate = birthDateStr == null || birthDateStr.isBlank() 
                ? null 
                : java.time.LocalDate.parse(birthDateStr);

            this.model.insertArtist(stageName, name, surname, birthDate, provenanceCountry, biography, startYear, artistType);
            // this.view.showSuccess("Artista inserito con successo!");
        } catch (final java.time.format.DateTimeParseException e) {
            // Gestione errore formato data non valido
            // this.view.showError("Formato data non valido. Usa YYYY-MM-DD.");
        } catch (final DAOException e) {
            // Gestione dell'errore di inserimento database
            // this.view.showError("Impossibile salvare l'artista.");
        }
    }

    @Override
    public void adminClickedSaveAlbumWithSongs(final int artistCode, final String title, final int releaseYear, 
                                               final String recordCompany, final java.util.List<soundwave.data.SongInput> songs) {
        try {
            this.model.insertAlbumWithSongs(artistCode, title, releaseYear, recordCompany, songs);
            // this.view.showSuccess("Album e brani inseriti con successo!");
        } catch (final DAOException e) {
            // Gestione dell'errore transazionale
            // this.view.showError("Impossibile registrare l'album.");
        }
    }

    @Override
    public void adminClickedSaveAlbum(int artistCode, String title, int releaseYear, String label) {

    }

    @Override
    public void adminClickedSaveTrack(int albumCode, String title, int duration, int trackNumber, String description) {

    }

    @Override
    public void adminClickedSavePodcast(final int artistCode, final String name, 
                                        final String description, final String category) {
        try {
            // Eventuale stato di caricamento nella view
            // this.view.loadingPodcast();
            this.model.insertPodcast(artistCode, name, description, category);
            // Notifica la view del successo (es. torna alla home o mostra un messaggio)
            // this.view.showSuccess("Podcast creato con successo!");
        } catch (final DAOException e) {
            // Notifica la view del fallimento passando i dati per un eventuale retry
            // this.view.failedToSavePodcast(artistCode, name, description, category);
        }
    }

    @Override
    public void adminClickedSaveEpisode(final int podcastCode, final String title, 
                                        final int duration, final String description, 
                                        final int episodeNumber) {
        try {
            this.model.insertEpisode(podcastCode, title, duration, description, episodeNumber);
            // this.view.showSuccess("Episodio aggiunto con successo!");
        } catch (final DAOException e) {
            // this.view.failedToSaveEpisode(podcastCode, title, duration, description, episodeNumber);
        }
    }

    @Override
    public void userGeneratedListeningEvent(final String username, final int contentCode, 
                                            final String device, final int eventDuration) {
        try {
            this.model.insertListeningEvent(username, contentCode, device, eventDuration);
        } catch (final DAOException e) {
            // Gestione dell'errore di tracciamento ascolto
            // this.view.showError("Impossibile registrare l'evento di ascolto.");
        }
    }

    @Override
    public void userClickedCreatePlaylist(final String username, final String playlistName, 
                                         final String visibility, final boolean isCollaborative) {
        try {
            this.model.insertPlaylist(username, playlistName, visibility, isCollaborative);
            // this.view.showSuccess("Playlist creata con successo!");
        } catch (final DAOException e) {
            // this.view.failedToCreatePlaylist(username, playlistName, visibility, isCollaborative);
        }
    }

    @Override
    public void userClickedAddTrackToPlaylist(final int playlistCode, final int trackCode) {
        try {
            this.model.addTrackToPlaylist(playlistCode, trackCode);
            // this.view.showSuccess("Brano aggiunto alla playlist!");
        } catch (final DAOException e) {
            // this.view.failedToAddTrackToPlaylist(playlistCode, trackCode);
        }
    }

    /**
     * Handles the request to load and view the list of system users.
     */
    @Override
    public void adminClickedLoadUsers() {
        try {
            final java.util.List<soundwave.data.User> users = this.model.loadUsers();
            // Passa la lista alla view per mostrarla nella dashboard
            this.view.showUsers(users);
        } catch (final Exception e) {
            // Gestione dell'errore
            // this.view.showError("Impossibile caricare la lista degli utenti.");
        }
    }
}
