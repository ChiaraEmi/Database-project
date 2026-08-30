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
}
