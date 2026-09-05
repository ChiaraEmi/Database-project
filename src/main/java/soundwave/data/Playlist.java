package soundwave.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Playlist entity.
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
     * @param playlistCode the primary key.
     * @param username the owner's username.
     * @param playlistName the name of the playlist.
     * @param creationDate the creation timestamp.
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
     * Gets the playlist code.
     *
     * @return the playlist code.
     */
    public int getPlaylistCode() {
        return playlistCode;
    }

    /**
     * Gets the username of the owner.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the playlist name.
     *
     * @return the playlist name.
     */
    public String getPlaylistName() {
        return playlistName;
    }

    /**
     * Gets the creation date.
     *
     * @return the creation date.
     */
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * Gets the visibility state.
     *
     * @return the visibility.
     */
    public String getVisibility() {
        return visibility;
    }

    /**
     * Checks if the playlist is collaborative.
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
         * Inserts a new playlist into the Playlist table.
         *
         * @param connection the database connection.
         * @param username the foreign key referring to Utenti.
         * @param playlistName the name of the playlist.
         * @param visibility the visibility state ('Pubblica' or 'Privata').
         * @param isCollaborative flag indicating if the playlist is collaborative.
         * @return the auto-generated primary key.
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
         * Retrieves all playlists associated with a specific user.
         *
         * @param connection the database connection.
         * @param username the username of the owner.
         * @return a list of playlists belonging to the user.
         */
        public static List<Playlist> getUserPlaylists(final Connection connection, final String username) {
            final List<Playlist> playlists = new ArrayList<>();
            try (var statement = DAOUtils.prepare(connection, Queries.SELECT_PLAYLISTS_BY_USER, username);
                 var rs = statement.executeQuery()) {
                while (rs.next()) {
                    playlists.add(new Playlist(
                        rs.getInt("CodicePlaylist"),
                        rs.getString("Username"),
                        rs.getString("NomePlaylist"),
                        rs.getString("DataCreazione"),
                        rs.getString("Visibilita"),
                        rs.getBoolean("Collaborativa")
                    ));
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
            return playlists;
        }

        /**
         * Inserts a track into a playlist via the Inclusioni table.
         *
         * @param connection the database connection.
         * @param playlistCode the foreign key referring to Playlist.
         * @param trackCode the foreign key referring to Brani.
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

        /**
         * Adds a track to a playlist after checking user permissions and avoiding duplicates.
         *
         * @param connection the database connection.
         * @param username the user performing the action.
         * @param playlistCode the playlist code.
         * @param trackCode the track code.
         * 
         * @return true if added successfully, false if permissions are missing or the track is already present.
         */
        public static boolean addTrackWithPermission(final Connection connection, final String username,
                                                   final int playlistCode, final int trackCode) {

            try (var checkStmt = DAOUtils.prepare(connection, Queries.CHECK_PERMESSI_PLAYLIST, playlistCode, username, username);
                 var rs = checkStmt.executeQuery()) {
                if (!rs.next()) {
                    return false; // Permesso negato
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }

            try (var dupStmt = DAOUtils.prepare(connection, Queries.CHECK_DUPLICATE_TRACK_IN_PLAYLIST, playlistCode, trackCode);
                 var rsDup = dupStmt.executeQuery()) {
                if (rsDup.next()) {
                    return false; // Brano già presente nella playlist
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }

            addTrack(connection, playlistCode, trackCode);
            return true;
        }

        /**
         * Removes a track from a playlist after checking user permissions.
         *
         * @param connection the database connection.
         * @param username the user performing the action.
         * @param playlistCode the playlist code.
         * @param trackCode the track code.
         * 
         * @return true if removed successfully, false if the user lacks permissions.
         */
        public static boolean removeTrack(final Connection connection, final String username,
                                          final int playlistCode, final int trackCode) {
            try (var checkStmt = DAOUtils.prepare(connection, Queries.CHECK_PERMESSI_PLAYLIST, playlistCode, username, username);
                 var rs = checkStmt.executeQuery()) {
                if (!rs.next()) {
                    return false; // Permesso negato
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }

            try (var deleteStmt = DAOUtils.prepare(connection, Queries.REMOVE_BRANO_FROM_PLAYLIST, playlistCode, trackCode)) {
                final int rowsAffected = deleteStmt.executeUpdate();
                return rowsAffected > 0;
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }
    }
}
