package soundwave.controller;

/**
 * Defines the controller interface for the Soundwave application.
 */
public interface Controller {

    /**
     * Handles the request to insert a new artist (OP 7).
     *
     * @param stageName the artist stage name
     * @param name the real name
     * @param surname the surname
     * @param birthDate the birth date
     * @param provenanceCountry the provenance country
     * @param biography the biography
     * @param startYear the start year
     * @param artistType the artist type
     */
    void adminClickedSaveArtist(String stageName, String name, String surname, String birthDate, 
                                String provenanceCountry, String biography, int startYear, String artistType);
    
    /**
     * Handles the request to insert a new album with its songs (OP 8).
     *
     * @param artistCode the artist code
     * @param title the album title
     * @param releaseYear the release year
     * @param recordCompany the record company
     * @param rawSongsText the raw text containing songs data from the text area
     */
    void adminClickedSaveAlbumWithSongs(int artistCode, String title, String releaseDate, 
                                        String recordCompany, String rawSongsText);

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

    /**
     * Handles the request to load and view global statistics (OP 22).
     * 
     * @param year the reference year for annual statistics
     */
    void adminRequestedGlobalStats(int year);

    /**
     * Filters and shows songs by a specific genre.
     *
     * @param genre the genre to filter by.
     */
    void userClickedFilterSongsByGenre(String genre);

    /**
     * Adds a like to a content for a specific user.
     *
     * @param username the username.
     * @param contentCode the code of the content to like.
     */
    void userClickedAddLike(String username, int contentCode);


    /**
     * Views the profile of an artist by their stage name.
     *
     * @param artistcode the artist distinctive code.
     */
    void userClickedViewArtistProfile(final int artistCode);

    /**
     * Requests the list of liked songs for a user.
     *
     * @param username the username.
     */
    void userRequestedLikedSongs(String username);

    /**
     * Removes a like from a content for a specific user.
     *
     * @param username the username.
     * @param contentCode the code of the content to unlike.
     */

    void userClickedRemoveLike(String username, int contentCode);

    /**
     * Searches for artists based on a partial query to populate the search dropdown.
     *
     * @param query the partial text entered by the user
     */
    void userClickedSearchArtists(final String query);

    /**
     * Handles the request to follow a specific artist.
     *
     * @param artistCode the code of the artist to follow.
     */
    void userClickedFollowArtist(int artistCode);

    /**
     * Searches for albums matching a query string.
     *
     * @param query the search query text.
     */
    void userClickedSearchAlbums(String query);

    /**
     * Handles the request to view complete album details and tracklist.
     *
     * @param albumCode the code of the album to view.
     */
    void userClickedViewAlbum(int albumCode);

    /**
     * Handles the request to view reviews for a specific album.
     *
     * @param albumCode the code of the album.
     */
    void userClickedViewAlbumReviews(int albumCode);

    /**
     * Handles the request to add or modify a review for an album.
     *
     * @param albumCode the code of the album.
     */
    void userClickedToggleReview(int albumCode);

}

