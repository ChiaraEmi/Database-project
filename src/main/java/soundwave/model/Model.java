package soundwave.model;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import soundwave.data.Artist;
import soundwave.data.Content;
import soundwave.data.LikeBrani;
import soundwave.data.Playlist;
import soundwave.data.Podcast;
import soundwave.data.Plan;
import soundwave.data.Album;
import soundwave.data.SongInput;
import soundwave.data.User;

/**
 * Represents the application model.
 */
public interface Model {

    /**
     * Creates a new Model instance backed by a live database connection.
     *
     * @param connection the active database connection.
     * 
     * @return a Model implementation connected to the database.
     */
    static Model fromConnection(final Connection connection) {
        return new DBModel(connection);
    }

    /**
     * Finds a user by their username.
     * 
     * @param username the username to search for.
     * 
     * @return the User object if found, or null otherwise.
     */
    User findUser(String username);

    /**
     * Insert a new promotion into the database (OP 6).
     * 
     * @param code the unique code of the promotion.
     * @param name the name of the promotion.
     * @param description the description of the promotion.
     * @param startDate the start date of the promotion validity.
     * @param endDate the end date of the promotion validity.
     * @param discountType the type of discount (e.g., percentage or fixed).
     * @param discountValue the numerical value of the discount.
     * @param requiredMonths the minimum required months for the plan (optional).
     * @param planCodes the list of eligible subscription plan codes.
     */
    void insertPromotion(String code, String name, String description, LocalDate startDate, 
                        LocalDate endDate, String discountType, double discountValue, 
                        Integer requiredMonths, List<Integer> planCodes);

    /**
     * Immediately renews an active subscription for a user.
     * 
     * @param username the username of the subscription owner
     * @param subscriptionCode the unique code of the subscription to renew
     */
    void renewSubscriptionNow(String username, int subscriptionCode);

    /**
     * Enables or disables automatic renewal for a user's subscription.
     * 
     * @param username the username of the subscription owner
     * @param subscriptionCode the unique code of the subscription to modify
     * @param enabled true to enable, false otherwise
     */
    void toggleAutoRenew(String username, int subscriptionCode, boolean enabled);

    /**
     * Retrieves the current automatic renewal status of a subscription.
     * 
     * @param username the username of the subscription owner
     * @param subscriptionCode the unique code of the subscription to check
     * @return true if automatic renewal is enabled, false otherwise
     */
    boolean getAutoRenewStatus(String username, int subscriptionCode);

    /**
     * Processes automatic renewals for all eligible subscriptions.
     * 
     * @return an array containing three integers: number of renewed, number of failed, number of expired
     */
    int[] processAutoRenewals();

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
     * 
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
     * 
     * @return the generated album code.
     */
    int insertAlbumWithSongs(int artistCode, String title, String releaseDate, String recordCompany, List<SongInput> songs);

    /**
     * Retrieves all artists authorized as album authors.
     *
     * @return a list of album authors.
     */
    List<Artist> getAlbumArtists();

    /**
     * Retrieves all artists authorized as podcast authors.
     *
     * @return a list of podcast authors.
     */
    List<Artist> getPodcastAuthors();

    /**
     * Inserts a new podcast into the database.
     *
     * @param artistCode the code of the artist creating the podcast.
     * @param name the name of the podcast.
     * @param description the description of the podcast.
     * @param category the category of the podcast.
     * 
     * @return the auto-generated code of the inserted podcast.
     */
    int insertPodcast(int artistCode, String name, String description, String category);

    /**
     * Retrieves all available podcasts in the system.
     *
     * @return a list of all podcasts.
     */
    List<Podcast> getPodcasts();

    /**
     * Checks whether the specified artist is authorized as a podcast author.
     *
     * @param artistCode the unique code of the artist to check.
     * 
     * @return true if the artist exists and is a podcast author, false otherwise.
     */
    boolean isPodcastAuthor(int artistCode);

    /**
     * Inserts a new episode into a specific podcast (OP 10).
     *
     * @param podcastCode the podcast code.
     * @param title the episode title.
     * @param duration the duration in seconds.
     * @param description the description of the episode.
     * @param episodeNumber the episode number within the podcast.
     * 
     * @return the auto-generated code of the inserted episode.
     */
    int insertEpisode(int podcastCode, String title, int duration, String description, int episodeNumber);

    /**
     * Creates a new playlist for a user.
     *
     * @param username the owner's username.
     * @param playlistName the name of the playlist.
     * @param visibility the visibility state ('Pubblica' or 'Privata').
     * @param isCollaborative true if the playlist is collaborative, false otherwise.
     * 
     * @return the auto-generated code of the created playlist.
     */
    int insertPlaylist(String username, String playlistName, String visibility, boolean isCollaborative);

