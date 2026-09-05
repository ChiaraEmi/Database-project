package soundwave.controller;

import java.util.List;

import soundwave.data.Artist;

/**
 * Defines the controller interface for the application.
 */
public interface Controller {

    /**
     * Handles the login attempt for a user by checking if they exist in the database.
     * 
     * @param username the username entered by the user.
     * 
     * @return true if the user exists and login succeeds, false otherwise.
     */
    boolean userLoggedIn(String username);

    /**
     * Handles the request to insert a new artist.
     *
     * @param stageName the artist stage name.
     * @param name the real name.
     * @param surname the surname.
     * @param birthDate the birth date.
     * @param provenanceCountry the provenance country.
     * @param biography the biography.
     * @param startYear the start year.
     * @param artistType the artist type.
     * 
     * @return true if successfully saved, false otherwise.
     */
    boolean adminClickedSaveArtist(String stageName, String name, String surname, String birthDate, 
                                String provenanceCountry, String biography, int startYear, String artistType);

    /**
     * Handles the request to insert a new album with its songs.
     *
     * @param artistCode the artist code.
     * @param title the album title.
     * @param releaseDate the release date.
     * @param recordCompany the record company.
     * @param rawSongsText the raw text containing songs data from the text area.
     * 
     * @return true if successfully saved, false otherwise.
     */
    boolean adminClickedSaveAlbumWithSongs(int artistCode, String title, String releaseDate, 
                                           String recordCompany, String rawSongsText);

    /**
     * Retrieves the list of artists authorized as album authors.
     * 
     * @return a list of album authors.
     */
    List<Artist> getAlbumArtists();

    /**
     * Retrieves the list of artists authorized as podcast authors.
     * 
     * @return a list of podcast authors.
     */
    List<Artist> getPodcastAuthors();

    /**
     * Handles the request to insert a new podcast.
     *
     * @param artistCode the artist's code.
     * @param name the podcast name.
     * @param description the description.
     * @param category the category.
     * 
     * @return true if successfully saved, false otherwise.
     */
    boolean adminClickedSavePodcast(int artistCode, String name, String description, String category);

    /**
     * Handles the request to insert a new episode into a podcast.
     *
     * @param podcastCode the podcast code.
     * @param title the episode title.
     * @param duration the duration in seconds.
     * @param description the description of the episode.
     * @param episodeNumber the episode number within the podcast.
     * 
     * @return true if successfully saved, false otherwise.
     */
    boolean adminClickedSaveEpisode(int podcastCode, String title, int duration, String description, int episodeNumber);

    /**
     * Handles the request to generate a listening event.
     *
     * @param username the username of the listener.
     * @param contentCode the content code.
     * @param device the playback device.
     * @param eventDuration the duration in seconds.
     * 
     * @return true if successfully saved, false otherwise.
     */
    boolean userGeneratedListeningEvent(String username, int contentCode, String device, int eventDuration);

    /**
     * Handles the creation of a new playlist for the specified user with custom options.
     * 
     * @param username the username of the creator.
     * @param playlistName the name of the playlist.
     * @param visibility the visibility mode (e.g., "Pubblica", "Privata").
     * @param isCollaborative true if the playlist is collaborative, false otherwise.
     * 
     * @return true if the playlist was successfully created, false otherwise.
     */
    boolean userClickedCreatePlaylist(String username, String playlistName, String visibility, boolean isCollaborative);

    /**
     * Adds a track to a playlist for a specific user after checking permissions.
     *
     * @param username the user performing the action.
     * @param playlistCode the playlist target code.
     * @param trackCode the track code to add.
     * 
     * @return true if successfully added, false otherwise.
     */
    boolean userClickedAddTrackToPlaylist(String username, int playlistCode, int trackCode);

    /**
     * Removes a track from a playlist for a specific user after checking permissions.
     *
     * @param username the user performing the action.
     * @param playlistCode the playlist target code.
     * @param trackCode the track code to remove.
     * 
     * @return true if successfully removed, false otherwise.
     */
    boolean userClickedRemoveTrackFromPlaylist(String username, int playlistCode, int trackCode);

    /**
     * Handles the request to load and view the list of system users.
     */
    void adminClickedLoadUsers();

    /**
     * Handles the request to load and view global statistics.
     * 
     * @param year the reference year for annual statistics.
     */
    void adminRequestedGlobalStats(int year);
}
