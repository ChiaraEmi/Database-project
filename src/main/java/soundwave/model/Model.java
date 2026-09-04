package soundwave.model;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import soundwave.data.Artist;
import soundwave.data.SongInput;
import soundwave.data.User;

/**
 * Represents the application model.
 */
public interface Model {

    /**
     * Creates a new Model instance backed by a live database connection.
     *
     * @param connection the active database connection
     * @return a Model implementation connected to the database
     */
    static Model fromConnection(final Connection connection) {
        return new DBModel(connection);
    }

    /**
     * Inserts a new artist into the database.
     *
     * @param stageName the stage name of the artist.
     * @param name the real first name of the artist (optional).
     * @param surname the real surname of the artist (optional).
     * @param birthDate the birth date of the artist (optional).
     * @param provenanceCountry the country of origin.
     * @param biography the biography of the artist (optional).
     * @param startYear the year the artist started their activity.
     * @param artistType the type of artist (e.g., 'Cantante', 'Autore Podcast', 'Band').
     * @return the generated artist code.
     */
    int insertArtist(String stageName, String name, String surname, LocalDate birthDate, 
                        String provenanceCountry, String biography, int startYear, String artistType);

    /**
     * Inserts a new album along with its songs, artists, and genres into the system (OP 8).
     *
     * @param artistCode the code of the main artist/band of the album.
     * @param title the title of the album.
     * @param releaseDate the release date.
     * @param recordCompany the record company name.
     * @param songs the list of song inputs containing details for each track.
     * @return the generated album code.
     */
    int insertAlbumWithSongs(int artistCode, String title, String releaseDate, String recordCompany, List<SongInput> songs);

    /**
     * Retrieves all artists authorized as podcast authors.
     *
     * @return a list of podcast authors.
     */
    List<Artist> getPodcastAuthors();

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
     * Checks whether the specified artist is authorized as a podcast author.
     *
     * @param artistCode the unique code of the artist to check
     * 
     * @return true if the artist exists and is a podcast author, false otherwise
     */
    boolean isPodcastAuthor(int artistCode);

    /**
     * Inserts a new episode into a specific podcast (OP 10).
     *
     * @param podcastCode the podcast code
     * @param title the episode title
     * @param duration the duration in seconds
     * @param description the description of the episode
     * @param episodeNumber the episode number within the podcast
     * @return the auto-generated code of the inserted episode
     */
    int insertEpisode(int podcastCode, String title, int duration, String description, int episodeNumber);

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
     * Retrieves the list of all users registered in the system.
     * 
     * @return a list of users
     */
    List<User> loadUsers();

    /**
     * Retrieves the most played artist in a specific year.
     *
     * @param year the year to check
     * @return a string with the artist details
     */
    String getMostPlayedArtist(int year);

    /**
     * Retrieves the most played music genre in a specific year.
     *
     * @param year the year to check
     * @return a string with the genre details
     */
    String getMostPlayedGenre(int year);

    /**
     * Retrieves users with a number of listens above the average for the given year.
     *
     * @param year the year to check
     * @return a list of strings representing the users
     */
    List<String> getUsersAboveAverageListens(int year);

    /**
     * Retrieves albums with a review average higher than the global average.
     *
     * @return a list of strings representing the top albums
     */
    List<String> getAlbumsAboveGlobalAverage();
}
