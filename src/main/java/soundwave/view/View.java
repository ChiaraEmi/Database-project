package soundwave.view;

import soundwave.controller.Controller;

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
    void showGlobalStats(String statsText);
}