    /**
     * Retrieves all playlists belonging to a specific user.
     *
     * @param username the username of the playlist owner.
     * 
     * @return a list of playlists.
     */
    List<Playlist> getUserPlaylists(String username);

    /**
     * Adds a track to a playlist after checking user permissions.
     *
     * @param username the user performing the action.
     * @param playlistCode the playlist code.
     * @param trackCode the track code.
     * 
     * @return true if added successfully, false otherwise.
     */
    boolean addTrackToPlaylist(String username, int playlistCode, int trackCode);

    /**
     * Removes a track from a playlist after checking user permissions.
     *
     * @param username the user performing the action.
     * @param playlistCode the playlist code.
     * @param trackCode the track code.
     * 
     * @return true if removed successfully, false otherwise.
     */
    boolean removeTrackFromPlaylist(String username, int playlistCode, int trackCode);

    /**
     * Retrieves all liked tracks for a specific user.
     *
     * @param username the username of the user.
     * 
     * @return a list of liked tracks.
     */
    List<LikeBrani> getLikedTracks(String username);

    /**
     * Retrieves the list of artists followed by a specific user.
     * 
     * @param username the username of the user.
     * 
     * @return a list of strings representing the followed artists.
     */
    List<String> getFollowedArtists(String username);

    /**
     * Adds a like to a track for a specific user.
     *
     * @param username the username.
     * @param trackCode the track code.
     */
    void likeTrack(String username, int trackCode);

    /**
     * Removes a like from a track for a specific user.
     *
     * @param username the username.
     * @param trackCode the track code.
     * 
     * @return true if removed successfully, false otherwise.
     */
    boolean unlikeTrack(String username, int trackCode);

    /**
     * Searches for contents by a partial title match.
     * 
     * @param query the partial title to search for.
     * @return a list of matching Content objects.
     */
    List<Content> searchContents(String query);

    /**
     * Records a listening event for a user.
     *
     * @param username the username of the user listening.
     * @param contentCode the code of the content being listened to.
     * @param device the device used for playback.
     * @param eventDuration the duration played in seconds.
     */
    void insertListeningEvent(String username, int contentCode, String device, int eventDuration);

    /**
     * Retrieves the list of all users registered in the system.
     * 
     * @return a list of users.
     */
    List<User> loadUsers();

    /**
     * Retrieves the total number of listens and total listening duration (in seconds)
     * for a specific user during a given year.
     *
     * @param username the username of the user.
     * @param year the year to filter by.
     * 
     * @return an Object array containing total listens (Integer) and total seconds (Integer).
     */
    Object[] getPersonalTotals(String username, int year);

    /**
     * Retrieves the top 5 most played tracks for a specific user during a given year.
     *
     * @param username the username of the user.
     * @param year the year to filter by.
     * 
     * @return a list of Object arrays containing track code, title, and play count.
     */
    List<Object[]> getPersonalTopTracks(String username, int year);

    /**
     * Retrieves the top 5 most played artists for a specific user during a given year.
     *
     * @param username the username of the user.
     * @param year the year to filter by.
     * 
     * @return a list of Object arrays containing artist code, stage name, and play count.
     */
    List<Object[]> getPersonalTopArtists(String username, int year);

    /**
     * Retrieves the most listened musical genre for a specific user during a given year.
     *
     * @param username the username of the user.
     * @param year the year to filter by.
     * 
     * @return the name of the top genre as a String, or "-" if none available.
     */
    String getPersonalTopGenre(String username, int year);

    /**
     * Retrieves the most played artist in a specific year.
     *
     * @param year the year to check.
     * 
     * @return a string with the artist details.
     */
    String getMostPlayedArtist(int year);

    /**
     * Retrieves the most played music genre in a specific year.
     *
     * @param year the year to check.
     * 
     * @return a string with the genre details.
     */
    String getMostPlayedGenre(int year);

    /**
     * Retrieves users with a number of listens above the average for the given year.
     *
     * @param year the year to check.
     * 
     * @return a list of strings representing the users.
     */
    List<String> getUsersAboveAverageListens(int year);

    /**
     * Retrieves albums with a review average higher than the global average.
     *
     * @return a list of strings representing the top albums.
     */
    List<String> getAlbumsAboveGlobalAverage();

    void followArtist(String string, int artistCode);

    /**
     * Recupera tutti i piani di abbonamento disponibili.
     *
     * @return lista di piani.
     */
    List<Plan> getSubscriptioPlans();

