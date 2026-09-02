package soundwave.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Song entity.
 */
public final class Song {

    private final int songCode;
    private final int albumCode;
    private final int trackNumber;

    /**
     * Constructs a new Song instance.
     *
     * @param songCode the song code (matches Contenuto code).
     * @param albumCode the code of the album it belongs to.
     * @param trackNumber the track number within the album.
     */
    public Song(final int songCode, final int albumCode, final int trackNumber) {
        this.songCode = songCode;
        this.albumCode = albumCode;
        this.trackNumber = trackNumber;
    }

    /**
     * Gets the song code.
     *
     * @return the song code.
     */
    public int getSongCode() {
        return songCode;
    }

    /**
     * Gets the album code.
     *
     * @return the album code.
     */
    public int getAlbumCode() {
        return albumCode;
    }

    /**
     * Gets the track number.
     *
     * @return the track number.
     */
    public int getTrackNumber() {
        return trackNumber;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other instanceof Song) {
            final var s = (Song) other;
            return s.songCode == this.songCode
                    && s.albumCode == this.albumCode
                    && s.trackNumber == this.trackNumber;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.songCode, this.albumCode, this.trackNumber);
    }

    @Override
    public String toString() {
        return Printer.stringify(
            "Song",
            List.of(
                Printer.field("songCode", this.songCode),
                Printer.field("albumCode", this.albumCode),
                Printer.field("trackNumber", this.trackNumber)
            )
        );
    }

    /**
     * Data Access Object for Song operations.
     */
    public static final class DAO {

        private DAO() { }

        /**
         * Inserts a new song into the database (OP 8).
         * First creates the parent record in Contenuti via Content.DAO.insert,
         * then links it to the Album in Brani, associates the singer in Cantare, 
         * and links its genres in Appartenenze.
         *
         * @param connection the database connection.
         * @param albumCode the album code.
         * @param title the title of the song.
         * @param duration the duration in seconds.
         * @param description the description.
         * @param trackNumber the track number.
         * @param artistCode the artist code who sings the song.
         * @param genres the list of genres.
         * @return the generated song code.
         */
        public static int insert(final Connection connection, final int albumCode, 
                                 final String title, final int duration, 
                                 final String description, final int trackNumber,
                                 final int artistCode, final List<String> genres) {
            boolean autoCommit = true;
            try {
                autoCommit = connection.getAutoCommit();
                connection.setAutoCommit(false);

                // 1. Inserimento in Contenuti (sfruttando Content.DAO.insert come in Episode)
                final int contentCode = Content.DAO.insert(connection, title, duration, description, "Brano");

                // 2. Inserimento in Brani
                try (var statement = DAOUtils.prepare(connection, Queries.INSERT_BRANO, contentCode, 
                                                        albumCode, trackNumber)) {
                    statement.executeUpdate();
                }

                // 3. Inserimento in Cantare (chi lo canta)
                try (var statement = DAOUtils.prepare(connection, Queries.INSERT_CANTARE, artistCode, contentCode)) {
                    statement.executeUpdate();
                }

                // 4. Inserimento in Appartenenze (generi musicali)
                for (final var genre : genres) {
                    try (var statement = DAOUtils.prepare(connection, Queries.INSERT_APPARTENENZA, contentCode, genre)) {
                        statement.executeUpdate();
                    }
                }

                connection.commit();
                return contentCode;
            } catch (final SQLException e) {
                try {
                    connection.rollback();
                } catch (final SQLException rollbackEx) {
                    e.addSuppressed(rollbackEx);
                }
                throw new DAOException(e);
            } finally {
                try {
                    connection.setAutoCommit(autoCommit);
                } catch (final SQLException ignored) {
                    // Intentionally ignored
                }
            }
        }
    }
}
