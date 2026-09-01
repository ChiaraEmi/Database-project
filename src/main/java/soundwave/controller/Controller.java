package soundwave.controller;

/**
 * Defines the controller interface for the Soundwave application.
 */
public interface Controller {

    /**
     * Handles the request to insert a new podcast (OP 9).
     *
     * @param artistCode the artist's code
     * @param name the podcast name
     * @param description the description
     * @param category the category
     */
    void adminClickedSavePodcast(int artistCode, String name, String description, String category);

    /**
     * Handles the request to insert a new episode into a podcast (OP 10).
     *
     * @param podcastCode the podcast code
     * @param title the episode title
     * @param duration the duration in seconds
     * @param description the description of the episode
     * @param episodeNumber the episode number within the podcast
     */
    void adminClickedSaveEpisode(int podcastCode, String title, int duration, String description, int episodeNumber);

    /**
     * Handles the request to generate a listening event (OP 11).
     *
     * @param username the username of the listener
     * @param contentCode the content code
     * @param device the playback device
     * @param eventDuration the duration in seconds
     */
    void userGeneratedListeningEvent(String username, int contentCode, String device, int eventDuration);

    /**
     * Handles the request to create a new playlist (OP 12).
     *
     * @param username the owner's username
     * @param playlistName the playlist name
     * @param visibility the visibility state
     * @param isCollaborative true if collaborative, false otherwise
     */
    void userClickedCreatePlaylist(String username, String playlistName, String visibility, boolean isCollaborative);

    /**
     * Handles the request to add a track to a playlist (OP 13).
     *
     * @param playlistCode the playlist code
     * @param trackCode the track code
     */
    void userClickedAddTrackToPlaylist(int playlistCode, int trackCode);

    /**
     * Handles the request to load and view the list of system users (Admin dashboard).
     */
    void adminClickedLoadUsers();
}
