package soundwave.view;

import java.util.List;

import soundwave.controller.Controller;
import soundwave.data.Artist;

/**
 * Represents a view architectural component of the application.
 */
public interface View {

    /**
     * Sets the controller controlled by this view (if works as input).
     *
     * @param controller the controller to attach
     */
    void setController(Controller controller);

    /**
     * Displays the main window frame.
     */
    void start();

    /**
     * Shows the specified panel by its card name.
     *
     * @param panelName the identifier of the panel to show
     */
    void showPanel(String panelName);

    /**
     * Displays the list of registered users in the admin dashboard.
     * 
     * @param users the list of users to display
     */
    void showUsers(java.util.List<soundwave.data.User> users);

    /**
     * Displays the formatted global statistics report.
     * 
     * @param statsText the formatted statistics string
     */
  /**
     * Displays the global statistics on the admin panel.
     *
     * @param statsText the formatted string containing global statistics
     */
    void showGlobalStats(String statsText);

    /**
     * Displays the list of liked songs in the user's library view.
     *
     * @param likedSongs the list of strings representing the user's liked songs
     */
    void showLikedSongs(List<String> likedSongs);

    /**
     * Populates the UI with the artist search results (e.g., in a dropdown combo box).
     *
     * @param artists the list of {@link Artist} objects found matching the search query
     */
    void showArtistSearchResults(List<Artist> artists);

    /**
     * Displays the detailed profile information for a specific artist.
     *
     * @param artist the {@link Artist} object whose profile needs to be shown
     */
    void showArtistProfile(Artist artist);

    /**
     * Displays the songs filtered by a specific criterion (such as genre) in the explore view.
     *
     * @param songs the list of strings representing the filtered songs
     */
    void showFilteredSongs(List<String> songs);
}
