package soundwave;

import soundwave.controller.ControllerImpl;
import soundwave.data.DAOUtils;
//import soundwave.model.MockedModel;
import soundwave.model.Model;
import soundwave.view.ViewImpl;
import java.sql.SQLException;

/**
 * Main class to start the application following the tutor's style.
 */
public final class Main {

    private Main() {
        // Private constructor to hide implicit public one
    }

    /**
     * The main entry point of the application.
     * 
     * @param args The command line arguments.
     * @throws SQLException if a database access error occurs.
     */
    public static void main(final String[] args) throws SQLException {
        final var connection = DAOUtils.localMySQLConnection("soundwave", "root", "Dolphin26*");
        final var model = Model.fromConnection(connection);
        //final Model model = new MockedModel();
        
        final var view = new ViewImpl(() -> {
            try {
                connection.close();
            } catch (final Exception ignored) {
                // Ignored on close
            }
        });
        
        final var controller = new ControllerImpl(model, view);
        view.setController(controller);
        view.start();
    }
}
