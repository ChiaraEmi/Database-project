package soundwave.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Represents the Follow entity and its DAO operations.
 */
public final class Follow {

    private Follow() {
        // Utility class
    }

    /**
     * Nested DAO class for Follow database operations.
     */
    public static final class DAO {

        private DAO() {
            // Utility class
        }

        /**
         * Adds a follow relationship for a specific artist by a user.
         *
         * @param connection the database connection
         * @param username the username of the user
         * @param artistCode the code of the artist
         * @param startDate the start date of the follow
         */
        public static void followArtist(final Connection connection, final String username, final int artistCode, final LocalDate startDate) {
           try (var statement = DAOUtils.prepare(connection, Queries.INSERT_FOLLOW, username, artistCode, java.sql.Date.valueOf(startDate), null)) {
                statement.executeUpdate();
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }

        /**
         * Updates the follow relationship by setting an end date (unfollow).
         *
         * @param connection the database connection
         * @param username the username of the user
         * @param artistCode the code of the artist
         * @param endDate the end date of the follow
         */
        public static void unfollowArtist(final Connection connection, final String username, final int artistCode, final LocalDate endDate) {
            try (var statement = DAOUtils.prepare(connection, Queries.UPDATE_UNFOLLOW, java.sql.Date.valueOf(endDate), username, artistCode)) {
                statement.executeUpdate();
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }
    }
}
