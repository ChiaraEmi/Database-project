package soundwave;

import javax.swing.SwingUtilities;
import soundwave.controller.Controller;
import soundwave.controller.ControllerImpl;
import soundwave.view.View;
import soundwave.view.ViewImpl;

/**
 * Main class to start the database project.
 */
public final class Main {

    private Main() {
        // This class should not be instantiated.
    }

    /**
     * The main entry point of the application.
     * 
     * @param args The command line arguments.
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            final Controller controller = new ControllerImpl();
            final View view = new ViewImpl();
            
            controller.setView(view);
            view.start();
        });
    }
}
