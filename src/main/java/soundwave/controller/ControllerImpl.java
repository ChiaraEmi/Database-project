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
}
