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
    public void adminClickedSaveAlbumWithSongs(final int artistCode, final String title, final String releaseDate, 
                                               final String recordCompany, final String rawSongsText) {
        try {
            // Qui viene chiamato il metodo di parsing per convertire il testo grezzo
            final java.util.List<soundwave.data.SongInput> songs = parseSongsInput(rawSongsText);

            // Invia tutto al Model per la transazione sul database
            this.model.insertAlbumWithSongs(artistCode, title, releaseDate, recordCompany, songs);
            
            // this.view.showSuccess("Album e brani salvati con successo!");
        } catch (final DAOException e) {
            // this.view.showError("Impossibile registrare l'album.");
        }
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

    @Override
    public void adminRequestedGlobalStats(final int year) {
        try {
            // Interroga il Model per ottenere le metriche della OP 22
            final String mostPlayedArtist = this.model.getMostPlayedArtist(year);
            final String mostPlayedGenre = this.model.getMostPlayedGenre(year);
            final java.util.List<String> usersAboveAvg = this.model.getUsersAboveAverageListens(year);
            final java.util.List<String> albumsAboveAvg = this.model.getAlbumsAboveGlobalAverage();

            // Compone il report testuale ordinato per la dashboard
            final StringBuilder sb = new StringBuilder();
            sb.append("=== Artista più ascoltato (Anno ").append(year).append(") ===\n")
              .append(mostPlayedArtist != null ? mostPlayedArtist : "Nessun dato").append("\n\n");
            
            sb.append("=== Genere più ascoltato (Anno ").append(year).append(") ===\n")
              .append(mostPlayedGenre != null ? mostPlayedGenre : "Nessun dato").append("\n\n");
            
            sb.append("=== Utenti sopra la media ascolti (Anno ").append(year).append(") ===\n");
            if (usersAboveAvg != null && !usersAboveAvg.isEmpty()) {
                for (final String u : usersAboveAvg) {
                    sb.append("• ").append(u).append("\n");
                }
            } else {
                sb.append("Nessun utente trovato.\n");
            }
            sb.append("\n");

            sb.append("=== Album sopra la media globale delle recensioni ===\n");
            if (albumsAboveAvg != null && !albumsAboveAvg.isEmpty()) {
                for (final String a : albumsAboveAvg) {
                    sb.append("• ").append(a).append("\n");
                }
            } else {
                sb.append("Nessun album trovato.\n");
            }

            // Passa il risultato pronto alla View
            this.view.showGlobalStats(sb.toString());

        } catch (final DAOException e) {
            // Gestione centralizzata dell'errore (es. messaggio di errore nella view)
            // this.view.showError("Impossibile caricare le statistiche globali.");
        }
    }

    /**
     * Helper method to parse raw text from the text area into a list of SongInput objects.
     * Expected format per line: Title, DurationInSeconds, TrackNumber, Description, ArtistCodeForSong, Genre1;Genre2
     *
     * @param rawText the raw string retrieved from the songs text area
     * @return a list of parsed SongInput items
     */
    private java.util.List<soundwave.data.SongInput> parseSongsInput(final String rawText) {
        final java.util.List<soundwave.data.SongInput> songList = new java.util.ArrayList<>();
        if (rawText == null || rawText.isBlank()) {
            return songList; 
        }

        final String[] lines = rawText.split("\n");
        for (final String line : lines) {
            if (!line.isBlank()) {
                final String[] parts = line.split(",");
                if (parts.length >= 4) {
                    final String songTitle = parts[0].trim();
                    final int duration = Integer.parseInt(parts[1].trim());
                    final int trackNumber = Integer.parseInt(parts[2].trim());
                    final String description = parts[3].trim();
                    
                    // Se l'artista del brano coincide con quello dell'album, puoi usare l'artistCode dell'album
                    // oppure estrarlo dal testo se specificato. Qui usiamo un valore di default o un parametro.
                    final int artistCodeForSong = parts.length > 4 ? Integer.parseInt(parts[4].trim()) : 0;

                    // Gestione dei generi separati da punto e virgola (es. "Pop;Rock")
                    final java.util.List<String> genres;
                    if (parts.length > 5 && !parts[5].isBlank()) {
                        genres = java.util.Arrays.asList(parts[5].trim().split(";"));
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
