package soundwave.view;

import java.util.List;

import soundwave.controller.Controller;
import soundwave.data.Artist;
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
     * Displays the formatted global statistics report.
     * 
     * @param statsText the formatted statistics string.
     */
    void showGlobalStats(String statsText);

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
