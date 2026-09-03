package soundwave.model;

import soundwave.data.Podcast;
import soundwave.data.SongInput;
import soundwave.data.User;
import soundwave.data.Playlist;
import soundwave.data.Album;
import soundwave.data.Artist;
import soundwave.data.Episode;
import soundwave.data.Genre;
import soundwave.data.ListeningEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Implementation of the {@link Model} interface backed by a relational database
 * using JDBC data access objects.
 */
public final class DBModel implements Model {

    private static final Logger LOGGER = Logger.getLogger(DBModel.class.getName());

    private final Connection connection;

    /**
     * Constructs a new DBModel instance with the given database connection.
     *
     * @param connection the active database connection.
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP2", 
        justification = "The database connection is managed externally and cannot be defensively copied."
    )
    public DBModel(final Connection connection) {
        Objects.requireNonNull(connection, "Model created with null connection");
        this.connection = connection;
    }

    @Override
    public int insertArtist(final String stageName, final String name, final String surname, 
                            final LocalDate birthDate, final String provenanceCountry, 
                            final String biography, final int startYear, final String artistType) {
        return Artist.DAO.insert(this.connection, stageName, name, surname, birthDate, 
                                 provenanceCountry, biography, startYear, artistType);
    }

    @Override
    public int insertAlbumWithSongs(final int artistCode, final String title, final String releaseDate,
                                    final String recordCompany, final List<SongInput> songs) {
        return Album.DAO.insertAlbumWithSongs(this.connection, artistCode, title, releaseDate, recordCompany, songs);
    }

    @Override
    public int insertPodcast(final int artistCode, final String name, final String description, final String category) {
        return Podcast.DAO.insert(connection, artistCode, name, description, category);
    }

    @Override
    public int insertEpisode(final int podcastCode, final String title, final int duration, 
                           final String description, final int episodeNumber) {
        return Episode.DAO.insert(connection, podcastCode, title, duration, description, episodeNumber);
    }

    @Override
    public int insertPlaylist(final String username, final String playlistName, final String visibility, 
                                final boolean isCollaborative) {
        return Playlist.DAO.insert(connection, username, playlistName, visibility, isCollaborative);
    }

    @Override
    public void addTrackToPlaylist(final int playlistCode, final int trackCode) {
        Playlist.DAO.addTrack(connection, playlistCode, trackCode);
    }

    @Override
    public void insertListeningEvent(final String username, final int contentCode, final String device, final int eventDuration) {
        ListeningEvent.DAO.insert(connection, username, contentCode, device, eventDuration);
    }

    @Override
    public List<User> loadUsers() {
        try {
            return User.DAO.list(this.connection);
        } catch (final SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to load users from the database.", e);
            return List.of();
        }
    }

    @Override
    public String getMostPlayedArtist(final int year) {
        return Artist.DAO.getMostPlayedArtist(this.connection, year);
    }

    @Override
    public String getMostPlayedGenre(final int year) {
        return Genre.DAO.getMostPlayedGenre(this.connection, year);
    }

    @Override
    public List<String> getUsersAboveAverageListens(final int year) {
        return User.DAO.getUsersAboveAverageListens(this.connection, year);
    }

    @Override
    public List<String> getAlbumsAboveGlobalAverage() {
        return Album.DAO.getAlbumsAboveGlobalAverage(this.connection);
    }
}
