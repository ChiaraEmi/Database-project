package soundwave.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Playlist entity corresponding to the Playlist table.
 */
public final class Playlist {

    private final int playlistCode;
    private final String username;
    private final String playlistName;
    private final String creationDate;
    private final String visibility;
    private final boolean isCollaborative;

    /**
     * Constructs a new Playlist instance.
     *
     * @param playlistCode the primary key (CodicePlaylist).
     * @param username the owner's username (Username).
     * @param playlistName the name of the playlist (NomePlaylist).
     * @param creationDate the creation timestamp (DataCreazione).
     * @param visibility the visibility state ('Pubblica' or 'Privata').
     * @param isCollaborative flag indicating if the playlist is collaborative.
     */
    public Playlist(final int playlistCode, final String username, final String playlistName,
                    final String creationDate, final String visibility, final boolean isCollaborative) {
        this.playlistCode = playlistCode;
        this.username = username == null ? "" : username;
        this.playlistName = playlistName == null ? "" : playlistName;
        this.creationDate = creationDate == null ? "" : creationDate;
        this.visibility = visibility == null ? "Privata" : visibility;
        this.isCollaborative = isCollaborative;
    }

    /**
     * Gets the playlist code (CodicePlaylist).
     *
     * @return the playlist code.
     */
    public int getPlaylistCode() {
        return playlistCode;
    }

    /**
     * Gets the username of the owner (Username).
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the playlist name (NomePlaylist).
     *
     * @return the playlist name.
     */
    public String getPlaylistName() {
        return playlistName;
    }

    /**
     * Gets the creation date (DataCreazione).
     *
     * @return the creation date.
     */
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * Gets the visibility state (Visibilita).
     *
     * @return the visibility.
     */
    public String getVisibility() {
        return visibility;
    }

    /**
     * Checks if the playlist is collaborative (Collaborativa).
     *
     * @return true if collaborative, false otherwise.
     */
    public boolean isCollaborative() {
        return isCollaborative;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other instanceof Playlist) {
            final var p = (Playlist) other;
            return p.playlistCode == this.playlistCode
                   && p.isCollaborative == this.isCollaborative
                   && p.username.equals(this.username)
                   && p.playlistName.equals(this.playlistName)
                   && p.creationDate.equals(this.creationDate)
                   && p.visibility.equals(this.visibility);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.playlistCode, this.username, this.playlistName, this.creationDate, 
                            this.visibility, this.isCollaborative);
    }

    @Override
    public String toString() {
        return Printer.stringify(
            "Playlist",
            List.of(
                Printer.field("playlistCode", this.playlistCode),
                Printer.field("username", this.username),
                Printer.field("playlistName", this.playlistName),
                Printer.field("creationDate", this.creationDate),
                Printer.field("visibility", this.visibility),
                Printer.field("isCollaborative", this.isCollaborative)
            )
        );
    }

    /**
     * Data Access Object for Playlist operations.
     */
    public static final class DAO {

        private DAO() { }

        /**
         * Inserts a new playlist into the Playlist table (OP 12).
         *
         * @param connection the database connection.
         * @param username the foreign key referring to Utenti (Username).
         * @param playlistName the name of the playlist (NomePlaylist).
         * @param visibility the visibility state ('Pubblica' or 'Privata').
         * @param isCollaborative flag indicating if the playlist is collaborative.
         * @return the auto-generated primary key (CodicePlaylist).
         */
        public static int insert(final Connection connection, final String username, 
                                 final String playlistName, final String visibility, 
                                 final boolean isCollaborative) {
            try (
                var statement = DAOUtils.prepareWithKeys(
                    connection, 
                    Queries.INSERT_PLAYLIST, 
                    java.sql.Statement.RETURN_GENERATED_KEYS, 
                    username, playlistName, visibility, isCollaborative
                )
            ) {
                statement.executeUpdate();

                try (var resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
            throw new DAOException("Unable to retrieve generated key for Playlist.");
        }

        /**
         * Inserts a track into a playlist via the Inclusioni table (OP 13).
         *
         * @param connection the database connection.
         * @param playlistCode the foreign key referring to Playlist (CodicePlaylist).
         * @param trackCode the foreign key referring to Brani (CodiceBrano).
         */
        public static void addTrack(final Connection connection, final int playlistCode, final int trackCode) {
            try (
                var statement = DAOUtils.prepare(
                    connection, 
                    Queries.ADD_BRANO_TO_PLAYLIST, 
                    playlistCode, trackCode
                )
            ) {
                statement.executeUpdate();
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }
    }
}
