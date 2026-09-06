package soundwave.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a LikeBrani entity linking a user to a favorite track.
 */
public final class LikeBrani {

    private final String username;
    private final int trackCode;
    private final String trackTitle;

    /**
     * Constructs a new LikeBrani instance.
     *
     * @param username the user's username.
     * @param trackCode the track code.
     * @param trackTitle the title of the track.
     */
    public LikeBrani(final String username, final int trackCode, final String trackTitle) {
        this.username = username == null ? "" : username;
        this.trackCode = trackCode;
        this.trackTitle = trackTitle == null ? "" : trackTitle;
    }

    /**
     * Gets the username.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the track code.
     *
     * @return the track code.
     */
    public int getTrackCode() {
        return trackCode;
    }

    /**
     * Gets the track title.
     *
     * @return the track title.
     */
    public String getTrackTitle() {
        return trackTitle;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other instanceof LikeBrani) {
            final var l = (LikeBrani) other;
            return l.trackCode == this.trackCode
                   && l.username.equals(this.username)
                   && l.trackTitle.equals(this.trackTitle);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.username, this.trackCode, this.trackTitle);
    }

    @Override
    public String toString() {
        return Printer.stringify(
            "LikeBrani",
            List.of(
                Printer.field("username", this.username),
                Printer.field("trackCode", this.trackCode),
                Printer.field("trackTitle", this.trackTitle)
            )
        );
    }

    /**
     * Data Access Object for LikeBrani operations.
     */
    public static final class DAO {

        private DAO() { }

        /**
         * Retrieves all liked tracks for a specific user.
         *
         * @param connection the database connection.
         * @param username the username of the user.
         * @return a list of liked tracks.
         */
        public static List<LikeBrani> getLikedTracks(final Connection connection, final String username) {
            final List<LikeBrani> likedTracks = new ArrayList<>();
            try (var statement = DAOUtils.prepare(connection, Queries.SELECT_LIKED_TRACKS_BY_USER, username);
                 var rs = statement.executeQuery()) {
                while (rs.next()) {
                    likedTracks.add(new LikeBrani(
                        username,
                        rs.getInt("CodiceBrano"),
                        rs.getString("Titolo")
                    ));
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
            return likedTracks;
        }

        /**
         * Adds a like to a track for a specific user.
         *
         * @param connection the database connection.
         * @param username the username.
         * @param trackCode the track code.
         */
        public static void likeTrack(final Connection connection, final String username, final int trackCode) {
            try (var statement = DAOUtils.prepare(connection, Queries.INSERT_LIKE_BRANO, username, trackCode)) {
                statement.executeUpdate();
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }

        /**
         * Removes a like from a track for a specific user.
         *
         * @param connection the database connection.
         * @param username the username.
         * @param trackCode the track code.
         * @return true if the like was successfully removed, false otherwise.
         */
        public static boolean unlikeTrack(final Connection connection, final String username, final int trackCode) {
            try (var statement = DAOUtils.prepare(connection, Queries.REMOVE_LIKE_BRANO, username, trackCode)) {
                final int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }
    }
}
