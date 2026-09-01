package soundwave.model;

import java.sql.Connection;

/**
 * Represents the application model, defining core business operations 
 * and data interactions for the Soundwave application.
 */
public interface Model {

    /**
     * Inserts a new podcast into the database.
     *
     * @param artistCode the code of the artist creating the podcast
     * @param name the name of the podcast
     * @param description the description of the podcast
     * @param category the category of the podcast
     * @return the auto-generated code of the inserted podcast
     */
    int insertPodcast(int artistCode, String name, String description, String category);

    /**
     * Creates a new playlist for a user.
     *
     * @param username the owner's username
     * @param playlistName the name of the playlist
     * @param visibility the visibility state ('Pubblica' or 'Privata')
     * @param isCollaborative true if the playlist is collaborative, false otherwise
     * @return the auto-generated code of the created playlist
     */
    int insertPlaylist(String username, String playlistName, String visibility, boolean isCollaborative);

    /**
     * Adds a track to a playlist.
     *
     * @param playlistCode the code of the playlist
     * @param trackCode the code of the track to add
     */
    void addTrackToPlaylist(int playlistCode, int trackCode);

    /**
     * Records a listening event for a user.
     *
     * @param username the username of the user listening
     * @param contentCode the code of the content being listened to
     * @param device the device used for playback
     * @param eventDuration the duration played in seconds
     */
    void insertListeningEvent(String username, int contentCode, String device, int eventDuration);

    /**
     * Creates a new Model instance backed by a live database connection.
     *
     * @param connection the active database connection
     * @return a Model implementation connected to the database
     */
    static Model fromConnection(final Connection connection) {
        return new DBModel(connection);
    }
}