    /**
     * Activates a new subscription for a user.
     * 
     * @param username the username of the subscriber.
     * @param plancode the unique code of the chosen subscription plan.
     * @param paymentMethod the payment method selected by the user.
     * @param promoCode the promotional code applied (if any).
     * @param inviteCode the invite code used (if any).
     * @param autoRenenw true to enable auto-renewal, false otherwise.
     * 
     * @return the unique code of the created subscription.
     */
    int activateSubscription(String username, int plancode, String paymentMethod, 
                            String promoCode, String inviteCode, boolean autoRenenw);

    /**
     * Verifies the validity of an invite code.
     * 
     * @param inviteCode the invite code string to verify.
     * @return true if the invite code is valid and active, false otherwise.
     */
    boolean verifyInviteCode(String inviteCode);

    /**
     * Verifies a promotional code against a specific subscription plan.
     * 
     * @param promoCode the promotional code string to verify.
     * @param planCode the plan code to check eligibility against.
     * 
     * @return an Object array containing verification details and discount properties.
     */
    Object[] verifyPromotionCode(String promoCode, int planCode);

    /**
     * Retrieve subscription data in a structured format.
     * 
     * @param username the username of the account holder.
     * 
     * @return a list of Object arrays representing: [subCode, planType, startDate, endDate, 
     *         status, autoRenew, promoCode, inviteCode, transactionsList].
     */
    List<Object[]> getSubscriptionData(String username);

    /** Removes a like from a content for a specific user.
     *
     * @param username the username of the user
     * @param contentCode the code of the content to unlike
     */
    void removeLike(String username, int contentCode);

    /**
     * Retrieves the list of liked songs for a specific user.
     *
     * @param username the username of the user
     * @return a list of strings representing the liked songs
     */
    List<String> getLikedSongs(String username);

    /**
     * Retrieves an artist profile by their code.
     *
     * @param artistCode the distinctive code of the artist
     * @return the {@link Artist} object if found, or null otherwise
     */
    Artist getArtistByCode(int artistCode);

    /**
     * Adds a like to a content for a specific user.
     *
     * @param username the username of the user
     * @param contentCode the code of the content to like
     */
    void addLike(String username, int contentCode);

    /**
     * Retrieves a list of songs filtered by a specific genre.
     *
     * @param genre the genre name
     * @return a list of strings representing the songs matching the genre
     */
    List<String> getSongsByGenre(String genre);

    /**
     * Retrieves a list of artists whose stage names match a partial query string.
     *
     * @param query the partial query string for the artist name
     * @return a list of matching {@link Artist} objects
     */
    List<Artist> getArtistsByPartialName(String query);

    /**
     * Retrieves a list of albums whose titles match a partial query string.
     *
     * @param query the partial title query
     * @return a list of matching {@link Album} objects
     */
    List<Album> getAlbumsByPartialTitle(String query);

    /**
     * Retrieves an album along with its complete tracklist and details.
     *
     * @param albumCode the code of the album
     * @return an object containing the album and its songs structure
     */
    Album.DAO.AlbumWithSongs getAlbumWithSongs(int albumCode);

    /**
     * Retrieves the list of reviews associated with a specific album.
     *
     * @param albumCode the code of the album
     * @return a list of strings representing the album reviews
     */
    List<String> getAlbumReviews(int albumCode);

    /**
     * Inserts a new review or updates an existing one for an album by a user.
     *
     * @param username the username of the reviewer
     * @param albumCode the code of the album
     * @param rating the numeric rating score
     * @param comment the textual comment of the review
     */
    void insertOrUpdateReview(String username, int albumCode, int rating, String comment);

    /**
     * Retrieves the current bonus credit balance for a user.
     * 
     * @param username the username of the user
     * @return the total number of bonus credits available for the user
     */
    int getBonusCredits(String username);

    /**
     * Redeems bonus credits to activate or renew a subscription.
     * 
     * @param username the username of the user redeeming bonus credits
     * @param planCode the unique code of the subscription plan to redeem
     * @param autoRenew whether to enable automatic renewal for the new subscription
     * @return the unique code of the newly created or renewed subscription
     */
    int redeemBonus(String username, int planCode, boolean autoRenew);

    /**
     * Registers a new user in the Soundwave system.
     * 
     * @param username the unique username for the new account
     * @param name the user's first name
     * @param surname the user's last name
     * @param email the user's email address
     * @param password the user's password
     * @param birthDate the user's date of birth
     * @param country the user's country of residence
     * @return the generated invite code for the newly registered user
     */
    String registerUser(String username, String name, String surname, String email, 
                        String password, LocalDate birthDate, String country);

}