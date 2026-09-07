package soundwave.data;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Represents the Like/Favorite entity and its DAO operations.
 */
public final class Like {

    private Like() {
        // Utility class
    }

    /**
     * Nested DAO class for Like database operations.
     */
    public static final class DAO {

        private DAO() {
            // Utility class
        }

        /**
         * Adds a like for a specific song by a user.
         *
         * @param connection the database connection
         * @param username the username of the user
         * @param songCode the code of the song
         */
        public static void addLike(final Connection connection, final String username, final int songCode) {
            System.out.println("DEBUG - Username che sta tentando di mettere il like: '" + username + "'");
            try (var statement = DAOUtils.prepare(connection, Queries.INSERT_LIKE, username, songCode)) {
                statement.executeUpdate();
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }

        /**
        * Returns the list of liked song titles for a specific user.
        *
        * @param connection the database connection
        * @param username the username of the user
        * @return a list containing the titles of the liked songs
        */
        public static java.util.List<String> getLikedSongs(final Connection connection, final String username) {
            final java.util.List<String> likedSongs = new java.util.ArrayList<>();
            try (var statement = DAOUtils.prepare(connection, Queries.SELECT_LIKED_SONGS, username);
                var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    final String songTitle = resultSet.getString("Titolo");
                    likedSongs.add(songTitle);
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
            return likedSongs;
        }

        /**
         * Removes a like for a specific song by a user.
         *
         * @param connection the database connection
         * @param username the username of the user
         * @param songCode the code of the song
         */
        public static void removeLike(final Connection connection, final String username, final int songCode) {
            try (var statement = DAOUtils.prepare(connection, Queries.DELETE_LIKE, username, songCode)) {
                statement.executeUpdate();
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }
    }
}