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
            final java.time.LocalDate birthDate = birthDateStr == null || birthDateStr.isBlank() 
                ? null 
                : java.time.LocalDate.parse(birthDateStr);

            this.model.insertArtist(stageName, name, surname, birthDate, provenanceCountry, biography, startYear, artistType);
        } catch (final java.time.format.DateTimeParseException e) {
            e.printStackTrace();
        } catch (final DAOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void adminClickedSaveAlbumWithSongs(final int artistCode, final String title, final String releaseDate, 
                                               final String recordCompany, final String rawSongsText) {
        try {
            final java.util.List<soundwave.data.SongInput> songs = parseSongsInput(rawSongsText);
            this.model.insertAlbumWithSongs(artistCode, title, releaseDate, recordCompany, songs);
        } catch (final DAOException e) {
            e.printStackTrace(); 
        }
    }

    @Override
    public void adminClickedSavePodcast(final int artistCode, final String name, 
                                        final String description, final String category) {
        try {
            this.model.insertPodcast(artistCode, name, description, category);
        } catch (final DAOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void adminClickedSaveEpisode(final int podcastCode, final String title, 
                                        final int duration, final String description, 
                                        final int episodeNumber) {
        try {
            this.model.insertEpisode(podcastCode, title, duration, description, episodeNumber);
        } catch (final DAOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userGeneratedListeningEvent(final String username, final int contentCode, 
                                            final String device, final int eventDuration) {
        try {
            this.model.insertListeningEvent(username, contentCode, device, eventDuration);
        } catch (final DAOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userClickedCreatePlaylist(final String username, final String playlistName, 
                                         final String visibility, final boolean isCollaborative) {
        try {
            this.model.insertPlaylist(username, playlistName, visibility, isCollaborative);
        } catch (final DAOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userClickedAddTrackToPlaylist(final int playlistCode, final int trackCode) {
        try {
            this.model.addTrackToPlaylist(playlistCode, trackCode);
        } catch (final DAOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void adminClickedLoadUsers() {
        try {
            final java.util.List<soundwave.data.User> users = this.model.loadUsers();
            this.view.showUsers(users);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void adminRequestedGlobalStats(final int year) {
        try {
            final String mostPlayedArtist = this.model.getMostPlayedArtist(year);
            final String mostPlayedGenre = this.model.getMostPlayedGenre(year);
            final java.util.List<String> usersAboveAvg = this.model.getUsersAboveAverageListens(year);
            final java.util.List<String> albumsAboveAvg = this.model.getAlbumsAboveGlobalAverage();

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

            this.view.showGlobalStats(sb.toString());

        } catch (final DAOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userClickedFilterSongsByGenre(final String genre) {
        try {
            final java.util.List<String> songs = this.model.getSongsByGenre(genre);
            this.view.showFilteredSongs(songs);
        } catch (final DAOException e) {
            e.printStackTrace(); 
        }
    }

    @Override
    public void userClickedAddLike(final String username, final int contentCode) {
        try {
            this.model.addLike(username, contentCode);
        } catch (final DAOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userClickedSearchArtists(final String query) {
        try {
            final java.util.List<soundwave.data.Artist> artists = this.model.getArtistsByPartialName(query);
            this.view.showArtistSearchResults(artists);
        } catch (final DAOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userClickedViewArtistProfile(final int artistCode) {
       try {
            final soundwave.data.Artist artist = this.model.getArtistByCode(artistCode); 
            if (artist != null) {
                this.view.showArtistProfile(artist);
            }
        } catch (final DAOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userRequestedLikedSongs(final String username) {
        try {
            final java.util.List<String> likedSongs = this.model.getLikedSongs(username);
            this.view.showLikedSongs(likedSongs);
        } catch (final DAOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userClickedRemoveLike(final String username, final int contentCode) {
        try {
            this.model.removeLike(username, contentCode);
            userRequestedLikedSongs(username);
        } catch (final DAOException e) {
            e.printStackTrace();
        }
    }

    /* --- Nuovi metodi implementati per risolvere gli errori --- */

    @Override
    public void userClickedFollowArtist(final int artistCode) {
        try {
            // Utilizziamo un utente di default o corrente (es. "user")
            this.model.followArtist("user", artistCode);
        } catch (final DAOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userClickedSearchAlbums(final String query) {
        try {
            final java.util.List<soundwave.data.Album> albums = this.model.getAlbumsByPartialTitle(query);
            this.view.showAlbumSearchResults(albums);
        } catch (final DAOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userClickedViewAlbum(final int albumCode) {
        try {
            final soundwave.data.Album.DAO.AlbumWithSongs albumInfo = this.model.getAlbumWithSongs(albumCode);
            if (albumInfo != null) {
                this.view.showAlbumDetails(albumInfo);
            }
        } catch (final DAOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userClickedViewAlbumReviews(final int albumCode) {
        try {
            final java.util.List<String> reviews = this.model.getAlbumReviews(albumCode);
            this.view.showAlbumReviews(reviews);
        } catch (final DAOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userClickedToggleReview(final int albumCode) {
        try {
            final Object[] reviewData = this.view.showReviewInputDialog();
            if (reviewData != null) {
                final int rating = (Integer) reviewData[0];
                final String comment = (String) reviewData[1];
                this.model.insertOrUpdateReview("user", albumCode, rating, comment);
            }
        } catch (final DAOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to parse raw text from the text area into a list of SongInput objects.
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
                    
                    final int artistCodeForSong = parts.length > 4 ? Integer.parseInt(parts[4].trim()) : 0;

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
