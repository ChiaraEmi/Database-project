package soundwave.controller;

import soundwave.view.View;

/**
 * Interface representing the controller.
 * 
 * <p>
 * This class coordinates the program execution by acting as the Control component
 * in the MVC/ECB architecture. It receives events from the Boundary
 * components (views), updates the game state accordingly, and notifies the
 * boundaries about changes to be displayed.
 * </p>
 */
public interface Controller {

    /**
     * Sets a new view to be controlled by this controller.
     *
     * @param view the view to be set
     */
    void setView(View view);

}
