package soundwave.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

import soundwave.data.Artist;
import soundwave.data.LikeBrani;
import soundwave.data.Playlist;
import soundwave.data.Podcast;
import soundwave.view.ActivateSubscriptionDialog;
import soundwave.view.RedeemBonusDialog;

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
     * Retrieves the list of all podcasts.
     *
     * @return a list of podcasts.
     */
    List<Podcast> getPodcasts();

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
     * Requests the redemption of bonus credits for a user
     * This method is called when a user wats to redeem their bonus credit
     * for a subscriptionn. The system will check if the user has sufficient
     * credits and if they are eligible for redemption
     * 
     * @param username the username of the user requisting bonus redemption
     */
    void userRequestedRedeemBonus(String username);

    /**
     * Processes the redemption of bonus credits for a subscription
     * 
     * @param username the username of the user redeeming bonus credits
     * @param data the redemption data containing plan information and other details
     */
    void userRedeemedBonus(String username, RedeemBonusDialog.RedeemData data);


    /**
     * Registers a new user in the Soundwave system
     * 
     * @param username the unique username for new account
     * @param name the user's first name
     * @param surname the user's last name
     * @param email the user's email adress
     * @param password the user's password
     * @param birthDate the user's date of birth
     * @param country the user's cuntry of residence
     */
    void userRegistered(String username, String name, String surname, String email, 
                    String password, LocalDate birthDate, String country);

    /**
     * Renews a subscription for the specified user
     * 
     * @param username the username of the subscription owner
     * @param subscriptionCode the unique code of the subscription to renew
     */
    void renewSubscriptionNow(String username, int subscriptionCode);

    /**
     * Toggles the auto-renewal setting for a user's subscription.
     * This method enables or disables automatic renewal for the specified
     * subscription. When enabled, the system will automatically attempt to
     * renew the subscription when it expires.
     * 
     * @param username the username of the subscription owner
     * @param subscriptionCode the unique code of the subscription to modify
     * @param enabled true to enable auto-renewal, false to disable it
     */
    void toggleAutoRenew(String username, int subscriptionCode, boolean enabled);

    /**
     * Retrieves the current auto-renewal status of a subscription
     * This method checks whether automatic renewal is enabled for the specified subscription.
     * 
     * @param username the username of the subscription owner
     * @param subscriptionCode the unique code of the subscription to check
     * @return true if auto-renewal is enabled, false otherwise
     */
    boolean getAutoRenewStatus(String username, int subscriptionCode);

    /**
     * Executes the automatic renewal process for all eligible subscriptions
     * 
     * @return an array of subscription codes that were successfully renewed, 
     *          or an empty array if no subscriptions were renewed
     */
    int[] adminRunAutoRenewal();


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
     * Retrieves the list of playlists belonging to a specific user.
     * 
     * @param username the owner's username.
     * 
     * @return a list of playlists.
     */
    List<Playlist> getUserPlaylists(String username);

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
     * Retrieves the list of liked tracks belonging to a specific user.
     * 
     * @param username the username of the user.
     * 
     * @return a list of liked tracks.
     */
    List<LikeBrani> getUserLikedTracks(String username);

    /**
     * Returns the list of artists followed by the user.
     * 
     * @param username the username.
     * @return the list of followed artist names.
     */
    List<String> getFollowedArtists(String username);

    /**
     * Handles the request to add a like to a track for a specific user.
     *
     * @param username the user performing the action.
     * @param trackCode the track code to like.
     * 
     * @return true if successfully liked, false otherwise.
     */
    boolean userClickedLikeTrack(String username, int trackCode);

    /**
     * Handles the request to remove a like from a track for a specific user.
     *
     * @param username the user performing the action.
     * @param trackCode the track code to unlike.
     * 
     * @return true if successfully removed, false otherwise.
     */
    boolean userClickedUnlikeTrack(String username, int trackCode);

    /**
     * Handles the request to load and view the list of system users.
     */
    void adminClickedLoadUsers();

    /**
     * Handles the request to load and display personal yearly listening statistics for a specific user.
     *
     * @param username the username of the user.
     * @param year the year to filter by.
     */
    void userRequestedPersonalStats(String username, int year);

    /**
     * Handles the request to fetch albums above global average.
     */
    void adminRequestedGlobalAlbums();

    /**
     * Handles the request to fetch yearly stats for a specific year.
     * 
     * @param year the year chosen by the admin.
     */
    void adminRequestedYearlyStats(int year);

    /**
     * Handles the request from an administrator to save a new promotional offer.
     *
     * @param code the promo code.
     * @param name the promotion name.
     * @param description the promotion description.
     * @param startDate the start date string.
     * @param endDate the end date string.
     * @param discountType the type of discount.
     * @param discountValueStr the discount value as a string.
     * @param rqrMonths the required months as a string.
     * @param planCodesStr the comma-separated plan codes associated with the promotion.
     * 
     * @return true if successfully saved, false otherwise.
     */
    boolean adminClickedSavePromotion(String code, String name, String description, String startDate, 
                                   String endDate, String discountType, String discountValueStr, 
                                   String rqrMonths, String planCodesStr);

    /**
     * Requests the available subscription plans for a user and displays the activation dialog.
     *
     * @param username the username requesting the subscription plans.
     */
    void userRequestedSubscriptionPlans(String username);

    /**
     * Handles the activation of a subscription for a user based on the dialog data.
     *
     * @param username the username activating the subscription.
     * @param data the subscription data container from the view.
     */
    void userActivateSubscription(String username, ActivateSubscriptionDialog.SubscriptionData data);

    /**
     * Asynchronously verifies if an invite code is valid.
     *
     * @param inviteCode the invite code to check.
     * @param callback the callback consumer receiving the verification result.
     */
    void verifyInviteCode(String inviteCode, Consumer<Boolean> callback);

    /**
     * Asynchronously verifies if a promotion code is valid for a specific plan.
     *
     * @param promoCode the promo code to check.
     * @param planCode the target plan code.
     * @param callback the callback consumer receiving the result array.
     */
    void verifyPromotionCode(String promoCode, int planCode, Consumer<Object[]> callback);

    /**
     * Retrieves subscription details and history for a specific user.
     *
     * @param username the username whose subscription data is requested.
     * @return a list of object arrays representing subscription details.
     */
    List<Object[]> getSubscriptionData(String username);
}
