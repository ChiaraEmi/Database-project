package soundwave.model;

import soundwave.data.Podcast;
import soundwave.data.Playlist;
import soundwave.data.Episode;
import soundwave.data.ListeningEvent;
import java.sql.Connection;
import java.util.Objects;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Implementation of the {@link Model} interface backed by a relational database
 * using JDBC data access objects.
 */
public final class DBModel implements Model {

    private final Connection connection;

    /**
     * Constructs a new DBModel instance with the given database connection.
     *
     * @param connection the active database connection
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
}
