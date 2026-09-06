package soundwave.view;

import java.util.List;

import soundwave.controller.Controller;
import soundwave.data.Artist;
import soundwave.data.Plan;
import soundwave.data.Podcast;
import soundwave.data.User;

/**
 * Represents a view architectural component of the application.
 */
public interface View {

    /**
     * Sets the controller controlled by this view.
     *
     * @param controller the controller to attach.
     */
    void setController(Controller controller);

    /**
     * Displays the main window frame.
     */
    void start();

    /**
     * Opens and displays the user panel for the specified user.
     * 
     * @param username the username of the logged-in user.
     */
    void openUserPanel(String username);

    /**
     * Sets the available album artists in the dropdown menu.
     * 
     * @param artists the list of artist objects authorized as album authors.
     */
    void setAlbumArtists(List<Artist> artists);

    /**
     * Sets the available podcast authors in the dropdown menu.
     * 
     * @param authors the list of artist objects authorized as podcast authors.
     */
    void setPodcastAuthors(List<Artist> authors);

    /**
     * Sets the list of podcasts available in the admin panel.
     *
     * @param podcasts the list of podcasts.
     */
    void setPodcasts(List<Podcast> podcasts);

    /**
     * Shows the specified panel by its card name.
     *
     * @param panelName the identifier of the panel to show.
     */
    void showPanel(String panelName);

    /**
     * Displays the list of registered users in the admin dashboard.
     * 
     * @param users the list of users to display.
     */
    void showUsers(List<User> users);

    /**
     * Show the dialog for activate subscription.
     * 
     * @param username the username of user.
     * @param plans the list of plans disponible.
     */
    void showActivateSubsriptionDialog(String username, List<Plan> plans);

    /**
     * Displays the personal yearly listening statistics for a user.
     *
     * @param statsText the formatted string containing the personal statistics.
     */
    void showPersonalStats(String statsText);

    /**
     * Shows the global albums above average in the admin panel.
     * 
     * @param statsText the text to display.
     */
    void showGlobalAlbumsStats(String statsText);

    /**
     * Shows the yearly statistics in the admin panel.
     * 
     * @param statsText the text to display.
     */
    void showYearlyStats(String statsText);

    /**
     * Displays an error message dialog to the user.
     * 
     * @param message the error message to display.
     */
    void showError(String message);

    /**
     * Displays a success message dialog to the user.
     * 
     * @param message the esuccess message to display.
     */
    void showSuccess(String message);

    /**
     * Displays a success message dialog to the user.
     * 
     * @param message the esuccess message to display.
     */
    void showSuccessAndCloseDialog(String message);

    /**
     * Gets the user panel.
     * 
     * @return the UserPanel instance.
     */
    UserPanel getUserPanel();

    /**
     * Gets the admin panel.
     * 
     * @return the AdminPanel instance.
     */
    AdminPanel getAdminPanel();
}
