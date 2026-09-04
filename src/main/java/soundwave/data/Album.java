package soundwave.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an Album entity.
 */
public final class Album {

    private final int albumCode;
    private final int artistCode;
    private final String title;
    private final String releaseDate;
    private final String recordCompany;
    private final double averageRating;
    private final int totalDuration;

    /**
     * Constructs a new Album instance.
     *
     * @param albumCode the album code.
     * @param artistCode the artist code associated with the album.
     * @param title the title of the album.
     * @param releaseDate the release year.
     * @param recordCompany the record company.
     * @param averageRating the average rating.
     * @param totalDuration the total duration in seconds.
     */
    public Album(final int albumCode, final int artistCode, final String title, final String releaseDate,
                 final String recordCompany, final double averageRating, final int totalDuration) {
        this.albumCode = albumCode;
        this.artistCode = artistCode;
        this.title = title == null ? "" : title;
        this.releaseDate = releaseDate;
        this.recordCompany = recordCompany == null ? "" : recordCompany;
        this.averageRating = averageRating;
        this.totalDuration = totalDuration;
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
     * Gets the artist code.
     *
     * @return the artist code.
     */
    public int getArtistCode() {
        return artistCode;
    }

    /**
     * Gets the title.
     *
     * @return the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the release year.
     *
     * @return the release year.
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Gets the record company.
     *
     * @return the record company.
     */
    public String getRecordCompany() {
        return recordCompany;
    }

    /**
     * Gets the average rating.
     *
     * @return the average rating.
     */
    public double getAverageRating() {
        return averageRating;
    }

    /**
     * Gets the total duration.
     *
     * @return the total duration.
     */
    public int getTotalDuration() {
        return totalDuration;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other instanceof Album) {
            final var a = (Album) other;
            return a.albumCode == this.albumCode
                    && a.artistCode == this.artistCode
                    && a.title.equals(this.title)
                    && a.releaseDate.equals(this.releaseDate)
                    && a.recordCompany.equals(this.recordCompany)
                    && Double.compare(a.averageRating, this.averageRating) == 0
                    && a.totalDuration == this.totalDuration;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.albumCode, this.artistCode, this.title, this.releaseDate,
                            this.recordCompany, this.averageRating, this.totalDuration);
    }

    @Override
    public String toString() {
        return Printer.stringify(
            "Album",
            List.of(
                Printer.field("albumCode", this.albumCode),
                Printer.field("artistCode", this.artistCode),
                Printer.field("title", this.title),
                Printer.field("releaseYear", this.releaseDate),
                Printer.field("recordCompany", this.recordCompany),
                Printer.field("averageRating", this.averageRating),
                Printer.field("totalDuration", this.totalDuration)
            )
        );
    }

    /**
     * Data Access Object for Album operations.
     */
    public static final class DAO {

        private DAO() { }

        /**
         * Inserts a new album and its associated songs into the database.
         *
         * @param connection the database connection.
         * @param artistCode the artist code.
         * @param title the album title.
         * @param releaseDate the release year.
         * @param recordCompany the record company.
         * @param songs the list of songs to insert.
         * @return the generated album code.
         */
        public static int insertAlbumWithSongs(final Connection connection, final int artistCode, 
                                               final String title, final String releaseDate, 
                                               final String recordCompany, final List<SongInput> songs) {
            boolean autoCommit = true;
            try {
                autoCommit = connection.getAutoCommit();
                connection.setAutoCommit(false);

                try (var checkStmt = DAOUtils.prepare(connection, Queries.CHECK_ARTIST_EXISTS, artistCode);
                     var resultSet = checkStmt.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new DAOException("Artist with ID " + artistCode + " does not exist.");
                    }
                }

                int albumCode = -1;
                try (var statement = DAOUtils.prepareWithKeys(
                    connection,
                    Queries.INSERT_ALBUM,
                    java.sql.Statement.RETURN_GENERATED_KEYS,
                    artistCode, title, releaseDate, recordCompany
                )) {
                    statement.executeUpdate();

                    try (var resultSet = statement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            albumCode = resultSet.getInt(1);
                        } else {
                            throw new DAOException("Unable to retrieve generated key for Album.");
                        }
                    }
                }

                for (final var songInput : songs) {
                    Song.DAO.insert(
                        connection,
                        albumCode,
                        songInput.getTitle(),
                        songInput.getDuration(),
                        songInput.getDescription(),
                        songInput.getTrackNumber(),
                        songInput.getArtistCodeForSong(),
                        songInput.getGenres(),
                        releaseDate
                    );
                }

                try (var updateStmt = DAOUtils.prepare(connection, Queries.UPDATE_ALBUM_DURATION, albumCode, albumCode)) {
                    updateStmt.executeUpdate();
                }

                connection.commit();
                return albumCode;

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

        /**
         * Retrieves albums with a review average higher than the global average.
         *
         * @param connection the database connection.
         * @return a list of strings representing the top albums.
         */
        public static List<String> getAlbumsAboveGlobalAverage(final Connection connection) {
            final List<String> albums = new ArrayList<>();
            try (var statement = DAOUtils.prepare(connection, Queries.SELECT_ALBUMS_ABOVE_GLOBAL_AVG_RATING);
                 var resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    albums.add("Album: " + resultSet.getString("TitoloAlbum") 
                                + " - Media Voti: " + resultSet.getDouble("MediaVoti"));
                }

            } catch (final SQLException e) {
                throw new DAOException(e);
            }

            return albums;
        }
    }
}
