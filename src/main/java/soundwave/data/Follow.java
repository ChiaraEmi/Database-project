package soundwave.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the Follow entity and its DAO operations.
 */
public final class Follow {

    private final String username;
    private final int artistCode;
    private final LocalDate startDate;
    private final LocalDate endDate;

    /**
     * Constructs a new Follow entity.
     *
     * @param username   the username of the user.
     * @param artistCode the code of the artist.
     * @param startDate  the start date of the follow.
     * @param endDate    the end date of the follow (can be null).
     */
    public Follow(final String username, final int artistCode, final LocalDate startDate, final LocalDate endDate) {
        this.username = username;
        this.artistCode = artistCode;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Returns the username.
     * 
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the artist code.
     * 
     * @return the artist code.
     */
    public int getArtistCode() {
        return artistCode;
    }

    /**
     * Returns the start date.
     * 
     * @return the start date.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Returns the end date.
     * 
     * @return the end date.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Nested DAO class for Follow database operations.
     */
    public static final class DAO {

        private static final Logger LOG = Logger.getLogger(DAO.class.getName());

        private DAO() {
            // Utility class for database operations
        }

        /**
         * Adds a follow relationship for a specific artist by a user.
         *
         * @param connection the database connection.
         * @param username   the username of the user.
         * @param artistCode the code of the artist.
         * @param startDate  the start date of the follow.
         */
        public static void followArtist(final Connection connection, final String username, 
                                        final int artistCode, final LocalDate startDate) {
            try (var statement = DAOUtils.prepare(connection, Queries.INSERT_FOLLOW, username, 
                                                    artistCode, java.sql.Date.valueOf(startDate), null)) {
                statement.executeUpdate();
            } catch (final SQLException e) {
                LOG.log(Level.SEVERE, "Errore durante il salvataggio del follow per l'artista: " + artistCode, e);
                throw new DAOException(e);
            }
        }

        /**
         * Updates the follow relationship by setting an end date (unfollow).
         *
         * @param connection the database connection.
         * @param username   the username of the user.
         * @param artistCode the code of the artist.
         */
        public static void unfollowArtist(final Connection connection, final String username, final int artistCode) {
            try (var statement = DAOUtils.prepare(connection, Queries.UPDATE_UNFOLLOW, username, artistCode)) {
                statement.executeUpdate();
            } catch (final SQLException e) {
                LOG.log(Level.SEVERE, "Errore durante l'unfollow per l'artista: " + artistCode, e);
                throw new DAOException(e);
            }
        }
    }
}
