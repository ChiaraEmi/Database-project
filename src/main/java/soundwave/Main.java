package soundwave;

import soundwave.controller.ControllerImpl;
import soundwave.data.DAOUtils;
//import soundwave.model.MockedModel;
import soundwave.model.Model;
import soundwave.view.ViewImpl;
import java.sql.SQLException;

/**
 * Main class to start the application.
 */
public final class Main {

    private Main() {
        // Private constructor to hide implicit public one
    }

    /**
     * The main entry point of the application.
     * 
     * @param args The command line arguments.
     * 
     * @throws SQLException if a database access error occurs.
     */
    public static void main(final String[] args) throws SQLException {
        String dbPassword = System.getenv("DB_PASSWORD");
        if (dbPassword == null || dbPassword.isEmpty()) {
            dbPassword = "Dolphin26*"; // Fallback for local testing
        }

        final var connection = DAOUtils.localMySQLConnection("soundwave", "root", dbPassword);
        final var model = Model.fromConnection(connection);
        //final Model model = new MockedModel();

        final var view = new ViewImpl(() -> {
            try {
                connection.close();
            } catch (final SQLException ignored) {
                // Ignored on close
            }
        });

        final var controller = new ControllerImpl(model, view);
        view.setController(controller);
        view.start();
    }
}
